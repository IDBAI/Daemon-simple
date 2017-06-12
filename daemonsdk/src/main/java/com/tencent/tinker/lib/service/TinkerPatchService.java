package com.tencent.tinker.lib.service;

import com.otherService.BaseService;

public class TinkerPatchService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }
//    private static final String TAG = "TinkerPatchService";
//
//    public TinkerPatchService() {
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        XLog.log2Sdcard(TAG, "onStartCommand() called 仿造微信 —> TinkerPatchService ");
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//        return super.onStartCommand(intent, flags, startId);
//    }

    public class InnerService extends BaseService {
        @Override
        public String getServieName() {
            return getClass().getSimpleName();
        }
//
//        @Nullable
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//
//        @Override
//        public int onStartCommand(Intent intent, int flags, int startId) {
//            XLog.log2Sdcard(TAG, "onStartCommand() called 仿造微信 —> TinkerPatchService $ InnerService ");
//            DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//            return super.onStartCommand(intent, flags, startId);
//        }
    }
}
