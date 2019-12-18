package com.xiling.ddmall.module.user.adapter;

import android.support.annotation.Nullable;

import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.Family;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class FamilyAdapter extends BaseQuickAdapter<Family.DatasEntity, BaseViewHolder> {

    public FamilyAdapter(@Nullable List<Family.DatasEntity> data) {
        super(R.layout.item_family, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Family.DatasEntity item) {
        SimpleDraweeView simpleDraweeView = helper.getView(R.id.ivAvatar);
        FrescoUtil.setImageSmall(simpleDraweeView, item.headImage);
        helper.setText(R.id.tvName, item.nickName+"("+item.phone+")");
        if (StringUtils.isEmpty(item.memberStr)) {
            helper.setVisible(R.id.tvTag, false);
        } else {
            helper.setText(R.id.tvTag, item.memberStr);
        }
        helper.setText(R.id.tvMoney, ConvertUtil.centToCurrency(helper.itemView.getContext(), item.currMonthSaleMoney));
    }
}
