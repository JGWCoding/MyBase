package com.dome.base.db.greendao.dao;

import android.database.sqlite.SQLiteDatabase;


import com.blankj.utilcode.util.CollectionUtils;
import com.blankj.utilcode.util.LogUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

import java.util.HashSet;
import java.util.Set;

/*
 * @Description: 数据库升级逻辑请参考demo
 * 作者  陈兆龙
 * 时间 2019年01月11
 *
 */
public class MyOpenHelper extends DaoMaster.DevOpenHelper {

    public MyOpenHelper(String name) {
        super(new GreenDaoContext(), name);
    }

    public MyOpenHelper(String name, SQLiteDatabase.CursorFactory factory) {
        super(new GreenDaoContext(), name, factory);
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
       /*
       此处不用super，因为父类中包含了
       dropAllTables(db, true);
        onCreate(db);
        需要自己定制升级
        */
        LogUtils.e("旧版本:"+oldVersion+"     新版本:"+newVersion);
        Set<Class<? extends AbstractDao<?, ?>>> updataCLassSet = new HashSet<>();
        for (int version = oldVersion + 1; version <= newVersion; version++) {
            switch (version) {
                case 2:
                case 3:
//                    updataCLassSet.add(LocalWaybillCacheBeanDao.class);
                case 4:
                    updataCLassSet.add(DomeBeanDao.class);
                    break;
                case 5:
                default:
            }
        }
        if (!CollectionUtils.isEmpty(updataCLassSet)) {
            Object[] arrays = updataCLassSet.toArray();
            Class<? extends AbstractDao<?, ?>>[] clzss = new Class[arrays.length];
            for (int i = 0; i < arrays.length; i++) {
                Class<? extends AbstractDao<?, ?>> obj = (Class<? extends AbstractDao<?, ?>>) arrays[i];
                clzss[i] = obj;
            }
            MigrationHelper.migrate(db, clzss);
        }

    }
}
