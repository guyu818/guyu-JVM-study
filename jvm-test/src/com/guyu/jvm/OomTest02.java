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

        max=514850816字节	491.0MB
        total=514850816字节	491.0MB
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
