package com.dome.base.myeditcore.myhashmap;

public interface Map<K, V> {
    /**
     * 保存数据
     * @param k
     * @param v
     * @return
     */
    V put(K k, V v);
    /**
     * 获取数据
     *
     * @param k
     * @return
     */
    V get(K k);
}