package com.xiling.ddmall.module.publish;

import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;

import java.util.ArrayList;

/**
 * @author Stone
 * @time 2018/4/16  11:36
 * @desc ${TODD}
 */

public class HisQuickAdapter extends BaseQuickAdapter<PublishHisModule,BaseViewHolder> {
    HisQuickAdapter() {
        super(R.layout.item_history_publish);
    }
    @Override
    protected void convert(BaseViewHolder helper, PublishHisModule item) {
        TextView itemStatusTv = helper.getView(R.id.item_order_status_tv);
        BlockView gridView=helper.getView(R.id.item_image_gv);
        itemStatusTv.setText(item.getStatusStr());
        if(item.getStatus()==0) {
            gridView.setRetryCommit(false);
            itemStatusTv.setTextColor(ContextCompat.getColor(mContext,R.color.color_66));
        }else if(item.getStatus()==1) {
            gridView.setRetryCommit(false);
            itemStatusTv.setTextColor(ContextCompat.getColor(mContext,R.color.color_ac87_90));
        }else {
            gridView.setRetryCommit(true);
            itemStatusTv.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        }
        helper.setText(R.id.item_content_iv,item.getContent());

        int type = item.getType();

        ArrayList<String> mediaImgs = new ArrayList<>();
        if(type==1) {
            helper.setText(R.id.item_hour_num_tv,item.getUpdateDate()+"  共"+item.getImages().size()+"张");
           if(item.getImages().size()>4) {
               mediaImgs.addAll(item.getImages().subList(0,4));
           }else {
               mediaImgs.addAll(item.getImages());
           }
        }else {
            helper.setText(R.id.item_hour_num_tv,item.getUpdateDate());
            mediaImgs.add(item.getMediaImage());
        }
        gridView.setIsVideo(type==2);
        gridView.setImages(mediaImgs);
    }
}
