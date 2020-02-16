package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.adapter.ClientAdapter;
import com.xiling.ddui.bean.CustomerOrderBean;
import com.xiling.ddui.bean.MyClientListBean;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.API_SALT;
import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * 我的客户
 */
public class MyClientFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    INewUserService iNewUserService;

    String type;
    String searchString;

    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;

    Unbinder unbinder;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;
    @BindView(R.id.recycler_order)
    RecyclerView recyclerOrder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    ClientAdapter clientAdapter;

    public static MyClientFragment newInstance(String type, String searchString) {
        MyClientFragment fragment = new MyClientFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        args.putString("searchString", searchString);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
            searchString = getArguments().getString(searchString);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_client, container, false);
        unbinder = ButterKnife.bind(this, view);
        iNewUserService = ServiceManager.getInstance().createService(INewUserService.class);

        noDataLayout.setTextView("暂无数据");
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        clientAdapter = new ClientAdapter();
        recyclerOrder.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerOrder.setAdapter(clientAdapter);

        getCustomerList();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取客户订单列表
     */
    public void getCustomerList() {

        if (!isAdded()) {
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        if (!TextUtils.isEmpty(type)){
            map.put("customerType",type);
            map.put("pageOffset",pageOffset+"");
            map.put("pageSize",pageSize+"");
        }

        APIManager.startRequest(iNewUserService.getCustomerList(APIManager.buildJsonBody(map)), new BaseRequestListener<MyClientListBean>() {
            @Override
            public void onSuccess(MyClientListBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();

                if (result != null) {
                    if (result.getDatas() != null) {
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            smartRefreshLayout.setEnableLoadMore(false);
                        } else {
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        if (pageOffset == 1) {
                            List<MyClientListBean.DataBean> list = result.getDatas();

                            for (int i = 0;i<3;i++){
                                MyClientListBean.DataBean dataBean = new MyClientListBean.DataBean();
                                dataBean.setMemberName("打开好物");
                                dataBean.setMonthlyConsumption(412.12345);
                                list.add(dataBean);
                            }



                            clientAdapter.setNewData(list);
                            if (list.size() == 0) {
                                noDataLayout.setVisibility(View.VISIBLE);
                                recyclerOrder.setVisibility(View.GONE);
                            } else {
                                noDataLayout.setVisibility(View.GONE);
                                recyclerOrder.setVisibility(View.VISIBLE);
                            }


                        } else {
                            clientAdapter.addData(result.getDatas());
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
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
