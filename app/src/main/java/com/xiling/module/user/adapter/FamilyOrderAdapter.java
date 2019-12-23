package com.xiling.module.user.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.bean.FamilyOrder;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class FamilyOrderAdapter extends BaseQuickAdapter<FamilyOrder.DatasEntity, BaseViewHolder> {

    public FamilyOrderAdapter(@Nullable List<FamilyOrder.DatasEntity> data) {
        super(R.layout.item_family_order, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FamilyOrder.DatasEntity item) {
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.ivAvatar);
        FrescoUtil.setImageSmall(simpleDraweeView,item.headImage);
        helper.setText(R.id.tvName, item.nickName+"("+item.phone+")");
        helper.setText(R.id.tvMoney, ConvertUtil.centToCurrency(helper.itemView.getContext(),item.payMoney));
        helper.setText(R.id.tvDate, item.payDate);
    }
}
