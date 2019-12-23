package com.xiling.shared.util;

import android.text.TextUtils;

/**
 * created by Jigsaw at 2018/8/27
 */
public class PhoneNumberUtil {
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
