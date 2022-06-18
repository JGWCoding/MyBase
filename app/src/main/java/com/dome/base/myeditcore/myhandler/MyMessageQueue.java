package com.dome.base.myeditcore.myhandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyMessageQueue {


    private BlockingQueue<MyMessage> blockingQueue=new LinkedBlockingQueue<>();

    /** 
     * 阻塞式，没有消息则一直等待 
     * @return 
     */  
    public MyMessage next() {
        try {  
            return blockingQueue.take();  
        } catch (InterruptedException e) {  
            throw new RuntimeException();  
        }  
    }  

    /** 
     * 插入到消息队列尾部 
     * @param message 
     */  
    void enqueueMessage(MyMessage message) {
        try {  
            blockingQueue.put(message);  
        } catch (InterruptedException e) {
            e.printStackTrace();  
        }  
    }  
}  