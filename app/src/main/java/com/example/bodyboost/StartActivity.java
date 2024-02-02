package com.example.bodyboost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    SharedPreferences tracking;
    SharedPreferences.Editor ed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        SharedPreferences tracking = getSharedPreferences("tracking", MODE_PRIVATE);
        if(!tracking.contains("counting")){
            Intent intent = new Intent(StartActivity.this, SleepActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(StartActivity.this, NightActivity.class);
            startActivity(intent);
        }



    }
}