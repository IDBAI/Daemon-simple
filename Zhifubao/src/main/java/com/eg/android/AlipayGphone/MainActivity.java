package com.eg.android.AlipayGphone;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eg.android.AlipayGphone.wakeup.assistantReceiver1;
import com.revenco.daemon.DaemonManager;
import com.revenco.daemon.java.notifys.NotifyHelper;
import com.revenco.daemon.java.services.DaemonEnv;
import com.revenco.daemon.java.services.IntentWrapper;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start();
    }

    private void Start() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //主要的业务逻辑进程
                String processName = "com.eg.android.AlipayGphone:business";
                String serviceName = TraceServiceImpl.class.getCanonicalName();
                String receiveName = assistantReceiver1.class.getCanonicalName();
                DaemonManager.INSTANCE.init(MainActivity.this, processName, serviceName, receiveName);
                //初始化开启LOG日志记录到SDCard，方便观察app如何被唤醒的日志
                DaemonManager.INSTANCE.initLogFile(MainActivity.this);

                //初始化配置
                DaemonEnv.initialize(MainActivity.this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
                try {
                    //启动
                    startService(new Intent(MainActivity.this, TraceServiceImpl.class));
                } catch (Exception ignored) {
                }

            }
        }).start();
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
