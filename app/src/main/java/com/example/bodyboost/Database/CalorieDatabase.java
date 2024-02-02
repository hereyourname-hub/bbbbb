package com.example.bodyboost.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(
        version = 1,
        entities = {CalorieDay.class},
        exportSchema = false
)
public abstract class CalorieDatabase extends RoomDatabase {
    public abstract CalorieDAO calorieDAO();
}
