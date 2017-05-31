package cn.jpush.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.utils.XLog;

/**
 * java.lang.RuntimeException: Unable to instantiate service cn.jpush.android.service.DaemonService:
 * <p>
 * java.lang.InstantiationException: can't instantiate class cn.jpush.android.service.DaemonService;
 * no empty constructor
 * <p>
 * <p>
 * 极光推送互相唤醒组件
 */
public class DaemonService extends Service {
    private static final String TAG = "DaemonService";

    /**
     * 解决：
     * Caused by: java.lang.InstantiationException: can't instantiate class cn.jpush.android.service.DaemonService; no empty constructor
     */
    public void DaemonService() {
    }

    private void sendSDKBroadcast(Intent intent) {
        XLog.log2Sdcard(TAG, "onStartCommand() called -> 极光互相唤醒！");
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        sendSDKBroadcast(intent);
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendSDKBroadcast(intent);
        return super.onStartCommand(intent, flags, startId);
    }
}
