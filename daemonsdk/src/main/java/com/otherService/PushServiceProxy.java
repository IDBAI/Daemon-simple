package com.otherService;

/**
 * 新浪微博
 *
 */
public class PushServiceProxy  extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
//    private static final String TAG = "PushServiceProxy";
//    public PushServiceProxy() {
//    }
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
//        String XLog = "伪造新浪微博 PushServiceProxy ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            XLog += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, XLog);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
}
