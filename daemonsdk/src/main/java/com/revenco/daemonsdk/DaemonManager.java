package com.revenco.daemonsdk;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.revenco.daemonsdk.assistant.assistantReceiver2;
import com.revenco.daemonsdk.assistant.assistantService2;
import com.revenco.daemonsdk.java.TransParentActivity;
import com.revenco.daemonsdk.natives.DaemonClient;
import com.revenco.daemonsdk.natives.DaemonConfigurations;

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

    /**
     * @param context
     * @param processName
     * @param service
     * @param recerver
     */
    public void init(Context context, String processName, String service, String recerver) {
        mDaemonClient = new DaemonClient(createDaemonConfigurations(processName, service, recerver));
        mDaemonClient.onAttachBaseContext(context);
    }

    private DaemonConfigurations createDaemonConfigurations(String processName, String service, String recerver) {
        DaemonConfigurations.DaemonConfiguration configuration1 = new DaemonConfigurations.DaemonConfiguration(
                processName,
                service,
                recerver);
        DaemonConfigurations.DaemonConfiguration configuration2 = new DaemonConfigurations.DaemonConfiguration(
                "com.revenco.daemonsdk:assistant",
                assistantService2.class.getCanonicalName(),
                assistantReceiver2.class.getCanonicalName());
        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
        return new DaemonConfigurations(configuration1, configuration2, listener);
    }

    public void startTransParentAct(Context context) {
        Intent intent = new Intent(context, TransParentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void destroyTransParent(Context context) {
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
