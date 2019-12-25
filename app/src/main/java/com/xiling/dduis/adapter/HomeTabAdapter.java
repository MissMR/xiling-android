package com.xiling.dduis.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.image.GlideUtils;

import java.util.List;

public class HomeTabAdapter extends BaseQuickAdapter<HomeDataBean.TabListBean  , BaseViewHolder> {

    public HomeTabAdapter(int layoutResId, @Nullable List<HomeDataBean.TabListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean.TabListBean item) {
        int width = (ScreenUtils.getScreenWidth((Activity) mContext)-ScreenUtils.dip2px(mContext,24))/4;
        ViewGroup.LayoutParams params = helper.getView(R.id.rootView).getLayoutParams();
        params.width = width;
        helper.getView(R.id.rootView).setLayoutParams(params);

        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getImgUrl());
        helper.setText(R.id.tv_title,item.getTitle());
    }





}
