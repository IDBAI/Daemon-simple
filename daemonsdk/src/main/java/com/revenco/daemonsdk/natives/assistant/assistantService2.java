package com.revenco.daemonsdk.natives.assistant;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class assistantService2 extends Service {
    public assistantService2() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
