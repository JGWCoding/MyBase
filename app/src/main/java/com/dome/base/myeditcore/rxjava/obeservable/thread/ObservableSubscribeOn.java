package com.dome.base.myeditcore.rxjava.obeservable.thread;

import com.dome.base.myeditcore.rxjava.obeservable.ObservableSource;
import com.dome.base.myeditcore.rxjava.obeservable.mapop.AbstractObservableWithUpStream;
import com.dome.base.myeditcore.rxjava.observer.Observer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 订阅过程切换线程用的
 */
public class ObservableSubscribeOn<T> extends AbstractObservableWithUpStream<T, T> {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public ObservableSubscribeOn(ObservableSource<T> source) {
        super(source);
    }

    @Override
    protected void subscribeActual(Observer observer) {
        final SubscribeOnObserver<T> parent = new SubscribeOnObserver<>(observer);
//        observer.onSubscribe();
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                source.subscribeObserver(parent);
            }
        });
    }

    static final class SubscribeOnObserver<T> implements Observer<T> {

        private Observer<T> actual;

        public SubscribeOnObserver(Observer<T> actual) {
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
