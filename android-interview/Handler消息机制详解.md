## Handler的源码分析

  * Handler的消息处理主要有五个部分组成，Message，Handler，Message Queue，Looper和ThreadLocal。

  * Message：Message是在线程之间传递的消息，它可以在内部携带少量的数据，用于线程之间交换数据。
  Message有四个常用的字段，what字段，arg1字段，arg2字段，obj字段。what，arg1，arg2可以携带整型数据，obj可以携带object对象。

  * Handler：它主要用于发送和处理消息的发送消息一般使用sendMessage（）方法,还有其他的一系列sendXXX的方法，
  但最终都是调用了sendMessageAtTime方法，除了sendMessageAtFrontOfQueue（）这个方法而发出的消息经过一系列
  的辗转处理后，最终会传递到Handler的handleMessage方法中。

  * Message Queue：MessageQueue是消息队列的意思，它主要用于存放所有通过Handler发送的消息，这部分的消息会
  一直存在于消息队列中，等待被处理。每个线程中只会有一个MessageQueue对象。

  * Looper：每个线程通过Handler发送的消息都保存在，MessageQueue中，Looper通过调用loop（）的方法，就会进入
  到一个无限循环当中，然后每当发现Message Queue中存在一条消息，就会将它取出，并传递到Handler的handleMessage()
  方法中。每个线程中只会有一个Looper对象。

  * ThreadLocal：MessageQueue对象，和Looper对象在每个线程中都只会有一个对象，怎么能保证它只有一个对象，
  就通过ThreadLocal来保存。Thread Local是一个线程内部的数据存储类，通过它可以在指定线程中存储数据，数据存储以后，
  只有在指定线程中可以获取到存储到数据，对于其他线程来说则无法获取到数据。
  
  * ThreadLocal 用于创建线程的本地变量，我们知道一个对象的所有线程会共享它的全局变量，所以这些变量不是线程安全的，
  我们可以使用同步技术。但是当我们不想使用同步的时候，我们可以选择ThreadLocal变量。

  * 每个线程都会拥有他们自己的Thread变量，它们可以使用get()\set()方法去获取他们的默认值或者在线程内部改变他们的值。
  ThreadLocal实例通常是希望它们同线程状态关联起来是private static属性。在ThreadLocal例子这篇文章中你可以看到
  一个关于ThreadLocal的小程序。


## 个人理解：
  * Android 是一个单线程模型, Android 操作 UI 必须在 UI 线程，如果在 UI 线程中执行耗时操作，就会造成 ANR，
  所以，我们在开启工作线程的时候执行完耗时操作，得到的返回值后需要更新 UI，这时候就需要用到 Handler。
## Handler 处理方式：
  * 使用 Handler 发送一个我们封装好的 Message，这个 Message 中会带有 target 和 args 参数，
  然后将这个 Message 发送到消息队列 MessageQueue 中，然后调用本地的 NativeWake 方法唤醒 Looper，
  Looper 通过 Loop 方法轮询消息队列 MessageQueue，当它发现 MessageQueue 中存在消息，
  就会调用 Message 携带的 target 的 dispatchMessage 方法分发消息，这个target 就是当前的 Handler 对象，
  然后使用 Callback 方式传递到 Handler 的 handlerMessage 方法中，每个线程只会有一个 Looper 对象。

  * Local BroadcastManager 应用内广播实现就是为了解决广播的不安全隐患，可以实现应用内的消息传递或者是消息通知。

## 多线程之 IntentService
  * 因为 IntentService 继承自 Service 所有它拥有 Service 的特性，拥有较高的优先级，不容易被系统杀死，
  因此它适合执行一些较高优先级的异步任务，IntentService 内部也是通过 HandlerThread + Handler 实现异步操作的。
## 多线程之 HandlerThread
  *  










