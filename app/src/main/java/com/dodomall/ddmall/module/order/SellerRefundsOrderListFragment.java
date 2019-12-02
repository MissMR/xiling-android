package com.dodomall.ddmall.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.order.adapter.RefundsOrderListAdapter;
import com.dodomall.ddmall.module.order.adapter.SellerRefundOrderListCategoryAdapter;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.RefundsOrder;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PopupWindowManage;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.OrderService;
import com.dodomall.ddmall.shared.service.contract.IOrderService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/19.
 */
public class SellerRefundsOrderListFragment extends BaseFragment {


    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    @BindView(R.id.layoutCategory)
    LinearLayout mLayoutCategory;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.viewYinying)
    View mViewYinying;

    private List<RefundsOrder> mDatas = new ArrayList<>();
    private RefundsOrderListAdapter mAdapter;
    private IOrderService mOrderService;
    private int mType;
    private int mRefundStatus = 100;

    private PopupWindowManage mWindowManage;
    private ArrayList<RefundStatusModel> mStatusModels;

    /**
     * 店主查看售后记录列表
     *
     * @param refundType (1:退货列表,2:退款列表,)
     * @return
     */
    public static SellerRefundsOrderListFragment newInstance(int refundType) {
        Bundle args = new Bundle();
        args.putInt(Config.INTENT_KEY_TYPE_NAME, refundType);
        SellerRefundsOrderListFragment fragment = new SellerRefundsOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_seller_refund_order_list, container, false);
        ButterKnife.bind(this, inflate);
        return inflate;
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getIntentData();
        initView();
        getData(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getData(final boolean isRefresh) {
        if (mOrderService == null) {
            mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        }
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        int page = mDatas.size() / Constants.PAGE_SIZE + 1;
        Observable<RequestResult<List<RefundsOrder>>> observable = null;
        observable = mOrderService.getStoreOrderRefundList(page, Constants.PAGE_SIZE,mType, mRefundStatus)
                .flatMap(new Function<RequestResult<PaginationEntity<RefundsOrder, Object>>, ObservableSource<RequestResult<List<RefundsOrder>>>>() {
                    @Override
                    public ObservableSource<RequestResult<List<RefundsOrder>>> apply(RequestResult<PaginationEntity<RefundsOrder, Object>> result) throws Exception {
                        RequestResult<List<RefundsOrder>> listRequestResult = new RequestResult<>();
                        listRequestResult.code = 0;
                        listRequestResult.data = result.data.list;
                        return Observable.just(listRequestResult);
                    }
                });
        APIManager.startRequest(observable, new BaseRequestListener<List<RefundsOrder>>(mRefreshLayout) {
            @Override
            public void onSuccess(List<RefundsOrder> datas) {
                super.onSuccess(datas);
                mDatas.addAll(datas);
                mAdapter.notifyDataSetChanged();
                if (datas.size() < Constants.PAGE_SIZE) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mAdapter.loadMoreFail();
            }
        });
    }

    private void initView() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData(true);
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new RefundsOrderListAdapter(getContext(), mDatas);
        mAdapter.setSellerModel(true);
        mAdapter.setRefundType(mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new RefundsItemChildClickListener());
        mAdapter.setEmptyView(new NoData(getContext()));
        mRecyclerView.addOnItemTouchListener(new RefundsItemClicklListener());

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });

        if (mType == 1) {
            mTvCategory.setText("全部退货单");
        } else {
            mTvCategory.setText("全部退款单");
        }
    }

    private void getIntentData() {
        mType = getArguments().getInt(Config.INTENT_KEY_TYPE_NAME);
    }

    private void refuseRefund(RefundsOrder refundsOrder) {
        if (mType == 1) {
            OrderService.showRefuseRefundGoodsDialog(getActivity(), refundsOrder);
        } else {
            OrderService.showRefuseRefundMoneyDialog(getActivity(), refundsOrder);
        }
    }

    private void agreeRefund(RefundsOrder refundsOrder) {
        if (mType == 1) {
            OrderService.showRefundGoodsDialog(getActivity(), refundsOrder);
        } else {
            OrderService.showRefundMoneyDialog(getActivity(), refundsOrder);
        }
    }

    @OnClick(R.id.layoutCategory)
    public void onViewClicked() {
        if (mWindowManage == null) {
            mWindowManage = PopupWindowManage.getInstance(getActivity());
            mWindowManage.setYinYing(mViewYinying);
        }
        if (mWindowManage.isShowing()) {
            mWindowManage.dismiss();
            return;
        }
        if (mStatusModels == null) {
            mStatusModels = new ArrayList<>();
            if (mType == 1) {
                mStatusModels.add(new RefundStatusModel("全部退货单", 100));
                mStatusModels.add(new RefundStatusModel("买家申请退货", 0));
                mStatusModels.add(new RefundStatusModel("已同意退货，待买家寄回商品", 1));
                mStatusModels.add(new RefundStatusModel("买家已寄回商品，待确认收货", 2));
                mStatusModels.add(new RefundStatusModel("已确认收货，待平台退款", 3));
                mStatusModels.add(new RefundStatusModel("退款成功", 4));
                mStatusModels.add(new RefundStatusModel("退款关闭", -2));
            } else {
                mStatusModels.add(new RefundStatusModel("全部退款单", 100));
                mStatusModels.add(new RefundStatusModel("买家申请退款", 0));
                mStatusModels.add(new RefundStatusModel("待平台退款", 1));
                mStatusModels.add(new RefundStatusModel("退款成功", 4));
                mStatusModels.add(new RefundStatusModel("退款关闭", -1));
            }
        }
        SellerRefundOrderListCategoryAdapter adapter = new SellerRefundOrderListCategoryAdapter(getContext(), mStatusModels);
        adapter.setOnItemClickListener(new SellerRefundOrderListCategoryAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(SellerRefundsOrderListFragment.RefundStatusModel model) {
                mWindowManage.dismiss();
                mRefundStatus = model.value;
                mTvCategory.setText(model.name);
                getData(true);
            }
        });
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        mWindowManage.showWindow(mLayoutCategory, recyclerView);
    }


    public class RefundStatusModel {
        public String name;
        public int value;

        public RefundStatusModel(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    class RefundsItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()) {
                case R.id.tvItemStoreAgree:
                    agreeRefund(mDatas.get(position));
                    break;
                case R.id.tvItemStoreRefuse:
                    refuseRefund(mDatas.get(position));
                    break;
                case R.id.tvItemStoreFinish:
                    OrderService.finishOrder(getContext(), mDatas.get(position));
                    break;
            }
        }
    }

    class RefundsItemClicklListener extends OnItemClickListener {
        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent intent = new Intent(getContext(), SellerRefundDetailActivity.class);
            intent.putExtra(Config.INTENT_KEY_ID, mDatas.get(position).apiRefundOrderBean.refundId);
            intent.putExtra("memberId", mDatas.get(position).apiRefundOrderBean.memberId);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        switch (msgStatus.getAction()) {
            case MsgStatus.ACTION_REFUND_CHANGE:
                getData(true);
                break;
        }
    }

}
