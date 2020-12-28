package com._basebase.base.myeditcore.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ZMessageQueue {

    // 阻塞队列
    BlockingQueue<ZMessage> myMessages = new ArrayBlockingQueue<>(50);

    public ZMessageQueue() {

    }

    /**
     * 将消息添加到队列
     */
    public void enqueueMessage(ZMessage message) {
        try {
            myMessages.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从消息队列中取出
     */
    public ZMessage next() {
        try {
            return myMessages.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
