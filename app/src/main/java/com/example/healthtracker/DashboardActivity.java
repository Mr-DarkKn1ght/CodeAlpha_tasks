package com.example.healthtracker;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class DashboardActivity extends AppCompatActivity implements SensorEventListener {
    AppPrefs p;
    TextView greeting, stepsText, caloriesText, distanceText, waterText, bmiText;
    SensorManager sensorManager;
    Sensor stepSensor;
    int stepsToday = 0;
    float lastSensorValue = -1;

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_dashboard);
        p = new AppPrefs(this);

        greeting=findViewById(R.id.greetingText);
        stepsText=findViewById(R.id.stepsText);
        caloriesText=findViewById(R.id.caloriesText);
        distanceText=findViewById(R.id.distanceText);
        waterText=findViewById(R.id.waterText);
        bmiText=findViewById(R.id.bmiText);

        greeting.setText("Hello, " + p.get("name","User") + "!");
        loadDashboard();

        ((Button)findViewById(R.id.addWaterButton)).setOnClickListener(v -> {
            int water = p.getInt("waterToday", 0) + 1;
            p.putInt("waterToday", water);
            loadDashboard();
        });

        ((Button)findViewById(R.id.exerciseButton)).setOnClickListener(v ->
                startActivity(new Intent(this, ExerciseActivity.class)));

        ((Button)findViewById(R.id.editProfileButton)).setOnClickListener(v ->
                startActivity(new Intent(this, HealthInputActivity.class)));

        ((Button)findViewById(R.id.logoutButton)).setOnClickListener(v -> {
            p.clearLogin();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        setupStepCounter();
    }

    private void loadDashboard() {
        stepsToday = p.getInt("stepsToday", 0);
        int water = p.getInt("waterToday", 0);
        int goal = Integer.parseInt(p.get("waterGoal","8"));
        float weight = parseFloat(p.get("weight","0"));
        float height = parseFloat(p.get("height","0"));
        float bmi = height > 0 ? weight / ((height/100f)*(height/100f)) : 0;

        stepsText.setText("Steps: " + stepsToday + " / " + p.get("stepGoal","6000"));
        caloriesText.setText("Calories: " + Math.round(stepsToday * 0.04f) + " kcal");
        distanceText.setText(String.format("Distance: %.2f km", stepsToday * 0.00075f));
        waterText.setText("Water: " + water + " / " + goal + " glasses");
        bmiText.setText(bmi > 0 ? String.format("BMI: %.1f", bmi) : "BMI: --");
    }

    private float parseFloat(String s) {
        try { return Float.parseFloat(s); } catch(Exception e) { return 0; }
    }

    private void setupStepCounter() {
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepSensor != null) {
            if (android.os.Build.VERSION.SDK_INT >= 29 &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 10);
            } else {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override public void onSensorChanged(SensorEvent event) {
        if (lastSensorValue < 0) lastSensorValue = event.values[0];
        stepsToday = p.getInt("stepsToday", 0) + Math.max(0, Math.round(event.values[0] - lastSensorValue));
        lastSensorValue = event.values[0];
        p.putInt("stepsToday", stepsToday);
        loadDashboard();
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override protected void onResume() {
        super.onResume();
        if (stepSensor != null) {
            if (android.os.Build.VERSION.SDK_INT < 29 ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                == PackageManager.PERMISSION_GRANTED) {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
        loadDashboard();
    }

    @Override protected void onPause() {
        super.onPause();
        if (sensorManager != null) sensorManager.unregisterListener(this);
    }
}
