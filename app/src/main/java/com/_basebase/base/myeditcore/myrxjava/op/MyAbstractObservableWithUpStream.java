package com._basebase.base.myeditcore.myrxjava.op;

import com._basebase.base.myeditcore.myrxjava.MyObservable;
import com._basebase.base.myeditcore.myrxjava.MyObservableSource;

public abstract class MyAbstractObservableWithUpStream<T, U> extends MyObservable<U> {

    protected final MyObservableSource<T> source;

    public MyAbstractObservableWithUpStream(MyObservableSource<T> source) {
        this.source = source;
    }
}
