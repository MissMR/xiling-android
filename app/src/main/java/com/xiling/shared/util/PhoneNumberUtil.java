package com.xiling.shared.util;

import android.text.TextUtils;

import com.blankj.utilcode.utils.RegexUtils;

/**
 * created by Jigsaw at 2018/8/27
 */
public class PhoneNumberUtil {
    /**
     * 校验手机号
     * @param phoneNumber
     * @return
     */
    public static Boolean checkPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11 || !phoneNumber.startsWith("1")) {
            return false;
        }
        return true;
    }

    /**
     * 校验身份证号
     * @param identityCard
     * @return
     */
    public static String checkIDNumber(String identityCard){
        if (TextUtils.isEmpty(identityCard)) {
            return "请输入身份证号";
        } else {
            boolean enableId = false;
            if (identityCard.length() == 15) {
                enableId = RegexUtils.isIDCard15(identityCard);
            } else {
                enableId = RegexUtils.isIDCard18(identityCard);
            }

            if (!enableId) {
                return "请录入真实的身份证号";
            }
        }

        return "";
    }


    public static String getLast4Number(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return "";
        }
        return phoneNumber.substring(phoneNumber.length() - 4);
    }

    // return 131****3221
    public static String getSecretPhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 11) {
            return "";
        }
        return phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(phoneNumber.length() - 4);
    }

}
