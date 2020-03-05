package com.xiling.ddui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.image.GlideUtils;
import com.xiling.shared.constant.AppTypes;

/**
 * 周卡特权
 */
public class PrivilegeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {


    public PrivilegeAdapter() {
        super(R.layout.item_privilege);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int pos = helper.getAdapterPosition()+1;
        helper.setText(R.id.tv_title, "特权" + pos );
        helper.setText(R.id.tv_message, item);
        if (helper.getAdapterPosition() == 0) {
            helper.setVisible(R.id.iv_hot, true);
        } else {
            helper.setVisible(R.id.iv_hot, false);
        }
    }

}
