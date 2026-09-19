const Farmer = require('../models/Farmer');
const User = require('../models/User');

// GET /api/admin/farmers/pending  (admin only)
exports.listPendingFarmers = async (req, res, next) => {
  try {
    const pending = await Farmer.find({ verified: false }).populate('user', 'name email phone');
    res.json(pending);
  } catch (err) { next(err); }
};

// PUT /api/admin/farmers/:id/verify  (admin only)
exports.verifyFarmer = async (req, res, next) => {
  try {
    const farmer = await Farmer.findByIdAndUpdate(req.params.id, { verified: true }, { new: true });
    if (!farmer) return res.status(404).json({ message: 'Farmer not found' });
    res.json(farmer);
  } catch (err) { next(err); }
};

// GET /api/admin/users  (admin only)
exports.listUsers = async (req, res, next) => {
  try {
    const users = await User.find().select('-password');
    res.json(users);
  } catch (err) { next(err); }
};
