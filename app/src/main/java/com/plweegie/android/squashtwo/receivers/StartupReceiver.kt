package com.plweegie.android.squashtwo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import com.plweegie.android.squashtwo.utils.WorkManagerUtil


class StartupReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED && context != null) {
            WorkManagerUtil.enqueueWorkRequest(context,
                    policy = ExistingPeriodicWorkPolicy.UPDATE)
        }
    }
}