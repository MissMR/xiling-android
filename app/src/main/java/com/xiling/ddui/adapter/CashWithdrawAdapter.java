package com.xiling.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.CashWithdrawRecordBean;
import com.xiling.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2018/9/12
 * 提现记录 adapter
 */
public class CashWithdrawAdapter extends BaseQuickAdapter<CashWithdrawRecordBean, BaseViewHolder> {
    public CashWithdrawAdapter() {
        super(R.layout.item_cash_withdraw);
    }

    @Override
    protected void convert(BaseViewHolder helper, CashWithdrawRecordBean item) {
        helper.setText(R.id.tv_title, item.getAuditStatusStr());
        helper.setText(R.id.tv_sub_title, item.getApplyTime());
        helper.setText(R.id.tv_balance, "剩余可用 " + ConvertUtil.cent2yuan(item.getAfterWithdrawalBlance()));
        helper.setText(R.id.tv_money, ConvertUtil.centToCurrency(mContext, item.getWithdrawalAmount()));
    }
}
