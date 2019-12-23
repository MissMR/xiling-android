package com.xiling.shared.service;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.xiling.ddui.manager.CSManager;
import com.xiling.module.MainActivity;
import com.xiling.module.user.SetPasswordActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.HasPasswordModel;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PushManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.SessionUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import cn.ntalker.manager.inf.outer.NtalkerCoreCallback;

public class UserService {

    public static void loginSuccess(Activity activity, User user) {
        CSManager.share().login(new NtalkerCoreCallback() {
            @Override
            public void successed() {
                CSManager.share().setUserLogin(true);
            }

            @Override
            public void failed(int i) {
                CSManager.share().setUserLogin(false);
            }
        });
        UserService.login(user);
        PushManager.setJPushInfo(activity, user);
        EventBus.getDefault().post(new EventMessage(Event.loginSuccess));
        activity.startActivity(new Intent(activity, MainActivity.class));
        activity.finish();
    }

    public static void login(User user) {
        //Todo 注册的什么鬼，不是很清楚
//        MobclickAgent.onProfileSignIn(user.id);
        SessionUtil.getInstance().setLoginUser(user);
    }

    public static void logout() {
        MobclickAgent.onProfileSignOff();
        SessionUtil.getInstance().logout();
    }

    public static void checkHasPassword(final Activity activity, final HasPasswordListener listener) {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(service.hasPassowrd(), new BaseRequestListener<HasPasswordModel>(activity) {
            @Override
            public void onSuccess(HasPasswordModel model) {
                super.onSuccess(model);
                if (model.status) {
                    listener.onHasPassword();
                } else {
                    showNoPasswordDialog(activity);
                }
            }
        });
    }

    private static void showNoPasswordDialog(final Activity activity) {
        final WJDialog dialog = new WJDialog(activity);
        dialog.show();
        dialog.setTitle("您还未设置密码");
        dialog.setContentText("密码可用于登录、转账、提现等，为保证账户安全，请设置密码");
        dialog.setCancelText("返回");
        dialog.setConfirmText("去设置");
        dialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                Intent intent = new Intent(activity, FindPasswordActivity.class);
//                intent.putExtra("isSetPassword",true);
                Intent intent = new Intent(activity, SetPasswordActivity.class);
                activity.startActivity(intent);
            }
        });
    }

    public interface HasPasswordListener {
        void onHasPassword();
    }
}
