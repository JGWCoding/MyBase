package com._basebase.base.myeditcore.myhandler;

public class MyLooper {
    private static final MyThreadLocal<MyLooper> threadLocal = new MyThreadLocal<>();
    /**
     * 存储Message的队列，阻塞式，没有消息则一直等待
     */
    final MyMessageQueue messageQueue;


    private MyLooper() {
        messageQueue = new MyMessageQueue();
    }

    /**
     * 为该线程创建Looper，
     * 若该线程已经有Looper了则不需要再次调用prepare
     */
    public static void prepare() {
        if (threadLocal.get() != null) {
            throw new RuntimeException("Only one MyLooper may be created per thread");
        }
        threadLocal.set(new MyLooper());
    }

    public static void loop() {
        MyLooper myLooper = myLooper();
        if (myLooper == null) {
            throw new RuntimeException("No MyLooper; MyLooper.prepare() wasn't called on this thread.");
        }
        MyMessageQueue messageQueue = myLooper.messageQueue;

        for (; ; ) {
            MyMessage message = messageQueue.next();
            message.target.handleMessage(message);
        }
    }

    /**
     * 获取当先线程的Looper
     *
     * @return
     */
    public static MyLooper myLooper() {
        return threadLocal.get();
    }
}  