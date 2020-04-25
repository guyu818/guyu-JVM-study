package com.guyu.jvm;

import java.util.Random;
//-Xms8m -Xmx8m -XX:+PrintGCDetails
//设置最大使用内存和初始化内存为8m,并打印GC过程
public class OomTest01 {
    public static void main(String[] args) {
        String str="guyulearnjvm";
        while (true) {
            str+=str+new Random().nextInt(88888888)+new Random().nextInt(99999999);
        }
    }
}
