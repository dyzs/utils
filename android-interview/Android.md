## Q1: Sqlite 怎么做批量插入，效率更高

我发现用transactions能够将数据保存在内存中, 只有在commit时候才写入文件系统. 因此我改动如下:
Java代码  收藏代码
String sql = "INSERT INTO table (number, nick) VALUES (?, ?)";  
db.beginTransaction();  
   
SQLiteStatement stmt = db.compileStatement(sql);  
for (int i = 0; i < values.size(); i++) {  
    stmt.bindString(1, values.get(i).number);  
    stmt.bindString(2, values.get(i).nick);  
    stmt.execute();  
    stmt.clearBindings();  
}  
   
db.setTransactionSuccessful();  
db.endTransaction();

## Q2: 网络通信原理，HTTP 和 TCP 原理

要想理解socket首先得熟悉一下TCP/IP协议族，即传输控制协议/网间协议，定义了主机如何连入因特网及数据如何再它们之间传输的标准。

   * Socket 是应用层与 TCP/IP 协议族通信的中间软件抽象层，它是一组接口。它把复杂的TCP/IP协议族隐藏在 Socket 接口后面，
   对用户来说，一组简单的接口就是全部，让 Socket 去组织数据，以符合指定的协议。

   * https://www.2cto.com/net/201310/251896.html TCP/IP 的三次握手
三次握手
  所谓三次握手（Three-Way Handshake）即建立TCP连接，就是指建立一个TCP连接时，需要客户端和服务端总共发送3个包
  以确认连接的建立。在socket编程中，这一过程由客户端执行connect来触发，整个流程如下图所示：

TCP三次握手
  （1）第一次握手：Client将标志位SYN置为1，随机产生一个值seq=J，并将该数据包发送给Server，Client进入
  SYN_SENT状态，等待Server确认。
  （2）第二次握手：Server收到数据包后由标志位SYN=1知道Client请求建立连接，Server将标志位SYN和ACK都置为1，
  ack=J+1，随机产生一个值seq=K，并将该数据包发送给Client以确认连接请求，Server进入SYN_RCVD状态。
  （3）第三次握手：Client收到确认后，检查ack是否为J+1，ACK是否为1，如果正确则将标志位ACK置为1，ack=K+1，
  并将该数据包发送给Server，Server检查ack是否为K+1，ACK是否为1，如果正确则连接建立成功，Client和Server
  进入ESTABLISHED状态，完成三次握手，随后Client与Server之间可以开始传输数据了。
 
  SYN攻击：
  在三次握手过程中，Server发送SYN-ACK之后，收到Client的ACK之前的TCP连接称为半连接（half-open connect），
  此时Server处于SYN_RCVD状态，当收到ACK后，Server转入ESTABLISHED状态。SYN攻击就是Client在短时间内伪造大
  量不存在的IP地址，并向Server不断地发送SYN包，Server回复确认包，并等待Client的确认，由于源地址是不存在的，
  因此，Server需要不断重发直至超时，这些伪造的SYN包将产时间占用未连接队列，导致正常的SYN请求因为队列满而被丢弃，
  从而引起网络堵塞甚至系统瘫痪。SYN攻击时一种典型的DDOS攻击，检测SYN攻击的方式非常简单，即当Server上有大量半
  连接状态且源IP地址是随机的，则可以断定遭到SYN攻击了，使用如下命令可以让之现行：
  #netstat -nap | grep SYN_RECV

