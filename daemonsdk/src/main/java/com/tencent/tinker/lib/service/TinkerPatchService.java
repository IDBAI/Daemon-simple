package com.tencent.tinker.lib.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;

public class TinkerPatchService extends Service {
    private static final String TAG = "TinkerPatchService";

    public TinkerPatchService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called 仿造微信 —> TinkerPatchService ");
        DaemonManager.INSTANCE.SendSyncAccountBroadcast(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    class InnerService extends Service {
        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.d(TAG, "onStartCommand() called 仿造微信 —> TinkerPatchService $ InnerService ");
            DaemonManager.INSTANCE.SendSyncAccountBroadcast(getApplicationContext());
            return super.onStartCommand(intent, flags, startId);
        }
    }
}
