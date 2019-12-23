package com.xiling.ddui.manager;

import android.app.Activity;
import android.content.Context;

import com.xiling.ddui.bean.FreeDataBean;
import com.xiling.ddui.bean.WXShareInfo;
import com.xiling.ddui.service.HtmlService;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ToastUtil;

public class FreeEventManager {

    public interface FreeEventListener {

        void onUserFreeEventStatus();

    }

    private static FreeEventManager self = null;

    public static FreeEventManager share() {
        if (self == null) {
            self = new FreeEventManager();
        }
        return self;
    }

    private FreeEventManager() {
        userService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private FreeEventListener listener = null;
    private IUserService userService = null;

    public FreeEventListener getListener() {
        return listener;
    }

    public void setListener(FreeEventListener listener) {
        this.listener = listener;
    }

    public void checkStatus(final Activity activity) {
        APIManager.startRequest(userService.getFreeBuyData(), new RequestListener<FreeDataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(FreeDataBean result) {
                super.onSuccess(result);
                if (result != null) {
                    int code = result.getResultCode();
                    String skuId = result.getSkuId();
                    switch (code) {
                        case 0://登录 - 交由下层界面判断登录逻辑
//                            ToastUtil.error(Config.NET_MESSAGE.NO_LOGIN);
//                            EventBus.getDefault().post(new EventMessage(Event.goToLogin));
//                            activity.finish();
//                            break;
                        case 1://首周未领取，进入详情页
                            //进入产品详情
//                            EventUtil.viewProductDetail(activity, skuId);
                            break;

                        case 3://首周已领取一次并邀请注册成功一次，进入H5，调用详情页
                        case 6://非首周并邀请注册成功一次，进入H5，调用详情页

                        case 2://首周已领取一次，当前次数是0，分享可获得一次机会
                        case 4://首周已领取两次，分享没机会
                        case 5://非首周，还没有注册成功的，分享可获得一次机会
                        case 7://非首周已经领取一次，分享没机会
                            jumpH5(activity, code, skuId);
                            break;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String EventUrl = HtmlService.FREE_BUY_APP_WEB;

    public void jumpH5(Context context, int status, String skuId) {
        String url = EventUrl + "?status=" + status + "&skuId=" + skuId + "&type=" + WXShareInfo.EventShareValue.FREE_BUY;
        WebViewActivity.jumpUrl(context, url);
    }
}
