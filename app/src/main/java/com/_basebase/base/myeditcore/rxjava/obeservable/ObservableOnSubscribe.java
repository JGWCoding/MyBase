package com._basebase.base.myeditcore.rxjava.obeservable;


import com._basebase.base.myeditcore.rxjava.obeservable.emitter.Emitter;

/**
 * 将发射器Emitter和Observable绑定到一起
 */
public interface ObservableOnSubscribe<T> {
    void subscribe(Emitter<T> emitter);
}
