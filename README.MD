**daemonsdk**
这个保活库，站在巨人肩膀之上进行封装的，主要在五个大方向上实现：
1、正确引导用户添加白名单，这个方法可以非常有效的进行保活，使用了这个库，并获得了作者授权：https://github.com/xingda920813/HelloDaemon
    代码位置在：Daemon-simple\daemonsdk\src\main\java\com\revenco\daemon\java 包
2、native层的暴力监听保活方案，也可以达到较好的效果，但是对于新的机型版本有可能失效，使用了这个库，并获得了作者授权：https://github.com/Marswin/MarsDaemon
    代码位置在：Daemon-simple\daemonsdk\src\main\java\com\revenco\daemon\natives 包
3、添加了一些提高进程优先级的方案，比如不可见的notifycation，锁屏情况下的一个像素透明的activity，账户同步功能
4、使用了第三方app开启的唤醒功能，反编译了各大主流的app，添加ACTION配置，仿造他们的service，以便可能被隐式启动
5、添加了第三方的推送服务配置，包括极光推送，小米推送，华为推送，信鸽推送，百度推送，个推推送，以便可以在同一个设备上有可能集成相同推送的app启动时候可以唤醒

使用方式在build.gradle添加如下即可：
compile 'com.idba:daemonsdk:1.0'