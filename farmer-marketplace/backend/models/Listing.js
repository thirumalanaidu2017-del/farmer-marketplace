const mongoose = require('mongoose');

// A Listing is a farmer selling a specific product at a price/quantity.
// This is the collection that search actually queries.
const listingSchema = new mongoose.Schema({
  farmer: { type: mongoose.Schema.Types.ObjectId, ref: 'Farmer', required: true },
  product: { type: mongoose.Schema.Types.ObjectId, ref: 'Product', required: true },
  price: { type: Number, required: true },
  unit: { type: String, default: 'kg' },
  quantityAvailable: { type: Number, required: true },
  qualityNotes: { type: String },
  active: { type: Boolean, default: true },
  updatedAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Listing', listingSchema);
