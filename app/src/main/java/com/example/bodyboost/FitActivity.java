package com.example.bodyboost;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FitActivity extends AppCompatActivity {

    Button button1,button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fit);
        setupMenuNavigation();
        button1 = findViewById(R.id.startfit1);
        button2 = findViewById(R.id.startfit2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FitActivity.this, SecondActivity.class);
                startActivity(intent);


            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FitActivity.this, SecondActivity2.class);
                startActivity(intent);


            }
        });

    }

    public void beforeage18(View view) {

        Intent intent = new Intent(FitActivity.this, SecondActivity.class);
        startActivity(intent);
    }

    public void Afterage18(View view) {

        Intent intent = new Intent(FitActivity.this, SecondActivity2.class);
        startActivity(intent);
    }

    public void food(View view) {

        Intent intent = new Intent(FitActivity.this, FoodActivity2.class);
        startActivity(intent);


    }

    private void setupMenuNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.fit) {
                    // Открываем активити FitActivity
                    startActivity(new Intent(FitActivity.this, FitActivity.class));
                    return true;
                }else if (id == R.id.home) {
                    // Открываем активити HomeActivity
                    startActivity(new Intent(FitActivity.this, MainActivity.class));
                    return true;
                }else if (id == R.id.profile) {
                    // Открываем активити ProfileActivity
                    startActivity(new Intent(FitActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }


}