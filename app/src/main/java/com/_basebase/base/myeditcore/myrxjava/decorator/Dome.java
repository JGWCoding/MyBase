package com._basebase.base.myeditcore.myrxjava.decorator;

class Dome {
    public void test(){
        BasePizza basePizza = new BasePizza();
        //可以对披萨进行一直包裹想要的 料(自己想要的处理) ,
//        PizzaA pizzaA = new PizzaA(basePizza);
        PizzaA pizzaA = new PizzaA(new PizzaB(basePizza));
        PizzaB pizzaB = new PizzaB(pizzaA);

    }
}
