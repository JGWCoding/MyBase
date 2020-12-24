package com._basebase.base.myeditcore.myrxjava.decorator;

class PizzaB  extends Decorator{
    private Pizza mPizza;

    public PizzaB(Pizza pizza) {
        mPizza = pizza;
    }

    @Override
    public String getName() {
        return mPizza.getName()+" (加点B料) ";
    }

    @Override
    double getPrice() {
        return mPizza.getPrice()+20;
    }
}