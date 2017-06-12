package com.revenco.daemon;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;

import com.revenco.daemon.java.accounts.Constants;
import com.revenco.daemon.java.accounts.LiveAccountProvider;
import com.revenco.daemon.java.activitys.TransParentActivity;
import com.revenco.daemon.java.other.OtherPushReceiver;
import com.revenco.daemon.natives.DaemonClient;
import com.revenco.daemon.natives.DaemonConfigurations;
import com.revenco.daemon.natives.PackageUtils;
import com.revenco.daemon.natives.assistant.assistantReceiver2;
import com.revenco.daemon.natives.assistant.assistantService2;
import com.revenco.daemon.utils.StorageUtils;
import com.revenco.daemon.utils.XLog;

import java.util.ArrayList;
import java.util.List;

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
        setComponentDefault(context);
        ///
        try {
            startInnerService(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set the component in our package default
     */
    private void setComponentDefault(Context context) {
        PackageUtils.setComponentDefault(context, OtherPushReceiver.class.getName());
    }

    /**
     * 自己的应用互相拉起功能，用于同一个设备，不用应用，如果都使用了这个保活库，则启动其中一个应用，会拉起其他应用
     *
     * @param context
     */
    private void startInnerService(Context context) throws Exception {
        XLog.d(TAG, "startInnerService() called ");
        Intent intent = new Intent();
        intent.setAction(Constant.ACTION_SDK_WAKEUP_INNER);
        List<Intent> explicitIntent = getExplicitIntent(context, intent);
        System.out.println("拉起了 ：" + explicitIntent.size() + " 个service！");
        for (Intent inte : explicitIntent) {
            context.startService(inte);
        }
    }

    /**
     * 初始化LOG File 信息，调用的话是默认初始化，非必须
     * 将会开启日志输出控制台，并且 log2Sdcard 方法的日志会写入到SDcard
     * XLog 日志将写入到 nmt/sdCard/Android/data/com.xx.xx/data/daemonLog 目录
     *
     * @param context
     */
    public void initLogFile(Context context) {
        String absolutePath = StorageUtils.getDirOnData(context, "daemonLog").getAbsolutePath();
        XLog.setLogConfig(true, true, absolutePath);
    }

    /**
     * 是否开启debug日志输出控制台模式
     *
     * @param isdebug
     */
    public void setIsDebug(boolean isdebug) {
        XLog.setIsDebug(isdebug);
    }

    /**
     * 设置是否需要将日志写入SDCard
     *
     * @param iswrite 默认为false
     * @param isdebug 默认为false
     * @param logPath XLog 的跟目录，建议设置 StorageUtils.getDirOnData(context, "daemonLog");
     */
    public void initLogFile(boolean iswrite, boolean isdebug, String logPath) {
        XLog.setLogConfig(iswrite, isdebug, logPath);
    }

    /**
     * 添加账户
     *
     * @param context
     */
    public void addAccount(final Context context) {
        mAccountManager = AccountManager.get(context);
        // 跳转添加账户页
        mAccountManager.addAccount(context.getResources().getString(R.string.account_auth_type), context.getResources().getString(R.string.account_auth_type), null, null, null, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                XLog.d(TAG, "添加账户 -> run() called ");
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
        XLog.e(TAG, "屏幕上锁了，启动透明act");
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
        String logstr = "SendSDKWakeUpBroadcast() called ";
        if (intent != null && !TextUtils.isEmpty(intent.getAction()))
            logstr += " with ACTION = " + intent.getAction();
        XLog.log2Sdcard(TAG, logstr);
        context.sendBroadcast(new Intent(Constant.ACTION_WAKE_UP_BY_MORE_METHOD));
    }

    /**
     * 有些时候我们使用Service的时需要采用隐私启动的方式，
     * 但是Android 5.0一出来后，其中有个特性就是Service Intent  must be explitict，
     * 也就是说从Lollipop开始，service服务必须采用显示方式启动。
     * 次方法解决隐式启动的问题，根据自带ACTION的intent ，查找到具体的package和组件名
     *
     * @return
     */
    public List<Intent> getExplicitIntent(Context context, Intent implicitIntent) {
        List<Intent> result = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfos = pm.queryIntentServices(implicitIntent, PackageManager.GET_META_DATA);
        // Make sure only one match was found
        if (resolveInfos == null) {
            return result;
        }
        for (ResolveInfo info : resolveInfos) {
            String packageName = info.serviceInfo.packageName;
            if (packageName.equalsIgnoreCase(context.getPackageName())) {
                //排除自己
                continue;
            }
            String className = info.serviceInfo.name;
            ComponentName component = new ComponentName(packageName, className);
            Intent explicitIntent = new Intent(implicitIntent);
            explicitIntent.setComponent(component);
            explicitIntent.addCategory(packageName);
            System.out.println("拉起的类：" + packageName + " : " + className);
            result.add(explicitIntent);
        }
        return result;
    }

    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
        @Override
        public void onPersistentStart(Context context) {
            XLog.i(TAG, "DaemonManager -> onPersistentStart");
        }

        @Override
        public void onDaemonAssistantStart(Context context) {
            XLog.i(TAG, "DaemonManager -> onDaemonAssistantStart");
        }

        @Override
        public void onWatchDaemonDaed() {
            XLog.e(TAG, "DaemonManager -> onWatchDaemonDaed");
        }
    }
}
