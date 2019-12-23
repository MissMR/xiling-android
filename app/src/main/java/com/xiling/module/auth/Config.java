package com.xiling.module.auth;

import com.xiling.ddui.config.AppConfig;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/3/19.
 */
public class Config {
    /**
     * 是否开启会员会员折扣功能
     */
    public static final boolean IS_DISCOUNT = false;
    public static final boolean IS_DEBUG = AppConfig.DEBUG;
    public static final int ERROR_CODE = -1;
    public static final String INTENT_KEY_TYPE_NAME = "intenttypekey";
    public static final String INTENT_KEY_ID = "ubdeda";
    public static final String KEY_EVENTBUS_TAG = "eventbus_tag";
    public static final int REQUEST_CODE_CHOOSE_IMAGE_SELECT = 0x998;

    //上一次的BUGLY_ID
//    public static final String BUGLY_APP_ID = "c89737a0c2";
    //生产ID
    public static final String BUGLY_APP_ID = "0c835fde24";
    //测试ID
    public static final String BUGLY_DEBUG_APP_ID = "5ed952827b";


    public static final class HOME {
        /**
         * 求购配件
         */
        public static final int INTENT_KEY_TYPE_BUY = 1;
        /**
         * 出售配件
         */
        public static final int INTENT_KEY_TYPE_SELL = INTENT_KEY_TYPE_BUY + 1;
        /**
         * 设备维修
         */
        public static final int INTENT_KEY_TYPE_REPAIR = INTENT_KEY_TYPE_BUY - 1;
        /**
         * 派单维修
         */
        public static final int INTENT_KEY_TYPE_SEND = INTENT_KEY_TYPE_BUY + 2;
        /**
         * 选择一级分类
         */
        public static final int INTENT_KEY_TYPE_SELECT_GENRE1 = INTENT_KEY_TYPE_BUY + 3;
        /**
         * 资料库-技术案例
         */
        public static final int INTENT_KEY_TYPE_DATA_LIB_CASE = INTENT_KEY_TYPE_BUY + 4;
        /**
         * 资料库-期刊杂志
         */
        public static final int INTENT_KEY_TYPE_DATA_LIB_MAGAZINE = INTENT_KEY_TYPE_BUY + 5;
        /**
         * 资料库-科研报告
         */
        public static final int INTENT_KEY_TYPE_DATA_LIB_MEETING = INTENT_KEY_TYPE_BUY + 6;
        /**
         * 资料库-原厂数据
         */
        public static final int INTENT_KEY_TYPE_DATA_LIB_DATA = INTENT_KEY_TYPE_BUY + 7;

        public static final int INTENT_KEY_TYPE_SELECT_GENRE2 = INTENT_KEY_TYPE_BUY + 8;
        /**
         * 选择品牌
         */
        public static final int INTENT_KEY_TYPE_SELECT_BRAND = INTENT_KEY_TYPE_BUY + 9;

        /**
         * 首页-经销商
         */
        public static final int INTENT_KEY_TYPE_DEALER = INTENT_KEY_TYPE_BUY + 10;
        /**
         * 首页-高校
         */
        public static final int INTENT_KEY_TYPE_SCHOOL = INTENT_KEY_TYPE_BUY + 11;
        /**
         * 首页-科研机构
         */
        public static final int INTENT_KEY_TYPE_RESEARCH = INTENT_KEY_TYPE_BUY + 12;
        /**
         * 首页-生产厂家
         */
        public static final int INTENT_KEY_TYPE_FIRMT = INTENT_KEY_TYPE_BUY + 13;
        /**
         * 首页-政府部门
         */
        public static final int INTENT_KEY_TYPE_GOV = INTENT_KEY_TYPE_BUY + 14;
        /**
         * 首页-协会
         */
        public static final int INTENT_KEY_TYPE_ASSOCIATOPM = INTENT_KEY_TYPE_BUY + 15;

        /**
         * 首页-法律咨询
         */
        public static final int INTENT_KEY_TYPE_LAW = INTENT_KEY_TYPE_BUY + 16;

        /**
         * 首页-认证机构
         */
        public static final int INTENT_KEY_TYPE_APPROVE = INTENT_KEY_TYPE_BUY + 17;

