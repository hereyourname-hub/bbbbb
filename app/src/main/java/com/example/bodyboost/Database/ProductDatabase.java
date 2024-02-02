package com.example.bodyboost.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        version = 1,
        entities = {CalorieProduct.class},
        exportSchema = false
)
public abstract class ProductDatabase extends RoomDatabase {
    public abstract ProductDAO productDAO();
}
