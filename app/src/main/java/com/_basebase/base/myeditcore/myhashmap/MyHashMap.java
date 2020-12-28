package com._basebase.base.myeditcore.myhashmap;

import java.util.ArrayList;
import java.util.List;

public class MyHashMap<K, V> implements Map<K, V> {

    // 数组默认长度
    private int defaultLength;

    // 默认负载因子
    private double defaultAddFactor;

    // 使用长度
    private double useSize;

    // entry数组
    Entry<K, V>[] entrys;

    /**
     * 有参构造器，传入长度，负载因子
     *
     * @param defaultLength    数组长度
     * @param defaultAddFactor 负载因子
     */
    public MyHashMap(int defaultLength, double defaultAddFactor) {
        if (defaultLength < 0) {
            throw new RuntimeException("长度错误");
        }

        if (defaultAddFactor <= 0 || Double.isNaN(defaultAddFactor)) {
            throw new RuntimeException("负载因子错误");
        }

        this.defaultLength = defaultLength;
        this.defaultAddFactor = defaultAddFactor;
        entrys = new Entry[this.defaultLength];
    }

    /**
     * 无参构造器，默认传16 0.75
     */
    public MyHashMap() {
        this(16, 0.75);
    }

    /**
     * 计算hashcode
     *
     * @param hashCode
     * @return
     */
    private int hash(int hashCode) {
        hashCode = hashCode ^ ((hashCode >>> 20) ^ (hashCode >>> 12));
        return hashCode ^ ((hashCode >>> 7) ^ (hashCode >>> 4));
    }

    /**
     * 获取保存位置的数组下标
     */
    private int getIndex(K k, int length) {
        //这里算法不知道怎么算的,一般直接使用 哈希值%数组长度 得到下标
        int m = length - 1;
        int index = hash(k.hashCode()) & m;
        return index > 0 ? index : -index;
    }

    @Override
    public V put(K k, V v) {

        // 判断是否需要库容
        if (this.useSize > this.defaultAddFactor * this.defaultLength) {
            this.dilatation();
        }

        // 计算出当前下标
        int index = this.getIndex(k, this.defaultLength);

        // 获取下标上的entry
        Entry<K, V> entry = this.entrys[index];

        // 创建一个新的 newentry
        Entry<K, V> newentry = new Entry<K, V>(k, v, null);

        // 判断当前下标是否被使用，如果没有被使用就将newentry填入
        if (entry == null) {
            this.entrys[index] = newentry;
            // 使用后 使用长度+1
            this.useSize++;
        } else {
            // -否则

            Entry<K, V> e = entry;
            // --判断当前key是否等于传入的k
            if (e.getKey() == k) {
                // --如果相等，就将传入v赋值给e.v
                e.v = v;
            } else {
                // ---如果不相等，就循环遍历entry的下一个entry .next，
                while (e.next != null) {
                    // ---判断下一个entry的key是否等于传入的k，如果相等就赋值v，
                    if (e.next.getKey() == k || e.next.getKey().equals(k)) {
                        e.next.v = v;
                        break;
                    } else {
                        e = e.next;
                    }
                }

                // ----判断上边循环后entry.next 是否等于null，如果等于null
                // ----将newentry设置到entry.next的位置
                if (e.next == null) {
                    e.next = newentry;
                }
            }
        }

        // 返回newentry.getValue()
        return newentry.getValue();
    }

    @Override
    public V get(K k) {

        // 获取当前下标
        int index = this.getIndex(k, this.entrys.length);

        // 得到下标上的entry
        Entry<K, V> entry = this.entrys[index];

        // entry非空校验
        if (entry == null) {
//            throw new NullPointerException("空空如也");
            return null;
        }

        // 循环entry != null
        while (entry != null) {
            // key相等就返回
            if (entry.getKey() == k || entry.getKey().equals(k)) {
                return entry.getValue();
            } else {
                // 如果不相等，将next赋值给entry继续循环
                entry = entry.next;
            }
        }

        // 找不到就返回null
        return null;
    }

    /**
     * 扩容
     */
    private void dilatation() {

        // 创建一个新的entry数组 ，长度为 defualtLength*2
        Entry<K, V>[] newEntryTable = new Entry[this.defaultLength * 2];

        // 创建一个list 用来存放entry
        List<Entry<K, V>> entryList = new ArrayList<Entry<K, V>>();

        // 1.先将历史的数据保存
        // 循环entry数组
        for (int i = 0; i < entrys.length; i++) {
            Entry<K, V> entry = entrys[i];
            // 判断数组下标上的entry!=null
            while (entry != null) {
                entryList.add(entry);
                // 将enrty存到list中，entry = entry.next;
                entry = entry.next;
            }
        }

        // 非空校验list
        if (entryList != null && entryList.size() > 0) {
            // 重新设置默认长度
            this.defaultLength = this.defaultLength * 2;
            // 使用长度重置为0
            this.useSize = 0;
            // 将newtable赋值给table
            this.entrys = newEntryTable;
            // 2.将保存好的数据存到新的容器中
            for (Entry<K, V> entry : entryList) {
                // 循环list中的entry，并将其next置位null
                if (entry.next != null) {
                    entry.next = null;
                }
                // 调用put方法,传入entry.k entry.v
                put(entry.getKey(), entry.getValue());
            }
        }
    }
}