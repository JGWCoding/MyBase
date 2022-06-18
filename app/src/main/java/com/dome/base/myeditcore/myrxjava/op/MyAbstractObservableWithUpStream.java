package com.dome.base.myeditcore.myrxjava.op;

import com.dome.base.myeditcore.myrxjava.MyObservable;
import com.dome.base.myeditcore.myrxjava.MyObservableSource;

public abstract class MyAbstractObservableWithUpStream<T, U> extends MyObservable<U> {

    protected final MyObservableSource<T> source;

    public MyAbstractObservableWithUpStream(MyObservableSource<T> source) {
        this.source = source;
    }
}
