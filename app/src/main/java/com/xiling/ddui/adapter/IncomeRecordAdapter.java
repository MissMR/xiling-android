package com.xiling.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.AchievementRecordBean;
import com.xiling.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2018/9/12
 * 收入明细 adapter
 */
public class IncomeRecordAdapter extends BaseQuickAdapter<AchievementRecordBean, BaseViewHolder> {
    public IncomeRecordAdapter() {
        super(R.layout.item_income_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, AchievementRecordBean item) {

        helper.setText(R.id.tv_title, item.getPrizeName());
        helper.setText(R.id.tv_sub_title, item.getSecretPhone());
        helper.setText(R.id.tv_money, "+" + ConvertUtil.centToCurrency(mContext, item.getPrize()));
        helper.setText(R.id.tv_time, item.getCalculationDate());
        helper.getView(R.id.item_root).setBackgroundResource(0 == getItemIndex(item) ? R.drawable.white_top_radius8 : R.color.white);
    }

    private int getItemIndex(AchievementRecordBean item) {
        for (int i = 0; i < getItemCount(); i++) {
            if (item == getItem(i)) {
                return i;
            }
        }
        return -1;
    }

}
