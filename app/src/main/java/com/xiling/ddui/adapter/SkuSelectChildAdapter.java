package com.xiling.ddui.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;

import java.util.List;

public class SkuSelectChildAdapter extends BaseQuickAdapter<ProductNewBean.PropertiesBean.PropertyValuesBean, BaseViewHolder> {


    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
        notifyDataSetChanged();
    }

    String selectId = "";

    public SkuSelectChildAdapter(int layoutResId, @Nullable List<ProductNewBean.PropertiesBean.PropertyValuesBean> data) {
        super(layoutResId, data);


    }

    @Override
    protected void convert(BaseViewHolder helper, ProductNewBean.PropertiesBean.PropertyValuesBean item) {
        helper.setText(R.id.tv_name, item.getPropertyValue());
        if (item.isNeedSelect()){
            if (!TextUtils.isEmpty(item.getPropertyValueId())){
                if (selectId.equals(item.getPropertyValueId())) {
                    helper.setBackgroundRes(R.id.tv_name, R.drawable.bg_sku_select);
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#DCB982"));
                } else {
                    helper.setBackgroundRes(R.id.tv_name, R.drawable.bg_sku_unselect);
                    helper.setTextColor(R.id.tv_name, Color.parseColor("#202020"));
                }
            }
        }else{
            helper.setTextColor(R.id.tv_name, Color.parseColor("#D7D7D7"));
        }

    }
}
