package com.xiling.shared.bean;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstantData {

    @SerializedName("secondKill")
    public SecondKill secondKill;
    @SerializedName("secondKillProducts")
    public List<Product> secondKillProducts;

    public static class SecondKill {
        @SerializedName("secondKillId")
        public String id;
        @SerializedName("title")
        public String title;
        @SerializedName("status")
        public int status;
        @SerializedName("event")
        public String event;
        @SerializedName("target")
        public String target;
        @SerializedName("image")
        public String image;
        @SerializedName("startDate")
        public String startDate;
        @SerializedName("endDate")
        public String endDate;
        @SerializedName("createDate")
        public String createDate;
        @SerializedName("statusStr")
        public String statusStr;
    }

    public static class Product {
        @SerializedName("skuId")
        public String skuId;
        @SerializedName("productId")
        public String productId;
        @SerializedName("skuName")
        public String skuName;
        @SerializedName("intro")
        public String intro;
        @SerializedName("weight")
        public int weight;
        @SerializedName("stock")
        public int stock;
        @SerializedName("retailPrice")
        public long retailPrice;
        @SerializedName("marketPrice")
        public long marketPrice;
        @SerializedName("saleCount")
        public int saleCount;
        @SerializedName("hasPresent")
        public int hasPresent;
        @SerializedName("quantity")
        public int quantity;
        @SerializedName("hasCoupon")
        public int hasCoupon;
        @SerializedName("thumbUrl")
        public String thumbUrl;
        @SerializedName("storeId")
        public String storeId;
        @SerializedName("storeName")
        public String storeName;
        @SerializedName("properties")
        public String properties;
        @SerializedName("sellBegin")
        public String sellBegin;
        @SerializedName("sellEnd")
        public String sellEnd;

        public static Product fromJson(@Nullable String json) {
            if (json == null || json.isEmpty()) {
                return null;
            }
            return new Gson().fromJson(json, Product.class);
        }
    }
}
