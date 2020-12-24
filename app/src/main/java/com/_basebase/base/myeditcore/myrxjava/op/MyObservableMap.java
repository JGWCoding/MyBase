package com._basebase.base.myeditcore.myrxjava.op;

import com._basebase.base.myeditcore.myrxjava.MyObservableSource;
import com._basebase.base.myeditcore.myrxjava.MyObserver;

public class MyObservableMap<T, U> extends MyAbstractObservableWithUpStream<T, U> {
    final MyFunction<T, U> function;

    public MyObservableMap(MyObservableSource<T> source, MyFunction<T, U> function) {
        super(source);
        this.function = function;
    }

    @Override
    protected void subscribeActual(MyObserver<U> observer) {
        source.subscribeObserver(new MyMapObserver(observer, function));
    }

    static final class MyMapObserver<T, U> extends MyBasicFuseableObserver<T, U> {
        private final MyFunction<T, U> function;

        public MyMapObserver(MyObserver<U> observer, MyFunction<T, U> function) {
            super(observer);
            this.function = function;
        }

        @Override
        public void onNext(T t) {
            U apply = function.apply(t);
            actualObserver.onNext(apply);
        }
    }
}
