package com.revenco.daemonsdk.java.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;

import com.revenco.daemonsdk.DaemonManager;
import com.revenco.daemonsdk.java.notifys.NotifyHelper;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WatchDogService extends Service {
    private static final String TAG = "WatchDogService";
    protected static Disposable sDisposable;
    protected static PendingIntent sPendingIntent;
    private ScreenStatusReverive screenStatusReverive;
    private AtomicBoolean isRegistBroad = new AtomicBoolean(false);

    /**
     * 用于在不需要服务运行的时候取消 Job / Alarm / Subscription.
     * <p>
     * 因 WatchDogService 运行在 :watch 子进程, 请勿在主进程中直接调用此方法.
     * 而是向 WakeUpReceiver 发送一个 Action 为 WakeUpReceiver.ACTION_CANCEL_JOB_ALARM_SUB 的广播.
     */
    public static void cancelJobAlarmSub() {
        if (!DaemonEnv.sInitialized)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobScheduler scheduler = (JobScheduler) DaemonEnv.sApp.getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.cancel(NotifyHelper.JOB_ALERT_ID);
        } else {
            AlarmManager am = (AlarmManager) DaemonEnv.sApp.getSystemService(ALARM_SERVICE);
            if (sPendingIntent != null)
                am.cancel(sPendingIntent);
        }
        if (sDisposable != null)
            sDisposable.dispose();
    }

    private void setupNotify() {
        startForeground(NotifyHelper.NOTIFY_ID, NotifyHelper.INSTANCE.getForgroundNotification(this));
    }

    /**
     * 守护服务，运行在:watch子进程中
     */
    protected final int onStart(Intent intent, int flags, int startId) {
        registBroadcast(this.getApplicationContext());
        if (!DaemonEnv.sInitialized)
            return START_STICKY;
        if (sDisposable != null && !sDisposable.isDisposed())
            return START_STICKY;
        //17以下不会显示在通知栏
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1)
            startForeground(1000, new Notification());
        //定时检查 AbsWorkService 是否在运行，如果不在运行就把它拉起来
        //Android 5.0+ 使用 JobScheduler，效果比 AlarmManager 好
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            JobInfo.Builder builder = new JobInfo.Builder(NotifyHelper.JOB_ALERT_ID, new ComponentName(DaemonEnv.sApp, JobSchedulerService.class));
            builder.setPeriodic(DaemonEnv.getWakeUpInterval());
            //Android 7.0+ 增加了一项针对 JobScheduler 的新限制，最小间隔只能是下面设定的数字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                builder.setPeriodic(JobInfo.getMinPeriodMillis(), JobInfo.getMinFlexMillis());
            builder.setPersisted(true);
            JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            scheduler.schedule(builder.build());
        } else {
            //Android 4.4- 使用 AlarmManager
            AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent i = new Intent(DaemonEnv.sApp, DaemonEnv.sServiceClass);
            sPendingIntent = PendingIntent.getService(DaemonEnv.sApp, NotifyHelper.JOB_ALERT_ID, i, PendingIntent.FLAG_UPDATE_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + DaemonEnv.getWakeUpInterval(), DaemonEnv.getWakeUpInterval(), sPendingIntent);
        }
        //使用定时 Observable，避免 Android 定制系统 JobScheduler / AlarmManager 唤醒间隔不稳定的情况
        sDisposable = Flowable
                .interval(DaemonEnv.getWakeUpInterval(), TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        startService(new Intent(DaemonEnv.sApp, DaemonEnv.sServiceClass));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
        //守护 Service 组件的启用状态, 使其不被 MAT 等工具禁用
        getPackageManager().setComponentEnabledSetting(new ComponentName(getPackageName(), DaemonEnv.sServiceClass.getName()),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
        return START_STICKY;
    }

    @Override
    public final int onStartCommand(Intent intent, int flags, int startId) {
        return onStart(intent, flags, startId);
    }

    @Override
    public final IBinder onBind(Intent intent) {
        onStart(intent, 0, 0);
        return null;
    }

    protected void onEnd(Intent rootIntent) {
        if (!DaemonEnv.sInitialized)
            return;
        try {
            startService(new Intent(DaemonEnv.sApp, DaemonEnv.sServiceClass));
        } catch (Exception ignored) {
        }
        try {
            startService(new Intent(DaemonEnv.sApp, WatchDogService.class));
        } catch (Exception ignored) {
        }
    }

    /**
     * 最近任务列表中划掉卡片时回调
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        onEnd(rootIntent);
    }

    /**
     * 设置-正在运行中停止服务时回调
     */
    @Override
    public void onDestroy() {
        onEnd(null);
        unRegistBroadcast(this.getApplicationContext());
    }

    private void registBroadcast(Context context) {
        if (!isRegistBroad.get()) {
            if (screenStatusReverive == null)
                screenStatusReverive = new ScreenStatusReverive();
            context.registerReceiver(screenStatusReverive, getFilter());
            isRegistBroad.set(true);
        }
    }

    private void unRegistBroadcast(Context context) {
        if (isRegistBroad.get()) {
            if (screenStatusReverive != null)
                context.unregisterReceiver(screenStatusReverive);
            isRegistBroad.set(false);
        }
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        return filter;
    }

    /**
     * 屏幕上锁广播接收
     */
    static class ScreenStatusReverive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case Intent.ACTION_SCREEN_OFF:
                        DaemonManager.INSTANCE.startTransParentAct(context);
                        break;
                }
            }
        }
    }
}
