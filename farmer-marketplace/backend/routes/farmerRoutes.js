const express = require('express');
const router = express.Router();
const { verifyToken, requireRole } = require('../middleware/auth');
const { getMyProfile, updateMyProfile, getFarmerById } = require('../controllers/farmerController');

router.get('/me', verifyToken, requireRole('farmer'), getMyProfile);
router.put('/me', verifyToken, requireRole('farmer'), updateMyProfile);
router.get('/:id', getFarmerById); // public profile view

module.exports = router;
