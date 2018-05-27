# <center>Java 基础</center>
# 描述一下 java 的访问修饰符，和它们之间的区别？
##### Java的四个关键字：public、protected、default、private
    适用范围<访问权限范围越小，安全性越高>
    访问权限   类   包  子类  其他包
    
    public     ∨   ∨   ∨    ∨          （对任何人都是可用的）
    protect    ∨   ∨   ∨    ×　　　 （继承的类可以访问以及和private一样的权限）
    default    ∨   ∨   ×    ×　　　 （包访问权限，即在整个包内均可被访问）
    private    ∨   ×   ×    ×　　　 （除类型创建者和类型的内部方法之外的任何人都不能访问的元素）
# int 和 Integer 的区别：
  * 由于Integer变量实际上是对一个Integer对象的引用，所以两个通过new生成的Integer变量永远是不相等的（因为new生
  成的是两个对象，其内存地址不同）。
  * Integer是int的包装类；int是基本数据类型； 
  * Integer变量必须实例化后才能使用；int变量不需要；
  * Integer实际是对象的引用，指向此new的Integer对象；int是直接存储数据值； 
  * Integer的默认值是null；int的默认值是0。

# Java两种数据类型
##### Java两种数据类型分类
  * 基本数据类型，分为boolean、byte、int、char、long、short、double、float；
  * 引用数据类型 ，分为数组、类、接口。
##### Java为每个原始类型提供了封装类
    为了编程的方便还是引入了基本数据类型，但是为了能够将这些基本数据类型当成对象操作，
    Java为每 一个基本数据类型都引入了对应的包装类型（wrapper class），int的包装类
    就是Integer，从Java 5开始引入了自动装箱/拆箱机制，使得二者可以相互转换。
  * 基本数据类型: boolean，char，byte，short，int，long，float，double
  * 封装类类型：Boolean，Character，Byte，Short，Integer，Long，Float，Double
##### Java 自动装箱：将基本数据类型重新转化为对象
Java 为每种基本数据类型都提供了对应的包装器类型
````java
public class Test {  
    public static void main(String[] args) {
        // 装箱
        Integer n1 = new Integer(10);
        // 自动装箱, 声明一个Integer对象
        Integer num = 9;
        //以上的声明就是用到了自动的装箱：解析为:Integer num = new Integer(9);
    }  
}  
``
##### Java 自动拆箱：将对象重新转化为基本数据类型
````java
public class Test {  
    public static void main(String[] args) {  
        // 声明一个Integer对象
        Integer num = 9;
        // 拆箱
        int i1 = num;
        // 进行计算时隐含的有自动拆箱
        System.out.print(num--);
    }  
}  
//装箱
Integer num = 10;
//拆箱
int num1 = num;
``
因为对象时不能直接进行运算的，而是要转化为基本数据类型后才能进行加减乘除。

##### Java 中 == 和 equals 的区别
equals 是值比较，== 是引用比较

##### 一个数组作为参数传递到方法中，被修改了，那么在方法外部，这个数组内的元素是否被改变了？
  * 是的，因为Java方法中传递的是引用传递，所以在操作数组时，实际操作的是该数组的值
##### 抽象方法(abstract)是否可以被 final、static、native修饰？
  * 都不可以，因为抽象方法是必须子类实现的，这是由于 java 中的封装和继承的特性
  * final 方法时不可以被重写，
  * static 是父类必须实现的方法，
  * native是贝蒂语言实现的方法


# HashMap 的底层实现原理

> 在Java编程语言中，最基本的结构就是两种，一个是数组，另外一个是模拟指针（引用），所有的数据结构都可以用这两个
基本结构来构造的，HashMap也不例外。HashMap实际上是一个“链表散列”的数据结构，即数组和链表的结合体。
> HashMap 的底层就是一个数组结构, 数组中的每一个项又是一个链表结构, 新建一个 HashMap 的时候就会初始化一个数组

```java
/**
 * The table, resized as necessary. Length MUST Always be a power of two.
 */
transient Entry[] table;

static class Entry<K,V> implements Map.Entry<K,V> {
    final K key;
    V value;
    Entry<K,V> next;
    final int hash;
    ……
}

public V put(K key, V value) {
    // HashMap允许存放null键和null值。
    // 当key为null时，调用putForNullKey方法，将value放置在数组第一个位置。
    if (key == null)
        return putForNullKey(value);
    // 根据key的keyCode重新计算hash值。
    int hash = hash(key.hashCode());
    // 搜索指定hash值在对应table中的索引。
    int i = indexFor(hash, table.length);
    // 如果 i 索引处的 Entry 不为 null，通过循环不断遍历 e 元素的下一个元素。
    for (Entry<K,V> e = table[i]; e != null; e = e.next) {
        Object k;
        if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
            V oldValue = e.value;
            e.value = value;
            e.recordAccess(this);
            return oldValue;
        }
    }
    // 如果i索引处的Entry为null，表明此处还没有Entry。
    modCount++;
    // 将key、value添加到i索引处。
    addEntry(hash, key, value, i);
    return null;
}

```

# 数组
> 所谓数组，是有序的元素序列。 若将有限个类型相同的变量的集合命名，那么这个名称为数组名。组成数组的各个变量
称为数组的分量，也称为数组的元素，有时也称为下标变量。用于区分数组的各个元素的数字编号称为下标。数组是在程序
设计中，为了处理方便， 把具有相同类型的若干元素按无序的形式组织起来的一种形式。 这些无序排列的同类数据元素的
集合称为数组。数组是用于储存多个相同类型数据的集合。


# 链表和数组有什么异同
不同：链表是链式的存储结构；数组是顺序的存储结构。链表通过指针来连接元素与元素，数组则是把所有元素按次序依次存储。
链表的插入删除元素相对数组较为简单，不需要移动元素，且较为容易实现长度扩充，但是寻找某个元素较为困难；数组寻找某
个元素较为简单，但插入与删除比较复杂，由于最大长度需要再编程一开始时指定，故当达到最大长度时，扩充长度不如链表方便。
相同：两种结构均可实现数据的顺序存储，构造出来的模型呈线性结构。