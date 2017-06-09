package com.eg.android.AlipayGphone.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.eg.android.AlipayGphone.TraceServiceImpl;
import com.revenco.daemonsdk.Constant;
import com.revenco.daemonsdk.java.services.WatchDogService;
import com.revenco.daemonsdk.utils.XLog;

/**
 * 主要接受SDK内部的广播，以方便唤醒具体的业务服务对象
 */
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
            XLog.log2Sdcard(TAG, "ACTION =  " + intent.getAction() + " --> 唤醒业务服务！");
            context.startService(new Intent(context, TraceServiceImpl.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
