# Java 的四种引用包括强引用，软引用，弱引用，虚引用。
### 强引用(StrongReference)：
    只要引用存在，垃圾回收器永远不会回收
    Object obj = new Object();
    //可直接通过obj取得对应的对象 如obj.equels(new Object());
    而这样 obj对象对后面new Object的一个强引用，只有当obj这个引用被释放之后，
    对象才会被释放掉，这也是我们经常所用到的编码形式。
### 软引用(SoftReference)：
    非必须引用，内存溢出之前进行回收，可以通过以下代码实现
    Object obj = new Object();
    SoftReference<Object> sf = new SoftReference<Object>(obj);
    obj = null;
    sf.get();//有时候会返回null
    这时候sf是对obj的一个软引用，通过sf.get()方法可以取到这个对象，当然，当这个对象被
    标记为需要回收的对象时，则返回null；
    软引用主要用户实现类似缓存的功能，在内存足够的情况下直接通过软引用取值，无需从繁忙的
    真实来源查询数据，提升速度；当内存不足时，自动删除这部分缓存数据，从真正的来源查询这
    些数据。
### 弱引用(WeakReference)：
    第二次垃圾回收时回收，可以通过如下代码实现
    Object obj = new Object();
    WeakReference<Object> wf = new WeakReference<Object>(obj);
    obj = null;
    wf.get();//有时候会返回null
    wf.isEnQueued();//返回是否被垃圾回收器标记为即将回收的垃圾
    弱引用是在第二次垃圾回收时回收，短时间内通过弱引用取对应的数据，可以取到，当执行过第
    二次垃圾回收时，将返回null。
    弱引用主要用于监控对象是否已经被垃圾回收器标记为即将回收的垃圾，可以通过弱引用的
    isEnQueued方法返回对象是否被垃圾回收器标记。
### 虚引用(PhantomReference)：
    垃圾回收时回收，无法通过引用取到对象值，可以通过如下代码实现
    Object obj = new Object();
    PhantomReference<Object> pf = new PhantomReference<Object>(obj);
    obj=null;
    pf.get();//永远返回null
    pf.isEnQueued();//返回是否从内存中已经删除
    虚引用是每次垃圾回收的时候都会被回收，通过虚引用的get方法永远获取到的数据为null，
    因此也被成为幽灵引用。
    虚引用主要用于检测对象是否已经从内存中删除。


# Java GC 垃圾回收机制


# Java 集合 Collections
>

### 什么时候用LinkedList，什么时候用ArrayList ?
    * ArrayList:
    ArrayList是使用数组实现的list，本质上就是数组。ArrayList中的元素可以通过索引随机获取一个元素。
    但是如果该数组已满，当添加新元素时需要分配一个新的数组然后将原来数组的元素移动过去，需要O(n)的时间复杂度。
    添加或删除一个元素需要移动数组中的其他元素。这是ArrayList最大的缺点。
    * LinkedList:
    LinkedList是一个双向链表。因此，当需要获取list中某个元素，需要从头到尾遍历list。
    另一方面，在链表中添加或删除元素很快，只需要O(1)的时间复杂度。从空间上来说，在链表中一个节点
    需要两个额外的指针来指向它的previous和next节点。
    * 总结:
    从时间复杂度来说，如果对list增加或删除操作较多，优先用LinkedList；如果查询操作较多，优先用ArrayList。
    从空间复杂度来说，LinkedList会占用较多空间。







# Java 异常分析
##### java 异常层次结构图
![](https://github.com/dyzs/utils/tree/master/android-interview/imgs/java_exception.jpg)

##### 在 Java 中，所有的异常都有一个共同的祖先 Throwable（可抛出）。Throwable 指定代码中可用异常传播机制通过 Java 应用程序传输的任何问题的共性。
  * Throwable 有两个重要的子类：Exception（异常）和 Error（错误），二者都是 Java 异常处理的重要子类，各自都包含大量子类。
    * Error（错误）
> 是程序无法处理的错误，表示运行应用程序中较严重问题。大多数错误与代码编写者执行的操作无关，而表示代码运行时 JVM（Java 虚拟机）
出现的问题。例如，Java虚拟机运行错误（Virtual MachineError），当 JVM 不再有继续执行操作所需的内存资源时，将出现 OutOfMemoryError。
这些异常发生时，Java虚拟机（JVM）一般会选择线程终止。
> 这些错误表示故障发生于虚拟机自身、或者发生在虚拟机试图执行应用时，如Java虚拟机运行错误（Virtual MachineError）、
类定义错误（NoClassDefFoundError）等。这些错误是不可查的，因为它们在应用程序的控制和处理能力之 外，而且绝大多数是
程序运行时不允许出现的状况。对于设计合理的应用程序来说，即使确实发生了错误，本质上也不应该试图去处理它所引起的异常状
况。在 Java中，错误通过Error的子类描述。
    * Exception（异常）:是程序本身可以处理的异常。
> Exception 类有一个重要的子类 RuntimeException。RuntimeException 类及其子类表示“JVM 常用操作”引发的错误。
例如，若试图使用空值对象引用、除数为零或数组越界，则分别引发运行时异常（NullPointerException、ArithmeticException）
和 ArrayIndexOutOfBoundException。
        * 运行时异常(RuntimeException)
> 都是RuntimeException类及其子类异常，如NullPointerException(空指针异常)、IndexOutOfBoundsException(下标越界异常)等，
这些异常是不检查异常，程序中可以选择捕获处理，也可以不处理。这些异常一般是由程序逻辑错误引起的，程序应该从逻辑角度尽
可能避免这类异常的发生。  运行时异常的特点是Java编译器不会检查它，也就是说，当程序中可能出现这类异常，即使没有用
try-catch语句捕获它，也没有用throws子句声明抛出它，也会编译通过。
        * 编译时异常()
> 是RuntimeException以外的异常，类型上都属于Exception类及其子类。从程序语法角度讲是必须进行处理的异常，如果不处理，
程序就不能编译通过。如IOException、SQLException等以及用户自定义的Exception异常，一般情况下不自定义检查异常。


##### 常见的异常
  * 空指针异常：NullPointerException
  * 类型强制转换异常：ClassCastException
  * 数组下角标越界：ArrayIndexOutOfBoundsException
  * 算术类异常：ArithmeticException
  * 文件未找到异常：FileNotFoundException
  * I/O异常：IOException
  * 方法未找到异常：NoSuchMethodException
  * 非法的状态异常：IllegalArgumentException
  * 内存溢出：OutOfMemoryException

##### 也可以在 Runtime 时自己选择抛出自定义异常: Throw new RuntimeException("exception");