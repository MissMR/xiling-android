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
import com.xiling.ddui.adapter.BalanceAdapter;
import com.xiling.ddui.bean.BalanceDetailsBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * pt
 * 余额分类
 */
public class IncomeIndexFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    INewUserService iNewUserService;
    String balanceType = "";

    int pageOffset = 1, pageSize = PAGE_SIZE;
    private int totalPage = 0;

    @BindView(R.id.noDataLayout)
    NoData noDataLayout;
    @BindView(R.id.recycler_order)
    RecyclerView recyclerOrder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    List<BalanceDetailsBean.DataBean> dataBeanList = new ArrayList<>();
    BalanceAdapter balanceAdapter;

    public static IncomeIndexFragment newInstance(String balanceType) {
        IncomeIndexFragment fragment = new IncomeIndexFragment();
        Bundle args = new Bundle();
        args.putString("balanceType", balanceType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_balance_details, container, false);
        unbinder = ButterKnife.bind(this, view);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);
        if (getArguments() != null) {
            balanceType = getArguments().getString("balanceType");
        }
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);
        noDataLayout.setTextView("您暂时没有当前数据");
        balanceAdapter = new BalanceAdapter();
        recyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerOrder.setAdapter(balanceAdapter);
        recyclerOrder.addItemDecoration(new SpacesItemDecoration(0, 1));

        if (balanceType.equals("收益")) {
            getBalanceDeteil();
        } else {
            //暂不支持下拉刷新
            smartRefreshLayout.setEnableLoadMore(false);
            smartRefreshLayout.setEnableRefresh(false);
            noDataLayout.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private void getBalanceDeteil() {
        if (!isAdded()) {
            smartRefreshLayout.finishLoadMore();
            smartRefreshLayout.finishRefresh();
            return;
        }
        APIManager.startRequest(iNewUserService.getIncomeList(pageOffset, pageSize), new BaseRequestListener<BalanceDetailsBean>() {
            @Override
            public void onSuccess(BalanceDetailsBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                if (result != null) {
                    if (result.getDatas() != null) {
                        if (pageOffset == 1) {
                            dataBeanList.clear();
                        }
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        dataBeanList.addAll(result.getDatas());
                        if (pageOffset == 1) {
                            balanceAdapter.setNewData(result.getDatas());
                            if (dataBeanList.size() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                recyclerOrder.setVisibility(View.GONE);
                            } else {
                                noDataLayout.setVisibility(View.GONE);
                                recyclerOrder.setVisibility(View.VISIBLE);
                            }


                        } else {
                            balanceAdapter.addData(result.getDatas());
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                smartRefreshLayout.finishLoadMore();
                smartRefreshLayout.finishRefresh();
                ToastUtil.error(e.getMessage());
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
            getBalanceDeteil();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        getBalanceDeteil();
    }
}
