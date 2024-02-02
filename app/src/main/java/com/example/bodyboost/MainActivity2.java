package com.example.bodyboost;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodyboost.Database.CalorieDAO;
import com.example.bodyboost.Database.CalorieDatabase;
import com.example.bodyboost.Database.CalorieDay;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {

    private final Utilities utilities = new Utilities();

    private TextView inputCalories;
    private TextView totalCalories;

    private ProgressBar progressBar;

    private static final String kcal = "kcal🔥";
    private int kcalDailyGoal;

    private ActivityResultLauncher<Intent> settingsResultLauncher;
    private ActivityResultLauncher<Intent> progressResultLauncher;
    private ActivityResultLauncher<Intent> productsResultLauncher;
    private CalorieDAO calorieDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        CalorieDatabase calorieDatabase = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "CalorieDatabase"
        ).allowMainThreadQueries().build();
        calorieDAO = calorieDatabase.calorieDAO();

        inputCalories = findViewById(R.id.newDailyGoal);
        totalCalories = findViewById(R.id.totalCalories);
        progressBar = findViewById(R.id.dailyProgressBar);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        kcalDailyGoal = pref.getInt("kcalDailyGoal", 2242);

        if (utilities.todayCalorieDayExists(calorieDAO)) {
            int storedCalories = pref.getInt("caloriesToday", 0);
            setTotalCalories(storedCalories);
        } else {
            setTotalCalories(0);
            storeCalories(0);
        }

        inputCalories.setOnEditorActionListener((v, actionId, event) -> {
            addCalories();
            return true;
        });

        progressResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
                );

        productsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (utilities.todayCalorieDayExists(calorieDAO)) {
                        CalorieDay calorieDay = calorieDAO.getLastRecords(1).get(0);
                        int currentCalorieAmount = calorieDay.getCalorieAmount();
                        setTotalCalories(currentCalorieAmount);
                    }
                }
        );

        settingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    kcalDailyGoal = pref.getInt("kcalDailyGoal", 2242);
                    String totalCals = totalCalories.getText().toString();
                    totalCals = totalCals.replaceFirst("/.*", "");

                    setTotalCalories(Integer.parseInt(totalCals));
                });
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.top_bar, menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settingsButton) {
            Intent intent = new Intent(this, SettingsActivity.class);
            settingsResultLauncher.launch(intent);
            return true;
        } else if (item.getItemId() == R.id.progressButton) {
            Intent intent = new Intent(this, ProgressActivity.class);
            progressResultLauncher.launch(intent);
            return true;
        } else if (item.getItemId() == R.id.deleteButton) {
            setTotalCalories(0);
            storeCalories(0);
        }

        return super.onOptionsItemSelected(item);
    }

    public void addItems(View view) {
        Intent intent = new Intent(this, ProductActivity.class);
        productsResultLauncher.launch(intent);
    }

    private void addCalories() {
        String inputText = inputCalories.getText().toString();
        if (inputText.isEmpty()) return;

        String firstChar = String.valueOf(inputText.charAt(0));
        if (firstChar.equals("0")) return;

        int calories = Integer.parseInt(inputText);

        String totalCals = totalCalories.getText().toString();
        totalCals = totalCals.replace("/" + kcalDailyGoal + " " + kcal, "");

        int newTotalCals = Integer.parseInt(totalCals) + calories;

        setTotalCalories(newTotalCals);
        storeCalories(newTotalCals);
    }

    @SuppressLint("DefaultLocale")
    private void setTotalCalories(int calories) {
        totalCalories.setText(String.format("%d/%d %s", calories, kcalDailyGoal, kcal));
        calculateCalorieProgress(calories);

        // Получаем экземпляр разделяемого класса
        SharedData sharedData = SharedData.getInstance();

        // Получаем значение totalCalories
        int totalCaloriesValue = Integer.parseInt(totalCalories.getText().toString().split("/")[0]);

        // Устанавливаем значение totalCalories в разделяемом классе
        sharedData.setTotalCalories(totalCaloriesValue);
    }

    private void calculateCalorieProgress(int calories) {
        if (calories <= 0) {
            progressBar.setProgress(0);
            return;
        }

        if (calories >= kcalDailyGoal) {
            progressBar.setProgress(100);
            return;
        }

        float calculation = (float)calories / kcalDailyGoal;
        int precision = 3;
        if (String.valueOf(calculation).length() > 3) precision = 4;

        String mainProgressValues = String.valueOf(calculation).substring(2, precision);

        if (precision == 3) {
            mainProgressValues += "0";
        }
        int progress = Integer.parseInt(mainProgressValues);

        progressBar.setProgress(progress);
    }

    private void storeCalories(int totalCalories) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("caloriesToday", totalCalories);
        editor.apply();

        if (utilities.todayCalorieDayExists(calorieDAO)) {
            CalorieDay calorieDay = calorieDAO.getLastRecords(1).get(0);
            calorieDay.setCalorieAmount(totalCalories);
            calorieDAO.update(calorieDay);
        } else {
            CalorieDay calorieDay = new CalorieDay();
            calorieDay.setCalorieAmount(totalCalories);
            calorieDay.setCalorieDate(utilities.getCurrentDate(
                    new SimpleDateFormat("y-MM-d", Locale.ENGLISH)
                )
            );
            calorieDAO.insert(calorieDay);
        }
    }
}