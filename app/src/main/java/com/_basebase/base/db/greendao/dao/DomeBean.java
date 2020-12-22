package com._basebase.base.db.greendao.dao;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity(nameInDb = "T_dome")
public class DomeBean {
    //主键(运单号)   时间  姓名 手机号码 运单号 派送及收件状态 地址
    @Id
    private String waybillNo;
    private String name;
    private String telNumber;
    private String address;
    //收件 --> 订单状态：1表示预订单,2表示已打印,3表示已取件待收款，4表示已取件，5表示待转寄
    private int state;
    private Long time;
    private int type;   //类型:0派件    1收件
    private Long lastUpdateTime;

    @Generated(hash = 814264804)
    public DomeBean(String waybillNo, String name, String telNumber, String address,
            int state, Long time, int type, Long lastUpdateTime) {
        this.waybillNo = waybillNo;
        this.name = name;
        this.telNumber = telNumber;
        this.address = address;
        this.state = state;
        this.time = time;
        this.type = type;
        this.lastUpdateTime = lastUpdateTime;
    }

    @Generated(hash = 188600049)
    public DomeBean() {
    }
    
    @Override
    public String toString() {
        return "T_dome{" +
                "waybillNo='" + waybillNo + '\'' +
                ", name='" + name + '\'' +
                ", telNumber='" + telNumber + '\'' +
                ", address='" + address + '\'' +
                ", state=" + state +
                ", time=" + time +
                ", type=" + type +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }

    public void setLastUpdateTime(Long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getWaybillNo() {
        return this.waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelNumber() {
        return this.telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Long getTime() {
        return this.time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

}
