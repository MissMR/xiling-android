package com.dodomall.ddmall.shared.util;

import android.text.Html;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Xml;

import com.dodomall.ddmall.ddui.tools.DLog;
import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.orhanobut.logger.Logger;

import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;

public class StringUtil {

    public static String md5(CharSequence charSequence) {
        return Hashing.md5().hashString(charSequence, Charset.defaultCharset()).toString();
    }


    public static String getSpacesWithLength(int width, int textSize) {
        if (width == 0) {
            return "";
        }
        TextPaint paint = new TextPaint();
        paint.setTextSize(textSize);
        float textWidth = paint.measureText(" ");
        return Strings.repeat(" ", (int) Math.ceil(width / textWidth));
    }

    public static String hashMap2Xml(HashMap<String, String> hashMap) {
        String xml = "<xml>";
        for (String key : hashMap.keySet()) {
            xml += "<" + key + "><![CDATA[" + hashMap.get(key) + "]]></" + key + ">";
        }
        return xml + "</xml>";
    }

    public static HashMap<String, String> xml2HashMap(String content) {
        try {
            Logger.e("" + content);
            HashMap<String, String> xml = new HashMap<>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (!"xml".equals(nodeName)) {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }
            return xml;
        } catch (Exception e) {
            Logger.e("解释XML出错", e);
        }
        return null;
    }

    public static String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }


    public static String maskPhone(String phone) {
        if (phone != null && !phone.isEmpty()) {
            return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
        }
        return "";
    }

    public static String maskName(String name) {
        if (name != null && !name.isEmpty()) {
            return name.substring(0, 1) + Strings.repeat("*", name.length() - 1);
        }
        return "";
    }

    public static <T> boolean isNullOrEmpty(Collection<T> c) {
        return (c == null) || (c.size() == 0);
    }

    /**
     * 去除html标签
     *
     * @param strHtml
     * @return
     */
    public static String html2Text(String strHtml) {
//        // 剔出<html>的标签
//        String txtcontent = strHtml.replaceAll("</?[^>]+>", "");
//        // 去除字符串中的空格,回车,换行符,制表符
//        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
//        DLog.i("html2Text: " + txtcontent);
        DLog.i("html2Text:" + Html.fromHtml(strHtml).toString());
        return Html.fromHtml(strHtml).toString().replaceAll("￼", "");
    }

}
