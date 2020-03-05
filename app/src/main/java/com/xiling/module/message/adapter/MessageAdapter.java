package com.xiling.module.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.XLMessageBean;
import com.xiling.ddui.manager.MessageManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Message;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends BaseAdapter<XLMessageBean, MessageAdapter.MessageViewHolder> {

    private MessageManager mMessageManager;

    public MessageAdapter(Context context) {
        super(context);
        mMessageManager = MessageManager.newInstance(context, null);
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MessageViewHolder(layoutInflater.inflate(R.layout.item_message_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        final XLMessageBean message = items.get(position);

        holder.itemTitleTv.setText(message.getTitle());
        holder.itemTimeTv.setText(message.getCreateDate());
        if (!TextUtils.isEmpty(message.getImage())) {
            holder.itemThumbIv.setVisibility(View.VISIBLE);
            GlideUtils.loadImage(context, holder.itemThumbIv, message.getImage());
        } else {
            holder.itemThumbIv.setVisibility(View.GONE);
        }
        holder.itemContentTv.setText(message.getContent());

        if (position == items.size() - 1) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            int margin = ConvertUtil.dip2px(10);
            layoutParams.setMargins(0, 0, 0, margin);
            holder.itemView.setLayoutParams(layoutParams);
        }
    }


    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitleTv)
        protected TextView itemTitleTv;
        @BindView(R.id.itemTimeTv)
        protected TextView itemTimeTv;
        @BindView(R.id.iv_head)
        protected ImageView itemThumbIv;
        @BindView(R.id.itemContentTv)
        protected TextView itemContentTv;

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
