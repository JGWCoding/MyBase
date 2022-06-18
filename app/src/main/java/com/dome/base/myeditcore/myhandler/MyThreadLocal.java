package com.dome.base.myeditcore.myhandler;

import java.util.HashMap;
import java.util.Map;

public class MyThreadLocal<T> {


    private Map<Thread,T> map;

    public MyThreadLocal() {
        map=new HashMap<>();
    }  

    public void set(T obj) {  
        map.put(Thread.currentThread(),obj);  
    }  

    public T get() {  
        return map.get(Thread.currentThread());  
    }  

}  