# jvm

## 1、JVm体系结构

![image-20200406202549096](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406202549096.png)

## 2、类加载器

![image-20200406204151926](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406204151926.png)类加载器运行图

1.虚拟机自带的加载器

2.启动类(根)加载器

3.拓展类加载器

4.应用程序加载器

### 双亲委派机制

![image-20200406205414603](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406205414603.png)

双亲委派机制为了安全，不断的向上去寻找是不是有相同的类，如果怕有就用，如果没有就抛出异常，然后不断的向下去寻找，直到找到，如果找不到就报错了 class not found ，一般我们写的类都是在application中加载的。

## 3、Native

![image-20200406211656856](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406211656856.png)唬住面试官，举这个例子，这里面的start0()方法就是调用了里面的本地方法

## 4、PC寄存器

 每个线程都有一个程序计数器，是线程私有的，就是一个指针，在执行引擎读取下一条指令的时候，加一，占据了非常少的内存空间。

## 5、方法区

方法区被所有线程共享，此区域属于 --共享区域

**静态常量、常量、类信息(构造方法、接口定义)、运行时的常量池 存在方法区中，但是new出来的实例变量是存在堆中的，和方法区无关。**

static final Class 常量池

## 6、栈

![image-20200406220034450](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406220034450.png)

![image-20200406215954659](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406215954659.png)

![image-20200406220137460](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406220137460.png)

![image-20200406215802713](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200406215802713.png)

## 7、三种jvm

- Sun公司 HotSpot
- BEA JRockit
- IBM J9 vm

## 8、堆

Heap,一个Jvm只有一个堆内存，堆内存的大小试可以调节的。

类加载器读取了类文件后，一般把什么东西放在堆中？类、方法、常量、变量~，保存我们所有引用类型的真实对象。

堆内存中细分成三个区域

- 新生区(Eden) 伊甸园区 Young/New
- 养老区 old
- 永久区 perm 

![image-20200407200757071](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407200757071.png)

GC垃圾回收主要在伊甸园区和养老区

假设内存满了，OOM，堆内存不够！无线的去new对象不断的把老年代也撑满了

举例

Exception in thread "main" java.lang.OutOfMemoryError: Java heap space

```java
String str="guyulearnjvm";
while (true) {
    str+=str+new Random().nextInt(88888888)+new Random().nextInt(99999999);
}

```

JDK8之后永久存储区改了个名字（元空间）

**新生区**

- 类：诞生和成长的地方，甚至死亡
- 伊甸园，所有的对象都是在伊甸园区new出来的
- 幸存者区（0.1）

**老年区**

经过一定次数洗礼之后还存活的，被移到养老区，这里的次数默认是15，

真理：99%的对象是临时对象，不会到这里

**永久区**

这个区域常驻内存的。用来存放JDK自身携带的Class对象。interface元数据，存储的是Java运行时一些环境或类信息，这个区域不存在垃圾回收，关闭Vm虚拟机就会释放这个区域的内存。

一个启动类，加载了大量的第三方jar包，Tomcat部署了太多的应用，大量动态生成的反射类。不断的被加载。直到内存满，就会出现OOM；

- jdk1.6之前：永久代，常量池是在方法区。
- jdk1.7 : 永久代，但是慢慢的退化了，去永久代，常量池在堆中。
- jdk1.8之后：无永久代，常量池在元空间。

![image-20200407204806755](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407204806755.png)

元空间逻辑上存在，物理上不存在

jvm常用的命令

-Xms512m(指定初始化内存分配大小 1/64)

 -Xmx512m(设置最大的分配内存，默认为1/4)

 -XX:+PrintGCDetails(打印GC的日志细节)

-XX:+HeapDumpOnOutOfMemoryError(OOM 的Dump 拓展后面可以改成ClassNotFound等等，自行百度 )

```java
package com.guyu.jvm;

public class OomTest02 {
    public static void main(String[] args) {
        //返回虚拟机尝试使用的最大内存
        long max = Runtime.getRuntime().maxMemory();//返回的是字节数
        //返回jvm的初始化总内存
        long total = Runtime.getRuntime().totalMemory();

        System.out.println("max="+max+"字节\t"+(max/(double)1024/1024)+"MB");
        System.out.println("total="+total+"字节\t"+(total/(double)1024/1024)+"MB");
        //总结
        // 默认情况下：分配的总内存 是电脑的内存的1/4 而初始化的内存：1/64

        /*
        发生OOM错误该怎么办
        1.先尝试着调大堆内存，看是不是堆内存不够，如果还报错，说明程序存在错误
        2.分析内存，用内存分析工具看哪里出错了，这里用的时IDEA的jprofiler插件
        -Xms512m -Xmx512m -XX:+PrintGCDetails
        下面计算可以得出，新生代+老年代就是堆内存的大小，所以元空间是逻辑上的存在，物理上的不存在

        max=514850816字节    491.0MB
        total=514850816字节  491.0MB
        Heap
         PSYoungGen      total 153088K, used 10527K [0x00000000f5580000, 0x0000000100000000, 0x0000000100000000)
          eden space 131584K, 8% used [0x00000000f5580000,0x00000000f5fc7c98,0x00000000fd600000)
          from space 21504K, 0% used [0x00000000feb00000,0x00000000feb00000,0x0000000100000000)
          to   space 21504K, 0% used [0x00000000fd600000,0x00000000fd600000,0x00000000feb00000)
         ParOldGen       total 349696K, used 0K [0x00000000e0000000, 0x00000000f5580000, 0x00000000f5580000)
          object space 349696K, 0% used [0x00000000e0000000,0x00000000e0000000,0x00000000f5580000)
         Metaspace       used 3519K, capacity 4500K, committed 4864K, reserved 1056768K
          class space    used 389K, capacity 392K, committed 512K, reserved 1048576K

         */
    }
}
```

