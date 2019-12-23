package com.xiling.module.auth.event;


import com.xiling.shared.basic.BaseEventMsg;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/4/16.
 */
public class MsgStatus extends BaseEventMsg {

    /**
     * 跳转到分类界面
     */
    public static final int JUMP_TO_CATEGORY = 1000;
    /**
     * 从新加载消息条数
     */
    public static final int ReloadMsgCount = 9999;

    public static final int ACTION_CARD_CHANGE = 1 << 0;
    public static final int ACTION_DEAL_SUCESS = 1 << 1;
    public static final int ACTION_EDIT_PHONE = 1 << 2;
    public static final int ACTION_USER_CHANGE = 1 << 4;
    public static final int ACTION_GO_MAIN = 1 << 5;
    public static final int ACTION_SCAN_SUCCEED = 1 << 6;
    public static final int ACTION_STORE_SHIT_SUCCEED = 1 << 7;
    public static final int ACTION_REFUND_CHANGE = 1 << 9;
    public static final int ACTION_TO_PAY = 1 << 10;
    public static final int ACTION_CANCEL_REFUNDS = 1 << 11;
    public static final int ACTION_COMMENT_ORDER = 1 << 12;
    public static final int ACTION_SELECT_STORE = 1 << 13;
    public static final int ACTION_STORE_CHANGE = 1 << 14;


    private String mAuthIdentityFailMsg;
    private String mTips;
    private double mMoney;
    private String code;

    public MsgStatus(int action) {
        super(action);
    }

    public String getAuthIdentityFailMsg() {
        return mAuthIdentityFailMsg;
    }

    public void setAuthIdentityFailMsg(String authIdentityFailMsg) {
        mAuthIdentityFailMsg = authIdentityFailMsg;
    }

    public void setTips(String tips) {
        mTips = tips;
    }

    public String getTips() {
        return mTips;
    }

    public void setMoney(double money) {
        mMoney = money;
    }

    public double getMoney() {
        return mMoney;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
