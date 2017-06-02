package com.taobao.accs.data;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.utils.XLog;

public class MsgDistributeService extends Service {
    private static final String TAG = "MsgDistributeService";

    public MsgDistributeService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        send(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        send(intent);
        return null;
    }

    private void send(Intent intent) {
        String log = "伪造 MsgDistributeService ，发送自定义广播";
        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
            log += " , ACTION = " + intent.getAction();
        XLog.log2Sdcard(TAG, log);
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
    }
}
