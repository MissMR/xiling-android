package com.dodomall.ddmall.shared.manager;

import android.content.Context;

import com.blankj.utilcode.utils.LogUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.shared.bean.User;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class PushManager {

    public static void registerJPushService(Context context) {
        JPushInterface.init(context);
    }

    public static void setJPushInfo(Context context, User user) {
        if (BuildConfig.DEBUG) {
            addTestTag(context);
        }
        if (user == null) {
            JPushInterface.deleteAlias(context, 1);
            LogUtils.e("清空推送 Alias");
        } else {
            if (BuildConfig.DEBUG) {
            }
            JPushInterface.setAlias(context, 1, user.id);
            LogUtils.e("设置推送 Alias   " + user.id);
        }
    }

    private static void addTestTag(Context context) {
        Set<String> set = new HashSet<String>();
        set.add("test");
        JPushInterface.setTags(context, 1, set);
    }

}
