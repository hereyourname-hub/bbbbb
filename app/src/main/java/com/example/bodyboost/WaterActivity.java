package com.example.bodyboost;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class WaterActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "WaterTrackerPrefs";
    private static final String KEY_CONSUMED_WATER = "consumedWater";
    private static final String KEY_LAST_DATE = "lastDate";

    private int dailyGoal = 2000; // Дневная норма воды
    private int consumedWater = 0; // Потребленная вода

    private TextView tvDailyGoal;
    private TextView tvConsumedWater;
    private Button btnAdd50ml;
    private Button btnAdd100ml;
    private Button btnAdd150ml;
    private Button btnAdd200ml;
    private Button btnDeleteEntry;
    private ProgressBar progressBar;
    private TextView tvPercentage;

    private Button mGo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);

        tvDailyGoal = findViewById(R.id.tvDailyGoal);
        tvConsumedWater = findViewById(R.id.tvConsumedWater);
        btnAdd50ml = findViewById(R.id.btnAdd50ml);
        btnAdd100ml = findViewById(R.id.btnAdd100ml);
        btnAdd150ml = findViewById(R.id.btnAdd150ml);
        btnAdd200ml = findViewById(R.id.btnAdd200ml);
        btnDeleteEntry = findViewById(R.id.btnDeleteEntry);
        progressBar = findViewById(R.id.progressBar);
        tvPercentage = findViewById(R.id.tvPercentage);
        loadConsumedWater(); // Загрузка сохраненного значения потребленной воды

        updateUI();

        btnAdd50ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater(50);
            }
        });

        btnAdd100ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater(100);
            }
        });

        btnAdd150ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater(150);
            }
        });

        btnAdd200ml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWater(200);
            }
        });

        btnDeleteEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry();
            }
        });
    }

    private void loadConsumedWater() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        consumedWater = preferences.getInt(KEY_CONSUMED_WATER, 0);

        long lastSavedTime = preferences.getLong(KEY_LAST_DATE, 0);
        Calendar lastDate = Calendar.getInstance();
        lastDate.setTimeInMillis(lastSavedTime);

        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.DAY_OF_YEAR, -1); // Отнимаем 1 день от текущей даты

        if (lastDate.before(currentTime)) {
            // Если прошло более 24 часов с последнего сохранения, сбрасываем значения
            consumedWater = 0;
            saveConsumedWater();
        }
    }

    private void saveConsumedWater() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_CONSUMED_WATER, consumedWater);
        editor.putLong(KEY_LAST_DATE, Calendar.getInstance().getTimeInMillis());
        editor.apply();
    }

    private void addWater(int amount) {
        consumedWater += amount; // Увеличиваем потребление на указанное количество
        saveConsumedWater();
        updateUI();
//сохраняем значение чтобы передать в профиль
        SharedPreferences preferences = getSharedPreferences("tracking", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("consumedWater", consumedWater);
        editor.apply();
//        вода
    }

    private void deleteEntry() {
        consumedWater = 0; // Устанавливаем потребленную воду в 0
        saveConsumedWater();
        updateUI();
    }

    private void updateUI() {
        tvDailyGoal.setText("Daily Goal: " + dailyGoal + " ml");
        tvConsumedWater.setText("Consumed: " + consumedWater + " ml");
        progressBar.setProgress(consumedWater);
        progressBar.setSecondaryProgress(consumedWater); // Устанавливаем вторичное значение для заполненности
        updateProgressBarPercentage();
    }

    private void updateProgressBarPercentage() {
        int progress = (int) ((float) consumedWater / (float) dailyGoal * 100); // Расчет процента заполненности
        tvPercentage.setText(progress + "%");
    }


}
