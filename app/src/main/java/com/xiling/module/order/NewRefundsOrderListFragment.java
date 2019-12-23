package com.xiling.module.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.order.adapter.RefundsOrderListAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.RefundsOrder;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.component.NoData;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.OrderService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.CSUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/19.
 */
public class NewRefundsOrderListFragment extends BaseFragment {

    @BindView(R.id.noDataLayout)
    NoData mNoDataLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private String mOrderCode;
    private List<RefundsOrder> mDatas = new ArrayList<>();
    private RefundsOrderListAdapter mAdapter;
    private IOrderService mOrderService;


    /**
     * 查看售后记录列表
     *
     * @param orderCode 如果传入则只显示当前订单的记录 不传入显示用户所有售后订单记录
     * @return
     */
    public static NewRefundsOrderListFragment newInstance(String orderCode) {
        Bundle args = new Bundle();
        args.putString(Config.INTENT_KEY_ID, orderCode);
        NewRefundsOrderListFragment fragment = new NewRefundsOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.base_list_layout, container, false);
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
        Observable<RequestResult<List<RefundsOrder>>> observable = null;
        if (StringUtils.isEmpty(mOrderCode)) {
            int page = mDatas.size() / Constants.PAGE_SIZE + 1;
            observable = mOrderService.getOrderRefundList(
                    page, Constants.PAGE_SIZE
            ).flatMap(new Function<RequestResult<PaginationEntity<RefundsOrder, Object>>, ObservableSource<RequestResult<List<RefundsOrder>>>>() {
                @Override
                public ObservableSource<RequestResult<List<RefundsOrder>>> apply(RequestResult<PaginationEntity<RefundsOrder, Object>> result) throws Exception {
                    if (result.code!=0) {
                        throw new RuntimeException(result.message);
                    }
                    RequestResult<List<RefundsOrder>> listRequestResult = new RequestResult<>();
                    listRequestResult.code = 0;
                    listRequestResult.data = result.data.list;
                    return Observable.just(listRequestResult);
                }
            });
        } else {
            observable = mOrderService.getOrderRefundList(mOrderCode);
        }
        APIManager.startRequest(observable, new BaseRequestListener<List<RefundsOrder>>(mRefreshLayout) {
            @Override
            public void onSuccess(List<RefundsOrder> datas) {
                super.onSuccess(datas);
                mDatas.addAll(datas);
                mAdapter.notifyDataSetChanged();
                if (datas.size() < Constants.PAGE_SIZE || !StringUtils.isEmpty(mOrderCode)) {
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
        mAdapter.setSellerModel(false);
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
    }

    private void getIntentData() {
        mOrderCode = getArguments().getString(Config.INTENT_KEY_ID);
    }

    private void cancelRefunds(final String refundId,boolean isRefundMoney) {
        final WJDialog wjDialog = new WJDialog(getContext());
        wjDialog.show();
        wjDialog.setTitle("撤回申请");
        if (isRefundMoney) {
            wjDialog.setContentText("撤回后，该退款单将被关闭");
        } else {
            wjDialog.setContentText("撤回后，该退货单将被关闭");
        }
        wjDialog.setCancelText("取消");
        wjDialog.setConfirmText("确定");
        wjDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wjDialog.dismiss();
                OrderService.cancelRefundExt(getActivity(), refundId);
            }
        });
    }

    private void inputRefundsInfo(String refundId) {
        Intent intent = new Intent(getActivity(), RefundExpressActivity.class);
        intent.putExtra("refundId", refundId);
        startActivity(intent);
    }

    private void editRefunds(RefundsOrder refundsOrder) {
        if (refundsOrder.apiRefundOrderBean. isRefundMoney()) {
            OrderService.editRefundMoneyActivity(getContext(),refundsOrder.apiRefundOrderBean.orderCode,refundsOrder.apiRefundOrderBean.refundId);
        }else {
            OrderService.addOrEditRefundOrder(getContext(), refundsOrder.apiRefundOrderBean.orderCode, refundsOrder.orderProducts, refundsOrder.apiRefundOrderBean.refundId);
        }
    }

    class RefundsItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener {
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            switch (view.getId()) {
                case R.id.tvItemCS:
                    CSUtils.start(getContext(), "退款退货用户");
                    break;
                case R.id.tvItemEdit:
                    editRefunds(mDatas.get(position));
                    break;
                case R.id.tvItemCancel:
                    RefundsOrder refundsOrder = mDatas.get(position);
                    cancelRefunds(refundsOrder.apiRefundOrderBean.refundId,refundsOrder.apiRefundOrderBean.isRefundMoney());
                    break;
                case R.id.tvItemInput:
                    inputRefundsInfo(mDatas.get(position).apiRefundOrderBean.refundId);
                    break;
            }
        }
    }


    class RefundsItemClicklListener extends OnItemClickListener {
        @Override
        public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
            Intent intent = new Intent(getContext(), RefundDetailActivity.class);
            intent.putExtra(Config.INTENT_KEY_ID, mDatas.get(position).apiRefundOrderBean.refundId);
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
