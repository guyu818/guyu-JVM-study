package com.guyu.jvm;

import java.util.ArrayList;
import java.util.Arrays;

//-Xms1m -Xmx8m -XX:+HeapDumpOnOutOfMemoryError ##jvm options
public class OomTest03WithJprofiler {

    byte[] array = new byte[1 * 1024 * 1024];//1m

    public static void main(String[] args) {
        ArrayList<OomTest03WithJprofiler> list=new ArrayList<>();
        int count=0;
        try{
            while (true) {
                list.add(new OomTest03WithJprofiler());
                count+=1;
            }

        }catch (Error e){
            System.out.println("count+"+count);
            e.printStackTrace();
        }


    }
}
