# daemonsdk Android保活库
###### 这个保活库，站在巨人肩膀之上进行封装的，主要在五个大方向上实现：  
### 1、
正确引导用户添加白名单，这个方法可以非常有效的进行保活，使用了这个库，并获得了作者授权：https://github.com/xingda920813/HelloDaemon  
代码位置在：Daemon-simple\daemonsdk\src\main\java\com\revenco\daemon\java 包


### 2、
native层的暴力监听保活方案，也可以达到较好的效果，但是对于新的机型版本有可能失效，使用了这个库，并获得了作者授权：https://github.com/Marswin/MarsDaemon  
代码位置在：Daemon-simple\daemonsdk\src\main\java\com\revenco\daemon\natives 包  
### 3、
添加了一些提高进程优先级的方案，比如不可见的notifycation，锁屏情况下的一个像素透明的activity，账户同步功能  
### 4、
使用了第三方app开启的唤醒功能，反编译了各大主流的app，添加ACTION配置，仿造他们的service，以便可能被隐式启动  
### 5、
添加了第三方的推送服务配置，包括极光推送，小米推送，华为推送，信鸽推送，百度推送，个推推送，以便可以在同一个设备上有可能集成相同推送的app启动时候可以唤醒  

***特性功能***：  
如果同一个设备有多个APP集成了这个保活库，则启动任何一个APP将会通过隐式启动的方式唤醒未启动的服务。
  




## 说明：  
1、引导用户添加白名单的方式，这种方式是最可靠最友好的保活方式，并且不受Android系统版本的限制。


2、native层的保活方式，可以参照原作者的博客，有详细讲解，虽然实际测试中有部分的机型达不到预期的效果，但是这不失为一个非常好的解决方案。

3、根据不同的Android系统版本，利用Android的系统漏洞，或者一些系统的特性的保活手段  
	3.1、在API19 以下，设置空的notification，而不显示在通知栏的方式漏洞，可以提升进程的优先级。  
	3.2、在锁屏时候，启动一个一个像素的Activity，提升进程优先级，避免在某些机型在锁屏时候进程被kill，适用于任何的系统版本。   
	3.3、增加了账户同步功能，此账户同步功能为空实现，仅仅是让系统定时触发同步功能一达到唤醒我们的服务，适用于任何的系统版本，弊端是，1、在系统设置下面可以看到账户信息，2、系统的下拉快捷方式那里关闭了自动同步功能的话，将失效，但是，此方案还是不错的。   
	3.4、增加了看门狗守护服务（在第一点中实现），定时查看服务的状态。

4、反编译各大主流app，在AndroidManifest上配置ACTION，可以在一定的程度上唤醒，适用于任何的系统版本，有测试成功案例：被百度云盘唤醒。   

5、添加第三方推送的配置，仿造一些服务和接收器，可以在一定的程度上唤醒,适用于任何的系统版本，有测试成功案例：启动了集成极光推送的app，可以被唤醒。


## 集成使用方式：
#### 1、使用方式在build.gradle添加如下即可：  
compile 'com.idba:daemonsdk:1.0'

#### 2、AndroidManifest.xml 配置如下：
	2.1 配置核心业务Service 和一个 辅助的 receive（注意：必须配置在同一个进程中）
	<!--下面的辅助接收器和核心业务服务必须配置在同一个进程中______________start-->
        <receiver
            android:name=".wakeup.assistantReceiver"
            android:enabled="true"
            android:exported="true"
            android:process=":business">
        </receiver>

        <service
            android:name=".TraceServiceImpl"
            android:enabled="true"
            android:exported="true"
            android:process=":business">
        </service>
        <!--下面的辅助接收器和核心业务服务必须配置在同一个进程中______________end-->
	
	2.2 配置核心的广播接收器，
		<!--唤醒接收器必须在application模块，只需要定义两个SDK内部的ACTION即可，便于唤醒 业务服务_________start-->
        <receiver
            android:name=".wakeup.WakeUpReceiver"
            android:process=":watch">
            <intent-filter>
                <!--用于取消服务工作,定义在SDK内部-->
                <action android:name="com.revenco.daemon.java.CANCEL_JOB_ALARM_SUB"/>
                <!--更加多的唤醒方式，在SDK内部实现，然后外发这个ACTION广播，实现唤醒-->
                <action android:name="com.revenco.daemon.java.ACTION_WAKE_UP_BY_MORE_METHOD"/>
            </intent-filter>
        </receiver>
        <!--唤醒接收器必须在application模块，只需要定义两个SDK内部的ACTION即可，便于唤醒 业务服务_________end-->





#### 3、在MainActivity中实现启动（注意：请不要将这个启动放在自定义的Application中，因为app处于多进程环境，如果将启动代码放在Application中，将会触发多次执行）
		
	
    /**
     * 在子线程配置，避免阻塞主线程
     */
    private void Start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //主要的业务逻辑进程
                String processName = "com.revenco.app:business";
                String serviceName = TraceServiceImpl.class.getCanonicalName();
                String receiveName = assistantReceiver.class.getCanonicalName();
                DaemonManager.INSTANCE.init(MainActivity.this, processName, serviceName, receiveName);
                //初始化开启LOG日志记录到SDCard，方便观察app如何被唤醒的日志
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

#### 4、TraceServiceImpl 核心业务服务，请继承 AbsWorkService，请参照simple的代码框架，实现自己的业务逻辑。

#### 5、为了统一归类，创建一个wakeup package ,分别创建一个Receive 和 Service，作为Native层保活的辅助类
	assistantReceiver 类 ：

	/**
 	* 辅助接收器,空实现即可
 	*/
	public class assistantReceiver extends BroadcastReceiver {
    public assistantReceiver() {
    	}

    @Override
    public void onReceive(Context context, Intent intent) {
    	}
	}

	WakeUpReceiver 类:
	
	/**
 	* 这个接收器唤醒业务服务，这里唤醒业务服务
 	*/
	public class WakeUpReceiver extends BroadcastReceiver {
    private static final String TAG = "WakeUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && Constant.ACTION_CANCEL_JOB_ALARM_SUB.equals(intent.getAction())) {
            WatchDogService.cancelJobAlarmSub();
            return;
        }
        //动态注册屏幕解锁开锁ACTION，等等以及自定义的其他广播，以唤醒app
        try {
            XLog.log2Sdcard(TAG, "ACTION =  " + intent.getAction() + " --> 唤醒业务服务！");
            context.startService(new Intent(context, TraceServiceImpl.class));
        } catch (Exception e) {
            e.printStackTrace();
	        }
	    }
	}
## 测试案例

## 联系方式
	radio.ysh@qq.com
