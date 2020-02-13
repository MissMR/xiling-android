package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.BalanceDetailsBean;
import com.xiling.ddui.tools.NumberHandler;

import java.util.List;

public class BalanceAdapter extends BaseQuickAdapter<BalanceDetailsBean.DataBean, BaseViewHolder> {

    public BalanceAdapter() {
        super(R.layout.item_balance_details);
    }

    @Override
    protected void convert(BaseViewHolder helper, BalanceDetailsBean.DataBean item) {
        helper.setText(R.id.tv_name, item.getTypeContent());
        helper.setText(R.id.tv_time, item.getOperatingDate());
        helper.setText(R.id.tv_price,item.getChangeValue() > 0 ? "+"+ NumberHandler.reservedDecimalFor2(item.getChangeValue()/100) : NumberHandler.reservedDecimalFor2(item.getChangeValue()/100));
        helper.setText(R.id.tv_status, item.getChangeValue() > 0 ? "收益" : "支出");
    }
}
