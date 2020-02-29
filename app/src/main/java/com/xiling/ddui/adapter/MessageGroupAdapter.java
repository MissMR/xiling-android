package com.xiling.ddui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.MessageGroupBean;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.image.GlideUtils;

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
        helper.setText(R.id.tv_badge, item.getNoReadNum());
        helper.setVisible(R.id.tv_badge, !(TextUtils.isEmpty(item.getNoReadNum()) || Integer.valueOf(item.getNoReadNum()) == 0));
        GlideUtils.loadHead(mContext, (ImageView) helper.getView(R.id.sdv_img), item.getImg());
        helper.setVisible(R.id.fgx, helper.getLayoutPosition() == 1);
    }


}
