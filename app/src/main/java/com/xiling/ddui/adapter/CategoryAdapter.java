package com.xiling.ddui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.tools.DLog;
import com.xiling.image.GlideUtils;
import com.xiling.shared.util.ConvertUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Collection;

/**
 * created by Jigsaw at 2018/12/10
 * 右侧分类 适配器
 */
public class CategoryAdapter extends BaseQuickAdapter<SecondCategoryBean.SecondCategoryListBean, BaseViewHolder> {

    public CategoryAdapter() {
        super(R.layout.item_category);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SecondCategoryBean.SecondCategoryListBean item) {
        helper.setText(R.id.tv_title,item.getCategoryName());
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getIconUrl());
    }



}
