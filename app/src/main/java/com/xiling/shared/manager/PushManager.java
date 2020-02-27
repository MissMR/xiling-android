package com.xiling.shared.manager;

import android.content.Context;
import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.BuildConfig;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class PushManager {

    public static void registerJPushService(Context context) {
        JPushInterface.init(context);
    }

    public static void setJPushInfo(Context context, NewUserBean user) {
        if (user == null) {
            JPushInterface.deleteAlias(context, 1);
            LogUtils.e("清空推送 Alias");
        } else {
            if (BuildConfig.DEBUG) {
            }
            JPushInterface.setAlias(context, 1, user.getMemberId());
            LogUtils.e("设置推送 Alias   " + user.getMemberId());
        }
    }

}
