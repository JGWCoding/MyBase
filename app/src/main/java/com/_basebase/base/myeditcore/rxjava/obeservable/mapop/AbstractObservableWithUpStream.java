package com._basebase.base.myeditcore.rxjava.obeservable.mapop;

import com._basebase.base.myeditcore.rxjava.obeservable.Observable;
import com._basebase.base.myeditcore.rxjava.obeservable.ObservableSource;
/**
 * 装饰类
 */
public abstract class AbstractObservableWithUpStream<T,U> extends Observable<U> {

    protected final ObservableSource<T> source;

    public AbstractObservableWithUpStream(ObservableSource<T> source) {
        this.source = source;
    }
}
