package com.xiling.ddui.config;

import com.xiling.BuildConfig;

public class H5UrlConfig {
    //购买记录
    public static final String WEEK_CARD_RECORD = BuildConfig.BASE_URL+ "weekCardRecord";
    //查看物流
    public static final String WEB_URL_EXPRESS = BuildConfig.BASE_URL + "logisticsDetail?expressCode=@expressCode&expressId=@expressId";
    //会员规则
    public static final String MEMBER_EXPLAIN = BuildConfig.BASE_URL+ "memberExplain";
    //成长值说明
    public static final String GROWTH_INTRO = BuildConfig.BASE_URL+ "growthIntro";

}
