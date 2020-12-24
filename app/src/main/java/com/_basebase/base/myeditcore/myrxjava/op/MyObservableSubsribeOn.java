package com._basebase.base.myeditcore.myrxjava.op;

import com._basebase.base.myeditcore.myrxjava.MyObservableSource;
import com._basebase.base.myeditcore.myrxjava.MyObserver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyObservableSubsribeOn<T> extends MyAbstractObservableWithUpStream<T, T> {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    ThreadRun threadRun;

    public MyObservableSubsribeOn(MyObservableSource<T> source, ThreadRun threadRun) {
        super(source);
        this.threadRun = threadRun;
    }

    @Override
    protected void subscribeActual(final MyObserver<T> observer) {
        if (threadRun != null && threadRun == ThreadRun.Main) {
            new MyObservableSubsribeOn<T>(source,threadRun).subscribeActual(observer);
//            source.subscribeObserver(new MyObservableObserveOn.ObserveOnObserver<T>(observer,threadRun));
        } else {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    source.subscribeObserver(new SubscribeOnObserver(observer));
                }
            });
        }
    }

    static final class SubscribeOnObserver<T> implements MyObserver<T> {

        private MyObserver<T> actual;

        public SubscribeOnObserver(MyObserver<T> actual) {
            this.actual = actual;
        }

        @Override
        public void onNext(T t) {
            actual.onNext(t);
        }

        @Override
        public void onSubscribe() {
            actual.onSubscribe();
        }

        @Override
        public void onError(Throwable e) {
            actual.onError(e);
        }

        @Override
        public void onComplete() {
            actual.onComplete();
        }
    }
}
