package com.xiling.ddui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;

import java.util.List;

public class TagFlowAdapter extends BaseQuickAdapter<ProductNewBean.SkusBean, BaseViewHolder> {

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    int selectPosition = 0;

    public TagFlowAdapter(int layoutResId, @Nullable List<ProductNewBean.SkusBean> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, ProductNewBean.SkusBean item) {

        helper.setText(R.id.tv_name, item.getPropertyValues());
        if (helper.getAdapterPosition() == selectPosition) {
            helper.setBackgroundRes(R.id.tv_name, R.drawable.bg_sku_select);
            helper.setTextColor(R.id.tv_name, Color.parseColor("#DCB982"));
        } else {
            helper.setBackgroundRes(R.id.tv_name, R.drawable.bg_sku_unselect);
            helper.setTextColor(R.id.tv_name, Color.parseColor("#202020"));
        }
    }
}
