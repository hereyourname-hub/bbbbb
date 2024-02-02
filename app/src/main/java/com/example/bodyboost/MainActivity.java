package com.example.bodyboost;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupMenuNavigation();


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

//        here
        CardView findWater = findViewById(R.id.tv_water_card);
        findWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WaterActivity.class));
            }
        });

        CardView findMedicine = findViewById(R.id.tv_medicine_card);
        findMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MedicineActivity.class));
            }
        });


        CardView findFood = findViewById(R.id.tv_food_card);
        findFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainActivity2.class));
            }
        });

        CardView findSleep = findViewById(R.id.tv_sleep_card);
        findSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SleepActivity.class));
            }
        });

    }

    private void setupMenuNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    // Открываем активити HomeActivity
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                } else if (id == R.id.fit) {
                    // Открываем активити FitActivity
                    startActivity(new Intent(MainActivity.this, FitActivity.class));
                    return true;
                } else if (id == R.id.profile) {
                    // Открываем активити ProfileActivity
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}