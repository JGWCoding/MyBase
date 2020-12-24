package com._basebase.base.myeditcore.myrxjava.emitter;

//发射器
public interface MyEmitter<T> {
    void onNext(T t);
    void onError(Throwable e);
    void onComplete();
}