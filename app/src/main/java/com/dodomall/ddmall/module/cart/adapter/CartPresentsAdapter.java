package com.dodomall.ddmall.module.cart.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.SkuInfo;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/31.
 */
public class CartPresentsAdapter extends BaseQuickAdapter<SkuInfo, BaseViewHolder> {

    public CartPresentsAdapter(@Nullable List<SkuInfo> data) {
        super(R.layout.item_cart_presents, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuInfo item) {
        helper.setText(R.id.tvName, item.name);
        helper.setText(R.id.tvQuantity, "x" + item.quantity);
    }
}
