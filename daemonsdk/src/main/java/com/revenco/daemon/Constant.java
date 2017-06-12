package com.revenco.daemon;

/**
 * <p>PROJECT : Daemon-simple</p>
 * <p>COMPANY : wanzhong</p>
 * <p>AUTHOR : Administrator on 2017-05-25 11:51.</p>
 * <p>CLASS DESCRIBE :</p>
 * <p>CLASS_VERSION : 1.0.0</p>
 */
public class Constant {
    public static final String ACTION_CANCEL_JOB_ALARM_SUB = "com.revenco.daemon.java.CANCEL_JOB_ALARM_SUB";
    /**
     * 更多的方式发送的广播ACTION，实现唤醒
     */
    public static final String ACTION_WAKE_UP_BY_MORE_METHOD = "com.revenco.daemon.java.ACTION_WAKE_UP_BY_MORE_METHOD";
    /**
     *  <!--自己的应用互相拉起功能，用于同一个设备，不用应用，如果都使用了这个保活库，则启动其中一个应用，会拉起其他应用-->
     */
    public static final String ACTION_SDK_WAKEUP_INNER = "com.revenco.daemon.java.ACTION_SDK_WAKEUP_INNER";
}
