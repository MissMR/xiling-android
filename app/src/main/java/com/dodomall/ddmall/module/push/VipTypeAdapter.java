package com.dodomall.ddmall.module.push;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/22.
 */
public class VipTypeAdapter extends BaseQuickAdapter<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity, BaseViewHolder> {

    public VipTypeAdapter(@Nullable List<PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity> data) {
        super(R.layout.item_vip_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PushSkuDetailModel.ApiSkuVipTypePriceBeansEntity item) {
        TextView tvVipType = helper.getView(R.id.tvVipType);
        tvVipType.setText(String.format("%s：¥%s", item.vipTypeStr, ConvertUtil.cent2yuanNoZero(item.price)));

        int resId;
        switch (item.vipType) {
            case AppTypes.FAMILY.MEMBER_NORMAL:
                resId = R.drawable.ic_push_vip0;
                break;
            case AppTypes.FAMILY.MEMBER_ZUNXIANG:
                resId = R.drawable.ic_push_vip1;
                break;
            case AppTypes.FAMILY.MEMBER_TIYAN:
                resId = R.drawable.ic_push_vip2;
                break;
            case AppTypes.FAMILY.MEMBER_ZHUANYING:
                resId = R.drawable.ic_push_vip3;
                break;
            default:
                resId = R.drawable.ic_push_vip0;
                break;
        }
        Drawable drawable = tvVipType.getContext().getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tvVipType.setCompoundDrawables(drawable, null, null, null);
    }
}
