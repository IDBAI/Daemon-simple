package com.revenco.daemon_simple;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class addService2 extends Service {
    public addService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
