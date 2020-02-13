package com.xiling.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.xiling.ddui.activity.CustomerOrderDetailsActivity;
import com.xiling.ddui.adapter.CustomterOrderAdapter;
import com.xiling.ddui.adapter.MyOrderAdapter;
import com.xiling.ddui.bean.CustomerOrderBean;
import com.xiling.ddui.bean.DetailsBean;
import com.xiling.ddui.bean.MyOrderDetailBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IOrderService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * pt
 * 客户订单-fragment
 */
public class CustomerOrderFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    String type = "";

    IOrderService mOrderService;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;
    @BindView(R.id.recycler_order)
    RecyclerView recyclerOrder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;

    CustomterOrderAdapter orderAdapter;

    /**
     * @param type 结算状态（2 已结算  1 未结算  空 全部）
     * @return
     */
    public static CustomerOrderFragment newInstance(String type) {
        CustomerOrderFragment fragment = new CustomerOrderFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_order, container, false);
        unbinder = ButterKnife.bind(this, view);
        noDataLayout.setTextView("当前暂无客户订单内容");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        orderAdapter = new CustomterOrderAdapter();
        recyclerOrder.setAdapter(orderAdapter);
        recyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));

        orderAdapter.setOnButtomItemClickListener(new CustomterOrderAdapter.OnButtomItemClickListener() {
            @Override
            public void onItemClickListerer(CustomerOrderBean.OrderDetailsBean recordsBean) {
                //点击事件
                Intent intent = new Intent(getActivity(), CustomerOrderDetailsActivity.class);
                intent.putExtra("recordsBean",recordsBean);
                startActivity(intent);
            }
        });

        getClientOrderList();

        return view;
    }

    /**
     * 获取客户订单列表
     */
    public void getClientOrderList() {

        if (!isAdded()) {
            return;
        }
        APIManager.startRequest(mOrderService.getClientOrderList(pageOffset, pageSize, type), new BaseRequestListener<CustomerOrderBean>() {
            @Override
            public void onSuccess(CustomerOrderBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();

                if (result != null) {
                    if (result.getRecords() != null) {
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        if (pageOffset == 1) {
                            List<CustomerOrderBean.OrderDetailsBean> list = result.getRecords();
                          // 测试数据，可以删掉
                            /*CustomerOrderBean.OrderDetailsBean xlOrderDetailsBean = new CustomerOrderBean.OrderDetailsBean();
                            xlOrderDetailsBean.setOrderCode("4827391245");
                            xlOrderDetailsBean.setOrderStatus("待付款");
                            xlOrderDetailsBean.setNickName("李灿灿");
                            xlOrderDetailsBean.setReceiptsIndices(81.00);
                            List<DetailsBean> details = new ArrayList<>();
                            DetailsBean detailsBean = new DetailsBean();
                            detailsBean.setSkuName("【海外直营商品】Swisse 女性青少年复合\n" +
                                    "  维生素60片/瓶");
                            detailsBean.setPrice(372.51);
                            detailsBean.setRetailPrice(506.00 );
                            detailsBean.setQuantity(15);
                            details.add(detailsBean);
                            xlOrderDetailsBean.setClientOrderDetailList(details);

                            list.add(xlOrderDetailsBean);*/

                            orderAdapter.setNewData(list);
                            if (list.size() == 0) {
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
            getClientOrderList();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        getClientOrderList();
    }
}
