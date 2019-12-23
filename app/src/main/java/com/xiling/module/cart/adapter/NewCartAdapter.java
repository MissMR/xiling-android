package com.xiling.module.cart.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.shared.bean.CartStore;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/11.
 */
public class NewCartAdapter extends BaseQuickAdapter<CartStore, BaseViewHolder> {

    public NewCartAdapter(@Nullable List<CartStore> data) {
        super(R.layout.item_cart_store_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CartStore item) {
        helper.addOnClickListener(R.id.itemTitleTv);
        helper.addOnClickListener(R.id.ivReceviceCoupon);

        TextView tvTitle = helper.getView(R.id.itemTitleTv);
        tvTitle.setSelected(item.isSelected());
        tvTitle.setText(item.name);
        helper.setVisible(R.id.ivReceviceCoupon, item.mCoupons!=null && item.mCoupons.size() > 0);

        RecyclerView rvList = helper.getView(R.id.itemRecyclerView);
        rvList.setLayoutManager(new LinearLayoutManager(tvTitle.getContext()));
//        rvList.addItemDecoration(new ListDividerDecoration(tvTitle.getContext()));
        CartItemAdapter cartItemAdapter = new CartItemAdapter(tvTitle.getContext());
        rvList.setAdapter(cartItemAdapter);
        cartItemAdapter.setItems(item.products);
    }

}
