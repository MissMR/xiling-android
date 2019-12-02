package com.dodomall.ddmall.module.order.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseAdapter;
import com.dodomall.ddmall.shared.basic.BaseCallback;
import com.dodomall.ddmall.shared.bean.Order;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.service.OrderService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order.adapter
 * @since 2017-07-06
 */
public class OrderListAdapter extends BaseAdapter<Order, OrderListAdapter.ViewHolder> {

    private Activity mContext;
    private int mModel;

    public OrderListAdapter(Activity context) {
        super(context);
        mContext = context;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        final Order order = items.get(position);
        holder.setOrder(order);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventUtil.viewOrderDetail(context, order.orderMain.orderCode);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemStoreNameTv)
        protected TextView mStoreNameTv;
        @BindView(R.id.itemStatusTv)
        protected TextView mStatusTv;
        @BindView(R.id.itemRecyclerView)
        protected RecyclerView mRecyclerView;
        @BindView(R.id.itemDescTv)
        protected TextView mDescTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mPriceTv;
        @BindView(R.id.bottomLayout)
        protected LinearLayout mBottomLayout;
        @BindView(R.id.itemCancelBtn)
        protected TextView mCancelBtn;
        @BindView(R.id.itemPayBtn)
        protected TextView mPayBtn;
        @BindView(R.id.itemApplyRefundMoneyBtn)
        protected TextView mApplyRefundMoneyBtn;
        @BindView(R.id.orderFinishBtn)
        protected TextView orderFinishBtn;
        @BindView(R.id.itemDetailBtn)
        protected TextView mDetailBtn;
        @BindView(R.id.itemViewExpressBtn)
        protected TextView mViewExpressBtn;
        @BindView(R.id.itemCsBtn)
        protected TextView mItemCsBtn;
        @BindView(R.id.itemShit)
        protected TextView mItemShit;
        @BindView(R.id.itemRefundMony)
        protected TextView itemRefundMony;
        @BindView(R.id.itemRefundGoods)
        protected TextView itemRefundGoods;
        @BindView(R.id.itemPayMoney)
        protected TextView itemPayMoney;
        @BindView(R.id.itemCancelRefundGoods)
        protected TextView itemCancelRefundGoods;
        @BindView(R.id.itemCancelRefundMoney)
        protected TextView itemCancelRefundMoney;
        @BindView(R.id.itemGoGroupBuy)
        protected TextView mItemGoGroupBuy;
        @BindView(R.id.itemCheckGroupBuy)
        protected TextView mItemCheckGroupBuy;
        @BindView(R.id.itemComment)
        protected TextView mItemComment;
        @BindView(R.id.itemEditRefund)
        protected TextView mItemEditRefund;
        @BindView(R.id.itemCancelRefund)
        protected TextView mItemCancelRefund;
        @BindView(R.id.tvPriceTag1)
        protected TextView mTvPriceTag1;
        @BindView(R.id.tvPriceTag2)
        protected TextView mTvPriceTag2;
        @BindView(R.id.tvCS)
        protected TextView mTvCS;

