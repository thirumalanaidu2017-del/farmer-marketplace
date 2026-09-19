package com.example.farmermarketplace.api;

import com.example.farmermarketplace.models.AuthResponse;
import com.example.farmermarketplace.models.Farmer;
import com.example.farmermarketplace.models.Listing;
import com.example.farmermarketplace.models.Product;
import com.example.farmermarketplace.models.SearchResult;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ---- Auth ----
    @POST("api/auth/register")
    Call<AuthResponse> register(@Body Map<String, Object> body);

    @POST("api/auth/login")
    Call<AuthResponse> login(@Body Map<String, String> body);

    // ---- Search (public, customer-facing) ----
    // Returns farmer details + location directly per result - no product detail page needed.
    @GET("api/search")
    Call<List<SearchResult>> search(
            @Query("product") String product,
            @Query("lng") Double lng,
            @Query("lat") Double lat,
            @Query("maxDistanceKm") Double maxDistanceKm
    );

    // ---- Farmer ----
    @GET("api/farmers/me")
    Call<Farmer> getMyFarmerProfile(@Header("Authorization") String bearerToken);

    @PUT("api/farmers/me")
    Call<Farmer> updateMyFarmerProfile(@Header("Authorization") String bearerToken, @Body Map<String, Object> body);

    @GET("api/farmers/{id}")
    Call<Farmer> getFarmerById(@Path("id") String id);

    @POST("api/listings")
    Call<Listing> createListing(@Header("Authorization") String bearerToken, @Body Map<String, Object> body);

    @GET("api/listings/mine")
    Call<List<Listing>> getMyListings(@Header("Authorization") String bearerToken);

    // ---- Admin ----
    @GET("api/admin/farmers/pending")
    Call<List<Farmer>> getPendingFarmers(@Header("Authorization") String bearerToken);

    @PUT("api/admin/farmers/{id}/verify")
    Call<Farmer> verifyFarmer(@Header("Authorization") String bearerToken, @Path("id") String id);

    // ---- Products ----
    @GET("api/products")
    Call<List<Product>> listProducts();
}
