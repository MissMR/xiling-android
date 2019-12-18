package com.xiling.ddmall.shared.util;

import com.blankj.utilcode.utils.SPUtils;
import com.xiling.ddmall.MyApplication;

/**
 * created by Jigsaw at 2018/8/21
 */
public class SharedPreferenceUtil {
    private static SPUtils sSPUtils;

    private SharedPreferenceUtil() {

    }

    public static SPUtils getInstance() {
        if (null == sSPUtils) {
            synchronized (SPUtils.class) {
                if (null == sSPUtils) {
                    sSPUtils = new SPUtils(MyApplication.getInstance().getPackageName());
                }
            }
        }
        return sSPUtils;
    }

}
