package com.dodomall.ddmall.module.user.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.VipTypeInfo;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/1/19.
 */
public class VipTypeMenuAdapter extends BaseQuickAdapter<VipTypeInfo,BaseViewHolder>{

    private int mSelectPosition;

    public VipTypeMenuAdapter(@Nullable List<VipTypeInfo> data) {
        super(R.layout.item_simple_text,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VipTypeInfo item) {
        TextView textView = helper.getView(R.id.text);
        if (helper.getLayoutPosition() == getData().size() - 1) {
            textView.setBackgroundResource(R.drawable.bg_common_white_normal);
        }
        if (mSelectPosition == helper.getLayoutPosition()) {
            textView.setTextColor(Color.parseColor("#f51861"));
        } else {
            textView.setTextColor(Color.parseColor("#333333"));
        }
        textView.setText(item.vipTypeStr);
    }

    public void setSelectPosition(int selectPosition) {
        mSelectPosition = selectPosition;
    }
}
