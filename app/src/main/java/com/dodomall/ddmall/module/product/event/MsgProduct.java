package com.dodomall.ddmall.module.product.event;

import com.dodomall.ddmall.module.auth.event.BaseMsg;
import com.dodomall.ddmall.shared.bean.SkuInfo;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/4.
 */
public class MsgProduct extends BaseMsg{

    public static final int DEL_VIEW_HOSTORY = 1<<0;
    private SkuInfo mSkuInfo;

    public MsgProduct(int action) {
        super(action);
    }


    public void setSkuInfo(SkuInfo skuInfo) {
        mSkuInfo = skuInfo;
    }

    public SkuInfo getSkuInfo() {
        return mSkuInfo;
    }
}
