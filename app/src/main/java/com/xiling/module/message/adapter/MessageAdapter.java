package com.xiling.module.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.manager.MessageManager;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Message;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends BaseAdapter<Message, MessageAdapter.MessageViewHolder> {

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
        final Message message = items.get(position);

        holder.itemTitleTv.setText(message.title);
        holder.itemTimeTv.setText(message.createDate);
        if (message.isImageMessage()) {
            holder.itemContentTv.setVisibility(View.GONE);
            holder.itemThumbIv.setVisibility(View.VISIBLE);
            FrescoUtil.setImageSmall(holder.itemThumbIv, message.thumb);
        } else {
            holder.itemContentTv.setVisibility(View.VISIBLE);
            holder.itemThumbIv.setVisibility(View.GONE);
            holder.itemContentTv.setText(message.getContent());
        }
        if (message.hasDetail()) {
            holder.itemMoreLayout.setVisibility(View.VISIBLE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewMessageDetail(message);
                }
            });
            holder.itemMoreLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewMessageDetail(message);
                }
            });
        } else {
            holder.itemMoreLayout.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(null);
        }
        if (position == items.size() - 1) {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
            int margin = ConvertUtil.dip2px(10);
            layoutParams.setMargins(0, 0, 0, margin);
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    private void viewMessageDetail(Message message) {
        mMessageManager.setMessage(message);
        mMessageManager.openMessageDetail();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitleTv)
        protected TextView itemTitleTv;
        @BindView(R.id.itemTimeTv)
        protected TextView itemTimeTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;
        @BindView(R.id.itemContentTv)
        protected TextView itemContentTv;
        @BindView(R.id.itemMoreLayout)
        protected LinearLayout itemMoreLayout;

        MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
