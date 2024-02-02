package com.example.bodyboost.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final TextView tv;

    /**
     * Creates a new SelectDateFragment
     *
     * @param textView The TextView to write the selected date to
     */
    public SelectDateFragment(TextView textView) {
        tv = textView;
    }

    /**
     * Instructions for how to create fragment
     *
     * @param savedInstanceState
     * @return A date picker dialog
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar;
        Calendar calendar1;
        calendar1 = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar1 = Calendar.getInstance();
        }
        calendar = calendar1;
        int yy = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            yy = calendar.get(Calendar.YEAR);
        }
        int mm = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            mm = calendar.get(Calendar.MONTH);
        }
        int dd = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            dd = calendar.get(Calendar.DAY_OF_MONTH);
        }
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    /**
     * Instructions for what to do when user picks a date
     *
     * @param view The DatePicker
     * @param yy   Year
     * @param mm   Month
     * @param dd   Day
     */
    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);
    }

    /**
     * Set writes date chosen by user to View whose id was passed to constructor
     *
     * @param year  Year chosen by user
     * @param month Month chosen by user
     * @param day   Day chosen by user
     */
    public void populateSetDate(int year, int month, int day) {
        LocalDate localDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            localDate = LocalDate.of(year, month, day);
        }

        tv.clearFocus();

        String date = month + "/" + day + "/" + year;
        tv.setTag(localDate);
        tv.setText(date);
    }
}
