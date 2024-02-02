package com.example.bodyboost;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.example.bodyboost.Database.CalorieDAO;
import com.example.bodyboost.Database.CalorieDatabase;
import com.example.bodyboost.Database.ProductDAO;
import com.example.bodyboost.Database.ProductDatabase;

public class SettingsActivity extends AppCompatActivity {

    private TextView newDailyGoal;

    private CalorieDAO calorieDAO;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        newDailyGoal = findViewById(R.id.newDailyGoal);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        newDailyGoal.setText(String.valueOf(pref.getInt("kcalDailyGoal", 0)));

        newDailyGoal.setOnEditorActionListener((v, actionId, event) -> {
            storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
            finish();
            return true;
        });

        CalorieDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                CalorieDatabase.class,
                "CalorieDatabase"
        ).allowMainThreadQueries().build();
        calorieDAO = db.calorieDAO();

        ProductDatabase productDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ProductDatabase.class,
                "ProductDatabase"
        ).allowMainThreadQueries().build();
        productDAO = productDatabase.productDAO();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        return true;
    }

    public void deleteAllQuestion(View view) {
        new MaterialAlertDialogBuilder(this, R.style.DeletePopup)
                .setTitle("Are you sure you want to delete all data?")
                .setMessage("This can't be undone.")
                .setPositiveButton("DELETE", (dialogInterface, i) -> youSure())
                .setNeutralButton("CANCEL", (dialogInterface, i) -> {})
                .show();
    }

    private void youSure() {
        new MaterialAlertDialogBuilder(this, R.style.DeletePopup)
                .setTitle("Are you sure, you want to do that?")
                .setMessage("Click CANCEL to dismiss.")
                .setPositiveButton("YES", (dialogInterface, i) -> deleteAll())
                .setNeutralButton("CANCEL", (dialogInterface, i) -> {})
                .show();
    }

    private void deleteAll() {
        calorieDAO.deleteALL();
        productDAO.deleteALL();

        View contextView = this.findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(contextView, "All data deleted.", Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public void applyChanges(View view) {
        storeCalories(Integer.parseInt(newDailyGoal.getText().toString()));
        finish();
    }

    private void storeCalories(int newGoal) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("CalorieCounter", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("kcalDailyGoal", newGoal);
        editor.apply();
    }

}