package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.ProductParameterBean;

import java.util.List;

public class SpuParameterAdapter extends BaseQuickAdapter<ProductParameterBean, BaseViewHolder> {

    public SpuParameterAdapter() {
        super(R.layout.item_spu_parameter);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductParameterBean item) {
        helper.setText(R.id.tv_title, item.getParameterName());
        helper.setText(R.id.tv_value, item.getParameterValue());
    }
}
