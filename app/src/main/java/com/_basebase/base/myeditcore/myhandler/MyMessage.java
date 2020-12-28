package com._basebase.base.myeditcore.myhandler;

public class MyMessage {

    MyHandler target;
    public Object obj;  
    public int what;  

    @Override  
    public String toString() {  
        return   "what="+what+" obj="+obj.toString();  
    }  

} 