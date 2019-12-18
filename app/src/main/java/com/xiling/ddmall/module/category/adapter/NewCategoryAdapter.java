package com.xiling.ddmall.module.category.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.Category;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/30.
 */
public class NewCategoryAdapter extends BaseQuickAdapter<Category,BaseViewHolder>{

    public NewCategoryAdapter(@Nullable List<Category> data) {
        super(R.layout.item_category_grid_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Category item) {
        helper.setText(R.id.itemTitleTv,item.name);
        FrescoUtil.loadRvItemImg(helper,R.id.itemThumbIv,item.icon);
    }
}
