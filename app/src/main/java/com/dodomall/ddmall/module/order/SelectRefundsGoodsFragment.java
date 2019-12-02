package com.dodomall.ddmall.module.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.order.adapter.OrderItemAdapter;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Order;
import com.dodomall.ddmall.shared.bean.OrderProduct;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.OrderService;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/21.
 */
public class SelectRefundsGoodsFragment extends BaseFragment {

    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.itemApplyRefundGoodsBtn)
    TextView mItemApplyRefundGoodsBtn;
    @BindView(R.id.layoutNodata)
    LinearLayout mLayoutNodata;

    private String mOrderCode;
    private List<OrderProduct> mDatas = new ArrayList<>();
    private OrderItemAdapter mAdapter;
    private Order mOrder;


    public static SelectRefundsGoodsFragment newInstance(String orderCode) {
        Bundle args = new Bundle();
        args.putString(Config.INTENT_KEY_ID, orderCode);
        SelectRefundsGoodsFragment fragment = new SelectRefundsGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_select_refunds_goods, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getIntentData();
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        IOrderService orderService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(orderService.getOrderByCode(mOrderCode), new BaseRequestListener<Order>(getActivity()) {

            @Override
            public void onSuccess(Order result) {
                mDatas.clear();
                mOrder = result;
                for (OrderProduct product : result.products) {
                    if (product.refundStatus == 0 || product.refundStatus >= 10) {
                        mDatas.add(product);
                    }
                }
                mAdapter.setMemberId(mOrder.orderMain.memberId);
                mAdapter.notifyDataSetChanged();
                mLayoutNodata.setVisibility(mDatas.size() > 0 ? View.GONE : View.VISIBLE);
            }

        });
    }

    private void initView() {
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvList.setNestedScrollingEnabled(false);
        mAdapter = new OrderItemAdapter(getContext(), mDatas);
        mAdapter.setSelectModel(true);
        mRvList.setAdapter(mAdapter);
    }

    private void getIntentData() {
        mOrderCode = getArguments().getString(Config.INTENT_KEY_ID);
    }

    @OnClick(R.id.itemApplyRefundGoodsBtn)
    public void onViewClicked() {
        if (mOrder == null) {
            ToastUtil.error("等待数据");
            return;
        }
        ArrayList<OrderProduct> orderProducts = new ArrayList<>();
        for (OrderProduct data : mDatas) {
            if (data.isSelect()) {
                orderProducts.add(data);
            }
        }

        int payMoney = 0;
        if (orderProducts.size() == mDatas.size()) {
            payMoney = mOrder.orderMain.payMoney;
        }

        if (orderProducts.size() > 0) {
            OrderService.addOrEditRefundOrder(getContext(), mOrderCode, orderProducts, null);
        } else {
            ToastUtil.error("请选择退货商品");
            return;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        switch (msgStatus.getAction()) {
            case MsgStatus.ACTION_REFUND_CHANGE:
                initData();
                break;
        }
    }
}
