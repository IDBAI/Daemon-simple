package com.revenco.daemonsdk.java.activitys;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕锁屏之后，启动这个一个像素点的透明的act，来防止app被系统kill，可以使进程的优先级在屏幕锁屏时间由4提升为最高优先级1。
 * 启动模式为 singleTask ，防止创建多个
 * 主题是：android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen">
 * 才能达到透明的效果！
 */
public class TransParentActivity extends Activity {
    private static final String TAG = "TransParentActivity";
    private ScreenStatusReverive screenStatusReverive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        if (decorView != null) {
            decorView.setBackgroundColor(Color.TRANSPARENT);
            //增加触摸事件，防止广播监听失败导致没法结束此页面的情况
            decorView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.e(TAG, "用户触摸，结束透明act");
                    finishActAndRemoveTask();
                    return false;
                }
            });
        }
        registBroadcast(this);
    }

    private void finishActAndRemoveTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    private void registBroadcast(Context context) {
        if (screenStatusReverive == null)
            screenStatusReverive = new ScreenStatusReverive();
        context.registerReceiver(screenStatusReverive, getFilter());
    }

    private void unRegistBroadcast(Context context) {
        if (screenStatusReverive != null)
            context.unregisterReceiver(screenStatusReverive);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegistBroadcast(this);
    }

    public IntentFilter getFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_USER_PRESENT);
        return filter;
    }

    class ScreenStatusReverive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case Intent.ACTION_USER_PRESENT:
                        Log.e(TAG, "解锁了，结束透明act");
                        finishActAndRemoveTask();
                        break;
                }
            }
        }
    }
}
