package com.dodomall.ddmall.module.order.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.order.OrderCommentActivity;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.OrderComment;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order.adapter
 * @since 2017-07-19
 */
public class OrderWaitCommentListAdapter extends BaseAdapter<OrderComment, OrderWaitCommentListAdapter.ViewHolder> {


    public OrderWaitCommentListAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_order_wait_comment, parent, false));
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final OrderComment orderComment = items.get(position);
        FrescoUtil.setImageSmall(holder.mItemThumbIv, orderComment.thumb);
        holder.mItemTitleTv.setText(orderComment.name);
        holder.mItemPropertyTv.setText(orderComment.properties);
        holder.mItemAmountTv.setText(String.format("× %d", orderComment.quantity));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtil.viewProductDetail(context, orderComment.productId);
            }
        });
        holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, orderComment.realPrice));
        holder.mItemPriceTvShow.setText(ConvertUtil.centToCurrency(context, orderComment.marketPrice));
        TextViewUtil.addThroughLine(holder.mItemPriceTvShow);
        holder.mCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderCommentActivity.class);
                intent.putExtra("orderCode", orderComment.orderCode);
                intent.putExtra("orderId", orderComment.order1Id);
                intent.putExtra("skuId", orderComment.skuId);
                context.startActivity(intent);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        TextView mItemTitleTv;
        @BindView(R.id.itemPropertyTv)
        TextView mItemPropertyTv;
        @BindView(R.id.itemPriceTv)
        TextView mItemPriceTv;
        @BindView(R.id.itemPriceTvShow)
        TextView mItemPriceTvShow;
        @BindView(R.id.itemAmountTv)
        TextView mItemAmountTv;
        @BindView(R.id.commentBtn)
        TextView mCommentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
