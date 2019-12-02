package com.dodomall.ddmall.module.publish;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.community.GroupCategoryModel;

/**
 * @author Stone
 * @time 2018/4/13  11:43
 * @desc ${TODD}
 */

public class CateGoryAdapter extends BaseQuickAdapter<GroupCategoryModel,BaseViewHolder> {
    public CateGoryAdapter() {
        super(R.layout.item_category_layout, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupCategoryModel item) {
        helper.setText(R.id.item_title_tv,item.getName());
        helper.getView(R.id.item_check_iv).setSelected(item.isCheck());
    }
}
