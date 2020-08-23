package com.example.zw_engineering.util;

import com.blankj.utilcode.util.SPUtils;
import com.example.zw_engineering.bean.AppSelectBean;
import com.example.zw_engineering.bean.SearchBean;

import java.util.ArrayList;

public class ConstantUtil {
    public static int zip_img_state_1 =-1;   //-1代表未选图片压缩 0代表压缩中 1代表压缩成功 2代表压缩失败 3代表不必压缩 指向源文件
    public static int zip_img_state_2 = -1;
    public static final String add_reportID_key= "add_reportID_key";
    public static String last_report_id = SPUtils.getInstance().getString(add_reportID_key);     //用来存储最近添加的信息的id

    public static String pic_url1 ;
    public static String pic_url2 ;
    public static String[] path_list = new String[2];
    public static ArrayList<SearchBean> search_list ;   //TODO 有可能因为数据太大导致内存不足
    public static AppSelectBean appSelectBean;
}
