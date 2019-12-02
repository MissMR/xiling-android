package com.dodomall.ddmall.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.CategoryBean;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * created by Jigsaw at 2018/12/12
 * 分类九宫格
 */
public class CategoryGridAdapter extends BaseQuickAdapter<CategoryBean, BaseViewHolder> {
    public CategoryGridAdapter(List<CategoryBean> categoryList) {
        super(R.layout.item_category_grid_item, categoryList);
    }

    @Override
    protected void convert(BaseViewHolder helper, CategoryBean item) {
        if (item == null) {
            helper.setVisible(R.id.item_root, false);
            return;
        }

        helper.setVisible(R.id.item_root, true);
        helper.setText(R.id.tv_category, item.getCategoryNameShort());
        DLog.i("=>" + item.getCategoryName() + ":" + item.getIconUrl());
        ((SimpleDraweeView) helper.getView(R.id.sdv_img)).setImageURI(item.getIconUrl());
    }
}
