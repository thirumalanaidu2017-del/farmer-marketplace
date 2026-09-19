const Listing = require('../models/Listing');
const Farmer = require('../models/Farmer');
const Product = require('../models/Product');

// POST /api/listings  (farmer only)
// body: { productName, price, unit, quantityAvailable, qualityNotes }
// If productName doesn't exist yet, it's created on the fly.
exports.createListing = async (req, res, next) => {
  try {
    const farmer = await Farmer.findOne({ user: req.user.id });
    if (!farmer) return res.status(404).json({ message: 'Farmer profile not found' });

    const { productName, price, unit, quantityAvailable, qualityNotes } = req.body;
    if (!productName || price === undefined || quantityAvailable === undefined) {
      return res.status(400).json({ message: 'productName, price and quantityAvailable are required' });
    }

    let product = await Product.findOne({ name: new RegExp(`^${productName}$`, 'i') });
    if (!product) product = await Product.create({ name: productName });

    const listing = await Listing.create({
      farmer: farmer._id,
      product: product._id,
      price, unit, quantityAvailable, qualityNotes
    });

    res.status(201).json(listing);
  } catch (err) { next(err); }
};

// GET /api/listings/mine  (farmer only)
exports.getMyListings = async (req, res, next) => {
  try {
    const farmer = await Farmer.findOne({ user: req.user.id });
    if (!farmer) return res.status(404).json({ message: 'Farmer profile not found' });
    const listings = await Listing.find({ farmer: farmer._id }).populate('product', 'name category');
    res.json(listings);
  } catch (err) { next(err); }
};

// PUT /api/listings/:id  (farmer only, must own the listing)
exports.updateListing = async (req, res, next) => {
  try {
    const farmer = await Farmer.findOne({ user: req.user.id });
    const listing = await Listing.findOne({ _id: req.params.id, farmer: farmer._id });
    if (!listing) return res.status(404).json({ message: 'Listing not found' });

    const { price, unit, quantityAvailable, qualityNotes, active } = req.body;
    if (price !== undefined) listing.price = price;
    if (unit !== undefined) listing.unit = unit;
    if (quantityAvailable !== undefined) listing.quantityAvailable = quantityAvailable;
    if (qualityNotes !== undefined) listing.qualityNotes = qualityNotes;
    if (active !== undefined) listing.active = active;
    listing.updatedAt = Date.now();
    await listing.save();

    res.json(listing);
  } catch (err) { next(err); }
};

// DELETE /api/listings/:id  (farmer only, must own the listing)
exports.deleteListing = async (req, res, next) => {
  try {
    const farmer = await Farmer.findOne({ user: req.user.id });
    const listing = await Listing.findOneAndDelete({ _id: req.params.id, farmer: farmer._id });
    if (!listing) return res.status(404).json({ message: 'Listing not found' });
    res.json({ message: 'Listing deleted' });
  } catch (err) { next(err); }
};
