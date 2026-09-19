package com.example.farmermarketplace.models;

import java.util.List;

// Mirrors the GeoJSON Point returned by the backend: { type: "Point", coordinates: [lng, lat] }
public class GeoLocation {
    public String type;
    public List<Double> coordinates; // [longitude, latitude]

    public double getLongitude() { return coordinates != null && coordinates.size() > 0 ? coordinates.get(0) : 0; }
    public double getLatitude() { return coordinates != null && coordinates.size() > 1 ? coordinates.get(1) : 0; }
}
