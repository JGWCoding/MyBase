package com.example.zw_engineering.bean;

import java.util.List;

public class AppSelectBean {

    public int code;
    public List<LabourItemBean> labour_item;
    public List<LabourTypeBean> labour_type;
    public List<PlantItemBean> plant_item;
    public List<PlantTypeBean> plant_type;
    public List<PlantModelBean> plant_model;



    public static class LabourItemBean {
        public int id;
        public String name;
    }

    public static class LabourTypeBean {
        public String name;
        public int pid;
    }

    public static class PlantItemBean {
        public int id;
        public String name;
    }

    public static class PlantTypeBean {
        public int id;
        public String name;
        public int pid;
    }

    public static class PlantModelBean {
        public int id;
        public String name;
        public int pid;
    }
    @Override
    public String toString() {
        return "AppSelectBean{" +
                "code=" + code +
                ", labour_item=" + labour_item.size() +
                ", labour_type=" + labour_type.size() +
                ", plant_item=" + plant_item.size() +
                ", plant_type=" + plant_type.size() +
                ", plant_model=" + plant_model.size() +
                '}';
    }
}
