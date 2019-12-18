package com.xiling.ddmall.shared.bean.body;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.bean.SkuAmount;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/12/5.
 */
public class CalcOrderCouponListBody {
    /**
     * products : [{"skuId":"3d5727dc23624c8b85aa42b91d1ccc25","quantity":100},{"skuId":"a61107919b07431db677151301ed3441","quantity":1}]
     */
    @SerializedName("products")
    public List<SkuAmount> products;
}
