package com.dome.base.myeditcore.myrxjava;

import com.dome.base.myeditcore.myrxjava.emitter.MyEmitter;

public interface MyObservableOnSubscribe<T> {
    void subscribe(MyEmitter<T> emitter);
}