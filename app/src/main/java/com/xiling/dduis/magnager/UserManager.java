package com.xiling.dduis.magnager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录对象管理类
 */
public class UserManager {
   private static UserManager userManager;
    private static SPUtils spUtils;
    public static final String USER_TABLE = "user_table_name";
    public static final String USER_MYUSER = "user";
    private static Gson gson;

    private UserManager(){
        spUtils = new SPUtils(USER_TABLE);
        gson = new Gson();
    }

    public static UserManager getInstance(){
        if (userManager == null){
            userManager = new UserManager();
        }

        return userManager;
    }



    public  void loginSuccess(NewUserBean user) {
        spUtils.putString(USER_MYUSER,gson.toJson(user));
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_SUCCESS));
    }

    /**
     * 退出登录
     */
    public  void loginOut(){
        spUtils.putString(USER_MYUSER,"");
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_OUT));
    }

    /**
     * 获取用户信息
     * @return
     */
    public  NewUserBean getUser(){
        String userJson = spUtils.getString(USER_MYUSER);

        if (!TextUtils.isEmpty(userJson)){
            return gson.fromJson(userJson,NewUserBean.class);
        }
        return null;
    }

    public boolean isLogin(){
        return  getUser() != null;
    }

    public boolean isLogin(Context context){

        if (getUser() == null){
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        return  getUser() != null;
    }

}
