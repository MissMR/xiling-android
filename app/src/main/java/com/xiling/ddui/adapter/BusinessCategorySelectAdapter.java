package com.xiling.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.bean.TopCategoryBean;

public class BusinessCategorySelectAdapter extends BaseQuickAdapter<TopCategoryBean, BaseViewHolder> {
    public BusinessCategorySelectAdapter() {
        super(R.layout.item_platform_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, TopCategoryBean item) {
        helper.setText(R.id.tv_title, item.getCategoryName());
        helper.getView(R.id.tv_title).setEnabled(item.isSelect());
    }
}
