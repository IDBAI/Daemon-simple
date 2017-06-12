package com.tencent.mm.booter;

import com.otherService.BaseService;

public class CoreService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

//    private static final String TAG = "CoreService";
//
//    public CoreService() {
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
//        String XLog = "伪造微信 CoreService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            XLog += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, XLog);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
//
//    public class InnerService extends Service {
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            send(intent);
//            return super.onStartCommand(intent, flags, startId);
//        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            send(intent);
//            return null;
//        }
//
//        private void send(Intent intent) {
//            String XLog = "伪造微信 CoreService$InnerService ，发送自定义广播";
//            if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//                XLog += " , ACTION = " + intent.getAction();
//            XLog.log2Sdcard(TAG, XLog);
//            DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//        }
//    }
}
