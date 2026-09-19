package com.example.farmermarketplace.admin;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.auth.SessionManager;
import com.example.farmermarketplace.models.Farmer;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Admin's job: approve/verify farmers before their listings become searchable.
public class AdminDashboardActivity extends AppCompatActivity {

    private SessionManager session;
    private final List<Farmer> pending = new ArrayList<>();
    private PendingFarmersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        session = new SessionManager(this);
        RecyclerView list = findViewById(R.id.pendingFarmersList);

        adapter = new PendingFarmersAdapter(pending, this::approveFarmer);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        loadPendingFarmers();
    }

    private void loadPendingFarmers() {
        ApiClient.getService().getPendingFarmers(session.getBearerToken())
                .enqueue(new Callback<List<Farmer>>() {
                    @Override
                    public void onResponse(Call<List<Farmer>> call, Response<List<Farmer>> response) {
                        pending.clear();
                        if (response.isSuccessful() && response.body() != null) {
                            pending.addAll(response.body());
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure(Call<List<Farmer>> call, Throwable t) {
                        Toast.makeText(AdminDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void approveFarmer(Farmer farmer, int position) {
        ApiClient.getService().verifyFarmer(session.getBearerToken(), farmer._id)
                .enqueue(new Callback<Farmer>() {
                    @Override
                    public void onResponse(Call<Farmer> call, Response<Farmer> response) {
                        if (response.isSuccessful()) {
                            pending.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(AdminDashboardActivity.this, "Farmer approved", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Farmer> call, Throwable t) {
                        Toast.makeText(AdminDashboardActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
