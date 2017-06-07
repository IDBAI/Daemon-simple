package com.taobao.accs;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.revenco.daemonsdk.DaemonManager;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-06-02 12:54.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(context, intent);
    }
}
