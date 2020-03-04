package com.xiling.dduis.magnager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.xiling.R;
import com.xiling.ddui.activity.RealAuthActivity;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.custom.D3ialogTools;
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
import com.xiling.shared.util.ToastUtil;

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

    public void loginSuccess(Context context, NewUserBean user) {
        spUtils.putString(USER_MYUSER, gson.toJson(user));
        EventBus.getDefault().post(new EventMessage(Event.LOGIN_SUCCESS));
        PushManager.setJPushInfo(context, user);
    }

    /**
     * 退出登录
     */
    public void loginOut(Context context) {
        spUtils.remove(Key.OAUTH);
        spUtils.remove(USER_MYUSER);
        PushManager.setJPushInfo(context, null);
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


    public int getUserLevel() {
        int level = 0;
        NewUserBean userBean = getUser();
        if (userBean != null && userBean.getAuthStatus() == 2) {
            level = userBean.getRole().getRoleLevel();
        }
        return level;
    }


    /**
     * 根据用户等级，获取商品价格
     *
     * @return
     */
    public double getPriceForUser(ProductNewBean item) {
        double mPrice = item.getMinPrice();
        NewUserBean userBean = getUser();
        if (userBean != null && userBean.getAuthStatus() == 2) {
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
        double mPrice = item.getMinPrice();
        NewUserBean userBean = getUser();
        if (userBean != null && userBean.getAuthStatus() == 2) {
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
    public double getPriceForUser(ProductNewBean.SkusBean item) {
        double mPrice = item.getRetailPrice();
        NewUserBean userBean = getUser();
        if (userBean != null && userBean.getAuthStatus() == 2) {
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
     * 设置昵称
     */
    public void setNickName(String nickName) {
        NewUserBean userBean = getUser();
        userBean.setNickName(nickName);
        setUser(userBean);
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
                    ToastUtil.error(e.getMessage());
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


    /**
     * 判断实名认证
     * 认证状态（0，未认证，1，认证申请，2，认证通过，4，认证拒绝）
     */
    public void isRealAuth(final Context mContext, int status) {
        String message = "";
        //认证状态（0，未认证，1，认证申请，2，认证通过，4，认证拒绝）
        if (status == 1) {
            message = "您的实名认证正在认证中\n1个工作日内通过，请耐心等待~~~";
            D3ialogTools.showSingleAlertDialog(mContext, "",
                    message, "我知道了",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // startActivity(new Intent(mContext, RealAuthActivity.class));
                        }
                    });
        } else {
            D3ialogTools.showAlertDialog(mContext, "请先实名认证当前商户信息", "去认证", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, RealAuthActivity.class));
                }
            }, "取消", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }


}