在一个项目中，突然出现了OOM的错误，那么该如何排查，研究为什么出错

- 能够找到代码的第几行出错最好，实现方式;内存快照分析工具，MAT，Jprofiler
- Debug,一行一行的分析

**MAT、Jprofiler作用**

- 分析Dump内存文件，快速定位内存泄漏
- 获取堆中的数据
- 获取最大的对象
- ……

## 9、模拟调试OOM错误，使用IDEA插件jprofile

1.安装jprofile的IDEA插件

2.去网上下载jprofiler工具

3.设置连接

4.制造错误

```java
package com.guyu.jvm;

import java.util.ArrayList;
//-Xms1m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError ##jvm options
public class OomTest03WithJprofiler {

    byte[] array = new byte[1 * 1024 * 1024];//1m

    public static void main(String[] args) {
        ArrayList<OomTest03WithJprofiler> list=new ArrayList<>();
        int count=0;
        try{
            while (true) {
                list.add(new OomTest03WithJprofiler());
                count+=count;
            }

        }catch (Error e){
            System.out.println("count+"+count);
            e.printStackTrace();
        }


    }
}
```

5.设置jvm参数

//-Xms1m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError ##jvm options

![image-20200407214724873](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407214724873.png)

6.启动测试出现下面的错误抛出

![image-20200407214813150](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407214813150.png)

7.查看Dump文件

![image-20200407215003008](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407215003008.png)

![image-20200407215118885](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407215118885.png)

8.双击打开之后，红圈标的几个是比较常用的

![image-20200407215527572](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407215527572.png)

9.一般先去查看大对象，看看那个对象出现异常了

![image-20200407215843225](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407215843225.png)

10.查看线程的Dump来判断哪一行代码出现问题

![image-20200407220024781](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407220024781.png)

11.至此以此比较完整的错误定位就结束了

12.

## 10、GC垃圾回收

![image-20200407144428873](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407144428873.png)

JVM在进行GC的时候，并不是针对这三个区域进行统一回收。大部分的时候，回收都是新生代。

新生代

幸存代（from、to)

老年代

GC 的两种类型：轻GC(普通GC)、重GC(全局GC 、full GC)

**GC题目**

- jvm的内存模型和分区，详细到每个区放什么东西
- 堆内存的分区有哪些？Eden、from、to、老年区，说说他们的特点
- GC的算法有哪些？标记清除算法、标记压缩算法、复制算法、引用计数器算法，他们是怎么用的？
- 轻GC和重GC什么时候发生？
  - 轻GC一般针对新生代 重GC一般发生在老年代也快满的时候，用来清理出空间

## 11、GC算法

### 1、引用计数法(基本上不用，对象太多了，根本不好用这个方法)

![image-20200407184951249](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407184951249.png)



### 2、复制算法

![image-20200407153815822](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407153815822.png)

![image-20200407154105167](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407154105167.png)复制算法的动态图，核心是from、to相互转化，是逻辑上的转换。

+ 好处：没有内存碎片
+ 坏处：浪费了内存空间，多了一半的空间永远是空to，假设对象100%存活，这个问题就被放大，很容易出现OOM问题，因为堆内存比较少。复制算法的最佳使用场景; 对象存活度较低————————所以适合新生代区算法

### 3、标记清除算法

![image-20200407191658280](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407191658280.png)

+ 优点：不需要额外的内存空间
+ 缺点：两次扫描，严重浪费时间，会产生内存碎片

### 4、标记压缩法(针对标记清除法优化)

![image-20200407192352253](C:\Users\asus\AppData\Roaming\Typora\typora-user-images\image-20200407192352253.png)



## 12、总结

内存效率：复制算法>标记清除算法>标记压缩算法（时间复杂度）

内存整齐度：复制算法=标记压缩算法>标记清除算法

内存利用率：标记压缩算法=标记清除算法>复制算法



思考一个问题：难道没有最优算法吗？

答案：没有，没有最好的算法，只有最合适的算法---------》GC：分代收集算法



年轻代：

+ 存活率低
+ 复制算法

老年代

+ 区域大：存活率
+ 标记清除（内存碎片不是太多 ）+标记压缩混活 实现

## 13、JMM(Java Memory Model)

可以参考https://www.jianshu.com/p/15106e9c4bf3















