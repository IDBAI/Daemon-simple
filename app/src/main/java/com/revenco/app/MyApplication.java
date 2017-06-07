package com.revenco.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.revenco.app.wakeup.assistantReceiver;
import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.java.services.DaemonEnv;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-07 9:42.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //主要的业务逻辑进程
        String processName = "com.revenco.app:business";
        String serviceName = TraceServiceImpl.class.getCanonicalName();
        String receiveName = assistantReceiver.class.getCanonicalName();
        DaemonManager.INSTANCE.init(context, processName, serviceName, receiveName);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        try {
            startService(new Intent(this, TraceServiceImpl.class));
        } catch (Exception ignored) {
        }
    }
}
