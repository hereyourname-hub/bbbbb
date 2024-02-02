package com.example.bodyboost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StatActivity extends AppCompatActivity {
    SharedPreferences tracking;
    SharedPreferences.Editor ed;
    //    ProgressWheel progressWheel;
    TextView datestat, wenttosleep, wakeup;
    Button backtohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        initUI();
        statset();

        backtohome = findViewById(R.id.backtohome);
        backtohome.setOnClickListener(view -> {
            Intent intent = new Intent(StatActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        });

    }
    private void statset() {
        SharedPreferences tracking = getSharedPreferences("tracking", MODE_PRIVATE);
        ed = tracking.edit();
        Date curdate = new Date();
        datestat.setText(DateFormat.getDateInstance().format(curdate));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        SimpleDateFormat displaytime = new SimpleDateFormat("HH:mm");
        String sleepDurationText = null;
        try {
            Date storedate = formatter.parse(tracking.getString("counting", formatter.format(curdate)));
            double different = curdate.getTime() - storedate.getTime();
            double hour = different / 3600000;

            // Progress max is 360
            int progress = (int) Math.round(hour * 45);

            // Percent max is 100
            String percent = String.valueOf(Math.round(progress / 3.6));

            wenttosleep.setText(displaytime.format(storedate));
            wakeup.setText(displaytime.format(curdate));

            ed.remove("counting");
            ed.apply();

            // Рассчитать длительность сна в часах и минутах
            long durationInMillis = curdate.getTime() - storedate.getTime();
            long hours = durationInMillis / (1000 * 60 * 60);
            long minutes = (durationInMillis % (1000 * 60 * 60)) / (1000 * 60);

            // Установить длительность сна в TextView в формате "ЧЧ:ММ"
            sleepDurationText = String.format("%02d:%02d", hours, minutes);
            TextView sleepDurationTextView = findViewById(R.id.sleepDuration);
            sleepDurationTextView.setText(sleepDurationText);

            // Save the current sleep duration to SharedPreferences using the current date as the key
            ed.putString(DateFormat.getDateInstance().format(curdate), sleepDurationText);
            ed.apply();

            // Retrieve the previous sleep duration from SharedPreferences using the previous date as the key
            String previousSleepDuration = tracking.getString(DateFormat.getDateInstance().format(storedate), "");
            // Display the previous sleep duration in the appropriate TextView
            TextView previousSleepDurationTextView = findViewById(R.id.previousSleepDuration);
            previousSleepDurationTextView.setText(previousSleepDuration);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        // Inside the statset() method
        SharedPreferences.Editor editor = tracking.edit();
        editor.putString("previousSleepDuration", sleepDurationText); // Save the previousSleepDuration
        editor.apply();
    }


    private void initUI() {
//        progressWheel = findViewById(R.id.wheelprogress);
        datestat = findViewById(R.id.datestat);
        wenttosleep = findViewById(R.id.wenttosleep);
        wakeup = findViewById(R.id.wakeup);

    }
}