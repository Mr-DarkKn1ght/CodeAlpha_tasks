package com.example.healthtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    AppPrefs prefs;
    TextInputEditText email, password;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_login);
        prefs = new AppPrefs(this);

        if (prefs.isLoggedIn()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
            return;
        }

        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

        ((Button)findViewById(R.id.loginButton)).setOnClickListener(v -> login());
        ((Button)findViewById(R.id.registerButton)).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String e = String.valueOf(email.getText()).trim();
        String pw = String.valueOf(password.getText());
        String savedE = prefs.get("email", "");
        String savedPw = prefs.get("password", "");

        if (e.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show();
        } else if (e.equals(savedE) && pw.equals(savedPw)) {
            prefs.put("loggedIn", "true");
            prefs.put("email", e);
            getSharedPreferences("health_tracker", MODE_PRIVATE).edit().putBoolean("loggedIn", true).apply();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid login details", Toast.LENGTH_SHORT).show();
        }
    }
}
