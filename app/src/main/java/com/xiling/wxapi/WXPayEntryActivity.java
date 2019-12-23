package com.xiling.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.LogUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xiling.module.pay.PayMsg;
import com.xiling.shared.util.WechatUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.weiju.wyhmall.wxapi
 * @since 2017-08-09
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI mWxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWxApi = WechatUtil.newWxApi(this);
        mWxApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {
    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (baseResp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WXPAY_SUCCEED,""));
                    LogUtils.e("发送 微信支付成功  ");
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WXPAY_FAIL,"用户取消支付"));
                    LogUtils.e("发送 微信支付取消  ");
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                default:
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_WXPAY_FAIL,baseResp.errStr));
                    LogUtils.e("发送 微信支付失败  ");
                    break;
            }
            finish();
        }
    }
}