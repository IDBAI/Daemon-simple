package cn.jpush.android.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;

/**
 * 极光推送互相唤醒组件
 */
public class DaemonService extends Service {
    private static final String TAG = "DaemonService";

    public DaemonService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        DaemonManager.INSTANCE.SendSyncAccountBroadcast(getApplicationContext());
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called -> 极光互相唤醒！");
        DaemonManager.INSTANCE.SendSyncAccountBroadcast(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }
}
