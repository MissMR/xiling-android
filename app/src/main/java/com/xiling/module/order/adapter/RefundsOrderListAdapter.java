package com.xiling.module.order.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.xiling.R;
import com.xiling.shared.bean.RefundsOrder;
import com.xiling.shared.decoration.ListDividerDecoration;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/22.
 */
public class RefundsOrderListAdapter extends BaseQuickAdapter<RefundsOrder, BaseViewHolder> {

    private Context mContext;
    private boolean mSellerModel;
    private int mRefundType;

    public RefundsOrderListAdapter(Context context, @Nullable List<RefundsOrder> data) {
        super(R.layout.item_refunds_order, data);
        mContext = context;
        setLoadMoreView();
    }

    private void setLoadMoreView() {
        setLoadMoreView(new LoadMoreView() {
            @Override
            public int getLayoutId() {
                return com.chad.library.R.layout.quick_view_load_more;
            }

            @Override
            protected int getLoadingViewId() {
                return com.chad.library.R.id.load_more_loading_view;
            }

            @Override
            protected int getLoadFailViewId() {
                return com.chad.library.R.id.load_more_load_fail_view;
            }

            @Override
            protected int getLoadEndViewId() {
                return R.id.load_more_load_end_view;
            }
        });
    }

    @Override
    protected void convert(BaseViewHolder helper, RefundsOrder item) {
        helper.addOnClickListener(R.id.tvItemCS);
        helper.addOnClickListener(R.id.tvItemEdit);
        helper.addOnClickListener(R.id.tvItemCancel);
        helper.addOnClickListener(R.id.tvItemInput);
        helper.addOnClickListener(R.id.tvItemStoreRefuse);
        helper.addOnClickListener(R.id.tvItemStoreAgree);
        helper.addOnClickListener(R.id.tvItemStoreFinish);

        helper.setText(R.id.tvRefundStatus, item.apiRefundOrderBean.refundStatusStr);

        RecyclerView rvProduch = helper.getView(R.id.rvProduch);
        rvProduch.setEnabled(false);
        rvProduch.addItemDecoration(new ListDividerDecoration(mContext));
        rvProduch.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(mContext, item.orderProducts);
        orderItemAdapter.setRefundModel(true);
        orderItemAdapter.setSellerModel(mSellerModel);
        rvProduch.setAdapter(orderItemAdapter);
        rvProduch.setHasFixedSize(true);
        rvProduch.addItemDecoration(new ListDividerDecoration(mContext));
        rvProduch.setClickable(false);
        LinearLayout layout = helper.getView(R.id.layoutBottom);
        for (int i = 0; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setVisibility(View.GONE);
        }
        if (mSellerModel) {
            helper.setText(R.id.tvRefundsCode, "买家：" + item.apiRefundOrderBean.nickName);
            helper.setText(R.id.tvPrompt, item.apiRefundOrderBean.storePromptList);
            helper.setText(R.id.tvItemStoreRefuse, item.apiRefundOrderBean.isRefundMoney() ? "拒绝退款" : "拒绝退货");
            helper.setText(R.id.tvItemStoreAgree, item.apiRefundOrderBean.isRefundMoney() ? "同意退款" : "同意退货");
            switch (item.apiRefundOrderBean.refundStatus) {
                case 0:
                    helper.setVisible(R.id.tvItemStoreRefuse, true);
                    helper.setVisible(R.id.tvItemStoreAgree, true);
                    break;
                case 2:
                    helper.setVisible(R.id.tvItemStoreFinish, true);
                    break;
            }
        } else {
            helper.setText(R.id.tvRefundsCode, "售后单号：" + item.apiRefundOrderBean.refundCode);
            helper.setText(R.id.tvPrompt, item.apiRefundOrderBean.mePromptList);
            if (item.apiRefundOrderBean.isRefundMoney()) {
                switch (item.apiRefundOrderBean.refundStatus) {
                    case 0:
                        helper.setVisible(R.id.tvItemCS, true);
                        helper.setVisible(R.id.tvItemEdit, true);
                        helper.setVisible(R.id.tvItemCancel, true);
                        break;
//                    case -1:
//                        break;
                    default:
                        helper.setVisible(R.id.tvItemCS, true);
                        break;
                }
            } else {
                switch (item.apiRefundOrderBean.refundStatus) {
                    case 0:
                        helper.setVisible(R.id.tvItemCS, true);
                        helper.setVisible(R.id.tvItemEdit, true);
                        helper.setVisible(R.id.tvItemCancel, true);
                        break;
                    case 1:
                        helper.setVisible(R.id.tvItemCS, true);
                        helper.setVisible(R.id.tvItemInput, true);
                        helper.setVisible(R.id.tvItemCancel, true);
                        break;
                    case 2:
                        helper.setVisible(R.id.tvItemCS, true);
                        helper.setVisible(R.id.tvItemCancel, true);
                        break;
                    default:
                        helper.setVisible(R.id.tvItemCS, true);
                        break;
                }
            }
        }

        // Jigsaw 隐藏商家客服
        helper.setVisible(R.id.tvItemCS, false);

    }

    public void setSellerModel(boolean sellerModel) {
        mSellerModel = sellerModel;
    }

    /**
     * @param refundType (1:退货列表,2:退款列表,)
     */
    public void setRefundType(int refundType) {
        mRefundType = refundType;
    }
}
