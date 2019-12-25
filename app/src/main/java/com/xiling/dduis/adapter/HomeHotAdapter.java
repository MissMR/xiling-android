package com.xiling.dduis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;

import java.util.List;

public class HomeHotAdapter extends BaseQuickAdapter<HomeDataBean.SecondCategoryListBean, BaseViewHolder> {

    public HomeHotAdapter(int layoutResId, @Nullable List<HomeDataBean.SecondCategoryListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean.SecondCategoryListBean item) {
        if (!TextUtils.isEmpty(item.getCategoryName())){
            helper.setText(R.id.tv_title,item.getCategoryName());
        }
    }
}
