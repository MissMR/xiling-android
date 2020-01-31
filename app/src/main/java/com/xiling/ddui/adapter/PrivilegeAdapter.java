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
public class PrivilegeAdapter extends BaseQuickAdapter<PrivilegeAdapter.PrivilegeBean,BaseViewHolder> {


    public PrivilegeAdapter() {
        super(R.layout.item_privilege);
    }

    @Override
    protected void convert(BaseViewHolder helper, PrivilegeBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head),item.iconRes);
        helper.setText(R.id.tv_title1,item.title1);
        helper.setText(R.id.tv_title2,item.title2);
    }

    public static class PrivilegeBean{
        int iconRes;
        String title1;
        String title2;

        public PrivilegeBean(int iconRes, String title1, String title2) {
            this.iconRes = iconRes;
            this.title1 = title1;
            this.title2 = title2;
        }
    }
}
