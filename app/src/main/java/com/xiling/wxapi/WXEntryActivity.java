package com.xiling.wxapi;

import android.os.Bundle;
import android.text.TextUtils;

import com.xiling.BuildConfig;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/7/30.
 */
public class WXEntryActivity extends DDWXEntryActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册API
        api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
//        Log.i("savedInstanceState"," sacvsa"+api.handleIntent(getIntent(), this));
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    //  发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        if (resp instanceof SendAuth.Resp) {
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
//            Log.i("newRespnewResp","   code    :"+code);
            if (TextUtils.isEmpty(code)) {
                EventBus.getDefault().post(new EventMessage(Event.wxLoginCancel));
            } else {
                EventBus.getDefault().post(new EventMessage(Event.wxLoginSuccess, code));
            }

        } else {
            EventBus.getDefault().post(new EventMessage(Event.wxLoginCancel));
        }

        finish();
    }
}
