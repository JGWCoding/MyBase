package com.dome.base.myeditcore.myrxjava.decorator;
//披萨 --- 装饰器模式dome
public abstract class Pizza {
    String name;

    public String getName() {
        return name;
    }
    abstract double getPrice();
}
