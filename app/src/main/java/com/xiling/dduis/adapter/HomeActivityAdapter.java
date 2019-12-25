package com.xiling.dduis.adapter;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.image.GlideUtils;

import java.util.List;

public class HomeActivityAdapter extends BaseQuickAdapter<HomeDataBean.ActivityListBean  , BaseViewHolder> {

    public HomeActivityAdapter(int layoutResId, @Nullable List<HomeDataBean.ActivityListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeDataBean.ActivityListBean item) {
        int width;
        if (helper.getAdapterPosition() == 0){
            width = (ScreenUtils.getScreenWidth((Activity) mContext)-ScreenUtils.dip2px(mContext,24))/2;
        }else{
            width =(ScreenUtils.getScreenWidth((Activity) mContext)-ScreenUtils.dip2px(mContext,24))/4;
        }

        ViewGroup.LayoutParams params = helper.getView(R.id.rootView).getLayoutParams();
        params.width = width;
        helper.getView(R.id.rootView).setLayoutParams(params);
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getImgUrl());
    }





}
