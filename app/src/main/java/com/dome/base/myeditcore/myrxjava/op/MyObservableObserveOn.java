package com.dome.base.myeditcore.myrxjava.op;

import android.os.Handler;
import android.os.Looper;

import com.dome.base.myeditcore.myrxjava.MyObservableSource;
import com.dome.base.myeditcore.myrxjava.MyObserver;

public class MyObservableObserveOn<T> extends MyAbstractObservableWithUpStream<T,T> {

    ThreadRun threadRun;

    public MyObservableObserveOn(MyObservableSource<T> source, ThreadRun threadRun) {
        super(source);
        this.threadRun = threadRun;
    }

    @Override
    protected void subscribeActual(MyObserver<T> observer) {
        source.subscribeObserver(new ObserveOnObserver(observer, threadRun));
    }

     static final class ObserveOnObserver<T> implements MyObserver<T> {
        private MyObserver<T> actual;

        private Handler handler;
        ThreadRun threadRun;

        public ObserveOnObserver(MyObserver<T> actual, ThreadRun threadRun) {
            this.actual = actual;
            handler = new Handler(Looper.getMainLooper());
            this.threadRun = threadRun;
        }

        @Override
        public void onNext(final T t) {
            if (threadRun != null && threadRun == ThreadRun.Main) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actual.onNext(t);
                    }
                });
            } else {
                actual.onNext(t);
            }
        }

        @Override
        public void onSubscribe() {
            actual.onSubscribe();
        }

        @Override
        public void onError(final Throwable e) {
            if (threadRun != null && threadRun == ThreadRun.Main) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actual.onError(e);
                    }
                });
            } else {
                actual.onError(e);
            }
        }

        @Override
        public void onComplete() {
            if (threadRun != null && threadRun == ThreadRun.Main) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        actual.onComplete();
                    }
                });
            } else {
                actual.onComplete();
            }
        }
    }
}
