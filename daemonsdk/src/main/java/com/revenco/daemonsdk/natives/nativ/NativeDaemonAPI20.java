package com.revenco.daemonsdk.natives.nativ;

import android.content.Context;

import com.revenco.daemonsdk.natives.NativeDaemonBase;

/**
 * natives code to watch each other when api under 20 (contains 20)
 *
 * @author Mars
 */
public class NativeDaemonAPI20 extends NativeDaemonBase {
    static {
        try {
            System.loadLibrary("daemon_api20");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NativeDaemonAPI20(Context context) {
        super(context);
    }

    public native void doDaemon(String pkgName, String svcName, String daemonPath);
}
