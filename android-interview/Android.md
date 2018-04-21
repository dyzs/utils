# Q1: Sqlite 怎么做批量插入，效率更高

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

# Q2: 网络通信原理，HTTP 和 TCP 原理

要想理解socket首先得熟悉一下TCP/IP协议族，即传输控制协议/网间协议，定义了主机如何连入因特网及数据如何再它们之间传输的标准。

   * Socket 是应用层与 TCP/IP 协议族通信的中间软件抽象层，它是一组接口。它把复杂的TCP/IP协议族隐藏在 Socket 接口后面，
   对用户来说，一组简单的接口就是全部，让 Socket 去组织数据，以符合指定的协议。

   * https://www.2cto.com/net/201310/251896.html TCP/IP 的三次握手
三次握手
  所谓三次握手（Three-Way Handshake）即建立TCP连接，就是指建立一个TCP连接时，需要客户端和服务端总共发送3个包
  以确认连接的建立。在socket编程中，这一过程由客户端执行connect来触发，整个流程如下图所示：

### TCP三次握手
   * (1)第一次握手：Client将标志位SYN置为1，随机产生一个值seq=J，并将该数据包发送给Server，Client 进入 SYN_SENT 状态，等待 Server 确认。
   * (2)第二次握手：Server收到数据包后由标志位SYN=1知道Client请求建立连接，Server将标志位SYN和ACK都置为1，ack=J+1，随机产生一个值seq=K，并将该数据包发送给Client以确认连接请求，Server进入SYN_RCVD状态。
   * (3)第三次握手：Client收到确认后，检查ack是否为J+1，ACK是否为1，如果正确则将标志位ACK置为1，ack=K+1，并将该数据包发送给Server，Server检查ack是否为K+1，ACK是否为1，如果正确则连接建立成功，Client和Server进入ESTABLISHED状态，完成三次握手，随后Client与Server之间可以开始传输数据了。
 
### SYN攻击：
  在三次握手过程中，Server发送SYN-ACK之后，收到Client的ACK之前的TCP连接称为半连接（half-open connect），此时Server处于SYN_RCVD状态，当收到ACK后，Server转入ESTABLISHED状态。SYN攻击就是Client在短时间内伪造大量不存在的IP地址，并向Server不断地发送SYN包，Server回复确认包，并等待Client的确认，由于源地址是不存在的，因此，Server需要不断重发直至超时，这些伪造的SYN包将产时间占用未连接队列，导致正常的SYN请求因为队列满而被丢弃，
  从而引起网络堵塞甚至系统瘫痪。SYN攻击时一种典型的DDOS攻击，检测SYN攻击的方式非常简单，即当Server上有大量半连接状态且源IP地址是随机的，则可以断定遭到SYN攻击了，使用如下命令可以让之现行：  
  #netstat -nap | grep SYN_RECV

