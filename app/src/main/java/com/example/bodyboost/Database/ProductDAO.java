package com.example.bodyboost.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDAO {
    @Query("SELECT * FROM CalorieProduct")
    List<CalorieProduct> getAll();

    @Query("DELETE FROM CalorieProduct")
    void deleteALL();

    @Query("DELETE FROM CalorieProduct WHERE id=:id")
    void deleteById(int id);

    @Insert
    void insert(CalorieProduct calorieProduct);

    @Update
    void update(CalorieProduct calorieProduct);

}
