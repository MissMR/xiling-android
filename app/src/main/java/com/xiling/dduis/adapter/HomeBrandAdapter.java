package com.xiling.dduis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.image.GlideUtils;

import java.util.List;

public class HomeBrandAdapter extends BaseQuickAdapter<HomeDataBean.BrandHotSaleListBean, BaseViewHolder> {

    public HomeBrandAdapter(int layoutResId, @Nullable List<HomeDataBean.BrandHotSaleListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean.BrandHotSaleListBean item) {
        if (!TextUtils.isEmpty(item.getImgUrl())){
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getImgUrl());
        }
    }
}
