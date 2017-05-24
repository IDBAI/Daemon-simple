package com.revenco.daemonsdk.java;

import android.app.Notification;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.revenco.daemonsdk.R;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-24 10:51.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class NotifyHelper {
    public static final int NOTIFY_ID = 1000;
    public static final int JOB_ALERT_ID = 999;

    public static NotifyHelper INSTANCE = new NotifyHelper();

    public Notification getForgroundNotification(Context context) {
        Notification notification;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setAutoCancel(true);
        builder.setContentTitle("呼叫服务");
        builder.setContentText("为了能够正常被呼叫，请保持此服务运行");
        builder.setWhen(System.currentTimeMillis());
        notification = builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        return notification;
    }

}
