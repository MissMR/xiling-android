package com.xiling.ddui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.MessageGroupBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.image.GlideUtils;
import com.xiling.module.community.DateUtils;

/**
 * @auth 逄涛
 * 消息父级Adapter列表
 */
public class MessageGroupAdapter extends BaseQuickAdapter<MessageGroupBean, BaseViewHolder> {
    public MessageGroupAdapter() {
        super(R.layout.item_message_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageGroupBean item) {

        GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_head), item.getImg());
        helper.setText(R.id.tv_badge, item.getNoReadNum());
        helper.setVisible(R.id.tv_badge, !(TextUtils.isEmpty(item.getNoReadNum()) || Integer.valueOf(item.getNoReadNum()) == 0));
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setVisible(R.id.tv_title, true);
            helper.setText(R.id.tv_title, item.getTitle());
        } else {
            helper.setVisible(R.id.tv_title, false);
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setVisible(R.id.tv_message, true);
            helper.setText(R.id.tv_message, item.getMsgTitle());
        } else {
            helper.setVisible(R.id.tv_message, false);
        }

        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setVisible(R.id.tv_time, true);


            if (DateUtils.IsToday( item.getMsgDate())){
                //如果是今天，显示时分
                helper.setText(R.id.tv_time,DateUtils.timeStamp2Date(DateUtils.date2TimeStampLong(item.getMsgDate(),""),"HH:mm"));
            }else{
                //如果是今天，显示时分
                helper.setText(R.id.tv_time,DateUtils.timeStamp2Date(DateUtils.date2TimeStampLong(item.getMsgDate(),""),"yyyy-MM-dd"));
            }


        } else {
            helper.setVisible(R.id.tv_time, false);
        }
    }


}
