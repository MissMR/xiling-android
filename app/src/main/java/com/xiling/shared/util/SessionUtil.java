package com.xiling.shared.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xiling.ddui.manager.CartAmountManager;
import com.xiling.shared.bean.MyStatus;
import com.google.gson.Gson;
import com.xiling.MyApplication;
import com.xiling.shared.bean.User;
import com.xiling.shared.constant.Key;

import org.greenrobot.eventbus.EventBus;

public class SessionUtil {

    private static SessionUtil instance;
    private static Context mContext;
    private static SharedPreferences sharedPreferences;

    private SessionUtil() {
        mContext = MyApplication.getInstance().getApplicationContext();
        sharedPreferences = mContext.getSharedPreferences("Settings.sp", Context.MODE_PRIVATE);
    }

    public static SessionUtil getInstance() {
        if (instance == null || mContext == null) {
            SessionUtil.destroy();
            instance = new SessionUtil();
        }
        return instance;
    }

    private static void destroy() {
        if (instance != null) {
            mContext = null;
            sharedPreferences = null;
            instance = null;
        }
    }

    public boolean setLoginUser(User user) {
        return sharedPreferences.edit().putString(Key.USER, new Gson().toJson(user)).commit();
    }

    public User getLoginUser() {
        String json = sharedPreferences.getString(Key.USER, "");
        return User.fromJson(json);
    }

    public boolean setOAuthToken(String token) {
        return sharedPreferences.edit().putString(Key.OAUTH, token).commit();
    }

    public String getOAuthToken() {
        return sharedPreferences.getString(Key.OAUTH, "");
    }

    public boolean isLogin() {
        return getLoginUser() != null;
    }

    public boolean isMaster() {
        return isLogin() ? getLoginUser().isStoreMaster() : false;
    }

    public void logout() {
        sharedPreferences.edit().remove(Key.USER).remove(Key.OAUTH).apply();
        //退出的时候清空购物车商品数量
        CartAmountManager.share().setAmount(0);

        MyStatus msgCount = new MyStatus();
        msgCount.messageCount = 0;
        EventBus.getDefault().post(msgCount);
    }
}
