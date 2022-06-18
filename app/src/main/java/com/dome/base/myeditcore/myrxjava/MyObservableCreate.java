package com.dome.base.myeditcore.myrxjava;

import com.dome.base.myeditcore.myrxjava.emitter.MyEmitter;

public class MyObservableCreate<T> extends MyObservable<T> {
    MyObservableOnSubscribe<T> source;

    public MyObservableCreate(MyObservableOnSubscribe<T> observableOnSubscribe) {
        this.source = observableOnSubscribe;
    }

    @Override
    protected void subscribeActual(MyObserver<T> observer) {
        observer.onSubscribe();
        source.subscribe(new CreateEmitter(observer));
    }
    class CreateEmitter<T> implements MyEmitter<T> {
        final MyObserver<T> observer;

        public CreateEmitter(MyObserver<T> observer) {
            this.observer = observer;
        }
        @Override
        public void onNext(T t) {
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }
}
