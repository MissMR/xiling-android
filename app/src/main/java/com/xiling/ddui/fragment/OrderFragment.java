package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.activity.OrderListActivit;
import com.xiling.ddui.activity.XLCashierActivity;
import com.xiling.ddui.activity.XLOrderDetailsActivity;
import com.xiling.ddui.adapter.MyOrderAdapter;
import com.xiling.ddui.bean.MyOrderDetailBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.custom.popupwindow.CancelOrderDialog;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NoData;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;
import static com.xiling.shared.constant.Event.CANCEL_ORDER;

/**
 * 我的订单
 */
public class OrderFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycler_order)
    RecyclerView recyclerOrder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;


    IOrderService mOrderService;
    public static final String ORDER_STATUS = "order_status";
    Unbinder unbinder;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;

    private String orderStatus = Constants.ORDER_ALL;

    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;

    MyOrderAdapter orderAdapter;
    List<XLOrderDetailsBean> recordsBeanList = new ArrayList<>();

    public static OrderFragment newInstance(String orderStatus) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putString(ORDER_STATUS, orderStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            orderStatus = getArguments().getString(ORDER_STATUS);
        }
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);


        unbinder = ButterKnife.bind(this, view);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        recyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));
        orderAdapter = new MyOrderAdapter();
        recyclerOrder.setAdapter(orderAdapter);
        noDataLayout.setTextView("还没有订单");
        orderAdapter.setOnButtomItemClickListener(new MyOrderAdapter.OnButtomItemClickListener() {
            @Override
            public void onSeeClickListerer(XLOrderDetailsBean recordsBean) {
                //查看物流
            }

            @Override
            public void onConfirmClickListerer(XLOrderDetailsBean recordsBean) {
                //确认收货
                confirmReceived(recordsBean.getOrderCode());
            }

            @Override
            public void onRemindClickListerer(XLOrderDetailsBean recordsBean) {
                //提醒发货
                remindDelivery(recordsBean.getOrderCode());
            }

            @Override
            public void onCancelClickListerer(XLOrderDetailsBean recordsBean) {
                //取消订单
                cancelOrder(recordsBean.getOrderCode());
            }

            @Override
            public void onPaymentClickListerer(XLOrderDetailsBean recordsBean) {
                //立即付款
                XLCashierActivity.jumpCashierActivity(mContext, recordsBean);
            }

            @Override
            public void onItemClickListerer(XLOrderDetailsBean recordsBean) {
                //item点击事件
                XLOrderDetailsActivity.jumpOrderDetailsActivity(mContext, recordsBean.getOrderId());
            }
        });
        getOrderList();

        return view;
    }

    /**
     * 获取订单列表
     */
    public void getOrderList() {

        if (!isAdded()) {
            return;
        }

        APIManager.startRequest(mOrderService.getOrderPage(pageOffset, pageSize, orderStatus), new BaseRequestListener<MyOrderDetailBean>() {
            @Override
            public void onSuccess(MyOrderDetailBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();

                if (result != null) {
                    if (result.getRecords() != null) {
                        if (pageOffset == 1) {
                            recordsBeanList.clear();
                        }
                        totalPage = result.getPages();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        recordsBeanList.addAll(result.getRecords());

                        if (pageOffset == 1) {
                            orderAdapter.setNewData(result.getRecords());
                            if (result.getRecords().size() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                recyclerOrder.setVisibility(View.GONE);
                            } else {
                                noDataLayout.setVisibility(View.GONE);
                                recyclerOrder.setVisibility(View.VISIBLE);
                            }


                        } else {
                            orderAdapter.addData(result.getRecords());
                        }
                    }
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        if (pageOffset < totalPage) {
            pageOffset++;
            getOrderList();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        getOrderList();
    }

    /**
     * 取消订单
     */
    CancelOrderDialog cancelOrderDialog;

    private void cancelOrder(final String orderCode) {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(mContext);
            cancelOrderDialog.setOnReasonSelectListener(new CancelOrderDialog.OnReasonSelectListener() {
                @Override
                public void onReasonSelected(String reason) {
                    ToastUtil.success(reason);
                    requestCancelOrder(orderCode, reason);
                }
            });
        }
        cancelOrderDialog.show();

    }

    /**
     * 取消订单请求
     *
     * @param orderCode
     * @param reason
     */
    private void requestCancelOrder(String orderCode, String reason) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        params.put("reason", reason);
        APIManager.startRequest(mOrderService.cancelOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(mContext) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(CANCEL_ORDER));
                getOrderList();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    /**
     * 提醒发货
     */
    private void remindDelivery(String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        APIManager.startRequest(mOrderService.remindDelivery(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(mContext) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.success("已提醒");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    /**
     * 订单-确认收货
     */
    private void confirmReceived(String orderCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderCode", orderCode);
        APIManager.startRequest(mOrderService.confirmReceived(APIManager.buildJsonBody(params)), new BaseRequestListener<Object>(mContext) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                ToastUtil.success("已提醒");
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

}
