const express = require('express');
const router = express.Router();
const { verifyToken, requireRole } = require('../middleware/auth');
const { listPendingFarmers, verifyFarmer, listUsers } = require('../controllers/adminController');

router.get('/farmers/pending', verifyToken, requireRole('admin'), listPendingFarmers);
router.put('/farmers/:id/verify', verifyToken, requireRole('admin'), verifyFarmer);
router.get('/users', verifyToken, requireRole('admin'), listUsers);

module.exports = router;
