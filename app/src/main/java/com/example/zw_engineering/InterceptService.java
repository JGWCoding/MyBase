
package com.example.zw_engineering;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.blankj.utilcode.util.LogUtils;

import java.lang.reflect.Method;

/**
 * ClassName:InterceptService <br/>
 * Function: 骚扰拦截的服务. <br/>
 * Date: Nov 8, 2016 11:11:28 AM <br/>
 * 
 * @author Alpha
 * @version
 */
public class InterceptService extends Service {

    private InterceptReceiver receiver;
    private TelephonyManager tm;
    private IntercetPhoneStateListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        interceptSMS();

        interceptTel();
    }

    // 拦截电话
    private void interceptTel() {
        // 电话响铃 --- > 电话号码 --- > 黑名单 --- > 拦截
        LogUtils.e("开始监听电话服务了");
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new IntercetPhoneStateListener();
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    // 拦截短信
    private void interceptSMS() {
        // 广播
        receiver = new InterceptReceiver();
        // 意图过滤器
        IntentFilter filter = new IntentFilter();
        // 要过滤的广播
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        // 指定广播的优先级
        filter.setPriority(Integer.MAX_VALUE);
        // 动态注册一个广播
        registerReceiver(receiver, filter);

    }

    /**
     * 
     * @see TelephonyManager#CALL_STATE_IDLE : 闲置
     * @see TelephonyManager#CALL_STATE_RINGING : 响铃状态
     * @see TelephonyManager#CALL_STATE_OFFHOOK : 摘机,通话
     */
    class IntercetPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            LogUtils.e(state,incomingNumber);
            if (state==TelephonyManager.CALL_STATE_OFFHOOK){
                try {
                    ITelephony telephony = null;

                    IBinder iBinder = null;

                    // 1.获取字节码文件
                    Class<?> clazz =
                            Class.forName("android.os.ServiceManager");
                    // 2.获取方法
                    Method method =
                            clazz.getMethod("getService", String.class);
                    // 3.执行方法
                    iBinder = (IBinder) method.invoke(null,
                            Context.TELEPHONY_SERVICE);
                    telephony = ITelephony.Stub.asInterface(iBinder);
                    telephony.endCall();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

//            if (state == TelephonyManager.CALL_STATE_RINGING) {
//                int type = BlackDbDAO.getType(InterceptService.this,
//                        incomingNumber);
//
//                if (type == BlackBean.TYPE_ALL || type == BlackBean.TYPE_TEL) {
//                    // 挂断电话
//                    // tm.endCall();
//                    try {
//                        ITelephony com.android.internal.telephony = null;
//
//                        IBinder iBinder = null;
//
//                        // 1.获取字节码文件
//                        Class<?> clazz =
//                                Class.forName("android.os.ServiceManager");
//                        // 2.获取方法
//                        Method method =
//                                clazz.getMethod("getService", String.class);
//                        // 3.执行方法
//                        iBinder = (IBinder) method.invoke(null,
//                                Context.TELEPHONY_SERVICE);
//                        com.android.internal.telephony = ITelephony.Stub.asInterface(iBinder);
//                        com.android.internal.telephony.endCall();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
        }
    }

    class InterceptReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            Bundle bundle = intent.getExtras();
//            Object[] objs = (Object[]) bundle.get("pdus");
//            for (Object object : objs) {
//                SmsMessage smsMessage =
//                        SmsMessage.createFromPdu((byte[]) object);
//                // 发信人
//                String sender = smsMessage.getOriginatingAddress();
//
//                int type = BlackDbDAO.getType(context, sender);
//                // 如果发信人属于黑名单号码,就中断广播
//                if (type == BlackBean.TYPE_ALL || type == BlackBean.TYPE_SMS) {
//                    abortBroadcast();
//                }
//            }
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 取消广播
        unregisterReceiver(receiver);
        // 取消电话状态的监听
        tm.listen(listener, PhoneStateListener.LISTEN_NONE);
    }

}
