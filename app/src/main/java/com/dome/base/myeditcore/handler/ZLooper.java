package com.dome.base.myeditcore.handler;

import android.util.Log;

public class ZLooper {

    /**
     * 当前线程保存数据对象
     */
    static final ThreadLocal<ZLooper> sThreadLocal = new ThreadLocal<>();

    /**
     * 消息队列
     */
    public ZMessageQueue myQueue;


    public ZLooper() {
        myQueue = new ZMessageQueue();
    }

    /**
     * 创建（准备）MyLooper
     */
    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one MyLooper may be created per thread");
        }
        sThreadLocal.set(new ZLooper());
    }

    /**
     * 获取 当前 MyLooper
     */
    public static ZLooper myLooper() {
        return sThreadLocal.get();
    }

    /**
     * 启动 looper 注意这个有个死循环
     */
    public static void loop() {
        // 从全局ThreadLocalMap中获取唯一looper对象
        ZLooper myLooper = myLooper();
        ZMessageQueue myQueue = myLooper.myQueue;

        for (; ; ) {
            ZMessage myMessage = myQueue.next();
            Log.i("TAG", "loop: " + myMessage);
            if (myMessage != null) {
                myMessage.target.dispatchMessage(myMessage);
            }
        }
    }

}
