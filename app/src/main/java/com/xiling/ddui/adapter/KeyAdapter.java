package com.xiling.ddui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;

import java.util.List;

public class KeyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public void setOnKeyClickListener(OnKeyClickListener onKeyClickListener) {
        this.onKeyClickListener = onKeyClickListener;
    }

   OnKeyClickListener onKeyClickListener;

    public KeyAdapter(@Nullable List<String> data) {
        super(R.layout.item_pay_key, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final String item) {
        if (!item.isEmpty()) {
            helper.setVisible(R.id.tv_key, true);
            if (item.equals("返回")) {
                helper.setVisible(R.id.tv_key, false);
                helper.setVisible(R.id.key_delete, true);
            } else {
                helper.setVisible(R.id.tv_key, true);
                helper.setVisible(R.id.key_delete, false);
                helper.setText(R.id.tv_key, item);
            }

        } else {
            helper.setVisible(R.id.tv_key, false);
            helper.setVisible(R.id.key_delete, false);
        }

        helper.setOnClickListener(R.id.tv_key, new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (onKeyClickListener != null) {
                    onKeyClickListener.onKeyClick(item);
                }
            }
        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onKeyClickListener != null) {
                    onKeyClickListener.onKeyClick(item);
                }
            }
        });

    }

    public interface OnKeyClickListener {
        void onKeyClick(String key);
    }



}
