package com.xiling.ddmall.ddui.tools;

import android.text.TextUtils;

import java.net.URI;

/**
 * created by Jigsaw at 2018/10/19
 */
public class URLFormatUtils {


    /**
     * 是否能够映射成长连接
     *
     * @param shortUrl
     * @return
     */
    public static boolean canMapLongURL(String shortUrl) {
        if (!checkURL(shortUrl)) {
            return false;
        }
        String query = URI.create(shortUrl).getQuery();
        if (TextUtils.isEmpty(query)) {
            return false;
        }
        if (query.toLowerCase().contains("st=1")) {
            return true;
        }
        return false;
    }

    /**
     * 获取带有特定模块参数的URL字符串
     *
     * @return 带有st=1参的短链接,若传入的不是URL返回空字符串
     */
    public static String getDDShortUrl(String shortUrl) {

        if (!checkURL(shortUrl)) {
            DLog.w("URL无效，url = " + shortUrl);
            return "";
        }

        String query = URI.create(shortUrl).getQuery();
        if (TextUtils.isEmpty(query)) {
            query = "?st=1";
        } else {
            query = "&st=1";
        }
        return shortUrl + query;
    }

    /**
     * 检查是否是合法URL
     *
     * @param URL 字符串
     * @return true URL合法  false URL不合法
     */
    private static boolean checkURL(String URL) {
        if (TextUtils.isEmpty(URL)) {
            return false;
        }
        return URL.toLowerCase().startsWith("http://") || URL.toLowerCase().startsWith("https://");
    }
}
