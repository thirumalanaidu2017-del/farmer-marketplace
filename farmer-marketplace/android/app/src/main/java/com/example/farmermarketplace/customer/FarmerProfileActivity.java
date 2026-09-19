package com.example.farmermarketplace.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.models.Farmer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Full farmer profile: contact info + map location + a "Call" action.
// Reached by tapping a search result.
public class FarmerProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);

        String farmerId = getIntent().getStringExtra("farmerId");
        String farmerPhone = getIntent().getStringExtra("farmerPhone");

        TextView nameText = findViewById(R.id.profileFarmerName);
        TextView addressText = findViewById(R.id.profileAddress);
        Button callButton = findViewById(R.id.callButton);

        callButton.setOnClickListener(v -> {
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + farmerPhone));
            startActivity(dial);
        });

        ApiClient.getService().getFarmerById(farmerId).enqueue(new Callback<Farmer>() {
            @Override
            public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Farmer farmer = response.body();
                    nameText.setText(farmer.user.name + (farmer.farmName != null ? " · " + farmer.farmName : ""));
                    addressText.setText(farmer.address != null ? farmer.address : "No address set");
                    // TODO: drop a marker at farmer.location.getLatitude()/getLongitude() on a MapView here
                }
            }

            @Override
            public void onFailure(Call<Farmer> call, Throwable t) { /* no-op, keep values from intent */ }
        });
    }
}
