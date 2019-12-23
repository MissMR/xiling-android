package com.xiling.module.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.bean.OrderProduct;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order.adapter
 * @since 2017-07-15
 */
class OrderItemPresentAdapter extends BaseAdapter<OrderProduct, OrderItemPresentAdapter.ViewHolder> {

    private BaseCallback<Object> mCallback;

    public OrderItemPresentAdapter(Context context) {
        super(context);
    }

    @Override
    public OrderItemPresentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderItemPresentAdapter.ViewHolder(layoutInflater.inflate(R.layout.item_cart_item_present_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderItemPresentAdapter.ViewHolder holder, int position) {
        OrderProduct product = items.get(position);
        holder.mItemTitleTv.setText(product.skuName);
        holder.mItemAmountTv.setText(product.getAmountString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.callback(null);
                }
            }
        });
    }

    public void setCallback(BaseCallback<Object> callback) {
        mCallback = callback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemAmountTv)
        protected TextView mItemAmountTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
