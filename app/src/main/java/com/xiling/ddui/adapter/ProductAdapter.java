package com.xiling.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.dduis.bean.DDProductBean;
import com.xiling.dduis.viewholder.SpuProductViewHolder;

/**
 * created by Jigsaw at 2019/1/23
 * 商品列表adapter
 */
public class ProductAdapter extends BaseQuickAdapter<DDProductBean, SpuProductViewHolder> {
    public ProductAdapter() {
        super(R.layout.layout_home_category_data);
    }

    @Override
    protected void convert(SpuProductViewHolder helper, DDProductBean item) {
        helper.setData(item);
        helper.render();
    }
}
