package com.revenco.daemon_simple;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.revenco.daemonsdk.java.notifys.NotifyHelper;
import com.revenco.daemonsdk.java.services.IntentWrapper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            startService(new Intent(this, TraceServiceImpl.class));
        } catch (Exception ignored) {
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                try {
                    startService(new Intent(this, TraceServiceImpl.class));
                } catch (Exception ignored) {
                }
                break;
            case R.id.btn_white:
                IntentWrapper.whiteListMatters(this, "轨迹跟踪服务的持续运行");
                break;
            case R.id.btn_stop:
                TraceServiceImpl.stopService();
                break;
            case R.id.btn_Notify:
                Notification notification = NotifyHelper.INSTANCE.getForgroundNotification(this.getApplicationContext());
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(1000, notification);
//                DaemonManager.INSTANCE.addAccount(this.getApplicationContext());
                break;
        }
    }
}
