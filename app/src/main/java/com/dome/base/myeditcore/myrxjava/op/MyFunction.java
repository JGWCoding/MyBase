package com.dome.base.myeditcore.myrxjava.op;

/**
 * 事件变换
 */
public interface MyFunction<T, U> {
    U apply(T t);
}