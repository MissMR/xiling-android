package com.xiling.ddmall.module.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.order.RefundDetailActivity;
import com.xiling.ddmall.module.order.SellerRefundDetailActivity;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.bean.OrderProduct;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.TextViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order.adapter
 * @since 2017-07-07
 */
public class OrderItemAdapter extends BaseAdapter<OrderProduct, OrderItemAdapter.ViewHolder> {

    private BaseCallback<Object> mCallback;
    private boolean mSelectModel;
    private boolean mDetailModel;
    private boolean mSellerModel = false;
    private boolean mRefundModel;
    private String mMemberId;

    public OrderItemAdapter(Context context, List<OrderProduct> products) {
        super(context);
        items = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final OrderProduct product = items.get(position);
        holder.mItemTitleTv.setText(product.name);
        FrescoUtil.setImageSmall(holder.mItemThumbIv, product.thumb);
        holder.mItemAmountTv.setText(product.getAmountString());
        holder.mItemPropertyTv.setText(product.properties);
        holder.mItemSelectorIv.setVisibility(mSelectModel ? View.VISIBLE : View.GONE);
        holder.mItemSelectorIv.setSelected(product.isSelect());


        holder.mItemPriceTvShow.setText(ConvertUtil.centToCurrency(context, product.marketPrice));
        TextViewUtil.addThroughLine(holder.mItemPriceTvShow);
        if (mSellerModel) {
            if (mRefundModel) {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.price));
            } else if (mDetailModel) {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.retailPrice));
            } else {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.price));
            }
        } else {
            if (mRefundModel) {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.realPrice));
            } else if (mDetailModel) {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.retailPrice));
            } else {
                holder.mItemPriceTv.setText(ConvertUtil.centToCurrency(context, product.realPrice));
            }
        }

        if (mRefundModel) {
            holder.mTvRefundStatus.setVisibility(View.GONE);
        } else if (product.refundStatus != 0) {
            holder.mTvRefundStatus.setVisibility(View.VISIBLE);

            String status = product.refundStatusStr;
            if (TextUtils.isEmpty(status)) {
                status = "退款详情";
            }
            holder.mTvRefundStatus.setText(status);

            if (mDetailModel) {
                //黑色字模式
                setBlackMode(holder.mTvRefundStatus);
                if (!mSellerModel) {
                    holder.mTvRefundStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(holder.mLayoutProduct.getContext(), RefundDetailActivity.class);
                            intent.putExtra(Config.INTENT_KEY_ID, product.refundId);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    holder.mTvRefundStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(holder.mLayoutProduct.getContext(), SellerRefundDetailActivity.class);
                            intent.putExtra(Config.INTENT_KEY_ID, product.refundId);
                            intent.putExtra("memberId", mMemberId);
                            context.startActivity(intent);
                        }
                    });
                }
            } else {
                //黄色字模式
                setYellowMode(holder.mTvRefundStatus);
            }

        }

        if (!product.presents.isEmpty()) {
            OrderItemPresentAdapter orderItemPresentAdapter = new OrderItemPresentAdapter(context);
            orderItemPresentAdapter.setCallback(mCallback);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            holder.mRecyclerView.setLayoutManager(linearLayoutManager);
            holder.mRecyclerView.setAdapter(orderItemPresentAdapter);
            orderItemPresentAdapter.setItems(product.presents);
            holder.mItemPresentLayout.setVisibility(View.VISIBLE);
        } else {
            holder.mItemPresentLayout.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCallback != null) {
                    mCallback.callback(product);
                }

            }
        });
        holder.mItemSelectorIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.setSelect(!holder.mItemSelectorIv.isSelected());
                notifyDataSetChanged();
            }
        });

        if (mSelectModel) {
            holder.mTvRefundStatus.setVisibility(View.GONE);
        }
    }

    private void setYellowMode(TextView tvRefundStatus) {
        tvRefundStatus.setTextColor(Color.parseColor("#ff9300"));
    }

    private void setBlackMode(TextView tvRefundStatus) {
        tvRefundStatus.setTextColor(Color.parseColor("#333333"));
        int top = ConvertUtils.dp2px(3);
        int left = ConvertUtils.dp2px(7);
        tvRefundStatus.setPadding(left, top, left, top);
        tvRefundStatus.setBackgroundResource(R.drawable.bg_solid_while_stoke_black_corners_12dp);
    }

    public void setCallback(BaseCallback<Object> callback) {
        mCallback = callback;
    }

    /**
     * @param selectModel 是否可选择item模式
     */
    public void setSelectModel(boolean selectModel) {
        mSelectModel = selectModel;
    }

    /**
     * @param detailModel 订单详情里面展示的（和其他模式的区别在于2个价格取值不一样）
     */
    public void setDetailModel(boolean detailModel) {
        mDetailModel = detailModel;
    }

    /**
     * @param sellerModel 是不是卖家端
     */
    public void setSellerModel(boolean sellerModel) {
        mSellerModel = sellerModel;
    }

    public void setRefundModel(boolean refundModel) {
        mRefundModel = refundModel;
    }

    public void setMemberId(String memberId) {
        mMemberId = memberId;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemSelectorIv)
        protected ImageView mItemSelectorIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mItemPriceTv;
        @BindView(R.id.itemPriceTvShow)
        protected TextView mItemPriceTvShow;
        @BindView(R.id.itemPropertyTv)
        protected TextView mItemPropertyTv;
        @BindView(R.id.itemAmountTv)
        protected TextView mItemAmountTv;
        @BindView(R.id.itemPresentLayout)
        protected LinearLayout mItemPresentLayout;
        @BindView(R.id.layoutProduct)
        protected LinearLayout mLayoutProduct;
        @BindView(R.id.recyclerView)
        protected RecyclerView mRecyclerView;
        @BindView(R.id.tvRefundStatus)
        protected TextView mTvRefundStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
