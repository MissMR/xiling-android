package com.dodomall.ddmall.shared.constant;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.constant
 * @since 2017-07-06
 */
public enum OrderStatus {
    colsed(0, "closed", "交易关闭"),
    waitPay(1, "wait-pay", "待付款"),
    waitShip(2, "paid", "待发货"),
    dispatched(3, "dispatched", "已发货"),
    hasReceived(4, "has-received", "已收货"),
    refundingMoney(5, "refunding-money", "退款中"),
    refundingGoods(6, "refunding-goods", "退货中"),
    refundMoneyClosed(7, "refund-money-closed", "退款关闭"),
    refundGoodsClosed(8, "refund-goods-closed", "退货关闭"),
    error(9, "error", "异常订单"),
    finished(17, "finished", "已完成"),
    splitOrder(99, "split-order", "已拆单的订单主单");

    private int code;
    private String key;
    private String name;

    OrderStatus(int code, String key, String name) {
        this.code = code;
        this.key = key;
        this.name = name;
    }

    public static int getCodeByKey(String key) {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getKey().equalsIgnoreCase(key)) {
                return orderStatus.getCode();
            }
        }
        return colsed.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
