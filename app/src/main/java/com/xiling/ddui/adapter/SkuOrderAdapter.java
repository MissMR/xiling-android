package com.xiling.ddui.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.DetailsBean;
import com.xiling.ddui.bean.MyOrderDetailBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.image.GlideUtils;

public class SkuOrderAdapter extends BaseQuickAdapter<DetailsBean, BaseViewHolder> {


    public SkuOrderAdapter() {
        super(R.layout.item_sku);
    }

    @Override
    protected void convert(BaseViewHolder helper, DetailsBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getProductImage());
        helper.setText(R.id.tv_product_name, item.getSkuName());
        helper.setText(R.id.tv_product_des, item.getSkuName());
        helper.setText(R.id.tv_quantity,"x "+item.getQuantity());
        helper.setText(R.id.tv_price, "¥" + NumberHandler.reservedDecimalFor2(item.getRetailPrice()));
        NumberHandler.setPriceText(item.getPrice(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));


    }
}
