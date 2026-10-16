package com.example.healthtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    AppPrefs prefs;
    EditText name, email, password;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_register);
        prefs = new AppPrefs(this);
        name = findViewById(R.id.nameEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);

        ((Button)findViewById(R.id.registerButton)).setOnClickListener(v -> register());
    }

    private void register() {
        String n = name.getText().toString().trim();
        String e = email.getText().toString().trim();
        String pw = password.getText().toString();

        if (n.isEmpty() || e.isEmpty() || pw.isEmpty()) {
            Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        prefs.put("name", n);
        prefs.put("email", e);
        prefs.put("password", pw);
        prefs.put("loggedIn", "true");
        getSharedPreferences("health_tracker", MODE_PRIVATE).edit().putBoolean("loggedIn", true).apply();

        startActivity(new Intent(this, HealthInputActivity.class));
        finish();
    }
}
