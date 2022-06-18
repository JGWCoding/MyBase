package com.dome.base.myeditcore.myhandler;

public class DomeUse {

    public  void main(String[] args) {

        new DomeUse().start();

    }  

    private void start(){  

        MyLooper.prepare();

        onCreate();  

        //死循环，阻塞式  模拟ThreadActivity中的死循环
        MyLooper.loop();

        //下面 的代码通常不会执行  
        System.out.println("exit........");  
        throw new RuntimeException("DomeUse thread loop unexpectedly exited");

    }  
    private void onCreate() {  
        //////////////////////////////////////////////////////////  
        ////// 下面的操作相当于运行在android的UI线程中 ////////////  
        //////////////////////////////////////////////////////////  

        final Thread thread = Thread.currentThread();  
        System.out.println("main thread=" + thread);  

        final MyHandler handler = new MyHandler() {
            @Override  
            public void handleMessage(MyMessage msg) {
                //若thread == Thread.currentThread()，则证明已经运行在主线程中了  
                System.out.println("current thread is main thread? " + (thread == Thread.currentThread()));  
                System.out.println(msg);
            }  
        };  
        // 测试1       主线程创建handler，子线程使用该handler发送消息   
        new Thread() {  
            public void run() {  
                try {//模拟耗时操作  
                    Thread.sleep(1000 * 2);  
                } catch (InterruptedException e) {  
                }
                MyMessage message = new MyMessage();
                message.obj = "new Thread" + Thread.currentThread();  
                message.what = (int) System.currentTimeMillis();  
                //在子线程中发送消息   
                handler.sendMessage(message);  
                try {
                    Thread.sleep(1000 * 2);  
                } catch (InterruptedException e) {  
                }  

                message = new MyMessage();
                message.obj = "hanler...waht==1" ;  
                message.what = 1;  
                //在子线程中发送消息   
                handler.sendMessage(message);  


                message = new MyMessage();
                message.obj = "hanler...waht==2" ;  
                message.what = 2;  
                //在子线程中发送消息   
                handler.sendMessage(message);  

                message = new MyMessage();
                message.obj = "hanler...waht==3" ;  
                message.what = 3;  
                //在子线程中发送消息   
                handler.sendMessage(message);  

            };  
        }.start();  


        // 测试2 在thread内部创建handler，结果会抛出异常  
        new Thread() {  
            public void run() {  
                try {  
                    sleep(1000 * 3);  
                } catch (InterruptedException e) {  
                }  
                /* 
                 * 在线程内部使用默认构造函数创建handler会抛出异常。 
                 * android中也可以在子线程中创建Handler，但要在初始化时传入Looper， 
                 * Looper.getMainLooper()获取到的就是主线程的Looper，所以可以这样创建 
                 *  
                 * new Handler(Looper.getMainLooper()){ 
                        @Override 
                        public void handleMessage(Message msg) { 
                            //运行在主线程中 
                        } 
                    }; 
                 */
                MyLooper.prepare();

                MyHandler h = new MyHandler() {
                    public void handleMessage(MyMessage msg) {
                        System.out.println("haneler msg...." + msg);
                    };  
                };

                MyMessage message = new MyMessage();
                message.obj = "handler in new Thread";  
                message.what = (int) System.currentTimeMillis();  
                //在子线程中发送消息   
                h.sendMessage(message);

                MyLooper.loop();
            };  
        }.start();  

        //////////////////////////////////////////////////////////  
        ////// 上面的操作相当于运行在android的UI线程中 ////////////  
        //////////////////////////////////////////////////////////  

    }  
}  