# Q3: Android 内存管理和 GC 回收机制
### Android 内存管理
##### 所有的内存都是基于物理内存的，即移动设备上的 RAM。当启动一个Android程序时，会启动一个Dalvik vm 进程，系统会给它分配固定的内存空间，这块内存会映射到RAM上某个区域，然后Android程序就运行在这块空间上。JAVA里会将这块空间分成Stack栈内存和Heap堆内存。stack里存放对象的引用，heap里存放实际对象数据。
![](https://github.com/dyzs/utils/tree/master/android-interview/imgs/memory_allocation.jpg)
  * 内存分配
  
  * 内存回收


### Android内存优化——常见内存泄露及优化方案
#####如果一个无用对象（不需要再使用的对象）仍然被其他对象持有引用，造成该对象无法被系统回收，以致该对象在堆中所占用的内存单元无法被释放而造成内存空间浪费，这中情况就是内存泄露。

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
    
  * 布局优化
    * 使用 HierarchyViewer 查看并减少 OverDraw。
    * 使用 include 标签：复用 xml。
    * 使用 merge 标签：解决 include 标签的问题，减少一个层级，并且方便使用。
    * 重用系统资源：比如一些系统颜色和系统样式 style。
   
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


# Q4：Android 进程优先级
  * android将进程的优先级分为5个层次：
    * 前台进程（Foreground process）。
    * 可见进程（Visible process）。
    * 服务进程（Service process）。
    * 后台进程（Background process）。
    * 空进程（Empty process）。


# Q5：消息推送的跳转方式
  * 如果是App进程已经被系统回收，直接在PendingIntent中传目标Activity的Intent，则在退出目标Activity时会直接
  退出应用，感觉像是闪退了一样；如果是跳转到首页，然后在首页中检测是否是由点击通知进入应用的来进行跳转，这样的话
  首页就会闪屏。综上方法都不是很理想，一个比较好的解决方案是给PendingIntent传递一个Intent数组，分别放置目标
  Activity和首页，这样效果比较好。
````java
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

# Q6：Android Binder 机制
  * Binder 是 Android 中的一个类，它继承了 IBinder 接口，它是 Android 中的一种跨进通信方式，

  * 为什么使用 Binder ?
    主要有两个方面的原因：
    * 性能方面：
    在移动设备上（性能受限制的设备，比如要省电），广泛地使用跨进程通信对通信机制的性能有严格的要求，Binder 相对出传统的 Socket 方式，更加高效。Binder 数据拷贝只需要一次，而管道、消息队列、Socket 都需要2次。

    * 安全方面：
    传统的进程通信方式对于通信双方的身份并没有做出严格的验证，比如Socket通信ip地址是客户端手动填入，很容易进行伪造，而Binder机制从协议本身就支持对通信双方做身份校检，因而大大提升了安全性。还有一些好处，如实现面象对象的调用方式，在使用Binder时就和调用一个本地实例一样。

##### Binder的线程管理:
    每个Binder的Server进程会创建很多线程来处理 Binder 请求，可以简单的理解为创建了一个
    Binder 的线程池吧。
    而真正管理这些线程并不是由这个 Server 端来管理的，而是由Binder驱动进行管理的。
    一个进程的 Binder 线程数默认最大是16，超过的请求会被阻塞等待空闲的 Binder 线程。
    理解这一点的话，你做进程间通信时处理并发问题就会有一个底，比如使用 ContentProvider
    时（又一个使用Binder机制的组件），
    你就很清楚它的CRUD（创建、检索、更新和删除）方法只能同时有16个线程在跑。
# Q7：Android 多渠道打包原理
  *

# Q8：Activity 启动流程

# Q9：Android 进程保活

# Q10：WebSocket

# Q11：Android AIDL
### AIDL是一个缩写，全称是Android Interface Definition Language，也就是 Android 接口定义语言。编译器可以通过 aidl 文件生成一段代码，通过预先定义的接口达到两个进程内部通信跨界访问。
### RPC（Remote Procedure Call 远程进程调用）

##### 服务端：
  * 创建一个 AIDL 接口，并写入自己要在进程间通信用的抽象方法。
  * 创建一个远程 Service（android:process即可开启多进程），在 Service 中创建一个类继承 AIDL 接口中的 Stub 类(extends Binder 实现了iBinder 接口)并实现 Stub 中的抽象方法，并在 onBind()中返回这个对象
##### 客户端：


# Q12：Android 8.0 奥利奥，代号(O)
升级的重点是电池续航能力、速度和安全，让用户更好地控制各种应用程序。谷歌正慢慢让安卓系统向竞争对手苹果的iOS靠拢，加大了对 App 在后台操作的限制。这种限制在一定程度上延长了安卓机在“睡眠”（Doze）模式下的电池的续航能力，它让不在使用的 App 进入睡眠状态，使用时再唤醒。它要达到的目标是在不卸载程序、不改变用户使用习惯的情况下，减少后台应用的用电。同时，这种对后台应用的限制也会加快运行的速度。
##### GooglePlayProtect
  * 这个功能主要用于 GooglePlay 中，下载的应用和游戏将会经过它的排查，来看看是否是有害甚至携带病毒的应用，不过国内用户可能不能访问 GooglePlay。
##### PinnedShortcuts
  * 类似 iOS 的 3DTouch。
##### SmartTextSelection
  * 系统将会预测用户将使用某些电话或者其他选项出现在将会用到的应用中，举个例子就是如果邀请朋友来吃饭，系统会根据发送的地址来开启地图并且导航。
##### 字体优化
  * Android O 中谷歌还增加了对系统字体的更多支持，开发者可以自行更改字体样式，让用户有了更多字体的选择。
##### 表情符号
  * Unicode 10标准的表情符号，比原来新加超过60个表情符，这也是比较明显的改变之一。
##### 分屏
  * 在AndroidO中，分屏画中画功能得到了强化，变得更加流畅，而且悬浮窗可以随意拖动位置，然后在主屏幕中继续工作。



# Q13：MVC，MVP 和 MVVM 的区别


# Q14：架构设计需要考虑哪些方面

# Q15：有没有好的埋点统计方案
  * Umeng 统计中的埋点，百点收集

# Q16：组件化有什么好的实现方式，热更新

# Q17：手机误点多个重复的 Activity，有什么好的规避方案

# Q18：App 性能优化有没有好的框架复用，从 UI 到代码监测

# Q19：冷启动有没有好的优化方式

# Q20：Android LruCache
##### 图片三级缓存原理图：
![](https://github.com/dyzs/utils/tree/master/android-interview/imgs/LruCache.jpg)
##### Android原生为我们提供了一个LruCache，其中维护着一个LinkedHashMap。LruCache可以用来存储各种类型的数据，但最常见的是存储图片（Bitmap）。LruCache创建LruCache时，我们需要设置它的大小，一般是系统最大存储空间的八分之一。 LruCache的机制是存储最近、最后使用的图片，如果LruCache中的图片大小超过了其默认大小，则会将最老、最远使用的图片移除出去。
##### 当图片被LruCache移除的时候，我们需要手动将这张图片添加到软引用（SoftReference）中。我们需要在项目中维护一个由SoftReference组成的集合，其中存储被LruCache移除出来的图片。软引用的一个好处是当系统空间紧张的时候，软引用可以随时销毁，因此软引用是不会影响系统运行的，换句话说，如果系统因为某个原因OOM了，那么这个原因肯定不是软引用引起的。

  * 图片的三级缓存：
    * 当我们的APP中想要加载某张图片时，先去LruCache中寻找图片，如果LruCache中有，则直接取出来使用，如果LruCache中没有，则去SoftReference中寻找，如果SoftReference中有，则从SoftReference中取出图片使用，同时将图片重新放回到LruCache中，如果SoftReference中也没有图片，则去文件系统中寻找，如果有则取出来使用，同时将图片添加到LruCache中，如果没有，则连接网络从网上下载图片。图片下载完成后，将图片保存到文件系统中，然后放到LruCache中。





