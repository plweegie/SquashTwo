package com.plweegie.android.squashtwo.receivers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;

import com.plweegie.android.squashtwo.services.CommitPollWorker;

public class NotificationReceiver extends BroadcastReceiver {

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (getResultCode() != Activity.RESULT_OK) {
            return;
        }

        int requestCode = intent.getIntExtra(CommitPollWorker.REQUEST_CODE, 0);
        Notification notif = intent.getParcelableExtra(CommitPollWorker.NOTIFICATION);

        if (notif != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(requestCode, notif);
        }
    }
}
