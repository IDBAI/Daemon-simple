package com.tencent.tinker.lib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.utils.XLog;

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
        XLog.log2Sdcard(TAG, "onStartCommand() called 仿造微信 —> DefaultTinkerResultService ");
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(),intent);
        return super.onStartCommand(intent, flags, startId);

    }
}
