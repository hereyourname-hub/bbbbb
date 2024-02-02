package com.example.bodyboost.Receivers;

import static com.example.bodyboost.Helpers.NotificationHelper.DOSE_TIME;
import static com.example.bodyboost.Helpers.NotificationHelper.MEDICATION_ID;
import static com.example.bodyboost.Helpers.NotificationHelper.MESSAGE;
import static com.example.bodyboost.Helpers.NotificationHelper.NOTIFICATION_ID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.time.LocalDateTime;

import com.example.bodyboost.Helpers.DBHelper;
import com.example.bodyboost.Helpers.NotificationHelper;
import com.example.bodyboost.Services.NotificationService;
import com.example.bodyboost.SimpleClasses.Medication;

public class NotificationReceiver extends BroadcastReceiver {
    /**
     * Receiver for notification PendingIntent
     *
     * @param context Context of caller.
     * @param intent  Intent to receive.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, NotificationService.class);
        Bundle extras = intent.getExtras();
        DBHelper db = new DBHelper(context);

        long medicationId = extras.getLong(MEDICATION_ID);
        Medication medication = db.getMedication(medicationId);
        LocalDateTime doseTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            doseTime = (LocalDateTime) extras.get(DOSE_TIME);
        }

        service.putExtra(NOTIFICATION_ID, extras.getLong(NOTIFICATION_ID, 0));
        service.putExtra(MESSAGE, extras.getString(MESSAGE));
        service.putExtra(DOSE_TIME, doseTime.toString());
        service.putExtra(MEDICATION_ID, medicationId);

        // Set new Intent for a new notification
        NotificationHelper.scheduleNotification(
                context,
                medication,
                doseTime,
                extras.getLong(NOTIFICATION_ID, 0)
        );

        // Fire notification if enabled
        if (db.getNotificationEnabled())
            context.startService(service);

        db.close();
    }
}
