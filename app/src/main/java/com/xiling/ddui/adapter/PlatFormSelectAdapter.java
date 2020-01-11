package com.xiling.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.PlatformBean;

public class PlatFormSelectAdapter extends BaseQuickAdapter<PlatformBean, BaseViewHolder> {
    public PlatFormSelectAdapter() {
        super(R.layout.item_platform_select);
    }

    @Override
    protected void convert(BaseViewHolder helper, PlatformBean item) {
        helper.setText(R.id.tv_title, item.getStoreName());
        helper.getView(R.id.tv_title).setEnabled(item.isSelect());
    }
}
