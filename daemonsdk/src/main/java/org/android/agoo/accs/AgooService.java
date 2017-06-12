package org.android.agoo.accs;

import com.otherService.BaseService;

public class AgooService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

//    private static final String TAG = "AgooService";
//
//    public AgooService() {
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
//        String logStr = "伪造淘宝 AgooService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            logStr += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, logStr);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
}
