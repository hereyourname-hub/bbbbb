package com.example.bodyboost;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.bodyboost.Database.CalorieDAO;
import com.example.bodyboost.Database.CalorieDatabase;
import com.example.bodyboost.Database.CalorieDay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ProgressActivity extends AppCompatActivity {

    private final Utilities utilities = new Utilities();

    private final SimpleDateFormat shortDateFormat = new SimpleDateFormat("d.M EEE", Locale.ENGLISH);
    private final SimpleDateFormat longDateFormat = new SimpleDateFormat("y-MM-d", Locale.ENGLISH);

    private CalorieDAO calorieDAO;

    private final List<String> calorieDates = new ArrayList<>();
    private final List<Integer> calories = new ArrayList<>();
    private final List<TextView> textViews = new ArrayList<>();
    private final List<ProgressBar> progressBars = new ArrayList<>();

    private final List<Integer> progressBarIds = Arrays.asList(
            R.id.progressBar1,
            R.id.progressBar2,
            R.id.progressBar3,
            R.id.progressBar4,
            R.id.progressBar5,
            R.id.progressBar6,
            R.id.progressBar7
    );
    private final List<Integer> textIds = Arrays.asList(
            R.id.daySeven,
            R.id.daySix,
            R.id.dayFive,
            R.id.dayFour,
            R.id.dayThree,
            R.id.dayTwo,
            R.id.dayOne
    );


    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        CalorieDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "CalorieDatabase"
        ).allowMainThreadQueries().build();

        calorieDAO = db.calorieDAO();
        getProgressData();
        getScreenItems();

        int max = utilities.getMax(calories);

        for (int i = 0; i < 7; i++) {
            String calorieDate = calorieDates.get(i);
            int calsToday = calories.get(i);

            ProgressBar progressBar = progressBars.get(i);
            progressBar.setMax(max);
            progressBar.setProgress(calsToday);

            TextView calsText = textViews.get(i);
            calsText.setText(String.format("%d", calsToday));
            calsText.setTextSize(15);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                calsText.setTooltipText(calorieDate);
            }
        }
    }

    private void getScreenItems() {
        for (int i = 0; i < 7; i++) {
            ProgressBar progressBar = findViewById(progressBarIds.get(i));
            TextView textView = findViewById(textIds.get(i));

            progressBars.add(progressBar);
            textViews.add(textView);
        }
    }

    private void getProgressData() {
        String startDate = utilities.getCurrentDate(longDateFormat);
        CalorieDay calorieDayToday = calorieDAO.getCalorieDayByDate(startDate);
        int caloriesToday = 0;

        if (calorieDayToday != null) { // If there's a record for today, get data from it.
            startDate = calorieDayToday.getCalorieDate();
            caloriesToday = calorieDayToday.getCalorieAmount();
        }

        for (int i = -6; i < 0; i++) { // This counts 6 days backwards and tries to get the CalorieDays.
            String dateToCheck = utilities.getDate(startDate, i);
            CalorieDay calorieDay = calorieDAO.getCalorieDayByDate(dateToCheck);

            if (calorieDay != null) { // If CalorieDay existed on that day, get values from that day.
                calorieDates.add(
                        utilities.convertFromLongToShort(calorieDay.getCalorieDate())
                );
                calories.add(calorieDay.getCalorieAmount());
            } else { // CalorieDay didn't exist on that day, "create" a day with 0 calories.
                calorieDates.add(utilities.convertFromLongToShort(dateToCheck));
                calories.add(0);
            }
        }

        calorieDates.add(utilities.getCurrentDate(shortDateFormat));
        calories.add(caloriesToday);
    }

}