package com.xiling.ddui.bean;

import java.util.List;

public class OrderDetailBean {
    /**
     * totalPrice : 301
     * totalRetailPrice : 301
     * totalDiscountPrice : 0
     * goodsTotalPrice : 300
     * goodsTotalRetailPrice : 300
     * goodsTotalDiscountPrice : 0
     * couponReductionPrice : 0
     * freight : 1
     * stores : [{"storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeName":"喜领","products":[{"order1Id":"","orderId":"","sortIndex":0,"productId":"c3aedb9c06bd4672bea46e7f143df4c7","storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeFreight":"","productImage":"","skuId":"66d574e4521843dba537936bfd68b22f","productType":0,"isCross":0,"barCode":"sku-3","skuCode":"sku-3","skuName":"测试产品2","quantity":1,"price":300,"retailPrice":300,"marketPrice":300,"costPrice":"","lineTotal":300,"lineWeight":10,"properties":"1018-1124","refundStatus":"","refundId":"","receiptsIndices":"","storeName":"喜领"}]}]
     */

    private double totalPrice;
    int isCross;
    private double taxes;

    public double getTotalPrice() {
        return totalPrice / 100;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getTotalRetailPrice() {
        return totalRetailPrice;
    }

    public void setTotalRetailPrice(double totalRetailPrice) {
        this.totalRetailPrice = totalRetailPrice;
    }

    public double getTotalDiscountPrice() {
        return totalDiscountPrice;
    }

    public void setTotalDiscountPrice(double totalDiscountPrice) {
        this.totalDiscountPrice = totalDiscountPrice;
    }

    public double getGoodsTotalPrice() {
        return goodsTotalPrice / 100;
    }

    public void setGoodsTotalPrice(double goodsTotalPrice) {
        this.goodsTotalPrice = goodsTotalPrice;
    }

    public double getGoodsTotalRetailPrice() {
        return goodsTotalRetailPrice / 100;
    }

    public void setGoodsTotalRetailPrice(double goodsTotalRetailPrice) {
        this.goodsTotalRetailPrice = goodsTotalRetailPrice;
    }

    public double getGoodsTotalDiscountPrice() {
        return goodsTotalDiscountPrice / 100;
    }

    public void setGoodsTotalDiscountPrice(double goodsTotalDiscountPrice) {
        this.goodsTotalDiscountPrice = goodsTotalDiscountPrice;
    }

    public double getCouponReductionPrice() {
        return couponReductionPrice / 100;
    }

    public void setCouponReductionPrice(double couponReductionPrice) {
        this.couponReductionPrice = couponReductionPrice;
    }

    public double getFreight() {
        return freight / 100;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    private double totalRetailPrice;
    private double totalDiscountPrice;
    private double goodsTotalPrice;
    private double goodsTotalRetailPrice;
    private double goodsTotalDiscountPrice;
    private double couponReductionPrice;
    private double freight;
    private List<StoresBean> stores;


    public List<StoresBean> getStores() {
        return stores;
    }

    public void setStores(List<StoresBean> stores) {
        this.stores = stores;
    }

    public int getIsCross() {
        return isCross;
    }

    public double getTaxes() {
        return taxes / 100;
    }

    public static class StoresBean {
        /**
         * storeId : 0270f3a9ba0545e3bc62be5ea00ac48d
         * storeName : 喜领
         * products : [{"order1Id":"","orderId":"","sortIndex":0,"productId":"c3aedb9c06bd4672bea46e7f143df4c7","storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeFreight":"","productImage":"","skuId":"66d574e4521843dba537936bfd68b22f","productType":0,"isCross":0,"barCode":"sku-3","skuCode":"sku-3","skuName":"测试产品2","quantity":1,"price":300,"retailPrice":300,"marketPrice":300,"costPrice":"","lineTotal":300,"lineWeight":10,"properties":"1018-1124","refundStatus":"","refundId":"","receiptsIndices":"","storeName":"喜领"}]
         */

        private String storeId;
        private String storeName;
        private List<ProductsBean> products;

        public String getStoreId() {
            return storeId;
        }

        public void setStoreId(String storeId) {
            this.storeId = storeId;
        }

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
        }

        public List<ProductsBean> getProducts() {
            return products;
        }

        public void setProducts(List<ProductsBean> products) {
            this.products = products;
        }

        public static class ProductsBean {
            /**
             * order1Id :
             * orderId :
             * sortIndex : 0
             * productId : c3aedb9c06bd4672bea46e7f143df4c7
             * storeId : 0270f3a9ba0545e3bc62be5ea00ac48d
             * storeFreight :
             * productImage :
             * skuId : 66d574e4521843dba537936bfd68b22f
             * productType : 0
             * isCross : 0
             * barCode : sku-3
             * skuCode : sku-3
             * skuName : 测试产品2
             * quantity : 1
             * price : 300
             * retailPrice : 300
             * marketPrice : 300
             * costPrice :
             * lineTotal : 300
             * lineWeight : 10
             * properties : 1018-1124
             * refundStatus :
             * refundId :
             * receiptsIndices :
             * storeName : 喜领
             */

            private String order1Id;
            private String orderId;
            private int sortIndex;
            private String productId;
            private String storeId;
            private String storeFreight;
            private String productImage;
            private String skuId;
            private int productType;
            private int isCross;
            private String barCode;
            private String skuCode;
            private String skuName;
            private int quantity;
            private double price;
            private double retailPrice;
            private int marketPrice;
            private String costPrice;
            private int lineTotal;
            private int lineWeight;
            private String properties;
            private String refundStatus;
            private String refundId;
            private String receiptsIndices;
            private String storeName;

            public double getTotalTaxes() {
                return totalTaxes / 100;
            }

            private double totalTaxes;


            public String getOrder1Id() {
                return order1Id;
            }

            public void setOrder1Id(String order1Id) {
                this.order1Id = order1Id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public int getSortIndex() {
                return sortIndex;
            }

            public void setSortIndex(int sortIndex) {
                this.sortIndex = sortIndex;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getStoreId() {
                return storeId;
            }

            public void setStoreId(String storeId) {
                this.storeId = storeId;
            }

            public String getStoreFreight() {
                return storeFreight;
            }

            public void setStoreFreight(String storeFreight) {
                this.storeFreight = storeFreight;
            }

            public String getProductImage() {
                return productImage;
            }

            public void setProductImage(String productImage) {
                this.productImage = productImage;
            }

            public String getSkuId() {
                return skuId;
            }

            public void setSkuId(String skuId) {
                this.skuId = skuId;
            }

            public int getProductType() {
                return productType;
            }

            public void setProductType(int productType) {
                this.productType = productType;
            }

            public int getIsCross() {
                return isCross;
            }

            public void setIsCross(int isCross) {
                this.isCross = isCross;
            }

            public String getBarCode() {
                return barCode;
            }

            public void setBarCode(String barCode) {
                this.barCode = barCode;
            }

            public String getSkuCode() {
                return skuCode;
            }

            public void setSkuCode(String skuCode) {
                this.skuCode = skuCode;
            }

            public String getSkuName() {
                return skuName;
            }

            public void setSkuName(String skuName) {
                this.skuName = skuName;
            }

            public int getQuantity() {
                return quantity;
            }

            public void setQuantity(int quantity) {
                this.quantity = quantity;
            }

            public double getPrice() {
                return price / 100;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public double getRetailPrice() {
                return retailPrice / 100;
            }

            public void setRetailPrice(double retailPrice) {
                this.retailPrice = retailPrice;
            }

            public int getMarketPrice() {
                return marketPrice;
            }

            public void setMarketPrice(int marketPrice) {
                this.marketPrice = marketPrice;
            }

            public String getCostPrice() {
                return costPrice;
            }

            public void setCostPrice(String costPrice) {
                this.costPrice = costPrice;
            }

            public int getLineTotal() {
                return lineTotal;
            }

            public void setLineTotal(int lineTotal) {
                this.lineTotal = lineTotal;
            }

            public int getLineWeight() {
                return lineWeight;
            }

            public void setLineWeight(int lineWeight) {
                this.lineWeight = lineWeight;
            }

            public String getProperties() {
                return properties;
            }

            public void setProperties(String properties) {
                this.properties = properties;
            }

            public String getRefundStatus() {
                return refundStatus;
            }

            public void setRefundStatus(String refundStatus) {
                this.refundStatus = refundStatus;
            }

            public String getRefundId() {
                return refundId;
            }

            public void setRefundId(String refundId) {
                this.refundId = refundId;
            }

            public String getReceiptsIndices() {
                return receiptsIndices;
            }

            public void setReceiptsIndices(String receiptsIndices) {
                this.receiptsIndices = receiptsIndices;
            }

            public String getStoreName() {
                return storeName;
            }

            public void setStoreName(String storeName) {
                this.storeName = storeName;
            }
        }
    }
}
