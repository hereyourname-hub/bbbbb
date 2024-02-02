package com.example.bodyboost;

public class SharedData {
    private static SharedData instance;
    private int totalCalories;

    private SharedData() {
        // Запрещаем создание экземпляра класса извне
    }

    public static SharedData getInstance() {
        if (instance == null) {
            instance = new SharedData();
        }
        return instance;
    }

    public int getTotalCalories() {
        return totalCalories;
    }

    public void setTotalCalories(int totalCalories) {
        this.totalCalories = totalCalories;
    }
}
