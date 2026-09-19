const Farmer = require('../models/Farmer');

// GET /api/farmers/me  (farmer only)
exports.getMyProfile = async (req, res, next) => {
  try {
    const farmer = await Farmer.findOne({ user: req.user.id }).populate('user', 'name email phone');
    if (!farmer) return res.status(404).json({ message: 'Farmer profile not found' });
    res.json(farmer);
  } catch (err) { next(err); }
};

// PUT /api/farmers/me  (farmer only)
// body: { farmName, address, longitude, latitude }
exports.updateMyProfile = async (req, res, next) => {
  try {
    const { farmName, address, longitude, latitude } = req.body;
    const update = {};
    if (farmName !== undefined) update.farmName = farmName;
    if (address !== undefined) update.address = address;
    if (longitude !== undefined && latitude !== undefined) {
      update.location = { type: 'Point', coordinates: [longitude, latitude] };
    }
    const farmer = await Farmer.findOneAndUpdate({ user: req.user.id }, update, { new: true });
    if (!farmer) return res.status(404).json({ message: 'Farmer profile not found' });
    res.json(farmer);
  } catch (err) { next(err); }
};

// GET /api/farmers/:id  (public - customer viewing a farmer's profile)
exports.getFarmerById = async (req, res, next) => {
  try {
    const farmer = await Farmer.findById(req.params.id).populate('user', 'name phone');
    if (!farmer) return res.status(404).json({ message: 'Farmer not found' });
    res.json(farmer);
  } catch (err) { next(err); }
};
