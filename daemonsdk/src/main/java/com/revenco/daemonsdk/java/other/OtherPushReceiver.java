package com.revenco.daemonsdk.java.other;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;

/**
 * 接收第三方推送的广播，以唤醒APP
 */
public class OtherPushReceiver extends BroadcastReceiver {
    private static final String TAG = "OtherPushReceiver";

    public OtherPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null)
            Log.e(TAG, "OtherPushReceiver -> ACTION = " + intent.getAction());
        DaemonManager.INSTANCE.SendSyncAccountBroadcast(context);
    }
}
