package com.xiling.ddmall.module.pay.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.bean.CartStore;
import com.xiling.ddmall.shared.decoration.ListDividerDecoration;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.pay.adapter
 * @since 2017-06-19
 */
public class PayAdapter extends BaseAdapter<CartStore, PayAdapter.ViewHolder> {
    public PayAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_pay_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final CartStore cartStore = items.get(position);
        holder.mItemTitleTv.setText(cartStore.name);
        holder.mItemTotalTv.setText(ConvertUtil.centToCurrency(context, cartStore.getTotal()));
        holder.mPayCartItemAdapter.setItems(cartStore.products);
        holder.mPayCartItemAdapter.notifyDataSetChanged();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemRecyclerView)
        protected RecyclerView mRecyclerView;
        @BindView(R.id.itemTotalTv)
        protected TextView mItemTotalTv;
        protected PayCartItemAdapter mPayCartItemAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPayCartItemAdapter = new PayCartItemAdapter(context);
            mPayCartItemAdapter.setHasStableIds(true);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(linearLayoutManager);
            mRecyclerView.addItemDecoration(new ListDividerDecoration(context));
            mRecyclerView.setAdapter(mPayCartItemAdapter);
        }
    }
}
