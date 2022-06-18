package com.dome.base.myeditcore.myrxjava.op;


import com.dome.base.myeditcore.myrxjava.MyObserver;

public abstract class MyBasicFuseableObserver<T,U> implements MyObserver<T> {

    protected final MyObserver<U> actualObserver;

    public MyBasicFuseableObserver(MyObserver<U> actual) {
        this.actualObserver = actual;
    }
    @Override
    public void onSubscribe() {
        actualObserver.onSubscribe();
    }

    @Override
    public void onComplete() {
        actualObserver.onComplete();
    }

    @Override
    public void onError(Throwable e) {
        actualObserver.onError(e);
    }
}
