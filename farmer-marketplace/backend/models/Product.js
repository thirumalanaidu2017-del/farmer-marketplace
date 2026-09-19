const mongoose = require('mongoose');

const productSchema = new mongoose.Schema({
  name: { type: String, required: true, trim: true },       // e.g. "Rice"
  category: { type: String, trim: true },                    // e.g. "Grains"
  createdAt: { type: Date, default: Date.now }
});

productSchema.index({ name: 'text' });

module.exports = mongoose.model('Product', productSchema);
