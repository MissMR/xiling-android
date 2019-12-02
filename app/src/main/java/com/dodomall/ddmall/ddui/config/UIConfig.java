package com.dodomall.ddmall.ddui.config;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.sobot.chat.widget.kpswitch.util.StatusBarHeightUtil;

import java.lang.reflect.Method;

public class UIConfig {

    /*主体色的代码调用*/
    public static final String kMainColor = "#FF4647";

    //店主用户的抢购颜色
    public static final int COLOR_RUSH_VIP = Color.parseColor("#FF4647");
    //普通会员的抢购颜色
    public static final int COLOR_RUSH_USER = Color.parseColor("#5FC730");

    //抢购进度条 - 灰色字
    public static final int COLOR_RUSH_PROGRESS_GARY_TEXT = Color.parseColor("#999999");
    //抢购进度条 - 白色字
    public static final int COLOR_RUSH_PROGRESS_WRITE_TEXT = Color.parseColor("#FFFFFF");

    //已订阅通知的提醒
    public static final int COLOR_RUSH_NOTICE = Color.parseColor("#F0FFE9");

    //店主用户的抢购进度背景
    public static final int COLOR_RUSH_VIP_PROGRESS_BACKGROUND = Color.parseColor("#FEE0DF");
    //普通会员的抢购进度背景
    public static final int COLOR_RUSH_USER_PROGRESS_BACKGROUND = Color.parseColor("#FEE0DF");

    /**
     * 是否启用
     */
    public static final boolean isUseNewUI = true;

    /*是否开启协议调试模式*/
    public static final boolean isProtocolTest = BuildConfig.DEBUG & false;

    /**
     * 首页UI常量定义
     */
    public static class HOME {
        /**
         * 今日特价的Fragment
         */
        public static final String FRAGMENT_TODAY = "TODAY";
        public static final String FRAGMENT_WEB = "WEB";
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取界面可视区域高度
     */
    public static int getActivityShowViewHeight(Context context, int titleBarHeight) {
        int screenHeight = getScreenHeight(context);
        int statusBarHeight = StatusBarHeightUtil.getStatusBarHeight(context);
        int titleBarHeightPx = ConvertUtil.dip2px(titleBarHeight);
        DLog.w(screenHeight + "-" + statusBarHeight + "-" + titleBarHeightPx);

        return screenHeight - statusBarHeight - titleBarHeightPx;
    }

    //获取虚拟按键的高度
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        return result;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }


    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

}
