package com.xiling.ddmall.ddui.service;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.util.SessionUtil;

/**
 * created by Jigsaw at 2018/9/1
 * h5页面
 */
public class HtmlService {

    /**
     * 业务主机域名白名单
     */
    public static String[] hostKey = {"dodomall", "ddmall", "dianduoduo.store"};

    /**
     * 检查业务主机是否在白名单中
     */
    public static boolean isAllowHost(String host) {
        DLog.i("isAllowHost:" + host);
        if (!TextUtils.isEmpty(host) && host.length() > 0) {
            for (String key : hostKey) {
                if (host.contains(key)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 首页H5地址
     * <p>
     * func = 1 表示二级分类可以使用原生模式进入
     */
    public static final String HOME_WEB = BuildConfig.H5_URL + "app_web/home?func=" + BuildConfig.H5_FUNC;

    //0元购的App内显示界面
    public static final String FREE_BUY_APP_WEB = BuildConfig.H5_URL + "app_web/zeroBuy";
    //0元购的App外着陆页
    public static final String FREE_BUY_SHARE_WEB = BuildConfig.H5_URL + "app_web/zeroShare";

    public static final String RUSH_URL = BuildConfig.BASE_URL + "spu/limited/";

    // 注册协议
    public static final String REGISTER_PROTOCOL = Constants.URL_WEB_PREFIX + "register-protocol";
    // 隐私协议
    public static final String PRIVACY_PROTOCOL = Constants.URL_WEB_PREFIX + "privacy-protocol";
    // 邀请有奖 店主
    public static final String SHARE_STORE_MASTER = Constants.URL_WEB_PREFIX + "beowener";
    // 邀请有奖 VIP
    public static final String SHARE_VIP = Constants.URL_WEB_PREFIX + "tobeowener";
    // 成为店主
    public static final String BESHOPKEPPER = Constants.URL_WEB_PREFIX + "beshopkepper";
    public static final String COMMUNITY_BESHOPKEPPER = Constants.URL_WEB_PREFIX + "keeper/to-buy";
    // 关注我们
    public static final String FOLLOW_US = Constants.URL_WEB_PREFIX + "share-qrcode";

    // 关于我们
    public static final String ABOUT_DODOMALL = Constants.URL_WEB_PREFIX + "mine/about";
    // 新手指引
    public static final String GUIDE = Constants.URL_WEB_PREFIX + "mine/newcomer-guide";
    // 常见问题
    public static final String QUESTION = Constants.URL_WEB_PREFIX + "mine/common-issues";

    // 分享创业礼包
    public static final String MASTER_SHARE_PACKAGE = BuildConfig.BASE_URL + "sharepackage";
    // 邀请专属粉丝
    public static final String MASTER_INVITATION = BuildConfig.BASE_URL + "invitefriend";

    public static final String WEB_REGISTER = BuildConfig.BASE_URL + "redirect?redirectPath=home";

    //创业礼包
//    public static final String SHARE_BADGE = Constants.URL_WEB_PREFIX + "shareBusiness-join";
    //20181122 需求变更，创业礼包的二维码地址为 - 成为店主 - 来源 周小璐 by hanQ
    public static final String SHARE_BADGE = BESHOPKEPPER;
    //专属粉丝 就你特殊 https://host/download
    public static final String SHARE_FANS = Constants.URL_WEB_PREFIX + "download";

    // DRM系统 微信
    public static final String DRM = Constants.URL_WEB_PREFIX + "drm";

    private static final String MASTER_FOLLOWER_ACTIVITY = BuildConfig.BASE_URL + "fansActivity/";

    public static void startBecomeStoreMasterActivity(Context context) {

        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.Extras.WEB_URL, HtmlService.BESHOPKEPPER);
        context.startActivity(intent);

    }

    public static String buildRushH5Url(String rushId) {
        if (SessionUtil.getInstance().isLogin()) {
            return RUSH_URL + rushId + "/" + SessionUtil.getInstance().getLoginUser().invitationCode;
        } else {
            return RUSH_URL + rushId + "/";
        }
    }

    public static void startInviteActivity(Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        String url;
        boolean isStoreMaster = SessionUtil.getInstance().getLoginUser().isStoreMaster();
        if (isStoreMaster) {
            url = HtmlService.SHARE_STORE_MASTER + "?inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode + "&func=" + BuildConfig.H5_FUNC;
        } else {
            url = HtmlService.SHARE_VIP + "?func=" + BuildConfig.H5_FUNC;
        }
//        url = "https://www.163.com/";
        intent.putExtra(Constants.Extras.WEB_URL, url);
        context.startActivity(intent);
    }

    public static String getMasterFollowerActivityURL() {
        return MASTER_FOLLOWER_ACTIVITY + SessionUtil.getInstance().getLoginUser().incId;
    }
}
