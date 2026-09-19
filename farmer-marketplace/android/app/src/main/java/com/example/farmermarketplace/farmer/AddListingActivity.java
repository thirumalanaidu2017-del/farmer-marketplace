package com.example.farmermarketplace.farmer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.auth.SessionManager;
import com.example.farmermarketplace.models.Listing;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Farmer creates a new listing, e.g. "Rice, ₹45/kg, 200kg available".
public class AddListingActivity extends AppCompatActivity {

    private EditText productNameInput, priceInput, unitInput, quantityInput, notesInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_listing);

        productNameInput = findViewById(R.id.productNameInput);
        priceInput = findViewById(R.id.priceInput);
        unitInput = findViewById(R.id.unitInput);
        quantityInput = findViewById(R.id.quantityInput);
        notesInput = findViewById(R.id.notesInput);
        Button saveButton = findViewById(R.id.saveListingButton);

        saveButton.setOnClickListener(v -> saveListing());
    }

    private void saveListing() {
        String productName = productNameInput.getText().toString().trim();
        String priceStr = priceInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();
        String quantityStr = quantityInput.getText().toString().trim();
        String notes = notesInput.getText().toString().trim();

        if (productName.isEmpty() || priceStr.isEmpty() || quantityStr.isEmpty()) {
            Toast.makeText(this, "Product, price and quantity are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("productName", productName);
        body.put("price", Double.parseDouble(priceStr));
        body.put("unit", unit.isEmpty() ? "kg" : unit);
        body.put("quantityAvailable", Integer.parseInt(quantityStr));
        body.put("qualityNotes", notes);

        SessionManager session = new SessionManager(this);
        ApiClient.getService().createListing(session.getBearerToken(), body)
                .enqueue(new Callback<Listing>() {
                    @Override
                    public void onResponse(Call<Listing> call, Response<Listing> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddListingActivity.this, "Listing added", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddListingActivity.this, "Failed to add listing", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Listing> call, Throwable t) {
                        Toast.makeText(AddListingActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
