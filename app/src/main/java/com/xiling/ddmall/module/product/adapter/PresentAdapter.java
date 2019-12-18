package com.xiling.ddmall.module.product.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.Presents;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.ArrayList;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/30.
 */
public class PresentAdapter extends BaseQuickAdapter<Presents,BaseViewHolder>{

    public PresentAdapter(@Nullable ArrayList<Presents> data) {
        super(R.layout.item_pressents_dialog,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Presents item) {
        SimpleDraweeView ivAvatar = helper.getView(R.id.ivAvatar);
        FrescoUtil.setImageSmall(ivAvatar,item.thumbUrl);
        helper.setText(R.id.tvName,item.skuName);
        helper.setText(R.id.tvCount,"x"+item.quantity);
    }
}
