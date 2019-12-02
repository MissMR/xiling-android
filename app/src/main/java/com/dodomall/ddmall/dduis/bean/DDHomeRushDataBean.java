package com.dodomall.ddmall.dduis.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class DDHomeRushDataBean implements Serializable {

    //每页查询条数
    private String time = "0";
    private ArrayList<DDRushHeaderBean> timeList = new ArrayList<>();
    private ArrayList<DDRushSpuBean> spuList = new ArrayList<>();

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<DDRushHeaderBean> getTimeList() {
        return timeList;
    }

    public void setTimeList(ArrayList<DDRushHeaderBean> timeList) {
        this.timeList = timeList;
    }

    public ArrayList<DDRushSpuBean> getSpuList() {
        return spuList;
    }

    public void setSpuList(ArrayList<DDRushSpuBean> spuList) {
        this.spuList = spuList;
    }
}
