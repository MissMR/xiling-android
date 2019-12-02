package com.dodomall.ddmall.module.pay.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.cart.adapter.CartItemPresentAdapter;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.CartItem;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.cart.adapter
 * @since 2017-06-19
 */
class PayCartItemAdapter extends BaseAdapter<CartItem, PayCartItemAdapter.ViewHolder> {

    public PayCartItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_pay_cart_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setCartItem(items.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemPropertyTv)
        protected TextView mItemPropertyTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mItemPriceTv;
        @BindView(R.id.itemAmountTv)
        protected TextView mItemAmountTv;
        @BindView(R.id.itemPresentLayout)
        protected LinearLayout mItemPresentLayout;
        @BindView(R.id.recyclerView)
        protected RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setCartItem(final CartItem cartItem) {
            itemView.setTag(cartItem);
            FrescoUtil.setImageSmall(mItemThumbIv, cartItem.thumb);
            mItemTitleTv.setText(cartItem.name);
            mItemPropertyTv.setText(cartItem.properties);
            mItemPriceTv.setText(ConvertUtil.centToCurrency(context, cartItem));
            mItemAmountTv.setText(String.format("× %d", cartItem.amount));

            if (cartItem.presents != null && cartItem.presents.size() > 0) {
                mItemPresentLayout.setVisibility(View.VISIBLE);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                CartItemPresentAdapter cartItemPresentAdapter = new CartItemPresentAdapter(context);
//                cartItemPresentAdapter.setHasStableIds(true);
                mRecyclerView.setAdapter(cartItemPresentAdapter);
                cartItemPresentAdapter.setItems(cartItem.presents);
            }else {
                mItemPresentLayout.setVisibility(View.GONE);
            }
        }

    }
}
