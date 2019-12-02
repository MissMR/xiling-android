package com.dodomall.ddmall.module.foot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.module.product.event.MsgProduct;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/15.
 */

public class FootAdapter extends BaseAdapter<SkuInfo, FootAdapter.ViewHolder> {


    public FootAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_foot, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setFoot(items.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DDProductDetailActivity.start(holder.itemView.getContext(), items.get(position).productId);
            }
        });
        holder.itemView.findViewById(R.id.itemTrashBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgProduct msgProduct = new MsgProduct(MsgProduct.DEL_VIEW_HOSTORY);
                msgProduct.setSkuInfo(items.get(position));
                EventBus.getDefault().post(msgProduct);
            }
        });
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemMoneyTv)
        protected TextView mItemMoneyTv;
        @BindView(R.id.itemMoneyTv2)
        protected TextView mItemMoneyTv2;
        @BindView(R.id.tvCount)
        protected TextView mtvCount;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        protected void setFoot(final SkuInfo skuInfo) {
            mItemTitleTv.setText(skuInfo.name);
            mItemMoneyTv.setText(String.valueOf(ConvertUtil.centToCurrency(mItemMoneyTv.getContext(), skuInfo)));
//            if (SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isShopkeeper()) {
            mItemMoneyTv2.setText(ConvertUtil.centToCurrency(mItemMoneyTv.getContext(), skuInfo.marketPrice));
            TextViewUtil.addThroughLine(mItemMoneyTv2);
            mItemMoneyTv2.setVisibility(View.VISIBLE);
//            }else {
//                mItemMoneyTv2.setVisibility(View.GONE);
//            }
            mtvCount.setText("销量：" + skuInfo.totalSaleCount);
            FrescoUtil.setImageSmall(mItemThumbIv, skuInfo.thumb);
        }
    }

}
