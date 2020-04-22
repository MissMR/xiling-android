package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.image.GlideUtils;

import java.util.List;

/**
 * 实名认证列表
 */
public class AuthListAdapter extends BaseQuickAdapter<RealAuthBean, BaseViewHolder> {
    public AuthListAdapter(@Nullable List<RealAuthBean> data) {
        super(R.layout.item_auth, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RealAuthBean item) {

        switch (item.getStoreType()) {
            case "1":
                helper.setText(R.id.tv_name, "网络店铺");
                GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head), R.drawable.auth_head_net);
                break;
            case "2":
                helper.setText(R.id.tv_name, "实体门店");
                GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head), R.drawable.auth_head_entity);
                break;
            case "3":
                helper.setText(R.id.tv_name, "微商代购");
                GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head), R.drawable.auth_head_wechat);
                break;
        }

    }
}
