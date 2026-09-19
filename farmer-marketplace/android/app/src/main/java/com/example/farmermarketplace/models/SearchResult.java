package com.example.farmermarketplace.models;

public class SearchResult {
    public String listingId;
    public String product;
    public double price;
    public String unit;
    public int quantityAvailable;
    public FarmerInfo farmer;
    public Double distanceKm; // null if customer location wasn't sent
}
