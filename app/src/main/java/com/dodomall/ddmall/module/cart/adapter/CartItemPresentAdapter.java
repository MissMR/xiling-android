package com.dodomall.ddmall.module.cart.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.CartItem;
import com.dodomall.ddmall.shared.basic.BaseAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.cart.adapter
 * @since 2017-06-19
 */
public class CartItemPresentAdapter extends BaseAdapter<CartItem, CartItemPresentAdapter.ViewHolder> {
    public CartItemPresentAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_cart_item_present_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartItem cartItem = items.get(position);
        holder.mItemTitleTv.setText(cartItem.name);
        holder.mItemAmountTv.setText(String.format("× %d", cartItem.amount));
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
