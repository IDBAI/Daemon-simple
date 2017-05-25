package com.revenco.daemon_simple;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.java.DaemonEnv;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-17 15:31.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        String processName = "com.revenco.daemon_simple:process1";
        String serviceName = TraceServiceImpl.class.getCanonicalName();
        String receiveName = assistantReceiver1.class.getCanonicalName();
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