package com.example.bodyboost.Database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CalorieProduct {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "product_name")
    public String productName;

    @ColumnInfo(name = "product_amount")
    public int productAmount;

    @ColumnInfo(name = "amount_prefix")
    public String amountPrefix;

    @ColumnInfo(name = "calorie_amount")
    public int calorieAmount;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setAmountPrefix(String amountPrefix) {
        this.amountPrefix = amountPrefix;
    }

    public String getAmountPrefix() {
        return amountPrefix;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }
}
