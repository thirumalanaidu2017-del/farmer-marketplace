const express = require('express');
const router = express.Router();
const { verifyToken, requireRole } = require('../middleware/auth');
const {
  createListing, getMyListings, updateListing, deleteListing
} = require('../controllers/listingController');

router.post('/', verifyToken, requireRole('farmer'), createListing);
router.get('/mine', verifyToken, requireRole('farmer'), getMyListings);
router.put('/:id', verifyToken, requireRole('farmer'), updateListing);
router.delete('/:id', verifyToken, requireRole('farmer'), deleteListing);

module.exports = router;
