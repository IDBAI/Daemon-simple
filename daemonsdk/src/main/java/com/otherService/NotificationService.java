package com.otherService;

/**
 * 支付宝推送服务
 */
public class NotificationService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
//    private static final String TAG = "NotificationService";
//
//    public NotificationService() {
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
//        String XLog = "伪造支付宝推送服务 NotificationService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            XLog += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, XLog);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
}
