package com._basebase.base.myeditcore.myrxjava;

public interface MyObservableSource<T> {
    //添加观察者
    void subscribeObserver(MyObserver<T> observer);
}
