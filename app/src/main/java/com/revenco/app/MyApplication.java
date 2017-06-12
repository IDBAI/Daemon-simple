package com.revenco.app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.revenco.app.wakeup.assistantReceiver;
import com.revenco.daemon.DaemonManager;
import com.revenco.daemon.java.services.DaemonEnv;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-07 9:42.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class MyApplication extends Application {
    private static final String TAG = "MyApplication";

    /**
     * 这个方法主要启动native层的保活方案
     * @param context
     */
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        //主要的业务逻辑进程
        String processName = "com.revenco.app:business";
        String serviceName = TraceServiceImpl.class.getCanonicalName();
        String receiveName = assistantReceiver.class.getCanonicalName();
        DaemonManager.INSTANCE.init(context, processName, serviceName, receiveName);
        //初始化开启LOG日志记录到SDCard，方便观察app如何被唤醒的日志
        DaemonManager.INSTANCE.initLogFile(context);
    }

    /**
     * 这里启动业务服务
     */
    @Override
    public void onCreate() {
        super.onCreate();
        DaemonEnv.initialize(this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
        try {
            startService(new Intent(this, TraceServiceImpl.class));
        } catch (Exception e) {
        }
    }
}
