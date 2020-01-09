package com.xiling.dduis.magnager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICaptchaService;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.SharedPreferenceUtil;

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
    ICaptchaService iCaptchaService;

    private UserManager() {
        spUtils = new SPUtils(USER_TABLE);
        gson = new Gson();
        iCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
    }

    public static UserManager getInstance() {
        if (userManager == null) {
            userManager = new UserManager();
        }

        return userManager;
    }

    public void setOAuthToken(String token) {
        spUtils.putString(Key.OAUTH, token);
    }

    public String getOAuthToken() {
        return spUtils.getString(Key.OAUTH, "");
    }

    public void setUser(NewUserBean user) {
        spUtils.putString(USER_MYUSER, gson.toJson(user));
    }

    public void loginSuccess(NewUserBean user) {
        spUtils.putString(USER_MYUSER, gson.toJson(user));
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_SUCCESS));
    }

    /**
     * 退出登录
     */
    public void loginOut() {
        spUtils.remove(Key.OAUTH);
        spUtils.remove(USER_MYUSER);
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_OUT));
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public NewUserBean getUser() {
        String userJson = spUtils.getString(USER_MYUSER);

        if (!TextUtils.isEmpty(userJson)) {
            return gson.fromJson(userJson, NewUserBean.class);
        }
        return null;
    }





    public boolean isLogin() {
        return getUser() != null;
    }

    public boolean isLogin(Context context) {
        if (getUser() == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
        return getUser() != null;
    }

    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(ProductNewBean item) {
        double mPrice = item.getLevel10Price();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    mPrice = item.getLevel10Price();
                    break;
                case 20:
                    mPrice = item.getLevel20Price();
                    break;
                case 30:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }

    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(HomeRecommendDataBean.DatasBean item) {
        double mPrice = item.getLevel10Price();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    mPrice = item.getLevel10Price();
                    break;
                case 20:
                    mPrice = item.getLevel20Price();
                    break;
                case 30:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }
    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser( ProductNewBean.SkusBean item) {
        double mPrice = item.getLevel10Price();
        NewUserBean userBean = getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    mPrice = item.getLevel10Price();
                    break;
                case 20:
                    mPrice = item.getLevel20Price();
                    break;
                case 30:
                    mPrice = item.getLevel30Price();
                    break;
            }
        }
        return mPrice;
    }



    //校验用户信息，保证用户信息的正确性
    public void checkUserInfo(final OnCheckUserInfoLisense onCheckUserInfoLisense) {
        if (UserManager.getInstance().isLogin()) {
            APIManager.startRequest(iCaptchaService.getUserInfo(), new BaseRequestListener<NewUserBean>() {
                @Override
                public void onSuccess(NewUserBean result) {
                    super.onSuccess(result);
                    UserManager.getInstance().setUser(result);
                    if (onCheckUserInfoLisense != null) {
                        onCheckUserInfoLisense.onCheckUserInfoSucess(result);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    if (onCheckUserInfoLisense != null) {
                        onCheckUserInfoLisense.onCheckUserInfoFail();
                    }
                }
            });
        } else {
            onCheckUserInfoLisense.onCheckUserInfoFail();
        }
    }

    public interface OnCheckUserInfoLisense {
        void onCheckUserInfoSucess(NewUserBean newUserBean);

        void onCheckUserInfoFail();
    }


}
