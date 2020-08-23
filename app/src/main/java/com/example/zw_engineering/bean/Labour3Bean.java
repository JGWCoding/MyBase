package com.example.zw_engineering.bean;

public class Labour3Bean {
   public String title;
   public String typeName;
   public String model;
   public String owner;
   public int quantity;
   public String itemDesc;
   public int status_switch;


   public boolean title_is_black;
   public boolean typeName_is_black;
   public boolean model_is_black;
   public boolean owner_is_black;
   public boolean quantity_is_black;
   public boolean itemDesc_is_black;

   public Labour3Bean() {
   }

   public Labour3Bean(String title, String typeName, String model, String owner, int quantity, String itemDesc, int status_switch) {
      this.title = title;
      this.typeName = typeName;
      this.model = model;
      this.owner = owner;
      this.quantity = quantity;
      this.itemDesc = itemDesc;
      this.status_switch = status_switch;
   }

   public Labour3Bean(String title, String typeName, String model, String owner, int quantity, String itemDesc) {
      this.title = title;
      this.typeName = typeName;
      this.model = model;
      this.owner = owner;
      this.quantity = quantity;
      this.itemDesc = itemDesc;

//      this.title_is_black = false;
//      this.typeName_is_black =false;
//      this.model_is_black = false;
//      this.owner_is_black = false;
//      this.quantity_is_black =  false;
//      this.itemDesc_is_black =  false;
   }

   @Override
   public String toString() {
      return "Labour3Bean{" +
              "title='" + title + '\'' +
              ", typeName='" + typeName + '\'' +
              ", model='" + model + '\'' +
              ", owner='" + owner + '\'' +
              ", quantity=" + quantity +
              ", itemDesc='" + itemDesc + '\'' +
              ", title_is_black=" + title_is_black +
              ", typeName_is_black=" + typeName_is_black +
              ", model_is_black=" + model_is_black +
              ", owner_is_black=" + owner_is_black +
              ", quantity_is_black=" + quantity_is_black +
              ", itemDesc_is_black=" + itemDesc_is_black +
              '}';
   }
}
