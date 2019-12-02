package com.dodomall.ddmall.module.balance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.Balance;
import com.dodomall.ddmall.shared.bean.CommonExtra;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/17.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.module.balance
 * @since 2017/6/17 下午3:12
 */

public class BalanceAdapter extends BaseAdapter<Balance, RecyclerView.ViewHolder> {

    private CommonExtra mExtra;

    public BalanceAdapter(Context context) {
        super(context);
        hasHeader = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new HeaderViewHolder(layoutInflater.inflate(R.layout.common_header_layout, parent, false));
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.item_balance, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position == 0) {
            ((HeaderViewHolder) holder).setData(mExtra);
        } else {
            ((ViewHolder) holder).setBalance(items.get(position - 1));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void setHeaderData(CommonExtra ex) {
        mExtra = ex;
        notifyItemChanged(0);
    }

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.titleTv)
        protected TextView mTitleTv;
        @BindView(R.id.totalProfitTv)
        protected TextView mTotalProfitTv;
        @BindView(R.id.leftProfitTv)
        protected TextView mLeftProfitTv;
        @BindView(R.id.textView)
        protected TextView mTextView;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(CommonExtra mExtras) {
            if (mExtras == null) {
                return;
            }
            mTitleTv.setText("您的账户余额为");
            mTotalProfitTv.setText(String.valueOf(ConvertUtil.cent2yuanNoZero(mExtras.availableMoney)));
            mLeftProfitTv.setVisibility(View.GONE);
            mTextView.setText("收支明细");
        }
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.itemTimeTv)
        protected TextView mItemTimeTv;
        @BindView(R.id.itemTypeTv)
        protected TextView mItemTypeTv;
        @BindView(R.id.itemProfitTv)
        protected TextView mItemProfitTv;
        @BindView(R.id.itemStatusTv)
        protected TextView mItemStatusTv;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void setBalance(final Balance balance) {
            mItemTimeTv.setText(String.format("时间：%s", balance.createDate));
            mItemTypeTv.setText(String.format("类型：%s", balance.typeName));
            if (balance.money < 0) {
                mItemProfitTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(balance.money)));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.text_black));
            } else {
                mItemProfitTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(balance.money)));
                mItemProfitTv.setTextColor(context.getResources().getColor(R.color.red));
            }
            mItemStatusTv.setText(String.format("状态：%s", balance.statusStr));
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BalanceDetailActivity.class);
                    intent.putExtra("typeId", balance.typeId);
                    intent.putExtra("did", balance.did);
                    intent.putExtra("data",balance);
                    context.startActivity(intent);
                }
            });
        }
    }
}
