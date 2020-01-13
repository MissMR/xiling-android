package com.xiling.ddui.bean;

import java.util.List;

public class MyOrderDetailBean {
    /**
     * size : 10
     * pages : 1
     * current : 1
     * total : 2
     * hasPre : false
     * hasNext : false
     * records : [{"orderId":"c1db744b253e4d919c046fd0de432692","orderCode":"0311578950267974","orderStatus":"待支付","orderStatusUs":"WAIT_PAY","details":[{"productId":"c3aedb9c06bd4672bea46e7f143df4c7","skuName":"测试产品2","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/20200103013222609Q7.png","productSpecification":"黑框黑灰片;,随机;","price":300,"retailPrice":300,"skuId":"66d574e4521843dba537936bfd68b22f","skuCode":"sku-3","quantity":1}],"totalQuantity":1,"totalPrice":300,"createTime":"2020-01-11 14:08:11","updateTime":"2020-01-11 14:08:11","canRemindDelivery":false,"contactUsername":"pt","contactPhone":"18354232734","contactProvince":"北京","contactCity":"北京市","contactDistrict":"东城区","contactDetail":"jxjdn","freight":1,"discountCoupon":0,"discountPrice":0,"taxes":0,"expressId":0,"expressCode":"","expressName":"","doneTime":"","shipDate":"","payType":"未支付","payMoney":301,"waitPayTimeMilli":1294914},{"orderId":"4220efd50ab64d278d5c939fdafd35a4","orderCode":"0311578950248875","orderStatus":"待支付","orderStatusUs":"WAIT_PAY","details":[{"productId":"c3aedb9c06bd4672bea46e7f143df4c7","skuName":"测试产品2","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/20200103013222609Q7.png","productSpecification":"黑框黑灰片;,随机;","price":300,"retailPrice":300,"skuId":"66d574e4521843dba537936bfd68b22f","skuCode":"sku-3","quantity":1}],"totalQuantity":1,"totalPrice":300,"createTime":"2020-01-11 14:07:52","updateTime":"2020-01-11 14:07:52","canRemindDelivery":false,"contactUsername":"pt","contactPhone":"18354232734","contactProvince":"北京","contactCity":"北京市","contactDistrict":"东城区","contactDetail":"jxjdn","freight":1,"discountCoupon":0,"discountPrice":0,"taxes":0,"expressId":0,"expressCode":"","expressName":"","doneTime":"","shipDate":"","payType":"未支付","payMoney":301,"waitPayTimeMilli":1275897}]
     * ex :
     * first : true
     * last : true
     */

    private int size;
    private int pages;
    private int current;
    private int total;
    private boolean hasPre;
    private boolean hasNext;
    private String ex;
    private boolean first;
    private boolean last;
    private List<RecordsBean> records;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isHasPre() {
        return hasPre;
    }

