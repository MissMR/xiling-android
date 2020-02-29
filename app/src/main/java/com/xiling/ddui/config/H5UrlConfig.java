package com.xiling.ddui.config;

import com.xiling.BuildConfig;

public class H5UrlConfig {
    //购买记录
    public static final String WEEK_CARD_RECORD = BuildConfig.BASE_URL + "weekCardRecord";
    //查看物流
    public static final String WEB_URL_EXPRESS = BuildConfig.BASE_URL + "logisticsDetail?expressCode=@expressCode&expressId=@expressId";
    //会员规则
    public static final String MEMBER_EXPLAIN = BuildConfig.BASE_URL + "memberExplain";
    //成长值说明
    public static final String GROWTH_INTRO = BuildConfig.BASE_URL + "growthIntro";
    //关于我们
    public static final String ABOUT_US = BuildConfig.BASE_URL + "aboutUs";
    //规则中心
    public static final String RULES_CENTER = BuildConfig.BASE_URL + "rulesPolicyCenter?type=2";
    //政策
    public static final String POLICY = BuildConfig.BASE_URL + "rulesPolicyCenter?type=1";
    //帮助与客服
    public static final String HELP = BuildConfig.BASE_URL + "help";
    //隐私协议
    public static final String PRIVACY_AGREEMENT = BuildConfig.BASE_URL + "privacyAgreement";
    //服务协议
    public static final String SERVICE_AGREEMENT = BuildConfig.BASE_URL + "serverAgreement";
    //大额支付
    public static final String LARGE_PAYMENT  =BuildConfig.BASE_URL +"/voucher?orderId=";

}
