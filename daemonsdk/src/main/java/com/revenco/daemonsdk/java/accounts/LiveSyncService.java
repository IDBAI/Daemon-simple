package com.revenco.daemonsdk.java.accounts;

import android.accounts.Account;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.utils.XLog;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-26 12:39.</p>
 * <p>CLASS DESCRIBE :同步服务</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class LiveSyncService extends Service {
    private static final Object syncAdapterLock = new Object();
    private static final String TAG = Debugger.TAG;
    private static LiveAdapter liveAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called ");
        synchronized (syncAdapterLock) {
            if (liveAdapter == null)
                liveAdapter = new LiveAdapter(getApplicationContext(), true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called ");
        return liveAdapter.getSyncAdapterBinder();
    }

    static class LiveAdapter extends AbstractThreadedSyncAdapter {
        public LiveAdapter(Context context, boolean autoInitialize) {
            super(context, autoInitialize);
            Log.d(TAG, "LiveAdapter() called ");
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            getContext().getContentResolver().notifyChange(LiveAccountProvider.CONTENT_URI, null, false);
            XLog.log2Sdcard(TAG, "onPerformSync() called - 将触发账户同步功能，释放SDK-ACTION! ");
            DaemonManager.INSTANCE.SendSDKWakeUpBroadcast(getContext(), null);
        }
    }
}
