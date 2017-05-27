package com.tencent.tinker.lib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;

public class DefaultTinkerResultService extends Service {
    private static final String TAG = "DefaultTinkerResultServ";
    public DefaultTinkerResultService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called 仿造微信 —> DefaultTinkerResultService ");
        DaemonManager.INSTANCE.SendSyncAccountBroadcast(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);

    }
}
