package com.example.bodyboost;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupMenuNavigation();

        // Получаем экземпляр разделяемого класса
        SharedData sharedData = SharedData.getInstance();

        // Получаем значение totalCalories из разделяемого класса
        int totalCaloriesValue = sharedData.getTotalCalories();

        // Находим элемент food_prof и устанавливаем полученное значение
        TextView foodProfTextView = findViewById(R.id.food_prof);
        foodProfTextView.setText(totalCaloriesValue + " ");

        // Получаем сохраненное значение consumedWater из SharedPreferences
        SharedPreferences preferences = getSharedPreferences("tracking", MODE_PRIVATE);
        int consumedWater = preferences.getInt("consumedWater", 0);

        // Находим элемент water_prof и устанавливаем полученное значение
        TextView waterProfTextView = findViewById(R.id.water_prof);
        waterProfTextView.setText(consumedWater + " ");

        // Получаем сохраненное значение previousSleepDuration из SharedPreferences
        String previousSleepDuration = preferences.getString("previousSleepDuration", "");

        // Если previousSleepDuration не установлено (т.е., равно ""), устанавливаем значение по умолчанию "0"
        if (previousSleepDuration.equals("")) {
            previousSleepDuration = "0";
        }

        // Находим элемент sleep_prof и устанавливаем полученное значение
        TextView sleepProfTextView = findViewById(R.id.sleep_prof);
        sleepProfTextView.setText(previousSleepDuration);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        tvName = findViewById(R.id.tv_name);
        tvEmail = findViewById(R.id.tv_email1);

        Button btnLogout = findViewById(R.id.btn_logOut);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutUser();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    tvName.setText("Name: " + user.name);
                    tvEmail.setText("Email: " + user.email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void LogoutUser() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupMenuNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    // Открываем активити HomeActivity
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    return true;
                } else if (id == R.id.fit) {
                    // Открываем активити FitActivity
                    startActivity(new Intent(ProfileActivity.this, FitActivity.class));
                    return true;
                } else if (id == R.id.profile) {
                    // Открываем активити ProfileActivity
                    startActivity(new Intent(ProfileActivity.this, ProfileActivity.class));
                    return true;
                }
                return false;
            }
        });
    }
}
