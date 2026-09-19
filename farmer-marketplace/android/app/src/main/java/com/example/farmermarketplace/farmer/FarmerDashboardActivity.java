package com.example.farmermarketplace.farmer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.auth.SessionManager;
import com.example.farmermarketplace.models.Farmer;
import com.example.farmermarketplace.models.Listing;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Farmer's home screen: see verification status, manage listings, add new ones.
public class FarmerDashboardActivity extends AppCompatActivity {

    private SessionManager session;
    private RecyclerView listingsList;
    private MyListingsAdapter adapter;
    private final List<Listing> listings = new ArrayList<>();
    private TextView verificationBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        session = new SessionManager(this);

        verificationBanner = findViewById(R.id.verificationBanner);
        Button addListingButton = findViewById(R.id.addListingButton);
        listingsList = findViewById(R.id.listingsList);

        adapter = new MyListingsAdapter(listings);
        listingsList.setLayoutManager(new LinearLayoutManager(this));
        listingsList.setAdapter(adapter);

        addListingButton.setOnClickListener(v ->
                startActivity(new Intent(this, AddListingActivity.class)));

        loadVerificationStatus();
        loadMyListings();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMyListings(); // refresh after returning from AddListingActivity
    }

    private void loadVerificationStatus() {
        ApiClient.getService().getMyFarmerProfile(session.getBearerToken())
                .enqueue(new Callback<Farmer>() {
                    @Override
                    public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            boolean verified = response.body().verified;
                            verificationBanner.setText(verified
                                    ? "Your account is verified - customers can find your listings."
                                    : "Pending admin verification - your listings won't appear in search yet.");
                        }
                    }
                    @Override
                    public void onFailure(Call<Farmer> call, Throwable t) { }
                });
    }

    private void loadMyListings() {
        ApiClient.getService().getMyListings(session.getBearerToken())
                .enqueue(new Callback<List<Listing>>() {
                    @Override
                    public void onResponse(Call<List<Listing>> call, Response<List<Listing>> response) {
                        listings.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            listings.addAll(response.body());
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<List<Listing>> call, Throwable t) {
                        Toast.makeText(FarmerDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
