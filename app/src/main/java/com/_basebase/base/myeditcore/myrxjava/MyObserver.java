package com._basebase.base.myeditcore.myrxjava;

public interface MyObserver<T> {
    void onSubscribe();

    void onNext(T t);

    void onComplete();

    void onError(Throwable e);

}
