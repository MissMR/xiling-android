package com.xiling.ddmall.ddui.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.xiling.ddmall.ddui.config.NTalkerConfig;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import cn.ntalker.api.Ntalker;
import cn.ntalker.manager.bean.ChatParamsBody;
import cn.ntalker.manager.inf.outer.NtalkerCoreCallback;

public class CSManager {

    private final static String TAG = "Ntalker";

    private static CSManager self = null;

    public static CSManager share() {
        if (self == null) {
            self = new CSManager();
        }
        return self;
    }

    private CSManager() {
    }

    public CSManager initSDK(Context context) {
        Ntalker.getInstance().initSDK(context.getApplicationContext(), NTalkerConfig.siteId, NTalkerConfig.nTalkServer, new NtalkerCoreCallback() {
            @Override
            public void successed() {
                DLog.d(TAG, "successed");
            }

            @Override
            public void failed(int i) {
                DLog.d(TAG, "failed:" + i);
            }
        });
        return this;
    }

    public CSManager setDebug(boolean flag) {
        //是否开启debug模式
        Ntalker.getInstance().enableDebug(false);
        return this;
    }

    //客服系统是否已登录
    private boolean isUserLogin = false;

    public boolean isUserLogin() {
        return isUserLogin;
    }

    public void setUserLogin(boolean userLogin) {
        isUserLogin = userLogin;
    }

    public void login(NtalkerCoreCallback callback) {
        boolean isLogin = SessionUtil.getInstance().isLogin();
        if (isLogin) {
            String userId = SessionUtil.getInstance().getLoginUser().id;
            String userName = SessionUtil.getInstance().getLoginUser().nickname;
            Ntalker.getInstance().login(userId, userName, callback);
        } else {
            callback.failed(-1);
        }
    }

    public void logout() {
        Ntalker.getInstance().logout();
    }

    public interface CSLoginListener {
        void onCSLoginSuccess();

        void onCSLoginFailure(int error);
    }

    public void check(final CSLoginListener listener) {
        if (isUserLogin()) {
            listener.onCSLoginSuccess();
        } else {
            ToastUtil.error("正在接通客服...");
            login(new NtalkerCoreCallback() {
                @Override
                public void successed() {
                    isUserLogin = true;
                    uiHandler.obtainMessage(EVENT_LOGIN_SUCCESS, listener).sendToTarget();
                }

                @Override
                public void failed(int i) {
                    isUserLogin = false;
                    uiHandler.obtainMessage(EVENT_LOGIN_FAILURE, i, 0, listener).sendToTarget();
                }
            });
        }
    }

    public void jumpToChat(final Context context, String skuId) {
        final ChatParamsBody chatParams = new ChatParamsBody();
        chatParams.goodsId = "" + skuId;
        chatParams.settingId = "" + NTalkerConfig.csGroupId;
        check(new CSLoginListener() {
            @Override
            public void onCSLoginSuccess() {
                Ntalker.getInstance().startChat(context, chatParams);
            }

            @Override
            public void onCSLoginFailure(int error) {
//                ToastUtil.error("客服暂不在线,反馈代码:" + error);
            }
        });
    }

    private static final int EVENT_LOGIN_SUCCESS = 0x00;
    private static final int EVENT_LOGIN_FAILURE = 0x01;

    Handler uiHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case EVENT_LOGIN_SUCCESS:
                    uiHandler.removeMessages(EVENT_LOGIN_SUCCESS);
                    CSLoginListener sListener = (CSLoginListener) msg.obj;
                    sListener.onCSLoginSuccess();
                    break;
                case EVENT_LOGIN_FAILURE:
                    uiHandler.removeMessages(EVENT_LOGIN_FAILURE);
                    CSLoginListener fListener = (CSLoginListener) msg.obj;
                    fListener.onCSLoginFailure(msg.arg1);
                    break;
            }
        }
    };
}

