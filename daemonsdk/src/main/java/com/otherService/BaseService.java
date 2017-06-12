package com.otherService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.revenco.daemon.DaemonManager;
import com.revenco.daemon.utils.XLog;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-12 10:29.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public abstract class BaseService extends Service {
    private static final String TAG = "BaseService";

    public abstract String getServieName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        XLog.d(TAG, "伪造第三方service类 ：" + getServieName());
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
        String logStr = "发送自定义广播";
        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
            logStr += " , ACTION = " + intent.getAction();
        XLog.log2Sdcard(TAG, logStr);
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
    }
}
