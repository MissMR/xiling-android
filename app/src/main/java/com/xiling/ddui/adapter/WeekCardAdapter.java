package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.WeekCardBean;

import java.util.List;

/**
 * 周卡包列表
 */
public class WeekCardAdapter extends BaseQuickAdapter<WeekCardBean, BaseViewHolder> {
    public void setOnOpeningListener(OnOpeningListener onOpeningListener) {
        this.onOpeningListener = onOpeningListener;
    }

    OnOpeningListener onOpeningListener;

    public WeekCardAdapter() {
        super(R.layout.item_week_card);
    }

    @Override
    protected void convert(BaseViewHolder helper, final WeekCardBean item) {
        if (item.getWeekType() == 1) {
            helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_vip);
        } else if (item.getWeekType() == 2) {
            helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_black);
        }
        helper.setText(R.id.tv_week_card_name, item.getWeekName());
        helper.setText(R.id.tv_week_remark, item.getWeekRemark());
        helper.setText(R.id.tv_week_price, item.getPrice() + "");

        helper.setOnClickListener(R.id.btn_Opening, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onOpeningListener != null) {
                    onOpeningListener.onOpen(item);
                }
            }
        });

        //如果未使用，显示 立即开通按钮，否则隐藏
        helper.setVisible(R.id.btn_Opening, item.getUseStatus() == 1);

        switch (item.getUseStatus()){
            case 1:
                //未使用
                break;
            case 2:
                //已使用
                helper.setBackgroundRes(R.id.bg_status,R.drawable.bg_already_used);
                break;
            case 3:
                //已失效
                helper.setBackgroundRes(R.id.bg_status,R.drawable.bg_failure);
                break;
        }





    }

    public interface OnOpeningListener {
        void onOpen(WeekCardBean weekCardBean);
    }

}
