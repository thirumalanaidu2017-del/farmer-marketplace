const express = require('express');
const router = express.Router();
const { searchByProduct } = require('../controllers/searchController');

// Public — customers can search without logging in
router.get('/', searchByProduct);

module.exports = router;