        /**
         * 选择分类里的设备类型
         */
        public static final String INTENT_KEY_DEVECE_TYPE_NAME = "intentdevicetypekey";

        /**
         * id
         */
        public static final String INTENT_KEY_ID_NAME = "intentgenreidkey";


    }

    public static final class NEWS {
        public static final int INTENT_KEY_TYPE_ALL = 20000;
        public static final int INTENT_KEY_TYPE_CONSULT = INTENT_KEY_TYPE_ALL + 1;
        public static final int INTENT_KEY_TYPE_MEETING = INTENT_KEY_TYPE_ALL + 2;
        public static final int INTENT_KEY_TYPE_NOTICE = INTENT_KEY_TYPE_ALL + 3;
    }

    public static final class USER {
        public static final String SP_NAME = "usersp";
        public static final String MODEL = "model";

        public static final int INTENT_KEY_TYPE_REGISTER = 10000;
        public static final int INTENT_KEY_TYPE_FIND_PASSWORD = 10001;
        /**
         * 实名认证提交成功
         */
        public static final int INTENT_KEY_TYPE_AUTH_IDENTITY_SUBMIT_SUCCESS = 10002;
        public static final int INTENT_KEY_TYPE_AUTH_PHONE = 10003;
        public static final int INTENT_KEY_TYPE_AUTH_CARD = 10004;
        /**
         * 验证提现
         */
        public static final int INTENT_KEY_TYPE_AUTH_DEAL = 10004 * 2;
        /**
         * 实名认证 审核失败
         */
        public static final int INTENT_KEY_TYPE_AUTH_IDENTITY_FAIL = 10005;
        /**
         * 实名认证 审核成功
         */
        public static final int INTENT_KEY_TYPE_AUTH_IDENTITY_SUCCESS = 10006;
        /**
         * 实名认证 审核中
         */
        public static final int INTENT_KEY_TYPE_AUTH_IDENTITY_WAIT = 10007;

        /**
         * 银行卡 提交成功
         */
        public static final int INTENT_KEY_TYPE_AUTH_CARD_SUBMIT_SUCCESS = 102;
        /**
         * 银行卡 审核失败
         */
        public static final int INTENT_KEY_TYPE_AUTH_CARD_FAIL = 105;
        /**
         * 银行卡 审核成功
         */
        public static final int INTENT_KEY_TYPE_AUTH_CARD_SUCCESS = 106;
        /**
         * 银行卡 审核中
         */
        public static final int INTENT_KEY_TYPE_AUTH_CARD_WAIT = 107;

        /**
         * 求购配件
         */
        public static final int INTENT_VALUE_TYPE_BUY = 1;
        /**
         * 出售配件
         */
        public static final int INTENT_VALUE_TYPE_SELL = 2;
        /**
         * 设备维修
         */
        public static final int INTENT_VALUE_TYPE_REPAIR = 0;
        /**
         * 派单维修
         */
        public static final int INTENT_VALUE_TYPE_SEND = 3;

        /**
         * 全部分配
         */
        public static final int INTENT_VALUE_TYPE_ALL = 99;

        /**
         * 求购配件
         */
        public static final int INTENT_VALUE_TYPE_BUY_PROJECT = 100;
        /**
         * 出售配件
         */
        public static final int INTENT_VALUE_TYPE_SELL_PROJECT = 1001;
        /**
         * 设备维修
         */
        public static final int INTENT_VALUE_TYPE_REPAIR_PROJECT = 1002;
        /**
         * 派单维修
         */
        public static final int INTENT_VALUE_TYPE_SEND_PROJECT = 1003;

        /**
         * 全部分配
         */
        public static final int INTENT_VALUE_TYPE_ALL_PROJECT = 1004;


    }

    public static final class MY_CREATE {
        public static final int INTENT_VALUE_TYPE_GET_CREATE_MY_PROJECT = 1 << 1;
    }

    public static final class COOKIE {
        public static final String SP_NAME = "cookiespname";
        public static final String SP_KEY_COOKIE = "cookieskey";

        public static final String VERSION = "version";
        public static final String OAUTH = "__outh";
        public static final String COOKIE_PATH = "/";
        public static final String COOKIE_DOMAIN = "*.kangerys.com";
    }

    public static final class NET_MESSAGE {
        public static final String NO_LOGIN = "请先登录";


    }


}
