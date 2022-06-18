package com.dome.base.myeditcore.myrxjava;


import com.dome.base.myeditcore.myrxjava.op.MyFunction;
import com.dome.base.myeditcore.myrxjava.op.MyObservableMap;
import com.dome.base.myeditcore.myrxjava.op.MyObservableObserveOn;
import com.dome.base.myeditcore.myrxjava.op.MyObservableSubsribeOn;

public abstract class MyObservable<T> implements MyObservableSource<T> {

    @Override
    public void subscribeObserver(MyObserver<T> observer) {
        subscribeActual(observer);
    }

    //这里是传入了观察者 --- 自身是被观察者    就可以订阅两者关系
    protected abstract void subscribeActual(MyObserver<T> observer);

    public static <T> MyObservable create(MyObservableOnSubscribe<T> observableOnSubscribe) {
        return new MyObservableCreate(observableOnSubscribe);
    }

    public <U> MyObservable map(MyFunction<T, U> function) {
        return new MyObservableMap(this, function);
    }

    //用来订阅观察者observer运行哪个线程
    public final MyObservable<T> observeOn(ThreadRun threadRun) {
        return new MyObservableObserveOn<T>(this,threadRun);
    }

    //用来订阅被观察者observable运行在哪个线程
    public final MyObservable<T> subscribeOn(ThreadRun threadRun) {
        return new MyObservableSubsribeOn<T>(this,threadRun);
    }
    public enum ThreadRun{
        Main,OtherThread;
    }
}
