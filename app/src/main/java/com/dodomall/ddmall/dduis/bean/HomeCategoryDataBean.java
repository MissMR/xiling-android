package com.dodomall.ddmall.dduis.bean;

import java.io.Serializable;

public class HomeCategoryDataBean implements Serializable {

    public enum Type {
        /*轮播图*/
        Banner,
        /*菜单*/
        Menu,
        /*筛选*/
        Filter,
        /*普通商品数据*/
        SPUData,
        /*抢购商品数据*/
        RUSHData,
        /*无数据*/
        No_Data,

    }

    private Type type;
    private Object object;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
