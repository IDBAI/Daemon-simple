package com.revenco.otherapp.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.revenco.daemon.Constant;
import com.revenco.daemon.java.services.WatchDogService;
import com.revenco.daemon.utils.XLog;
import com.revenco.otherapp.TraceServiceImpl;

/**
 * 这个接收器唤醒业务服务
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
