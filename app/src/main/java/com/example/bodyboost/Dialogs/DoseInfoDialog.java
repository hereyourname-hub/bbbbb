package com.example.bodyboost.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import kotlin.Triple;
import com.example.bodyboost.Fragments.SelectDateFragment;
import com.example.bodyboost.Fragments.TimePickerFragment;
import com.example.bodyboost.Helpers.DBHelper;
import com.example.bodyboost.Helpers.TimeFormatting;
import com.example.bodyboost.Interfaces.IDialogCloseListener;
import com.example.bodyboost.R;
import com.example.bodyboost.SimpleClasses.Medication;

public class DoseInfoDialog extends DialogFragment {
    private final long doseId;
    private final DBHelper db;
    private final TextView textView;
    private boolean changed = false;
    private TextInputEditText timeTaken;
    private TextInputEditText dateTaken;

    public DoseInfoDialog(long doseId, DBHelper database, TextView tv) {
        this.doseId = doseId;
        db = database;
        textView = tv;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstances) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        Medication med = ((Triple<Medication, Long, LocalDateTime>) textView.getTag()).getFirst();

        builder.setView(inflater.inflate(R.layout.dialog_dose_info, null));
        builder.setTitle(R.string.this_dose);

        builder.setPositiveButton(getString(R.string.save), ((dialogInterface, i) -> save()));
        builder.setNegativeButton(R.string.close, ((dialogInterface, i) -> dismiss()));

        if (med.getFrequency() == 0) {
            builder.setNeutralButton(R.string.delete, ((dialogInterface, i) -> deleteAsNeededDose()));
        }

        return builder.create();
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!db.getTaken(doseId)) {
            getDialog().findViewById(R.id.notTakenMessage).setVisibility(View.VISIBLE);
        } else {
            LocalDateTime doseDate = db.getTimeTaken(doseId);

            timeTaken = getDialog().findViewById(R.id.dose_time_taken);
            dateTaken = getDialog().findViewById(R.id.dose_date_taken);

            timeTaken.setShowSoftInputOnFocus(false);
            dateTaken.setShowSoftInputOnFocus(false);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTaken.setText(TimeFormatting.localTimeToString(doseDate.toLocalTime()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                timeTaken.setTag(doseDate.toLocalTime());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTaken.setText(TimeFormatting.localDateToString(doseDate.toLocalDate()));
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateTaken.setTag(doseDate.toLocalDate());
            }

            timeTaken.setOnFocusChangeListener((view, b) ->
            {
                if (b) {
                    DialogFragment timePicker = new TimePickerFragment(timeTaken);
                    timePicker.show(getParentFragmentManager(), null);

                    changed = true;
                }
            });

            dateTaken.setOnFocusChangeListener((view, b) ->
            {
                if (b) {
                    DialogFragment datePicker = new SelectDateFragment(dateTaken);
                    datePicker.show(getParentFragmentManager(), null);

                    changed = true;
                }
            });

            getDialog().findViewById(R.id.dose_time_details).setVisibility(View.VISIBLE);
        }
    }

    private void save() {
        if (changed) {
            LocalDate date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = (LocalDate) dateTaken.getTag();
            }
            LocalTime time = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                time = (LocalTime) timeTaken.getTag();
            }
            LocalDateTime dateTime = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateTime = LocalDateTime.of(date, time);
            }

            db.updateDoseStatus(
                    doseId,
                    TimeFormatting.localDateTimeToString(dateTime),
                    true
            );
        }

        dismiss();
    }

    private void deleteAsNeededDose() {
        db.deleteDose(doseId);

        Fragment fragment = getParentFragment();

        if (fragment instanceof IDialogCloseListener) {
            ((IDialogCloseListener) fragment).handleDialogClose(
                    IDialogCloseListener.Action.DELETE, doseId
            );
        }
    }
}
