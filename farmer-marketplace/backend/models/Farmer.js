const mongoose = require('mongoose');

// A Farmer profile extends a User (role = 'farmer').
const farmerSchema = new mongoose.Schema({
  user: { type: mongoose.Schema.Types.ObjectId, ref: 'User', required: true, unique: true },
  farmName: { type: String },
  address: { type: String },
  // GeoJSON point for map + "near me" search
  location: {
    type: { type: String, enum: ['Point'], default: 'Point' },
    coordinates: { type: [Number], default: [0, 0] } // [longitude, latitude]
  },
  verified: { type: Boolean, default: false }, // admin must approve before listings are searchable
  createdAt: { type: Date, default: Date.now }
});

farmerSchema.index({ location: '2dsphere' });

module.exports = mongoose.model('Farmer', farmerSchema);
