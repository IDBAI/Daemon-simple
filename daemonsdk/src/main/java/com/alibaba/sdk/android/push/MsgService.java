package com.alibaba.sdk.android.push;

import com.otherService.BaseService;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-02 12:50.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class MsgService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

//    private static final String TAG = "MsgService";
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        send(intent);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        send(intent);
//        return null;
//    }
//
//    private void send(Intent intent) {
//        String logStr = "伪造 MsgService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            logStr += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, logStr);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
}
