package com.xiling.ddui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.image.GlideUtils;

/**
 * 赚成长值
 */
public class MackAdapter extends BaseQuickAdapter<MackAdapter.MackBean, BaseViewHolder> {


    public MackAdapter() {
        super(R.layout.item_mack);
    }

    @Override
    protected void convert(BaseViewHolder helper, MackBean item) {
        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head), item.iconRes);
        helper.setText(R.id.tv_title, item.title);
        if (!TextUtils.isEmpty(item.describe)){
            helper.setVisible(R.id.tv_describe,true);
            helper.setText(R.id.tv_describe, item.describe);
        }else{
            helper.setVisible(R.id.tv_describe,false);
        }
        helper.setText(R.id.btn_go, item.buttomName);
    }

    public static class MackBean {
        int iconRes;
        String title;
        String describe;
        String buttomName;

        public MackBean(int iconRes, String title, String describe, String buttomName) {
            this.iconRes = iconRes;
            this.title = title;
            this.describe = describe;
            this.buttomName = buttomName;
        }
    }
}