    public void setHasPre(boolean hasPre) {
        this.hasPre = hasPre;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public List<RecordsBean> getRecords() {
        return records;
    }

    public void setRecords(List<RecordsBean> records) {
        this.records = records;
    }

    public static class RecordsBean {
        /**
         * orderId : c1db744b253e4d919c046fd0de432692
         * orderCode : 0311578950267974
         * orderStatus : 待支付
         * orderStatusUs : WAIT_PAY
         * details : [{"productId":"c3aedb9c06bd4672bea46e7f143df4c7","skuName":"测试产品2","productImage":"http://xiling-test.oss-cn-qingdao.aliyuncs.com/product/2020-01/20200103013222609Q7.png","productSpecification":"黑框黑灰片;,随机;","price":300,"retailPrice":300,"skuId":"66d574e4521843dba537936bfd68b22f","skuCode":"sku-3","quantity":1}]
         * totalQuantity : 1
         * totalPrice : 300
         * createTime : 2020-01-11 14:08:11
         * updateTime : 2020-01-11 14:08:11
         * canRemindDelivery : false
         * contactUsername : pt
         * contactPhone : 18354232734
         * contactProvince : 北京
         * contactCity : 北京市
         * contactDistrict : 东城区
         * contactDetail : jxjdn
         * freight : 1
         * discountCoupon : 0
         * discountPrice : 0
         * taxes : 0
         * expressId : 0
         * expressCode :
         * expressName :
         * doneTime :
         * shipDate :
         * payType : 未支付
         * payMoney : 301
         * waitPayTimeMilli : 1294914
         */

        private String orderId;
        private String orderCode;
        private String orderStatus;
        private String orderStatusUs;
        private int totalQuantity;
        private double totalPrice;
        private String createTime;
        private String updateTime;
        private boolean canRemindDelivery;
        private String contactUsername;
        private String contactPhone;
        private String contactProvince;
        private String contactCity;
        private String contactDistrict;
        private String contactDetail;
        private int freight;
        private int discountCoupon;
        private int discountPrice;
        private int taxes;
        private int expressId;
        private String expressCode;
        private String expressName;
        private String doneTime;
        private String shipDate;
        private String payType;
        private int payMoney;
        private int waitPayTimeMilli;
        private List<DetailsBean> details;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOrderCode() {
            return orderCode;
        }

        public void setOrderCode(String orderCode) {
            this.orderCode = orderCode;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderStatusUs() {
            return orderStatusUs;
        }

        public void setOrderStatusUs(String orderStatusUs) {
            this.orderStatusUs = orderStatusUs;
        }

        public int getTotalQuantity() {
            return totalQuantity;
        }

        public void setTotalQuantity(int totalQuantity) {
            this.totalQuantity = totalQuantity;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public boolean isCanRemindDelivery() {
            return canRemindDelivery;
        }

        public void setCanRemindDelivery(boolean canRemindDelivery) {
            this.canRemindDelivery = canRemindDelivery;
        }

        public String getContactUsername() {
            return contactUsername;
        }

        public void setContactUsername(String contactUsername) {
            this.contactUsername = contactUsername;
        }

        public String getContactPhone() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone = contactPhone;
        }

        public String getContactProvince() {
            return contactProvince;
        }

        public void setContactProvince(String contactProvince) {
            this.contactProvince = contactProvince;
        }

        public String getContactCity() {
            return contactCity;
        }

        public void setContactCity(String contactCity) {
            this.contactCity = contactCity;
        }

        public String getContactDistrict() {
            return contactDistrict;
        }

        public void setContactDistrict(String contactDistrict) {
            this.contactDistrict = contactDistrict;
        }

        public String getContactDetail() {
            return contactDetail;
        }

        public void setContactDetail(String contactDetail) {
            this.contactDetail = contactDetail;
        }

        public int getFreight() {
            return freight;
        }

        public void setFreight(int freight) {
            this.freight = freight;
        }

        public int getDiscountCoupon() {
            return discountCoupon;
        }

        public void setDiscountCoupon(int discountCoupon) {
            this.discountCoupon = discountCoupon;
        }

        public int getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(int discountPrice) {
            this.discountPrice = discountPrice;
        }

        public int getTaxes() {
            return taxes;
        }

        public void setTaxes(int taxes) {
            this.taxes = taxes;
        }

        public int getExpressId() {
            return expressId;
        }

        public void setExpressId(int expressId) {
            this.expressId = expressId;
        }

        public String getExpressCode() {
            return expressCode;
        }

        public void setExpressCode(String expressCode) {
            this.expressCode = expressCode;
        }

        public String getExpressName() {
            return expressName;
        }

        public void setExpressName(String expressName) {
            this.expressName = expressName;
        }

        public String getDoneTime() {
            return doneTime;
        }

        public void setDoneTime(String doneTime) {
            this.doneTime = doneTime;
        }

        public String getShipDate() {
            return shipDate;
        }

        public void setShipDate(String shipDate) {
            this.shipDate = shipDate;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public int getPayMoney() {
            return payMoney;
        }

        public void setPayMoney(int payMoney) {
            this.payMoney = payMoney;
        }

        public int getWaitPayTimeMilli() {
            return waitPayTimeMilli;
        }

        public void setWaitPayTimeMilli(int waitPayTimeMilli) {
            this.waitPayTimeMilli = waitPayTimeMilli;
        }

        public List<DetailsBean> getDetails() {
            return details;
        }

        public void setDetails(List<DetailsBean> details) {
            this.details = details;
        }

    }
}
