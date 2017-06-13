package com.revenco.otherapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.revenco.daemon.DaemonManager;
import com.revenco.daemon.java.services.DaemonEnv;
import com.revenco.otherapp.wakeup.assistantReceiver;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Start();
    }

    /**
     * 在子线程配置，避免阻塞主线程
     */
    private void Start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主要的业务逻辑进程
                String processName = "com.revenco.otherapp:business";
                String serviceName = TraceServiceImpl.class.getCanonicalName();
                String receiveName = assistantReceiver.class.getCanonicalName();
                DaemonManager.INSTANCE.init(MainActivity.this, processName, serviceName, receiveName);
                if (BuildConfig.DEBUG)
                    //Debug模式开启控制台LOG日志，部分唤醒日志则会记录到SDCard，方便观察app如何被唤醒的日志
                    DaemonManager.INSTANCE.initLogFile(MainActivity.this);
                //配置
                DaemonEnv.initialize(MainActivity.this, TraceServiceImpl.class, DaemonEnv.DEFAULT_WAKE_UP_INTERVAL);
                try {
                    //启动
                    startService(new Intent(MainActivity.this, TraceServiceImpl.class));
                } catch (Exception e) {
                }
            }
        }).start();
    }
}