## Q3: Android 内存管理和 GC 回收机制
## Android内存优化——常见内存泄露及优化方案
###如果一个无用对象（不需要再使用的对象）仍然被其他对象持有引用，造成该对象无法被系统回收，以致该对象在堆中所占用
的内存单元无法被释放而造成内存空间浪费，这中情况就是内存泄露。
  * 单例导致内存泄露，单例最好传递 application 的 context
    * 单例模式在Android开发中会经常用到，但是如果使用不当就会导致内存泄露。因为单例的静态特性使得它的生命周期同
    应用的生命周期一样长，如果一个对象已经没有用处了，但是单例还持有它的引用，那么在整个应用程序的生命周期它都不
    能正常被回收，从而导致内存泄露。
  
  * 静态变量导致内存泄露
    * 静态变量存储在方法区，它的生命周期从类加载开始，到整个进程结束。一旦静态变量初始化后，它所持有的引用只有
    等到进程结束才会释放。在Android开发中，静态持有很多时候都有可能因为其使用的生命周期不一致而导致内存泄露，
    所以我们在新建静态持有的变量的时候需要多考虑一下各个成员之间的引用关系，并且尽量少地使用静态持有的变量，
    以避免发生内存泄露。当然，我们也可以在适当的时候讲静态量重置为null，使其不再持有引用，这样也可以避免内存泄露。

  * 非静态内部类导致内存泄露
    * 非静态内部类（包括匿名内部类）默认就会持有外部类的引用，当非静态内部类对象的生命周期比外部类对象的生命周期长时，就会导致内存泄露。
    * 非静态内部类导致的内存泄露在Android开发中有一种典型的场景就是使用Handler。
    * 通常在Android开发中如果要使用内部类，但又要规避内存泄露，一般都会采用静态内部类+弱引用的方式。
    * WeakReference，mHandler通过弱引用的方式持有Activity，当GC执行垃圾回收时，遇到Activity就会回收并释放
    所占据的内存单元。这样就不会发生内存泄露了。
  
  * 未取消注册或回调导致内存泄露
    * 比如我们在Activity中注册广播，如果在Activity销毁后不取消注册，那么这个刚播会一直存在系统中，并且它会一直
    持有Activity引用，导致内存泄露。因此注册广播后在Activity销毁后一定要取消注册。
    * 在注册观察则模式的时候，如果不及时取消也会造成内存泄露。比如使用Retrofit+RxJava注册网络请求的观察者回调，
    同样作为匿名内部类持有外部引用，所以需要记得在不用或者销毁的时候取消注册。
  
  * Timer和TimerTask导致内存泄露
  * 集合中的对象未清理造成内存泄露
    * 这个比较好理解，如果一个对象放入到ArrayList、HashMap等集合中，这个集合就会持有该对象的引用。当我们不再
    需要这个对象时，也并没有将它从集合中移除，这样只要集合还在使用（而此对象已经无用了），这个对象就造成了内存泄露。
    并且如果集合被静态引用的话，集合里面那些没有用的对象更会造成内存泄露了。所以在使用集合时要及时将不用的对象从
    集合remove，或者clear集合，以避免内存泄漏。
  * 资源未关闭或释放导致内存泄露
    * 在使用IO、File流或者Sqlite、Cursor等资源时要及时关闭。这些资源在进行读写操作时通常都使用了缓冲，如果不
    及时关闭，这些缓冲对象就会一直被占用而得不到释放，以致发生内存泄露。因此我们在不需要使用它们的时候就及时关闭，
    以便缓冲能及时得到释放，从而避免内存泄露。

  * 属性动画造成内存泄露
    * 在销毁的时候，没有调用cancle方法。
   
### 总结
  * 内存泄露在Android内存优化是一个比较重要的一个方面，很多时候程序中发生了内存泄露我们不一定就能注意到，所有在
  编码的过程要养成良好的习惯。总结下来只要做到以下这几点就能避免大多数情况的内存泄漏：

    * 构造单例的时候尽量别用Activity的引用；
    * 静态引用时注意应用对象的置空或者少用静态引用；
    * 使用静态内部类+软引用代替非静态内部类；
    * 及时取消广播或者观察者注册；
    * 耗时任务、属性动画在Activity销毁时记得cancel；
    * 文件流、Cursor等资源及时关闭；
    * Activity销毁时WebView的移除和销毁。


### Android 进程优先级
  * android将进程的优先级分为5个层次：
    * 前台进程（Foreground process）。
    * 可见进程（Visible process）。
    * 服务进程（Service process）。
    * 后台进程（Background process）。
    * 空进程（Empty process）。


### 消息推送的跳转方式
  * 如果是App进程已经被系统回收，直接在PendingIntent中传目标Activity的Intent，则在退出目标Activity时会直接
  退出应用，感觉像是闪退了一样；如果是跳转到首页，然后在首页中检测是否是由点击通知进入应用的来进行跳转，这样的话
  首页就会闪屏。综上方法都不是很理想，一个比较好的解决方案是给PendingIntent传递一个Intent数组，分别放置目标
  Activity和首页，这样效果比较好。
``java
    Intent[] intents = new Intent[2];
    Intent intent_main = new Intent(getApplicationContext(), MainActivity.class);
    Intent intent_target = new Intent(getApplicationContext(), TargetActivity.class);
    intents[0] = intent_main;
    intents[1] = intent_target;

    //关键的地方
    PendingIntent contentIntent = PendingIntent.getActivities(context, 0, intents, PendingIntent.FLAG_UPDATE_CURRENT);

    NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
    //省略其他的一些设置
    .setContentIntent(contentIntent)
    //省略其他的一些设置

    Notification notification = builder.build();
    notification.flags = Notification.FLAG_AUTO_CANCEL;
    mNotificationManager.notify((int) System.currentTimeMillis() / 1000, notification);
  * 通过以上的设置后，点击通知栏就会打开TargetActivity，从TargetActivity返回后会打开MainActivity，而不会直接退出 
需要注意的是，MainActivity需要设置启动模式为singleInstance
``
````java
    /**判断应用是否已经启动
    @param context     一个context
    @param packageName 要判断应用的包名
    @return boolean
    */
    public static boolean isAppAlive(Context context, StringpackageName) {
    ActivityManager activityManager =
            (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    List<ActivityManager.RunningAppProcessInfo> processInfos
            = activityManager.getRunningAppProcesses();
    for (int i = 0; i < processInfos.size(); i++) {
        if (processInfos.get(i).processName.equals(packageName)) {
            Log.i("NotificationLaunch",
                    String.format("the %s is running, isAppAlive return true", packageName));
            return true;
        }
    }
    Log.i("NotificationLaunch",
            String.format("the %s is not running, isAppAlive return false", packageName));
    return false;
    }
``

### Android Binder 机制
  * Binder 是 Android 中的一个类，它继承了 IBinder 接口，它是 Android 中的一种跨进通信方式，
  * 
### Android 多渠道打包原理
  *  