package com._basebase.base.db.greendao.dao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WaybillCacheDBUtil {

    //增加或者更新数据
    public static void addOrUpdateDeliveryAsyn() {
        queryAll();
        ArrayList<LocalWaybillCacheBean> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            LocalWaybillCacheBean cacheBean = new LocalWaybillCacheBean(System.currentTimeMillis()+i+"", i+"",i+"", i+"", i, System.currentTimeMillis(), 0,System.currentTimeMillis());
            list.add(cacheBean);
        }

        GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().insertOrReplaceInTx(list);
    }

//    private static void addOrUpdateDelivery(List<DeliveryOrdersBean.DeliveryOrdersData> deliveryList) {
//        ArrayList<LocalWaybillCacheBean> list = new ArrayList<>();
//        if (deliveryList != null && deliveryList.size() > 0) {
//            for (DeliveryOrdersBean.DeliveryOrdersData bean : deliveryList) {
//                LocalWaybillCacheBean cacheBean = new LocalWaybillCacheBean(bean.waybillNo, bean.addresseeContName, bean.addresseeMobile, bean.addresseeAddr, bean.deliveryStatus , bean.sortTime, 0,System.currentTimeMillis());
//                list.add(cacheBean);
//            }
//        }
//        GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().insertOrReplaceInTx(list);
//    }
//
//    public static void addOrUpdateCollectAsyn(List<OrdersBean.OrderData> collectList) {
//        RxUtils.runTaskInSilence(new Runnable() {
//            @Override
//            public void run() {
//                addOrUpdateCollect(collectList);
//            }
//        });
//    }
//
//    private static void addOrUpdateCollect(List<OrdersBean.OrderData> collectList) {
//        ArrayList<LocalWaybillCacheBean> list = new ArrayList<>();
//        if (collectList != null && collectList.size() > 0) {
//            for (OrdersBean.OrderData bean: collectList) {
//                LocalWaybillCacheBean cacheBean = new LocalWaybillCacheBean(bean.waybillNo, bean.consignorContName,bean.consignorAddr,bean.consignorAddr,bean.status,bean.abnormalTime,1,System.currentTimeMillis());
//                list.add(cacheBean);
//            }
//        }
//        GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().insertOrReplaceInTx(list);
//    }
//    public static void delete() {
//
//    }
//
//    //删除一周前数据
//    public static void delAWeekAgoDataAsyn() {
//        RxUtils.runTaskInSilence(new Runnable() {
//            @Override
//            public void run() {
//                GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().queryBuilder()//le 小于或等于
//                        .where(LocalWaybillCacheBeanDao.Properties.LastUpdateTime.le(System.currentTimeMillis()-1000*60*60*24*7)).
//                        buildDelete().executeDeleteWithoutDetachingEntities();
//            }
//        });
//    }
//
//    //查询手机号最近更新的数据
//    public static LocalWaybillCacheBean queryTelNumber(String telNumber) {
//        List<LocalWaybillCacheBean> list = GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().queryBuilder()
//                .where(LocalWaybillCacheBeanDao.Properties.TelNumber.eq(telNumber))
//                .orderDesc(LocalWaybillCacheBeanDao.Properties.LastUpdateTime).build().list();
//        if (list!=null&&list.size()>0){
//            return list.get(0);
//        }
//        return null;
//    }
    public static List<LocalWaybillCacheBean> queryAll() {
        int count = 0;
        List<LocalWaybillCacheBean> list = GreenDaoDBManager.getDaoSession().getLocalWaybillCacheBeanDao().queryBuilder().build().list();
        if (list==null){
            list = new ArrayList<>();
        }
        for (LocalWaybillCacheBean bean : list) {
            Log.e("mytag"+(count+=1), bean.toString());
        }
        return list;
    }
}
