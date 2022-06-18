package com.dome.base.db.greendao.dao;

/*
 * @Description:
 * 作者  陈兆龙
 * 时间 2019年01月11
 *
 */


import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.Utils;


public class GreenDaoDBManager {
    // 是否加密
    public static final boolean ENCRYPTED = true;

    private static final String DB_NAME = "dome_db";
    static String mDBName;
    private static GreenDaoDBManager mDbManager;
    private static MyOpenHelper mDevOpenHelper;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    private GreenDaoDBManager() {
        // 初始化数据库信息
        mDevOpenHelper = new MyOpenHelper(mDBName);
        getDaoMaster();
        getDaoSession();
    }

    public static GreenDaoDBManager init() {
        String userName;
        if (Utils.getApp() != null) {
            userName = "test";
        } else {
            userName = "temp_test";
        }
        mDBName = String.format("%s_%s", userName, DB_NAME);
        if (null == mDbManager) {
            synchronized (GreenDaoDBManager.class) {   //NOSONAR
                if (null == mDbManager) {
                    mDbManager = new GreenDaoDBManager();
                }
            }
        }
        return mDbManager;
    }

    /**
     * 获取可读数据库
     *
     * @return
     */
    public static SQLiteDatabase getReadableDatabase() {
        if (null == mDevOpenHelper) {
            init();
        }
        return mDevOpenHelper.getReadableDatabase();
    }

    /**
     * 获取可写数据库
     *
     * @return
     */
    public static SQLiteDatabase getWritableDatabase() {
        if (null == mDevOpenHelper) {
            init();
        }
        return mDevOpenHelper.getWritableDatabase();
    }

    /**
     * 获取DaoMaster
     * <p>
     * 判断是否存在数据库，如果没有则创建数据库
     *
     * @return
     */
    private static DaoMaster getDaoMaster() {
        if (null == mDaoMaster) {
            synchronized (GreenDaoDBManager.class) {  //NOSONAR
                if (null == mDaoMaster) {
                    MyOpenHelper mDevOpenHelper = new MyOpenHelper(mDBName);
                    mDaoMaster = new DaoMaster(mDevOpenHelper.getWritableDatabase());
                }
            }
        }
        return mDaoMaster;
    }


    /**
     * 获取DaoSession
     *
     * @return
     */
    public static DaoSession getDaoSession() {
        if (null == mDevOpenHelper) {
            init();
        }
        if (null == mDaoSession) {
            synchronized (GreenDaoDBManager.class) {
                mDaoSession = getDaoMaster().newSession();
            }
        }
        return mDaoSession;
    }

    public static void clear() {
        mDaoSession = null;
        mDaoMaster = null;
        mDbManager = null;
        mDevOpenHelper = null;
    }
}