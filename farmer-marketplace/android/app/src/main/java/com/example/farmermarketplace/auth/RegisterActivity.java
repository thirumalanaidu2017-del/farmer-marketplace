package com.example.farmermarketplace.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.farmermarketplace.R;
import com.example.farmermarketplace.api.ApiClient;
import com.example.farmermarketplace.customer.SearchActivity;
import com.example.farmermarketplace.farmer.FarmerDashboardActivity;
import com.example.farmermarketplace.models.AuthResponse;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, phoneInput, passwordInput;
    private RadioGroup roleGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        roleGroup = findViewById(R.id.roleGroup);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> attemptRegister());
    }

    private void attemptRegister() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        // Only customer/farmer can self-register; admin accounts are created directly in the database.
        String role = roleGroup.getCheckedRadioButtonId() == R.id.roleFarmer ? "farmer" : "customer";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("phone", phone);
        body.put("password", password);
        body.put("role", role);

        ApiClient.getService().register(body).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse auth = response.body();
                    new SessionManager(RegisterActivity.this).save(
                            auth.token, auth.user.id, auth.user.role, auth.user.name);

                    Intent intent = "farmer".equals(auth.user.role)
                            ? new Intent(RegisterActivity.this, FarmerDashboardActivity.class)
                            : new Intent(RegisterActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed (email may already exist)", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
