package com.xiling.shared;

import com.xiling.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * 常量
 * Created by JayChan on 2016/12/13.
 */
public class Constants {

    public static final String API_VERSION = "1.0";

    /*
    iOS(0, "IOS"),
    Android(1, "Android"),
    WeChat(2, "微信"),
    MiniProgram(3, "小程序"),
    PC(4, "PC"),
    NO(5, "未知");
    */
    public static final String PLATFORM = "1";

    public static final String URL_API_PREFIX = BuildConfig.BASE_URL + "xl-api/";

    public static final String URL_WEB_PREFIX = BuildConfig.H5_URL + "app_web/";

    public static final String EXPRESS_URL = BuildConfig.BASE_URL + "check-express?company=%s&code=%s";

    public static final String COOKIE_PATH = "/";
    public static final int REQUEST_TIMEOUT = 30;
    public static final int REQUEST_WRITE_TIMEOUT = 2 * 60;
    public static final int NOT_LOGIN_CODE = 1;
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = 2;
    public static final String KEY_TYPE = "type";

    public static final String API_SALT = "a70c34cc321f407d990c7a2aa7900729";

    public static final SimpleDateFormat FORMAT_DATE_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
    public static final SimpleDateFormat FORMAT_DATE_SIMPLE = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    public static final int SHARE_THUMB_SIZE = 150;

    public static final int PAGE_SIZE = 10;

    public static final String KEY_EXTROS = "key";
    public static final String share_tile_text = "店多多，随时发现好货";
    public static final String KEY_IS_EDIT = "isEdit";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_IS_VIDEO = "key_video";
    public static final String KEY_LIBRARY_ID = "key_library";
    public static final String KEY_MEDIAURL = "key_mdiaurl";

    public static class Extras {
        public static final String PUSH_MESSAGE = "PUSH_MESSAGE";
        public static final String PRODUCT = "DATA_PRODUCT";
        public static final String DATA_HANDLER = "DATA_DATAHANDLER";
        public static final String STRING = "DATA_STRING";
        public static final String NICKNAME = "DATA_NICKNAME";
        public static final String PHONE = "DATA_PHONE";
        public static final String PASSWORD = "DATA_PASSWORD";
        public static final String ADDRESS = "DATA_ADDRESS";
        public static final String CARTS = "DATA_CARTS";
        public static final String ORDER_STATUS = "ORDER_STATUS";
        public static final String ORDER_CODE = "ORDER_CODE";
        public static final String ORDER_TRANSFER = "ORDER_TRANSFER";
        public static final String USER = "DATA_USER";
        public static final String FEEDBACK = "FEEDBACK";
        public static final String ORDER_ID = "ORDER_ID";
        public static final String QUESTION_ID = "QUESTION_ID";
        public static final String CATEGORY_ID = "CATEGORY_ID";
        public static final String PRODUCT_CODE = "PRODUCT_CODE";
        public static final String SHOPPING = "SHOPPING";
        public static final String COURSE = "COURSE";
        public static final String MOMENT = "MOMENT";
        public static final String AUTHOR = "AUTHOR";
        public static final String TOPICID = "TOPICID";
        public static final String CART_IDS = "CART_IDS";

        public static final String KEY_EXTRAS = "key_data";
        public static final String KET_TYPE = "KEY_TYPE";
        public static final String KEY_BANK_APPLYID = "KEY_ACCOUND_ID";
        public static String SEARCH_ORDER = "search_order";
        public static final String PHONE_NUMBER = "phone_number";
        public static final String TITLE = "title";
        public static final String RESULT = "result";
        public static final String WECHAT_USER = "wechat_user";
        public static final String REGISTER_CAPTCHA = "captcha_register";
        public static final String WEB_URL = "url";
        public static final String ROUTE = "route";
        public static final String ID_URLS = "id_urls";
        public static final String USER_AUTH = "user_auth";
        public static final String WITHDRAW = "withdraw";
        public static final String QUESTION = "question";
        public static final String DATA = "data";
        public static final String ID = "id";
        public static final String HAS_SET_QUESTION = "has_set_question";

        public static final String COURSE_TYPE_NEWER = "newer";
        public static final String COURSE_TYPE_MASTER = "master";

        public static final String TOPIC_ID = "topic_id";

        public static final String TAB_INDEX = "tab_index";
        public static final String SPU_ID = "spu_id";
        public static final String FROM_ACTION = "from_action";
        public static final String PAY_TYPE = "pay_type";
        public static final String WECHAT = "wechat";
        public static final String WECHAT_QR_CODE = "wechat_qr_code";
        public static final String USER_ID = "user_id";
        public static final String TYPE = "type";
        public static final String PAY_RESULT = "pay_result";
        public static final String CONTENT = "content";
        public static final String FLASH_SALE_ID = "flash_sale_id";
        //0 微信登录 1、手机号登录
        public static final String LOGINTYPE = "logintype";

    }

}
