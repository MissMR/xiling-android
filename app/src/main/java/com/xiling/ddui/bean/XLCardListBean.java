package com.xiling.ddui.bean;

import java.util.List;

/**
 * 购物车列表对象
 */
public class XLCardListBean {

    /**
     * storeId : 0270f3a9ba0545e3bc62be5ea00ac48d
     * storeName : 喜领
     * skuProductList : [{"storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeName":"喜领","productId":"269a548ffc344e85883c702ef0a90f8d","productName":"24年老国货依风凡士林润肤霜250g 防干防裂 软化死皮","thumbUrl":"","skuId":"ad40d372566d4211b074703999a55f8f","skuName":"0283 24年老国货依风凡士林润肤霜250g 防干防裂 软化死皮","price":1590,"marketPrice":2900,"retailPrice":1790,"stock":4578,"quantity":99,"properties":"","status":1},{"storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeName":"喜领","productId":"01cff951a602430994a399a27804f843","productName":"法国进口黑科技  梵斯萃丽素颜CC霜","thumbUrl":"","skuId":"208a97bcf5994765aab5f0ae61b30eb7","skuName":"法国进口黑科技  梵斯萃丽素颜CC霜","price":22700,"marketPrice":29900,"retailPrice":22900,"stock":9993,"quantity":1,"properties":"","status":1},{"storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeName":"喜领","productId":"7dd133b39a4c4506af2a86dde899b08e","productName":"袋鼠妈妈燕窝深润亮皙滋养手膜（6对）","thumbUrl":"","skuId":"fe6ba4a061754aedbb71845944d8b0e0","skuName":"0051袋鼠妈妈燕窝深润亮皙滋养手膜（6对）","price":4590,"marketPrice":6900,"retailPrice":4790,"stock":4821,"quantity":1,"properties":"","status":1},{"storeId":"0270f3a9ba0545e3bc62be5ea00ac48d","storeName":"喜领","productId":"8c547a9faf3243f890a72da078c3d16b","productName":"102g*4块bofayalo84消毒肥皂 强效去渍 去污不伤手","thumbUrl":"","skuId":"3d27405f76a2466f9d47715eea1bf385","skuName":"0299   102g*4块bofayalo84消毒肥皂 强效去渍 去污不伤手","price":1590,"marketPrice":2900,"retailPrice":1790,"stock":0,"quantity":999,"properties":"","status":1}]
     */

    private String storeId;
    private String storeName;
    private List<SkuProductListBean> skuProductList;

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

    public List<SkuProductListBean> getSkuProductList() {
        return skuProductList;
    }

    public void setSkuProductList(List<SkuProductListBean> skuProductList) {
        this.skuProductList = skuProductList;
    }

    public static class SkuProductListBean {


        /**
         * storeId : 0270f3a9ba0545e3bc62be5ea00ac48d
         * storeName : 喜领
         * productId : 9bb42459744c4090be97db34fee9d0ee
         * productName : test07
         * thumbUrl : http://oss.xilingbm.com/product/2020-02/20200217015752912VJ.jpg
         * skuId : e9219255bfe44217851806c3df49c534
         * skuName : test07
         * price : 1
         * marketPrice : 5699
         * retailPrice : 5555
         * stock : 246
         * quantity : 2
         * properties : 60粒/盒;
         * status : 1
         */

        private String storeId;
        private String storeName;
        private String productId;
        private String productName;
        private String thumbUrl;
        private String skuId;
        private String skuName;
        private double price;
        private int marketPrice;
        private int retailPrice;
        private int stock;
        private int quantity;
        private String properties;
        private int status;

        @Override
        public boolean equals(Object obj) {
            return this.getSkuId().equals(((SkuProductListBean)obj).getSkuId());
        }

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

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getSkuId() {
            return skuId;
        }

        public void setSkuId(String skuId) {
            this.skuId = skuId;
        }

        public String getSkuName() {
            return skuName;
        }

        public void setSkuName(String skuName) {
            this.skuName = skuName;
        }

        public double getPrice() {
            return price/100;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getMarketPrice() {
            return marketPrice;
        }

        public void setMarketPrice(int marketPrice) {
            this.marketPrice = marketPrice;
        }

        public int getRetailPrice() {
            return retailPrice;
        }

        public void setRetailPrice(int retailPrice) {
            this.retailPrice = retailPrice;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getProperties() {
            return properties;
        }

        public void setProperties(String properties) {
            this.properties = properties;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
