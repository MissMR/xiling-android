package com.xiling.ddui.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.image.GlideUtils;

public class MineServiceAdapter extends BaseQuickAdapter<MineServiceAdapter.ServiceBean,BaseViewHolder> {


    public MineServiceAdapter() {
        super(R.layout.item_mine_service);
    }

    @Override
    protected void convert(BaseViewHolder helper, ServiceBean item) {
        helper.setBackgroundRes(R.id.iv_icon,item.url);
        helper.setText(R.id.tv_title,item.title);
    }


    public static class ServiceBean{
        int url;
        String title;

        public ServiceBean(int url, String title) {
            this.url = url;
            this.title = title;
        }
    }

}
