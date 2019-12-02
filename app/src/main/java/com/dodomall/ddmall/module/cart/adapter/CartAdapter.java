package com.dodomall.ddmall.module.cart.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.CartItem;
import com.dodomall.ddmall.shared.bean.CartStore;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.bean.event.EventMessage;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartAdapter extends BaseAdapter<CartStore, CartAdapter.ViewHolder> {

    public CartAdapter(Context context) {
        super(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_cart_store_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CartStore cartStore = items.get(position);
        holder.mItemTitleTv.setSelected(cartStore.isSelected());
        holder.mItemTitleTv.setText(cartStore.name);
        holder.mItemTitleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setSelected(!view.isSelected());
                for (CartItem product : cartStore.products) {
                    product.isSelected = view.isSelected();
                }
                holder.mCartItemAdapter.setItems(cartStore.products);
                holder.mCartItemAdapter.notifyDataSetChanged();
                EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
            }
        });
        holder.mCartItemAdapter.setItems(cartStore.products);
        holder.mCartItemAdapter.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemRecyclerView)
        protected RecyclerView mRecyclerView;
        protected CartItemAdapter mCartItemAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mCartItemAdapter = new CartItemAdapter(context);
            mCartItemAdapter.setHasStableIds(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(new ListDividerDecoration(context));
            mRecyclerView.setAdapter(mCartItemAdapter);
        }
    }
}
