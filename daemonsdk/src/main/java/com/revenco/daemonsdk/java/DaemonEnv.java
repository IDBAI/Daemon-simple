package com.revenco.daemonsdk.java;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class DaemonEnv {
    public static final long DEFAULT_WAKE_UP_INTERVAL = 1 * 60 * 1000;
    private static final long MINIMAL_WAKE_UP_INTERVAL = 1 * 60 * 1000;
    static Context sApp;
    static Class<? extends AbsWorkService> sServiceClass;
    static boolean sInitialized;
    private static long sWakeUpInterval = DEFAULT_WAKE_UP_INTERVAL;

    private DaemonEnv() {
    }

    /**
     * @param app            Application Context.
     * @param wakeUpInterval 定时唤醒的时间间隔(ms).
     */
    public static void initialize(@NonNull Context app, @NonNull Class<? extends AbsWorkService> serviceClass, @Nullable Long wakeUpInterval) {
        sApp = app;
        sServiceClass = serviceClass;
        if (wakeUpInterval != null)
            sWakeUpInterval = wakeUpInterval;
        sInitialized = true;
    }

    static long getWakeUpInterval() {
        return Math.max(sWakeUpInterval, MINIMAL_WAKE_UP_INTERVAL);
    }
}
