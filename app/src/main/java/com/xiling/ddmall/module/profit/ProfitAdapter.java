package com.xiling.ddmall.module.profit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.bean.CommonExtra;
import com.xiling.ddmall.shared.bean.Profit;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/16.
 */

public class ProfitAdapter extends BaseAdapter<Profit, RecyclerView.ViewHolder> {


    private CommonExtra mExtras;

    public ProfitAdapter(Context context) {
        super(context);
        hasHeader = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(layoutInflater.inflate(R.layout.common_header_layout, parent, false));
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.item_profit, parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HeaderViewHolder) holder).setData(mExtras);
        } else {
            ((ViewHolder) holder).setProfit(items.get(position - 1));
        }
    }

    public void setHeaderData(CommonExtra ex) {
        mExtras = ex;
        notifyItemChanged(0);
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.totalProfitTv)
        protected TextView mTotalProfitTv;

        @BindView(R.id.leftProfitTv)
        protected TextView mLeftProfitTv;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(CommonExtra mExtras) {
            if (mExtras == null) {
                return;
            }
            mTotalProfitTv.setText(String.valueOf(ConvertUtil.cent2yuan(mExtras.profitSumMoney)));
            mLeftProfitTv.setText(String.format("剩余店多多：%s", ConvertUtil.cent2yuan(mExtras.freezeSumMoney)));
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemTimeTv)
        protected TextView mItemTimeTv;
        @BindView(R.id.itemMoneyTv)
        protected TextView mItemMoneyTv;
        @BindView(R.id.itemProfitTv)
        protected TextView mItemProfitTv;
        @BindView(R.id.itemNicknameTv)
        protected TextView mItemNicknameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void setProfit(final Profit profit) {
            mItemTimeTv.setText(String.format("时间：%s", profit.createDate));
            mItemMoneyTv.setText(String.valueOf(ConvertUtil.centToCurrency(context, profit.orderMoney)));
            if (profit.freezeProfitMoney < 0) {
                mItemProfitTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(profit.freezeProfitMoney)));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.text_black));
            } else {
                mItemProfitTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(profit.freezeProfitMoney)));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.red));
            }
            mItemNicknameTv.setVisibility(profit.nickName.isEmpty() ? View.GONE : View.VISIBLE);
            mItemNicknameTv.setText(String.format("下单用户：%s", profit.nickName));
        }
    }
}
