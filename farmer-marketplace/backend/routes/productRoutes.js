const express = require('express');
const router = express.Router();
const { verifyToken, requireRole } = require('../middleware/auth');
const { listProducts, createProduct } = require('../controllers/productController');

router.get('/', listProducts);
router.post('/', verifyToken, requireRole('admin'), createProduct);

module.exports = router;
