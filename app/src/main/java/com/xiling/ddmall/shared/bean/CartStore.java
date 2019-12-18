package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.util.SessionUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-06-09
 */
public class CartStore {

    @SerializedName("storeId")
    public String id;
    @SerializedName("storeName")
    public String name;
    @SerializedName("skuProductList")
    public List<CartItem> products = new ArrayList<>();
    public List<Coupon> mCoupons;

    public boolean isSelected() {
        if (products == null) {
            return false;
        }
        boolean isSelected = true;
        for (CartItem product : products) {
            if (!product.isSelected) {
                isSelected = false;
            }
        }

        return isSelected;
    }

    public long getTotal() {
        long total = 0L;
        if (products == null) {
            return total;
        }
        User loginUser = SessionUtil.getInstance().getLoginUser();
        for (CartItem product : products) {
            if (loginUser != null) {
                total += product.retailPrice * product.amount;
            }else {
                total += product.marketPrice * product.amount;
            }
        }
        return total;
    }
}
