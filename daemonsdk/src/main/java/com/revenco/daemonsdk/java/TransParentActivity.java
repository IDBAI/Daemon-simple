package com.revenco.daemonsdk.java;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕锁屏之后，启动这个全屏透明的act，来防止app被系统kill
 */
public class TransParentActivity extends Activity {
    private ScreenStatusReverive screenStatusReverive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
        View decorView = window.getDecorView();
        if (decorView != null) {
            decorView.setBackgroundColor(Color.TRANSPARENT);
            //增加触摸事件，防止广播监听失败导致没法结束此页面的情况
            decorView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    finish();
                    return false;
                }
            });
        }
        registBroadcast(this);
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
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        return filter;
    }

    class ScreenStatusReverive extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                switch (intent.getAction()) {
                    case Intent.ACTION_USER_PRESENT:
                        finish();
                        break;
                    case Intent.ACTION_SCREEN_ON:
                        break;
                }
            }
        }
    }
}
