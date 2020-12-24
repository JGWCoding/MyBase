package com._basebase.base.myeditcore.rxjava;

import android.util.Log;

import com._basebase.base.myeditcore.rxjava.obeservable.Observable;
import com._basebase.base.myeditcore.rxjava.obeservable.ObservableCreate;
import com._basebase.base.myeditcore.rxjava.obeservable.ObservableOnSubscribe;
import com._basebase.base.myeditcore.rxjava.obeservable.emitter.Emitter;
import com._basebase.base.myeditcore.rxjava.obeservable.mapop.Function;
import com._basebase.base.myeditcore.rxjava.obeservable.mapop.ObservableMap;
import com._basebase.base.myeditcore.rxjava.observer.Observer;

public class DomeUse {

    public  void dome() {
//        MyObservable.create(new MyObservableOnSubscribe<String>() {
//            @Override
//            public void subscribe(MyEmitter<String> emitter) {
//                emitter.onNext("第一步操作--->");
//                log("subscribe订阅线程 = " + Thread.currentThread().getName());
//            }
//        }).map(new MyFunction<String,String>() {
//            @Override
//            public String apply(String o) {
//                String u = o + "第二步操作--->";
//                log("map订阅线程 = " + Thread.currentThread().getName());
//                return u;
//            }
//        }).map(new MyFunction<String,String>() {
//            @Override
//            public String apply(String t) {
//                String u = t + "第三步操作";
//                log("map订阅线程 = " + Thread.currentThread().getName());
//                return u;
//            }
//        }).subscribeOn(MyObservable.ThreadRun.OtherThread)
//                .observeOn(MyObservable.ThreadRun.Main)
//                .subscribeObserver(new MyObserver() {
//                    @Override
//                    public void onNext(Object o) {
//                        log("当前线程的名字 = " + Thread.currentThread().getName());
//                        log(o.toString());
//                    }
//
//                    @Override
//                    public void onSubscribe() {
//                        log("onSubscribe()");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        log("onError()");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        log("onComplete()");
//                    }
//                });
//        if (true) return;
        //调用相当于 代码
//         new ObservableMap(new ObservableMap(new ObservableCreate<>(source),function),function).subscribeObserver(observer);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(Emitter<String> emitter) {
                emitter.onNext("第一步操作--->");
                log("subscribe订阅线程 = " + Thread.currentThread().getName());
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String t) {
                String u = t + "第二步操作--->";
                log("map订阅线程 = " + Thread.currentThread().getName());
                return u;
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String t) {
                String u = t + "第三步操作";
                log("map订阅线程 = " + Thread.currentThread().getName());
                return u;
            }
        })
                .subscribeOn()
                .observeOn()
                .subscribeObserver(new Observer() {
                    @Override
                    public void onNext(Object o) {
                        log("当前线程的名字 = " + Thread.currentThread().getName());
                        log(o.toString());
                    }

                    @Override
                    public void onSubscribe() {
                        log("onSubscribe()");
                    }

                    @Override
                    public void onError(Throwable e) {
                        log("onError()");
                    }

                    @Override
                    public void onComplete() {
                        log("onComplete()");
                    }
                });
    }


    public void log(String msg) {
        Log.d("观察者模式", msg);
    }


    public void test(){
        //上面代码调用相当于  (装饰器)
//         new ObservableMap(new ObservableMap(new ObservableCreate<>(source),function1),function2).subscribeObserver(observer);
        //new ObservableMap(new ObservableMap(new ObservableCreate<>(source),function1),function2).subscribeActual(observer);
        //new ObservableMap(new ObservableCreate<>(source),function1)).subscribeObserver(new MapObserver<>(observer, function2));
        //new ObservableCreate<>(source).subscribeObserver(new MapObserver<>(new MapObserver<>(observer, function2), function1));
        //new ObservableCreate<>(source).subscribeActual(new MapObserver<>(new MapObserver<>(observer, function2), function1));
        //ObservableCreate调用 observer.onSubscribe(); observableOnSubscribe.subscribe(new CreateEmitter(observer));
         new ObservableMap(new ObservableMap(new ObservableCreate<>(new ObservableOnSubscribe<String>() {
             @Override
             public void subscribe(Emitter<String> emitter) {
                 emitter.onNext("第一步操作--->");
                 log("subscribe订阅线程 = " + Thread.currentThread().getName());
             }
         }),new Function<String, String>() {
             @Override
             public String apply(String t) {
                 String u = t + "第二步操作--->";
                 log("map订阅线程 = " + Thread.currentThread().getName());
                 return u;
             }
         }),new Function<String, String>() {
             @Override
             public String apply(String t) {
                 String u = t + "第三步操作";
                 return u;
             }
         }).subscribeObserver(new Observer() {
             @Override
             public void onNext(Object o) {
                 log(o.toString());
                 log("当前线程的名字 = " + Thread.currentThread().getName());
             }

             @Override
             public void onSubscribe() {
                 log("onSubscribe()");
             }

             @Override
             public void onError(Throwable e) {
                 log("onError()");
             }

             @Override
             public void onComplete() {
                 log("onComplete()");
             }
         });
    }
}