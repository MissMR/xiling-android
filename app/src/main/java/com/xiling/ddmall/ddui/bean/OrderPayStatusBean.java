package com.xiling.ddmall.ddui.bean;

import android.text.TextUtils;

import com.xiling.ddmall.shared.constant.OrderStatus;

/**
 * created by Jigsaw at 2019/1/23
 */
public class OrderPayStatusBean {

    /**
     * payType : 2
     * payMoney : 1
     * orderStatus : 2
     * showBannerFlag : -1
     * <p>
     * ==支付类型==
     * Unknow(0, "未支付"),
     * Wechatpay(1, "微信支付"),
     * Alipay(2, "支付宝"),
     * offinePay(3, "线下付款"),
     * systemPay(4, "系统代付"),
     * moneyPay(5, "零钱支付"),
     * WechatAppPay(6,"微信支付");//微信app支付
     */

    private int payType;
    private long payMoney;
    private int orderStatus;
    // 是否显示店主送流量banner（非0展示、0不展示）
    private int showBannerFlag = 0;
    private String imageUrl;
    private int storeFlag;

    public boolean isStoreGift() {
        return this.storeFlag == 1;
    }

    public int getStoreFlag() {
        return storeFlag;
    }

    public void setStoreFlag(int storeFlag) {
        this.storeFlag = storeFlag;
    }

    public boolean isShowActivityBanner() {
        return showBannerFlag != 0 && !TextUtils.isEmpty(imageUrl);
    }

    // 待发货状态：后台已收到第三方支付回调
    public boolean isOrderPaySuccess() {
        return payType != OrderStatus.waitPay.getCode() && payType != OrderStatus.error.getCode();
    }

    public String getPayWayStr() {
        return payType == 2 ? "支付宝支付" : "微信支付";
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public long getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(long payMoney) {
        this.payMoney = payMoney;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getShowBannerFlag() {
        return showBannerFlag;
    }

    public void setShowBannerFlag(int showBannerFlag) {
        this.showBannerFlag = showBannerFlag;
    }
}
