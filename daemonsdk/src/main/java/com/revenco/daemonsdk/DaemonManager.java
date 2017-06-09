package com.revenco.daemonsdk;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.revenco.daemonsdk.java.accounts.Constants;
import com.revenco.daemonsdk.java.accounts.LiveAccountProvider;
import com.revenco.daemonsdk.java.activitys.TransParentActivity;
import com.revenco.daemonsdk.natives.DaemonClient;
import com.revenco.daemonsdk.natives.DaemonConfigurations;
import com.revenco.daemonsdk.natives.assistant.assistantReceiver2;
import com.revenco.daemonsdk.natives.assistant.assistantService2;
import com.revenco.daemonsdk.utils.StorageUtils;
import com.revenco.daemonsdk.utils.XLog;

import java.io.File;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-24 18:58.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class DaemonManager {
    private static final String TAG = "DaemonManager";
    public static DaemonManager INSTANCE = new DaemonManager();
    public static File daemonLog;
    private DaemonClient mDaemonClient;
    private AccountManager mAccountManager;

    /**
     * @param context
     * @param processName
     * @param service
     * @param recerver
     */
    public void init(Context context, String processName, String service, String recerver) {
        mDaemonClient = new DaemonClient(createDaemonConfigurations(processName, service, recerver));
        mDaemonClient.onAttachBaseContext(context);
        addAccount(context);
        initLogFile(context);
    }

    private void initLogFile(Context context) {
        daemonLog = StorageUtils.getDirOnData(context, "daemonLog");
    }

    public void addAccount(Context context) {
        mAccountManager = AccountManager.get(context);
        // 跳转添加账户页
        mAccountManager.addAccount(context.getResources().getString(R.string.account_auth_type), context.getResources().getString(R.string.account_auth_type), null, null, null, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                Log.d(TAG, "添加账户 -> run() called ");
                final Account account = new Account(Constants.mUsername, Constants.ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, Constants.mPassword, null);
                //设置让这个账号可以自己主动同步
                ContentResolver.setSyncAutomatically(account, LiveAccountProvider.AUTHORITY, true);
            }
        }, null);
    }

    private DaemonConfigurations createDaemonConfigurations(String processName, String service, String recerver) {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                processName,
                service,
                recerver);
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.revenco.daemonsdk:keeplive",
                assistantService2.class.getCanonicalName(),
                assistantReceiver2.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    public void startTransParentAct(Context context) {
        Log.e(TAG, "屏幕上锁了，启动透明act");
        Intent intent = new Intent(context, TransParentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * SDK内部向外发送唤醒广播
     *
     * @param context
     * @param intent
     */
    public void SendSDKWakeUpBroadcast(Context context, Intent intent) {
        String log = "SendSDKWakeUpBroadcast() called ";
        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
            log += " with ACTION = " + intent.getAction();
        XLog.log2Sdcard(TAG, log);
        context.sendBroadcast(new Intent(Constant.ACTION_WAKE_UP_BY_MORE_METHOD));
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
            Log.i(TAG, "DaemonManager -> onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            Log.i(TAG, "DaemonManager -> onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            Log.e(TAG, "DaemonManager -> onWatchDaemonDaed");
        }
    }
}
