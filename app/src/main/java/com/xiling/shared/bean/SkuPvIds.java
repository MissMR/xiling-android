package com.xiling.shared.bean;

import com.google.common.collect.Sets;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;


public class SkuPvIds {
    @SerializedName("skuId")
    public String skuId;
    // 规格分类Id
    @SerializedName("propertyIds")
    public String propertyIds;
    // sku对应的 规格值id组合
    @SerializedName("propertyValueIds")
    public String propertyValueIds;
    // 180g/盒
    @SerializedName("propertyValues")
    public String propertyValues;
    // 库存
    @SerializedName("stock")
    public int stock;

    @SerializedName("thumbUrlForShopNow")
    public String thumbUrlForShopNow;

    // 零售价
    @SerializedName("retailPrice")
    public long retailPrice;
    // 市场价
    @SerializedName("marketPrice")
    public long marketPrice;
    // 返利值
    @SerializedName("rewardPrice")
    public long rewardPrice;

    private List<String> propertyValueIdList;

    public boolean isMatch(String ids) {
        return Sets.difference(new HashSet<>(Collections.singletonList(propertyValueIds)), new HashSet<>(Collections.singleton(ids))).isEmpty();
    }

    public boolean contains(String id) {
        return getPropertyValueIdList().containsAll(Arrays.asList(id.split(",")));
    }

    public boolean contains(List<String> ids) {
        return getPropertyValueIdList().containsAll(ids);
    }

    private List<String> getPropertyValueIdList() {
        if (propertyValueIdList == null) {
            propertyValueIdList = Arrays.asList(propertyValueIds.split(","));
        }
        return propertyValueIdList;
    }

    @Override
    public String toString() {
        return "SkuPvIds{" +
                "skuId='" + skuId + '\'' +
                ", propertyIds='" + propertyIds + '\'' +
                ", propertyValueIds='" + propertyValueIds + '\'' +
                ", propertyValues='" + propertyValues + '\'' +
                ", stock=" + stock +
                ", thumbUrlForShopNow='" + thumbUrlForShopNow + '\'' +
                ", retailPrice=" + retailPrice +
                ", marketPrice=" + marketPrice +
                ", rewardPrice=" + rewardPrice +
                ", propertyValueIdList=" + propertyValueIdList +
                '}';
    }
}
