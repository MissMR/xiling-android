package com.dodomall.ddmall.module.instant.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.component.InstantProgressBar;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class InstantAdapter2 extends BaseQuickAdapter<SkuInfo, BaseViewHolder> {

    public InstantAdapter2(@Nullable List<SkuInfo> data) {
        super(R.layout.item_instant_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SkuInfo item) {
        helper.getView(R.id.layoutRoot).setBackgroundColor(Color.TRANSPARENT);

        FrescoUtil.loadRvItemImg(helper, R.id.ivImg, item.thumb);
        helper.setText(R.id.tvTitle, item.name);
        helper.setText(R.id.tvRetailPrice, ConvertUtil.centToCurrency(helper.getConvertView().getContext(), item.retailPrice));
        helper.setText(R.id.tvMarketPrice, ConvertUtil.centToCurrency(helper.getConvertView().getContext(), item.marketPrice));

        TextView tvMarketPrice = helper.getView(R.id.tvMarketPrice);
        TextViewUtil.addThroughLine(tvMarketPrice);

        long startTime = TimeUtils.string2Millis(item.sellBegin);
        long endTime = TimeUtils.string2Millis(item.sellEnd);
        long currentTime = System.currentTimeMillis();
        TextView tvGo = helper.getView(R.id.tvGo);
        if (currentTime < startTime) {
            tvGo.setText("即将开抢");
            tvGo.setEnabled(true);
        } else if (currentTime > endTime) {
            tvGo.setText("已结束");
            tvGo.setEnabled(false);
            tvGo.setTextColor(Color.WHITE);
        } else {
            tvGo.setEnabled(true);
            tvGo.setText("马上抢");
            helper.addOnClickListener(R.id.tvGo);
        }

        InstantProgressBar progressBar = helper.getView(R.id.progressBar);
        if (item.quantity == 0) {
            progressBar.setProgress(0);
        } else if (item.sales >= item.quantity || item.stock == 0) {
            LogUtils.e("总数" + item.quantity + "  当前销量" + item.sales);
            progressBar.setProgress(100);
//            if (!tvGo.isSelected()) {
            tvGo.setText("抢光了");
            tvGo.setEnabled(false);
            tvGo.setTextColor(Color.WHITE);
//            }
        } else {
            progressBar.setProgress((int) (100 * item.sales / item.quantity));
        }

    }
}
