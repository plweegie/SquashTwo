package com.plweegie.android.squashtwo.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import com.plweegie.android.squashtwo.services.CommitPollWorker;

public abstract class VisibleActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(CommitPollWorker.ACTION_SHOW_NOTIFICATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.registerReceiver(
                    mOnShowNotification,
                    filter,
                    CommitPollWorker.PERMISSION_PRIVATE,
                    null,
                    Context.RECEIVER_NOT_EXPORTED
            );
        } else {
            this.registerReceiver(
                    mOnShowNotification,
                    filter,
                    CommitPollWorker.PERMISSION_PRIVATE,
                    null
            );
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(mOnShowNotification);
    }

    private final BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setResultCode(Activity.RESULT_CANCELED);
        }
    };
}
