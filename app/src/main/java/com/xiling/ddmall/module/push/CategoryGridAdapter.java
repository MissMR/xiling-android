package com.xiling.ddmall.module.push;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.Category;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
@Deprecated
public class CategoryGridAdapter extends BaseQuickAdapter<Category, BaseViewHolder> {

    public CategoryGridAdapter(@Nullable List<Category> data) {
        super(R.layout.item_category_grid, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.tvCategory, item.name);
    }
}
