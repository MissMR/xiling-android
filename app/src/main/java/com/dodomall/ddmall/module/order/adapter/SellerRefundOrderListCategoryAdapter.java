package com.dodomall.ddmall.module.order.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.order.SellerRefundsOrderListFragment;
import com.dodomall.ddmall.shared.basic.BaseAdapter;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/28.
 */
public class SellerRefundOrderListCategoryAdapter extends BaseAdapter<SellerRefundsOrderListFragment.RefundStatusModel,SellerRefundOrderListCategoryAdapter.ViewHolder>{

    private OnItemClickListener mOnItemClickListener;

    private List<SellerRefundsOrderListFragment.RefundStatusModel> mDatas;

    public SellerRefundOrderListCategoryAdapter(Context context, List<SellerRefundsOrderListFragment.RefundStatusModel> datas) {
        super(context);
        mDatas = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_seller_refunds_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvName;

        public ViewHolder(View itemView) {
            super(itemView);
            mTvName = (TextView) itemView.findViewById(R.id.tvName);
        }

        public void setData(final SellerRefundsOrderListFragment.RefundStatusModel model){
            mTvName.setText(model.name);
            if (mOnItemClickListener != null) {
                mTvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(model);
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(SellerRefundsOrderListFragment.RefundStatusModel model);
    }
}
