package com.example.healthtracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class HealthInputActivity extends AppCompatActivity {
    AppPrefs p;
    EditText age, gender, height, weight, steps, water;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_health_input);
        p = new AppPrefs(this);

        age=findViewById(R.id.ageEditText);
        gender=findViewById(R.id.genderEditText);
        height=findViewById(R.id.heightEditText);
        weight=findViewById(R.id.weightEditText);
        steps=findViewById(R.id.stepGoalEditText);
        water=findViewById(R.id.waterGoalEditText);

        age.setText(p.get("age",""));
        gender.setText(p.get("gender",""));
        height.setText(p.get("height",""));
        weight.setText(p.get("weight",""));
        steps.setText(p.get("stepGoal","6000"));
        water.setText(p.get("waterGoal","8"));

        ((Button)findViewById(R.id.saveButton)).setOnClickListener(v -> save());
    }

    private void save() {
        if (age.getText().toString().trim().isEmpty() ||
            height.getText().toString().trim().isEmpty() ||
            weight.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please enter age, height and weight", Toast.LENGTH_SHORT).show();
            return;
        }
        p.put("age", age.getText().toString());
        p.put("gender", gender.getText().toString());
        p.put("height", height.getText().toString());
        p.put("weight", weight.getText().toString());
        p.put("stepGoal", steps.getText().toString());
        p.put("waterGoal", water.getText().toString());
        p.putInt("waterToday", 0);
        startActivity(new Intent(this, DashboardActivity.class));
        finish();
    }
}
