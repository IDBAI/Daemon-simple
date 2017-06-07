package com.revenco.app.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.revenco.app.TraceServiceImpl;
import com.revenco.daemonsdk.Constant;
import com.revenco.daemonsdk.java.services.WatchDogService;

public class WakeUpReceiver extends BroadcastReceiver {
    private static final String TAG = "WakeUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Constant.ACTION_CANCEL_JOB_ALARM_SUB.equals(intent.getAction())) {
            WatchDogService.cancelJobAlarmSub();
            return;
        }
        //动态注册屏幕解锁开锁ACTION，等等以及自定义的其他广播，以唤醒app
        try {
            Log.e(TAG, "ACTION =  " + intent.getAction() + " --> 唤醒业务服务！");
            context.startService(new Intent(context, TraceServiceImpl.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
