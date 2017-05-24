package com.revenco.daemonsdk.natives.nativ;

import android.content.Context;

import com.revenco.daemonsdk.natives.NativeDaemonBase;

/**
 * natives code to watch each other when api over 21 (contains 21)
 *
 * @author Mars
 */
public class NativeDaemonAPI21 extends NativeDaemonBase {
    static {
        try {
            System.loadLibrary("daemon_api21");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NativeDaemonAPI21(Context context) {
        super(context);
    }

    public native void doDaemon(String indicatorSelfPath, String indicatorDaemonPath, String observerSelfPath, String observerDaemonPath);
}
