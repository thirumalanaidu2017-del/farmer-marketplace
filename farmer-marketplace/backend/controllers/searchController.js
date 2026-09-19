const Listing = require('../models/Listing');
const Farmer = require('../models/Farmer');
const Product = require('../models/Product');

// GET /api/search?product=rice&lng=77.59&lat=12.97&maxDistanceKm=50
// Public endpoint (customers do not need to log in to search).
//
// Returns one result PER FARMER selling that product, each carrying the
// farmer's name, phone, location and the listing's price/quantity —
// this is what lets the app skip a generic product page and go straight
// to "who's selling this near me".
exports.searchByProduct = async (req, res, next) => {
  try {
    const { product, lng, lat, maxDistanceKm } = req.query;
    if (!product) return res.status(400).json({ message: 'product query param is required' });

    // 1. Find matching products by name (case-insensitive partial match)
    const matchingProducts = await Product.find({ name: new RegExp(product, 'i') }).select('_id');
    const productIds = matchingProducts.map(p => p._id);
    if (productIds.length === 0) return res.json([]);

    // 2. Find active listings for those products, only from verified farmers
    const listings = await Listing.find({ product: { $in: productIds }, active: true })
      .populate({
        path: 'farmer',
        match: { verified: true },
        populate: { path: 'user', select: 'name phone' }
      })
      .populate('product', 'name category');

    // 3. Drop listings whose farmer didn't match (unverified) and shape the response
    let results = listings
      .filter(l => l.farmer)
      .map(l => ({
        listingId: l._id,
        product: l.product.name,
        price: l.price,
        unit: l.unit,
        quantityAvailable: l.quantityAvailable,
        farmer: {
          id: l.farmer._id,
          name: l.farmer.user.name,
          phone: l.farmer.user.phone,
          farmName: l.farmer.farmName,
          address: l.farmer.address,
          location: l.farmer.location // GeoJSON { type: 'Point', coordinates: [lng, lat] }
        }
      }));

    // 4. If the customer sent their coordinates, compute distance and sort by nearest first
    if (lng !== undefined && lat !== undefined) {
      const customerLng = parseFloat(lng);
      const customerLat = parseFloat(lat);
      const maxDist = maxDistanceKm ? parseFloat(maxDistanceKm) : null;

      results = results
        .map(r => {
          const [flng, flat] = r.farmer.location.coordinates;
          const distanceKm = haversineKm(customerLat, customerLng, flat, flng);
          return { ...r, distanceKm: Math.round(distanceKm * 10) / 10 };
        })
        .filter(r => (maxDist ? r.distanceKm <= maxDist : true))
        .sort((a, b) => a.distanceKm - b.distanceKm);
    } else {
      // no location given — fall back to cheapest first
      results.sort((a, b) => a.price - b.price);
    }

    res.json(results);
  } catch (err) { next(err); }
};

// Great-circle distance between two lat/lng points, in kilometers
function haversineKm(lat1, lon1, lat2, lon2) {
  const R = 6371;
  const dLat = toRad(lat2 - lat1);
  const dLon = toRad(lon2 - lon1);
  const a = Math.sin(dLat / 2) ** 2 +
            Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) * Math.sin(dLon / 2) ** 2;
  return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
}
function toRad(deg) { return deg * (Math.PI / 180); }
