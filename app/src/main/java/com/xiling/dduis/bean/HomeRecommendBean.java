package com.xiling.dduis.bean;

public class HomeRecommendBean {

    public enum RecommendType {
        /*轮播区域*/
        Banner,
        /*菜单区域*/
        Menu,
        /*618*/
        Event618,
        /*20dp高度的空白行*/
        EmptyView,
        /*活动区域*/
        Event,
        /*秒杀抢购 - 头部*/
        RushHeader,
        /*秒杀抢购 - 数据*/
        Rush,
        /*秒杀抢购 - 底部*/
        RushFooter,
        /*今日必推头部*/
        TodayHeader,
        /*今日必推 - 数据*/
        Today,
    }

    private RecommendType type;
    private Object object;

    public RecommendType getType() {
        return type;
    }

    public void setType(RecommendType type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
