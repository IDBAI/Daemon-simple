package com.tencent.mm.modelstat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.utils.XLog;

public class WatchDogPushReceiver extends BroadcastReceiver {
    private static final String TAG = "WatchDogPushReceiver";

    public WatchDogPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        XLog.log2Sdcard(TAG, "微信的 WatchDogPushReceiver 唤醒");
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(context, intent);
    }
}
