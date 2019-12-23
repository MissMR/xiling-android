package com.xiling.module.pay;

import com.xiling.module.auth.event.BaseMsg;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/12.
 */
public class PayMsg extends BaseMsg {
    public final static int ACTION_ALIPAY_SUCCEED = 0x1;
    public final static int ACTION_ALIPAY_FAIL = 0x2;
    public final static int ACTION_WEBPAY_SUCCEED = 0x4;
    public final static int ACTION_WEBPAY_FAIL = 0x3;
    public final static int ACTION_WXPAY_FAIL = 0x5;
    public final static int ACTION_WXPAY_SUCCEED = 0x6;
    public final static int ACTION_BALANCE_SUCCEED = 0x7;

    public String message = "支付失败";

    public PayMsg(int action) {
        super(action);
    }

    public PayMsg(int action,String message) {
        super(action);
        this.message = message;
    }
}
