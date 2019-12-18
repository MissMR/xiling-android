package com.xiling.ddmall.module.NearStore.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.NearStore.model.NearStoreModel;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/19.
 */
public class NearStoreAdapter extends BaseQuickAdapter<NearStoreModel.DatasEntity, BaseViewHolder> {

    public NearStoreAdapter(@Nullable List<NearStoreModel.DatasEntity> data) {
        super(R.layout.item_near_store, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NearStoreModel.DatasEntity item) {
        helper.setText(R.id.tvName, item.nickName);
        helper.setText(R.id.tvLocation, item.getLocationStr());
        helper.setText(R.id.tvDistancen, item.getDistanceStr());
        FrescoUtil.loadRvItemImg(helper, R.id.ivAvatar, item.headImage);
    }
}
