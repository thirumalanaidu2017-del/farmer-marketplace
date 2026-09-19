package com.example.farmermarketplace.models;

// The farmer block embedded directly in a search result -
// this is what lets the UI show farmer + location without a second API call.
public class FarmerInfo {
    public String id;
    public String name;
    public String phone;
    public String farmName;
    public String address;
    public GeoLocation location;
}
