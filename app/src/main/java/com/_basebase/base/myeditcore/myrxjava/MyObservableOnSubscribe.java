package com._basebase.base.myeditcore.myrxjava;

import com._basebase.base.myeditcore.myrxjava.emitter.MyEmitter;

public interface MyObservableOnSubscribe<T> {
    void subscribe(MyEmitter<T> emitter);
}