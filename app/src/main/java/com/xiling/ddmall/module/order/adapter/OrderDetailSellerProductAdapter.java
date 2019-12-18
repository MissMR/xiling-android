package com.xiling.ddmall.module.order.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.OrderProduct;
import com.xiling.ddmall.shared.util.ConvertUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/27.
 */
public class OrderDetailSellerProductAdapter extends BaseQuickAdapter<OrderProduct, BaseViewHolder> {

    private Context mContext;
    private int mModel;

    public OrderDetailSellerProductAdapter(Context context, @Nullable List<OrderProduct> data) {
        super(R.layout.item_order_detail_seller_product, data);
        mContext = context;
    }

    /**
     *
     * @param model 1：买家的一些东西
     */
    public void setModel(int model) {
        mModel = model;
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderProduct item) {
        helper.setText(R.id.tvName, item.name);
        helper.setText(R.id.tvQuantity, "x" + item.quantity);
        String totlaMoney ;
        String prict ;
        if (mModel == 1) {
            prict = ConvertUtil.centToCurrencyNoZero(mContext, item.realPrice);
            totlaMoney = ConvertUtil.centToCurrencyNoZero(mContext, item.realPrice * item.quantity);
        } else {
            prict = ConvertUtil.centToCurrencyNoZero(mContext, item.price);
            totlaMoney = ConvertUtil.centToCurrencyNoZero(mContext, item.lineTotal);
        }
        helper.setText(R.id.tvTotlaMoney, ("小计:" + totlaMoney));
        helper.setText(R.id.tvPrice, prict);
    }
}
