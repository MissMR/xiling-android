package com.xiling.ddui.manager;

import android.text.TextUtils;

import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMessageService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 *
 */
public class XLMessageManager {

    /**
     * 用来读取未读消息条数
     */
    public static void loadUserStatus() {
        if (!UserManager.getInstance().isLogin()) {
            return;
        }

        IMessageService messageService = ServiceManager.getInstance().createService(IMessageService.class);
        APIManager.startRequest(messageService.getUnReadCount(), new BaseRequestListener<String>() {
            @Override
            public void onSuccess(String result) {
                super.onSuccess(result);
                if (!TextUtils.isEmpty(result)) {
                    MyStatus status = new MyStatus();
                    status.messageCount = Integer.valueOf(result);
                    EventBus.getDefault().post(status);
                } else {
                    MyStatus status = new MyStatus();
                    status.messageCount = 0;
                    EventBus.getDefault().post(status);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

}
