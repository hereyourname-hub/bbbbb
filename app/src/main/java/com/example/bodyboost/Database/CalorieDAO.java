package com.example.bodyboost.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CalorieDAO {
    //@Query("SELECT * FROM CalorieDay")
    //List<CalorieDay> getAll();

    @Query("SELECT * FROM CalorieDay ORDER BY id DESC LIMIT 0 , :limit")
    List<CalorieDay> getLastRecords(int limit);

    @Query("SELECT * FROM CalorieDay WHERE calorie_date == :date")
    CalorieDay getCalorieDayByDate(String date);

    @Query("DELETE FROM CalorieDay")
    void deleteALL();

    @Insert
    void insert(CalorieDay calorieDay);

    @Update
    void update(CalorieDay calorieDay);
}
