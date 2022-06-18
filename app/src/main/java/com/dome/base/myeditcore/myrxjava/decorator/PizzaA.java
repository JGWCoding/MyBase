package com.dome.base.myeditcore.myrxjava.decorator;

class PizzaA extends Decorator{
    private Pizza mPizza;

    public PizzaA(Pizza pizza) {
        mPizza = pizza;
    }

    @Override
    public String getName() {
        return mPizza.getName()+" (加点A料) ";
    }

    @Override
    double getPrice() {
        return mPizza.getPrice()+10;
    }
}
