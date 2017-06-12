package com.taobao.accs;

import com.otherService.BaseService;

public class ChannelService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

//
//    private static final String TAG = "ChannelService";
//
//    public ChannelService() {
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
//        String XLog = "伪造 ChannelService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            XLog += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, XLog);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
//
//    public class KernelService extends Service {
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
//            String XLog = "伪造 ChannelService$KernelService ，发送自定义广播";
//            if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//                XLog += " , ACTION = " + intent.getAction();
//            XLog.log2Sdcard(TAG, XLog);
//            DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//        }
//    }
}
