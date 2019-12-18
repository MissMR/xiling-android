package com.xiling.ddmall.module.lottery.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.LotteryWinner;
import com.xiling.ddmall.shared.util.FrescoUtil;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/13.
 */
public class WinnerAdapter extends BaseQuickAdapter<LotteryWinner, BaseViewHolder> {

    public WinnerAdapter(@Nullable List<LotteryWinner> data) {
        super(R.layout.item_winner, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LotteryWinner item) {
        FrescoUtil.loadRvItemImg(helper, R.id.ivPrize, item.prizeImg);
        helper.setText(R.id.tvPrizeName, item.prizeName);
        helper.setText(R.id.tvDate, "获奖时间：" + item.createDate);
        helper.setText(R.id.tvStatus, item.statusStr);
        helper.getView(R.id.tvStatus).setSelected(item.status != 1);
    }
}
