package com.xiling.ddui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.image.GlideUtils;

/**
 * created by Jigsaw at 2018/12/10
 * 右侧分类 品牌
 */
public class CategoryBrandAdapter extends BaseQuickAdapter<SecondCategoryBean.BrandBeanListBean, BaseViewHolder> {

    public CategoryBrandAdapter() {
        super(R.layout.item_category_brand);
    }

    @Override
    protected void convert(BaseViewHolder helper, final SecondCategoryBean.BrandBeanListBean item) {
        helper.setText(R.id.tv_title,item.getBrandName());
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon),item.getCategoryUrl());
    }



}
