const Product = require('../models/Product');

// GET /api/products  (public - list/browse all products, e.g. for a category picker)
exports.listProducts = async (req, res, next) => {
  try {
    const products = await Product.find().sort({ name: 1 });
    res.json(products);
  } catch (err) { next(err); }
};

// POST /api/products  (admin only)
// body: { name, category }
exports.createProduct = async (req, res, next) => {
  try {
    const { name, category } = req.body;
    if (!name) return res.status(400).json({ message: 'name is required' });
    const product = await Product.create({ name, category });
    res.status(201).json(product);
  } catch (err) { next(err); }
};
