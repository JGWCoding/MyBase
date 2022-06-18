package com.dome.base.myeditcore.myrxjava.decorator;

public class BasePizza extends Pizza {
    @Override
    public String getName() {
        this.name = "基本披萨";
        return super.getName();
    }

    @Override
    double getPrice() {
        return 50;
    }
}
