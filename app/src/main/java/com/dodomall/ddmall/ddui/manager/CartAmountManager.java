package com.dodomall.ddmall.ddui.manager;

import com.blankj.utilcode.utils.SPUtils;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.util.SharedPreferenceUtil;

import org.greenrobot.eventbus.EventBus;

public class CartAmountManager {

    private static CartAmountManager self = null;

    public static CartAmountManager share() {
        if (self == null) {
            self = new CartAmountManager();
        }
        return self;
    }

    SPUtils spUtils = null;

    private CartAmountManager() {
        spUtils = SharedPreferenceUtil.getInstance();
    }

    private String kAmount = "kAmount";
    private int amount = -1;

    /**
     * 获取购物车数量
     */
    public int getAmount() {
        if (amount < 0) {
            amount = getLocalValue();
        }
        return amount;
    }

    /**
     * 设置购物车数量
     */
    public void setAmount(int amount) {
        this.amount = amount;
        setLocalValue(amount);
        callback();
    }

    /**
     * 增加购物车数量
     */
    public void addAmount(int count) {
        this.amount += count;
        setAmount(this.amount);
    }

    /**
     * 重新加载购物车数量
     */
    public void reload() {
        amount = getLocalValue();
        callback();
    }

    private void callback() {
        EventBus.getDefault().post(new EventMessage(Event.cartAmountUpdate, amount));
    }

    private int getLocalValue() {
        if (spUtils != null) {
            return spUtils.getInt(kAmount, 0);
        } else {
            return 0;
        }
    }

    private void setLocalValue(int value) {
        if (spUtils != null) {
            spUtils.putInt(kAmount, value);
        } else {
            DLog.e("setLocalValue failure because SharedPreference is null.");
        }
    }

}
