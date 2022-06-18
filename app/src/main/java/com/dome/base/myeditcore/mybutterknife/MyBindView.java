package com.dome.base.myeditcore.mybutterknife;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)//CLASS 编译时注解  RUNTIME运行时注解 SOURCE 源码注解
@Target(ElementType.FIELD) //注解作用范围:FIELD 属性  METHOD方法  TYPE 放在类上
public @interface MyBindView {  //@interface则是表明这个类是一个注解
    int value(); //表示@ViewById() 注解时，括号里面的编写的为int类型的值
}