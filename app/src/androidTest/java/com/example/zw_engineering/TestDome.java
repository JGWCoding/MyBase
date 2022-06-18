package com.example.zw_engineering;

import android.os.Environment;
import android.util.Log;

import com.dome.base.myeditcore.myhandler.DomeUse;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class TestDome implements Serializable {
    @Test
    public void addition_isCorrect() throws Exception {
//        new DomeUse().test();
//        new DomeUse().dome();
        new DomeUse().main(new String[]{});
        System.out.println("My name is longyoung");
    }

    @Test
    public void test() throws Exception {
        TestDome et = new TestDome();
        System.out.println("et=" + et.hashCode() + "===" + new TestDome().hashCode());
        File parent = Environment.getExternalStorageDirectory();
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                new File(parent, "test")));
        out.writeObject(et);

        ObjectInput in = new ObjectInputStream(new FileInputStream(new File(
                parent, "test")));
        et = (TestDome) in.readObject();
        System.out.println("et=" + et.content);

        out.close();
        in.close();
    }

    @Test
    public void test2() {
        SyncUpload.addSync(MySyncUploadHandler.class, "我自己传的");
    }

    public static class MySyncUploadHandler implements SyncUpload.SyncUploadHandler, Serializable {
        @Override
        public boolean syncUpload(Object obj) {
            Log.e("mytag", "syncUpload 开始上传 " + obj);
            return false;
        }
    }


    public static class SyncUpload {
        public interface SyncUploadHandler {
            boolean syncUpload(Object obj);//TODO 上传真正处理的逻辑
        }

        public static void addSync(Class<? extends SyncUploadHandler> handler, Object obj) {
            Log.e("mytag", "开始上传");
            //TODO 这里进行 一个添加到数据库中 -- 存储
            try {
                addFile(handler);
            } catch (Exception e) {
                Log.e("mytag", e.getMessage());
            }
            dispatchSyncUpload(handler, obj);
        }

        static void dispatchSyncUpload(final Class<? extends SyncUploadHandler> handler, final Object obj) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SyncUploadHandler syncUploadHandler = handler.newInstance();
                        boolean upload = syncUploadHandler.syncUpload(obj);
                        //TODO 成功进行删除,不成功进行下次上传
                        if (upload) {

                        }
                    } catch (Exception e) {
                        Log.e("mytag", e.getMessage());
                    }
                }
            }).start();
        }

        public static void addFile(final Class<? extends SyncUploadHandler> handler) throws Exception {
            SyncUploadHandler et = handler.newInstance();
            System.out.println("mytag et=" + et.hashCode());
            String parentStr = Environment.getExternalStorageDirectory() + "/Sync/";
            File parent = new File(parentStr);
            if (!parent.exists()) {
                parent.mkdirs();
            }
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(
                    new File(parent, "test")));
            out.writeObject(et);

            ObjectInput in = new ObjectInputStream(new FileInputStream(new File(
                    parent, "test")));
            et = (SyncUploadHandler) in.readObject();
            System.out.println("mytag et=" + et.hashCode());
            out.close();
            in.close();
            et.syncUpload("文件里取出来的");
        }
    }


    private String content = "是的，我将会被序列化，不管我是否被transient关键字修饰";

}
