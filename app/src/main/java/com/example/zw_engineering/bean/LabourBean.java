package com.example.zw_engineering.bean;

public class LabourBean {
    public String title;
    public String quantity = "0";
    public String owner;
    public boolean title_isBlack = false;
    public boolean quantity_isBlack = false;
    public boolean owner_isBlack = false;
//    public int id;
    public String item;

//    public LabourBean() {
//    }

    public LabourBean(String item) {
        this.item = item;
    }

    public LabourBean(String title, String quantity, String owner) {
        this.title = title;
        this.quantity = quantity;
        this.owner = owner;
    }

    public LabourBean(String title, String quantity, String owner, boolean title_isBlack, boolean quantity_isBlack, boolean owner_isBlack,String item) {
        this.title = title;
        this.quantity = quantity;
        this.owner = owner;
        this.title_isBlack = title_isBlack;
        this.quantity_isBlack = quantity_isBlack;
        this.owner_isBlack = owner_isBlack;
        this.item = item;
    }

    @Override
    public String toString() {
        return "LabourBean{" +
                "title='" + title + '\'' +
                ", quantity='" + quantity + '\'' +
                ", owner='" + owner + '\'' +
                ", title_isBlack=" + title_isBlack +
                ", quantity_isBlack=" + quantity_isBlack +
                ", owner_isBlack=" + owner_isBlack +
                ", item='" + item + '\'' +
                '}';
    }
}
