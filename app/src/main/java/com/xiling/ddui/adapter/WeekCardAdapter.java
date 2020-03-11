package com.xiling.ddui.adapter;

import android.graphics.Color;
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

        helper.setText(R.id.tv_week_card_name, item.getWeekName());
        helper.setText(R.id.tv_week_remark, item.getWeekRemark());
        helper.setText(R.id.tv_week_price, "¥" + item.getPrice() + "");

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
        helper.setVisible(R.id.ll_us, item.getUseStatus() == 2);
        helper.setVisible(R.id.rel_invalid, item.getUseStatus() == 3);
        switch (item.getUseStatus()) {
            case 1:
                //未使用
                if (item.getWeekType() == 1) {
                    helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_vip);
                    helper.setTextColor(R.id.tv_experience, Color.parseColor("#6D8891"));
                    helper.setTextColor(R.id.btn_Opening, Color.parseColor("#6D8891"));
                } else if (item.getWeekType() == 2) {
                    helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_black);
                    helper.setTextColor(R.id.tv_experience, Color.parseColor("#B68B2A"));
                    helper.setTextColor(R.id.btn_Opening, Color.parseColor("#B68B2A"));
                }
                break;
            case 2:
                //已使用
                if (item.getWeekType() == 1) {
                    helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_vip);
                    helper.setTextColor(R.id.tv_experience, Color.parseColor("#6D8891"));
                    helper.setTextColor(R.id.btn_Opening, Color.parseColor("#6D8891"));
                    helper.setBackgroundRes(R.id.iv_us, R.drawable.icon_us_vip);
                    helper.setTextColor(R.id.tv_us, Color.parseColor("#6D8891"));
                } else if (item.getWeekType() == 2) {
                    helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_member_week_card_black);
                    helper.setTextColor(R.id.tv_experience, Color.parseColor("#B68B2A"));
                    helper.setTextColor(R.id.btn_Opening, Color.parseColor("#B68B2A"));
                    helper.setBackgroundRes(R.id.iv_us, R.drawable.icon_us_black);
                    helper.setTextColor(R.id.tv_us, Color.parseColor("#B68B2A"));
                }
                break;
            case 3:
                //已失效
                helper.setBackgroundRes(R.id.rel_week_card_backgroud, R.drawable.bg_week_card_invalid);
                helper.setTextColor(R.id.tv_week_card_name, Color.parseColor("#666666"));
                helper.setTextColor(R.id.tv_experience, Color.parseColor("#666666"));
                helper.setTextColor(R.id.tv_week_price, Color.parseColor("#666666"));

                break;
        }


    }

    public interface OnOpeningListener {
        void onOpen(WeekCardBean weekCardBean);
    }

}
