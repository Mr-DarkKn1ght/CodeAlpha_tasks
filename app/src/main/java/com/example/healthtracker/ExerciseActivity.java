package com.example.healthtracker;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ExerciseActivity extends AppCompatActivity {
    AppPrefs p;
    TextView timerText, caloriesText;
    Spinner spinner;
    Button startStop, save;
    Handler handler = new Handler();
    long startTime = 0;
    boolean running = false;
    int seconds = 0;

    final Runnable timerRunnable = new Runnable() {
        @Override public void run() {
            if (running) {
                seconds = (int)((System.currentTimeMillis()-startTime)/1000);
                int min=seconds/60, sec=seconds%60;
                timerText.setText(String.format("%02d:%02d",min,sec));
                caloriesText.setText("Calories: " + Math.round(seconds * 0.12f) + " kcal");
                handler.postDelayed(this, 500);
            }
        }
    };

    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_exercise);
        p = new AppPrefs(this);
        spinner=findViewById(R.id.exerciseSpinner);
        timerText=findViewById(R.id.timerText);
        caloriesText=findViewById(R.id.caloriesExerciseText);
        startStop=findViewById(R.id.startStopButton);
        save=findViewById(R.id.saveExerciseButton);

        String[] exercises={"Walking","Running","Cycling","Squats","Push Ups","Plank","Jumping Jacks"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, exercises));

        startStop.setOnClickListener(v -> {
            if (!running) {
                running=true;
                startTime=System.currentTimeMillis() - seconds*1000L;
                startStop.setText("STOP");
                handler.post(timerRunnable);
            } else {
                running=false;
                startStop.setText("START");
            }
        });

        save.setOnClickListener(v -> {
            p.put("lastExercise", spinner.getSelectedItem().toString());
            p.putInt("exerciseSeconds", seconds);
            p.putInt("exerciseCalories", Math.round(seconds*0.12f));
            finish();
        });
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(timerRunnable);
    }
}
