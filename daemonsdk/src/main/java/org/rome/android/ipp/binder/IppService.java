package org.rome.android.ipp.binder;

import com.otherService.BaseService;

/**
 * 阿里系列的隐式唤醒服务
 */
public class IppService extends BaseService {
    @Override
    public String getServieName() {
        return getClass().getSimpleName();
    }

//    private static final String TAG = "IppService";
//
//    public IppService() {
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
//        String logStr = "伪造 IppService ，发送自定义广播";
//        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
//            logStr += " , ACTION = " + intent.getAction();
//        XLog.log2Sdcard(TAG, logStr);
//        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getApplicationContext(), intent);
//    }
}
