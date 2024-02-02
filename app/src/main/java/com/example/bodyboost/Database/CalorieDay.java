package com.example.bodyboost.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class CalorieDay implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "calorie_date")
    public String calorieDate;

    @ColumnInfo(name = "calorie_amount")
    public int calorieAmount;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getCalorieDate() {
        return calorieDate;
    }

    public void setCalorieDate(String calorieDate) {
        this.calorieDate = calorieDate;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

}