        private Order mOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @SuppressLint("DefaultLocale")
        public void setOrder(Order order) {
            mOrder = order;
            mStatusTv.setText(order.orderMain.orderStatusStr);

            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            layoutManager.setAutoMeasureEnabled(true);
            mRecyclerView.setEnabled(false);
            mRecyclerView.addItemDecoration(new ListDividerDecoration(context));
            mRecyclerView.setLayoutManager(layoutManager);
            OrderItemAdapter orderItemAdapter = new OrderItemAdapter(context, order.products);
            orderItemAdapter.setSellerModel(mModel != 0);
            orderItemAdapter.setCallback(new BaseCallback<Object>() {
                @Override
                public void callback(Object data) {
                    EventUtil.viewOrderDetail(context, mOrder.orderMain.orderCode);
                }
            });
            orderItemAdapter.setMemberId(mOrder.orderMain.memberId);
            mRecyclerView.setAdapter(orderItemAdapter);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setClickable(false);
            mDescTv.setText(String.format("共 %d 件商品 合计：", order.products.size()));


            for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
                mBottomLayout.getChildAt(i).setVisibility(View.GONE);
            }
            if (mModel == 0) {//买家
                mStoreNameTv.setText(order.storeName);
                mPriceTv.setText(ConvertUtil.centToCurrency(context, order.orderMain.totalProductMoney));
                itemPayMoney.setText(ConvertUtil.centToCurrency(context, order.orderMain.totalMoney));

                switch (order.orderMain.status) {
                    case AppTypes.ORDER.STATUS_BUYER_WAIT_PAY:
                        mPayBtn.setVisibility(View.VISIBLE);
                        mTvPriceTag2.setText("待付款：");
//                        itemPayMoney.setText(ConvertUtil.centToCurrency(context, order.orderMain.totalMoney));
                        break;
                    case AppTypes.ORDER.STATUS_BUYER_HAS_SHIP:
                        mViewExpressBtn.setVisibility(View.VISIBLE);
                        orderFinishBtn.setVisibility(View.VISIBLE);
                        break;
                    case AppTypes.ORDER.STATUS_BUYER_HAS_RECEIVED:
//                        mItemComment.setVisibility(View.VISIBLE);
                        break;
                    case AppTypes.ORDER.STATUS_BUYER_RETURN_GOODING:
                    case AppTypes.ORDER.STATUS_BUYER_RETURN_MONEYING:
//                        mItemCsBtn.setVisibility(View.VISIBLE);
//                        mItemEditRefund.setVisibility(View.VISIBLE);
//                        mItemCancelRefund.setVisibility(View.VISIBLE);
//
//
//                        mStoreNameTv.setText("售后单号：" + mOrder.refundOrder.refundId);
                        break;
                    case AppTypes.ORDER.STATUS_BUYER_HAS_CLOSE:
                        if (order.orderMain.payMoney < order.orderMain.totalMoney) {
                            mTvPriceTag2.setText("待付款：");
                        }
                        break;
                }
            } else {//卖家
                mStoreNameTv.setText("买家：" + order.nickName);
                mTvPriceTag1.setText("买家实付款：");
                mTvPriceTag2.setText("你实收款：");
                mPriceTv.setText(ConvertUtil.centToCurrency(context, order.orderMain.payMoney));
                itemPayMoney.setText(ConvertUtil.centToCurrency(context, order.orderMain.payMoney + order.orderMain.discountCoupon + order.orderMain.score * 10));
//                long totalPrice = 0;
//                for (OrderProduct product : order.products) {
//                    totalPrice += product.price * product.quantity;
//                }
//                itemPayMoney.setText(ConvertUtil.centToCurrency(context, totalPrice));

                switch (order.orderMain.status) {
                    case AppTypes.ORDER.STATUS_SELLER_WAIT_SHIP:
                        mItemShit.setVisibility(View.VISIBLE);
                        break;
                    case AppTypes.ORDER.STATUS_SELLER_HAS_SHIP:
                        mViewExpressBtn.setVisibility(View.VISIBLE);
                        break;
                    case AppTypes.ORDER.STATUS_SELLER_HAS_COMPLETE:
//                        mDetailBtn.setVisibility(View.VISIBLE);
                        break;
                    case AppTypes.ORDER.STATUS_SELLER_HAS_CLOSE:
                    case 8:
                    case 7:
//                        mDetailBtn.setVisibility(View.VISIBLE);
                        break;
//                    case 5://退款申请
//                        itemRefundMony.setVisibility(View.VISIBLE);
//                        break;
//                    case 6://退货申请
//                        itemRefundGoods.setVisibility(View.VISIBLE);
//                        break;
                    default:
//                        mDetailBtn.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }

        @OnClick(R.id.itemRefundMony)
        public void itemRefundMony() {
//            OrderService.showRefundMoneyDialog(mContext, mOrder);
        }

        @OnClick(R.id.itemRefundGoods)
        public void itemRefundGoods() {
//            OrderService.showRefundGoodsDialog(mContext, mOrder);
        }

        @OnClick(R.id.itemShit)
        public void shit() {
            OrderService.ship(context, mOrder);
        }

        @OnClick(R.id.itemCsBtn)
        public void contactCs() {
            OrderService.contactCs(context, mOrder);
        }

        @OnClick(R.id.itemCancelBtn)
        public void cancelOrder() {
            OrderService.cancelOrder(mContext, mOrder);
        }

        @OnClick(R.id.itemPayBtn)
        public void payOrder() {
            OrderService.viewPayActivity(context, mOrder.orderMain.orderCode);
        }

        @OnClick(R.id.itemApplyRefundMoneyBtn)
        public void applyRefundMoney() {
            OrderService.viewApplyRefundMoneyActivity(context, mOrder);
        }

        @OnClick(R.id.orderFinishBtn)
        public void orderFinishBtn() {
            OrderService.finishOrder(context, mOrder);
        }

        @OnClick(R.id.itemViewExpressBtn)
        public void viewExpress() {
            OrderService.viewExpress(context, mOrder);
        }

        @OnClick(R.id.itemDetailBtn)
        public void viewOrderDetail() {
            EventUtil.viewOrderDetail(context, mOrder.orderMain.orderCode);
        }

        @OnClick({R.id.itemCancelRefundGoods, R.id.itemCancelRefundMoney})
        public void viewCancelRefund() {
            OrderService.showCancelRefund(mContext, mOrder);
        }

        @OnClick({R.id.itemGoGroupBuy, R.id.itemCheckGroupBuy})
        public void goGroupBuy() {
            OrderService.goGroupBuy(context, mOrder);
        }

        @OnClick(R.id.itemComment)
        public void comment() {
            ToastUtil.error("评价订单");
//            OrderService.viewPayActivity(context, mOrder.orderMain.orderCode);
        }
    }
}
