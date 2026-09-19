package com.example.farmermarketplace.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.admin.AdminDashboardActivity;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.customer.SearchActivity;
import com.example.farmermarketplace.farmer.FarmerDashboardActivity;
import com.example.farmermarketplace.models.AuthResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginButton);
        TextView registerLink = findViewById(R.id.registerLink);

        // Already logged in? skip straight to the right dashboard.
        SessionManager session = new SessionManager(this);
        if (session.isLoggedIn()) {
            routeToDashboard(session.getRole());
        }

        loginButton.setOnClickListener(v -> attemptLogin());
        registerLink.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        ApiClient.getService().login(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();
                    new SessionManager(LoginActivity.this).save(
                            auth.token, auth.user.id, auth.user.role, auth.user.name);
                    routeToDashboard(auth.user.role);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void routeToDashboard(String role) {
        Intent intent;
        switch (role) {
            case "farmer":
                intent = new Intent(this, FarmerDashboardActivity.class);
                break;
            case "admin":
                intent = new Intent(this, AdminDashboardActivity.class);
                break;
            default:
                intent = new Intent(this, SearchActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
