package com.xiling.ddmall.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.MessageGroupBean;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2019/1/8
 */
public class MessageGroupAdapter extends BaseQuickAdapter<MessageGroupBean, BaseViewHolder> {
    public MessageGroupAdapter() {
        super(R.layout.item_message_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageGroupBean item) {

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_sub_title, item.getMsgTitle());

        helper.setText(R.id.tv_badge, String.valueOf(item.getNoReadNum()));
        helper.setVisible(R.id.tv_badge, item.getNoReadNum() > 0);

        ((SimpleDraweeView) helper.getView(R.id.sdv_img)).setImageURI(item.getImg());

    }


}
