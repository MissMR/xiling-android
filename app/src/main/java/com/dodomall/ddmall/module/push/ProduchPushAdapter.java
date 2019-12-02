package com.dodomall.ddmall.module.push;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.component.TagTextView;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
public class ProduchPushAdapter extends BaseQuickAdapter<SkuInfo, BaseViewHolder> {

    private Context mContext;

    public ProduchPushAdapter(@Nullable List<SkuInfo> data, Context context) {
        super(R.layout.item_product_push, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuInfo item) {
        TagTextView titleView = helper.getView(R.id.itemTitleTv);
        titleView.setText(item.name);
        titleView.setTags(item.tags);
        FrescoUtil.loadRvItemImg(helper, R.id.itemThumbIv, item.thumb);
        helper.setText(R.id.itemPriceTv, "会员价:" + ConvertUtil.centToCurrencyNoZero(mContext, item.retailPrice));
        helper.setText(R.id.tvGuige, item.spec);
        helper.setText(R.id.itemSalesTv, "销量:" + item.sales);
        helper.setText(R.id.tvSharePrice, "分享赚 " + (item.minPrice / 100) + "~" + (item.maxPrice / 100) + "元");
        helper.setText(R.id.itemVipPriceTv, item.userTypeStr + "价:" + ConvertUtil.centToCurrencyNoZero(mContext, item.currentVipTypePrice));
    }
}
