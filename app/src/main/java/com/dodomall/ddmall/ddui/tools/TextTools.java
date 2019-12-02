package com.dodomall.ddmall.ddui.tools;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.Html;
import android.text.Spanned;

import com.dodomall.ddmall.ddui.custom.DDImageGetter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextTools {

    /**
     * 增加使用一个换行符解析块标记的兼容
     *
     * @param text html文本
     */
    public static Spanned fromHtml(String text) {
        Spanned result = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        } else {
            result = Html.fromHtml(text);
        }
        return result;
    }

    /**
     * 增加使用一个换行符解析块标记的兼容
     *
     * @param text       html文本
     * @param tagHandler 自定义标签解析Handler
     */
    public static Spanned fromHtml(String text, Html.TagHandler tagHandler) {
        Spanned result = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT, new DDImageGetter(), tagHandler);
        } else {
            result = Html.fromHtml(text, new DDImageGetter(), tagHandler);
        }
        return result;
    }


    /**
     * 判断字符串是否包含数字
     */
    public static boolean isHasDigit(String text) {
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(text);
        return m.matches();
    }

    /**
     * 判断字符串是否包含字母
     */
    public static boolean isHasABC(String text) {
        String regex = ".*[a-zA-Z]+.*";
        Matcher m = Pattern.compile(regex).matcher(text);
        return m.matches();
    }

    /**
     * 判断是否含有特殊字符
     */
    public static boolean isHasSpecialChar(String text) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        return m.find();
    }

    /**
     * 获取meta数据中的value
     *
     * @param context 上下文
     * @param key     键值
     */
    public static String getMetaData(Context context, String key) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString("" + key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        DLog.d(key + "=" + value);
        return value;
    }

    public static long getIntMetaData(Context context, String key) {
        int value = 999;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            value = appInfo.metaData.getInt("" + key);
        } catch (Exception e) {
            DLog.w("getLongInMetaData get exception : " + e.getMessage());
            e.printStackTrace();
        }
        DLog.d(key + "=" + value);
        return value;
    }

    /**
     * 获取渠道ID
     */
    public static String getChannelId(Context context) {
        long channelId = getIntMetaData(context, "CHANNEL_ID");
        return "" + channelId;
    }

    /**
     * 获取渠道名
     */
    public static String getChannelName(Context context) {
        return getMetaData(context, "UMENG_CHANNEL");
    }
}