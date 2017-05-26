package com.revenco.daemonsdk.java.notifys;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.service.notification.NotificationListenerService;

/**
 * API 18以上有效,不需要处理任何信息！需要在设置-》安全-》勾选允许此app接收通知消息
 */
@SuppressLint("OverrideAbstract")
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NotificationMonitor extends NotificationListenerService {
    private static final String TAG = "NotificationMonitor";

    public NotificationMonitor() {
    }
//    @Override
//    public void onNotificationPosted(StatusBarNotification sbn) {
////        super.onNotificationPosted(sbn);
//        Log.d(TAG, "onNotificationPosted --> " + sbn.toString());
//    }
//
//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn) {
////        super.onNotificationRemoved(sbn);
//        Log.d(TAG, "onNotificationRemoved --> " + sbn.toString());
//    }
//
//    @Override
//    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
////        super.onNotificationRemoved(sbn, rankingMap);
//        Log.d(TAG, "onNotificationRemoved --> " + sbn.toString() + "  rankingMap = " + rankingMap.toString());
//    }
//
//    @Override
//    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
////        super.onNotificationPosted(sbn, rankingMap);
//        Log.d(TAG, "onNotificationPosted --> " + sbn.toString() + "  rankingMap = " + rankingMap.toString());
//    }
//
//    @Override
//    public void onNotificationRankingUpdate(RankingMap rankingMap) {
////        super.onNotificationRankingUpdate(rankingMap);
//        Log.d(TAG, "onNotificationRankingUpdate -->   rankingMap = " + rankingMap.toString());
//    }
}
