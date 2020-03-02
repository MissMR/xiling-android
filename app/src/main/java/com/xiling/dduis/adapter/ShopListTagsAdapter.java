package com.xiling.dduis.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.dduis.bean.HomeDataBean;

import java.util.List;

/**
 * pt
 * 商品标签
 */
public class ShopListTagsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public ShopListTagsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (!TextUtils.isEmpty(item)){
            helper.setText(R.id.tv_title,item);
        }
    }

}
