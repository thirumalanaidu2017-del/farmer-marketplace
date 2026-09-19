package com.example.farmermarketplace.auth;

import android.content.Context;
import android.content.SharedPreferences;

// Small wrapper around SharedPreferences to persist the JWT + role after login.
public class SessionManager {
    private static final String PREFS = "session";
    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void save(String token, String userId, String role, String name) {
        prefs.edit()
                .putString("token", token)
                .putString("userId", userId)
                .putString("role", role)
                .putString("name", name)
                .apply();
    }

    public String getToken() { return prefs.getString("token", null); }
    public String getBearerToken() { return "Bearer " + getToken(); }
    public String getRole() { return prefs.getString("role", null); }
    public String getName() { return prefs.getString("name", null); }
    public boolean isLoggedIn() { return getToken() != null; }

    public void clear() { prefs.edit().clear().apply(); }
}
