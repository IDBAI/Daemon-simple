package com.revenco.daemon_simple;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.revenco.daemonsdk.java.DaemonEnv;
import com.revenco.daemonsdk.natives.DaemonClient;
import com.revenco.daemonsdk.natives.DaemonConfigurations;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-17 15:31.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";
    private DaemonClient mDaemonClient;

    @Override
    public void onCreate() {
        super.onCreate();
        DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        try {
            startService(new Intent(this, TraceServiceImpl.class));
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mDaemonClient = new DaemonClient(createDaemonConfigurations());
        mDaemonClient.onAttachBaseContext(base);
    }

    private DaemonConfigurations createDaemonConfigurations() {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                "com.revenco.daemon_simple:process1",
                TraceServiceImpl.class.getCanonicalName(),
                addReceiver1.class.getCanonicalName());
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.revenco.daemon_simple:process2",
                addService2.class.getCanonicalName(),
                addReceiver2.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
            Log.i(TAG, "MyApplication -> onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.i(TAG, "MyApplication -> onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            Log.e(TAG, "MyApplication -> onWatchDaemonDaed");
        }
    }
}
