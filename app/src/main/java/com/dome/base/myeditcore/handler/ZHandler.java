package com.dome.base.myeditcore.handler;

public class ZHandler {

    private ZLooper myLooper;
    private ZMessageQueue myQueue;

    public ZHandler() {
        myLooper = ZLooper.myLooper();
        if (myLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread " + Thread.currentThread()
                            + " that has not called MyLooper.prepare()");
        }
        myQueue = myLooper.myQueue;
    }

    /**
     * 消息处理 MyHandler 实现 重写当前
     */
    public void handleMessage(ZMessage msg) {

    }

    /**
     * 发送消息
     */
    public void sendMessage(ZMessage message) {
        message.target = this;
        // 将消息放入到消息队列中
        myQueue.enqueueMessage(message);
    }

    /**
     * 消息分发 处理message的时候调用当前
     */
    public void dispatchMessage(ZMessage myMessage) {
        handleMessage(myMessage);
    }

}