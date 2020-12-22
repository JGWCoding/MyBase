package com.example.zw_engineering;

import com.blankj.utilcode.util.LogUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        LogUtils.e("异常"+i);
                    }
                    LogUtils.e("1nihao"+i);
                }
            }
        });
        thread.start();
//        thread.interrupt();
        boolean interrupted = false;
//        boolean interrupted = thread.isInterrupted();
        LogUtils.e("nihao"+interrupted);
        LogUtils.e("阿斯蒂芬"+Thread.currentThread().getPriority());
        try {
            Thread.sleep(1000);
            thread.stop();
//             interrupted = thread.isInterrupted();
            LogUtils.e("niha2o"+interrupted);
            thread.join();
        } catch (Exception e) {
            LogUtils.e("异常");
        }
    }


}
