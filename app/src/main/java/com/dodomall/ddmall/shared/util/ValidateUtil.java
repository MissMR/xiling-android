package com.dodomall.ddmall.shared.util;

import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.shared.constant.Regex;

import java.util.regex.Pattern;

public class ValidateUtil {

    /**
     * 校验手机号
     *
     * @param phone 手机号
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isPhone(String phone) {
        return !StringUtils.isEmpty(phone) && phone.length() > 5;
    }

    /**
     * 校验身份证
     *
     * @param idCard 身份证号
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isIdCard(String idCard) {
        return Pattern.matches(Regex.ID_CARD, idCard);
    }

    /**
     * 校验URL
     *
     * @param url URL
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isUrl(String url) {
        return Pattern.matches(Regex.URL, url);
    }

    public static boolean isMoney(String money) {
        return Pattern.matches(Regex.MONEY, money);
    }
}
