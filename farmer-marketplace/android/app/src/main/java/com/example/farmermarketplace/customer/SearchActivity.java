package com.example.farmermarketplace.customer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.models.SearchResult;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Customer-facing search: type a product name (e.g. "rice"), get back a list
// of farmers selling it with price, distance and location - straight to the
// point, no product detail page in between.
public class SearchActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST = 100;

    private EditText searchInput;
    private RecyclerView resultsList;
    private SearchResultsAdapter adapter;
    private final List<SearchResult> results = new ArrayList<>();
    private FusedLocationProviderClient locationClient;
    private Double lastKnownLat, lastKnownLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchInput = findViewById(R.id.searchInput);
        ImageButton searchButton = findViewById(R.id.searchButton);
        resultsList = findViewById(R.id.resultsList);

        adapter = new SearchResultsAdapter(results);
        resultsList.setLayoutManager(new LinearLayoutManager(this));
        resultsList.setAdapter(adapter);

        locationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocationThenReady();

        searchButton.setOnClickListener(v -> performSearch());
    }

    private void requestLocationThenReady() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST);
            return;
        }
        fetchLastLocation();
    }

    @SuppressWarnings("MissingPermission")
    private void fetchLastLocation() {
        locationClient.getLastLocation().addOnSuccessListener(this, (Location location) -> {
            if (location != null) {
                lastKnownLat = location.getLatitude();
                lastKnownLng = location.getLongitude();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST
                && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLastLocation();
        }
        // if denied, search still works - just falls back to cheapest-first sorting server-side
    }

    private void performSearch() {
        String query = searchInput.getText().toString().trim();
        if (query.isEmpty()) {
            Toast.makeText(this, "Enter a product to search, e.g. rice", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiClient.getService().search(query, lastKnownLng, lastKnownLat, null)
                .enqueue(new Callback<List<SearchResult>>() {
                    @Override
                    public void onResponse(Call<List<SearchResult>> call, Response<List<SearchResult>> response) {
                        results.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            results.addAll(response.body());
                        }
                        adapter.notifyDataSetChanged();
                        if (results.isEmpty()) {
                            Toast.makeText(SearchActivity.this, "No farmers found for \"" + query + "\"", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<SearchResult>> call, Throwable t) {
                        Toast.makeText(SearchActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
