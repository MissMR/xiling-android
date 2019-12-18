package com.xiling.ddmall.module.groupBuy;

import com.xiling.ddmall.shared.bean.Product;
import com.xiling.ddmall.shared.bean.SkuInfo;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/2.
 */
public class MsgGroupDialog {
    private SkuInfo mSkuInfo;
    private Product mProduct;

    public MsgGroupDialog(SkuInfo skuInfo, Product product) {
        mSkuInfo = skuInfo;
        mProduct = product;
    }

    public SkuInfo getSkuInfo() {
        return mSkuInfo;
    }

    public void setSkuInfo(SkuInfo skuInfo) {
        mSkuInfo = skuInfo;
    }

    public Product getProduct() {
        return mProduct;
    }

    public void setProduct(Product product) {
        mProduct = product;
    }
}
