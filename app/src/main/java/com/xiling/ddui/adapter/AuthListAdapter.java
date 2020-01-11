package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.RealAuthBean;

import java.util.List;

/**
 * 实名认证列表
 */
public class AuthListAdapter extends BaseQuickAdapter<RealAuthBean, BaseViewHolder> {
    public AuthListAdapter( @Nullable List<RealAuthBean> data) {
        super(R.layout.item_auth, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RealAuthBean item) {
        helper.setText(R.id.tv_name,item.getUserName());
        helper.setText(R.id.tv_id,item.getIdentityCard());
    }
}
