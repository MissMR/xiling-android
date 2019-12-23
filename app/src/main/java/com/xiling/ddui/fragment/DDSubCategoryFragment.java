package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.adapter.DDSearchSkuAdapter;
import com.xiling.ddui.tools.DLog;
import com.xiling.dduis.adapter.HomeSpuAdapter;
import com.xiling.dduis.bean.DDProductBean;
import com.xiling.dduis.bean.DDProductPageBean;
import com.xiling.dduis.custom.FilterLayoutView;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.component.NoData;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DDSubCategoryFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener, FilterLayoutView.FilterListener {

    public static final String kCategoryId = "DDSubCategoryFragment.kCategoryId";
    public static final String kCategoryParentId = "DDSubCategoryFragment.kCategoryParentId";

    String categoryParentId = "";
    String categoryId = "";

    DDSearchSkuAdapter adapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DLog.d("=========" + clazzName + " onCreate " + categoryId + "=========");
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);
        if (getArguments() != null) {
            categoryId = getArguments().getString(kCategoryId);
            categoryParentId = getArguments().getString(kCategoryParentId);
        }
    }

    /*单页数据量*/
    static int page_Size = 15;
    /*默认页码*/
    static int page_default = 1;
    /*当前页码*/
    int page = page_default;

    String s_categorysId = "";
    String s_minPrice = "";
    String s_maxPrice = "";
    int s_orderBy = -1;
    int s_orderType = 0;
    int s_isRush = 0;
    int s_isFreeShip = 0;

    FilterLayoutView filterLayoutView = null;

    @BindView(R.id.layout_filter_and_sort)
    LinearLayout filterAndSortLayout = null;

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.noDataView)
    NoData noDataView;

    LinearLayoutManager gManager = null;
    HomeSpuAdapter mAdapter;

    DDHomeService homeService = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        DLog.d("=========" + clazzName + " onCreateView " + categoryId + "=========");

        View view = inflater.inflate(R.layout.fragment_ddsub_category, container, false);
        ButterKnife.bind(this, view);

        mAdapter = new HomeSpuAdapter(getContext());

        gManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(gManager);
        mRecyclerView.setAdapter(mAdapter);

        noDataView.setTextView("哎呀\n这里没有你要的商品哦~");

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        filterLayoutView = new FilterLayoutView(filterAndSortLayout);
        filterLayoutView.setListener(this);
        filterLayoutView.setCategoryVisibility(false);
        filterLayoutView.setVipFlag(SessionUtil.getInstance().isMaster());

        if (!isDataLoaded) {
            page = page_default;
            loadNetData();
            isDataLoaded = true;
        }

        return view;
    }


    boolean isDataLoaded = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        DLog.d("=========" + clazzName + " setUserVisibleHint(" + isVisibleToUser + ") " + categoryId + "=========");
        if (isVisibleToUser && !isDataLoaded) {
            page = page_default;
            loadNetData();
            isDataLoaded = true;
        }
    }

    String clazzName = this.getClass().getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        DLog.d("=========" + clazzName + " onStart " + categoryId + "=========");
    }

    @Override
    public void onResume() {
        super.onResume();
        DLog.d("=========" + clazzName + " onResume " + categoryId + "=========");
    }

    @Override
    public void onPause() {
        super.onPause();
        DLog.d("=========" + clazzName + " onPause " + categoryId + "=========");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        DLog.d("=========" + clazzName + " onDestroyView " + categoryId + "=========");
        isDataLoaded = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DLog.d("=========" + clazzName + " onDestroy " + categoryId + "=========");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        DLog.d("=========" + clazzName + " onSaveInstanceState " + categoryId + "=========");
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        DLog.d("=========" + clazzName + " setRetainInstance " + categoryId + "=========");
    }

    @Override
    public void onFilterChanged(String categorysId, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice) {
        DLog.d("subCategory.onFilterChanged:" + categorysId + "，" + isFreeShip + "," + isFreeShip + "," + minPrice + "，" + maxPrice);

        s_categorysId = categorysId;

        s_isFreeShip = isFreeShip ? 1 : 0;
        s_isRush = isRush ? 1 : 0;

        s_minPrice = minPrice;
        s_maxPrice = maxPrice;

        page = page_default;
        loadNetData();
    }

    @Override
    public void onSortChanged(int orderBy, int orderType) {
        DLog.d("subCategory.onSortChanged:" + orderBy + "，" + orderType);
        s_orderBy = orderBy;
        s_orderType = orderType;

        page = page_default;
        loadNetData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = page_default;
        loadNetData();
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        loadNetData();
    }

    void loadNetData() {

        if (homeService == null) {
            homeService = ServiceManager.getInstance().createService(DDHomeService.class);
            if (getArguments() != null) {
                categoryId = getArguments().getString(kCategoryId);
            }
        }

        //将用户数据的价格转为分回传服务器
        long minPrice = ConvertUtil.yuan2cent(s_minPrice);
        String minValue = minPrice > -1 ? minPrice + "" : "";
        long maxPrice = ConvertUtil.yuan2cent(s_maxPrice);
        String maxValue = maxPrice > -1 ? maxPrice + "" : "";

        APIManager.startRequest(homeService.getCategoryProductByFilter(
                "",
                categoryId,
                page,
                page_Size,
                s_isRush,
                s_isFreeShip,
                minValue,
                maxValue,
                s_orderType,
                s_orderBy
        ), new RequestListener<DDProductPageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDProductPageBean result) {
                super.onSuccess(result);

                filterLayoutView.setFilterData(
                        s_categorysId,
                        s_minPrice,
                        s_maxPrice,
                        s_orderBy,
                        s_orderType,
                        s_isRush,
                        s_isFreeShip
                );

                ArrayList<DDProductBean> response = result.getDatas();
                if (page > 1) {
                    mAdapter.appendData(response);
                    mSmartRefreshLayout.finishLoadMore();
                } else {
                    if (result.getTotalRecord() > 0) {
                        mAdapter.setData(response);
                        noDataView.setVisibility(View.GONE);
                    } else {
                        mAdapter.clear();
                        noDataView.setVisibility(View.VISIBLE);
                    }
                    mSmartRefreshLayout.finishRefresh();
                }
                int nowCount = mAdapter.getItemCount();
                long total = result.getTotalRecord();
                mSmartRefreshLayout.setNoMoreData(!(nowCount < total));

                for (DDProductBean bean : result.getDatas()) {
                    DLog.i("=>>" + bean.getProductName());
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
