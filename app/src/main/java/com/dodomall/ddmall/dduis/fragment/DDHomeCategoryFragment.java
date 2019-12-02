package com.dodomall.ddmall.dduis.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.CategoryBean;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.dduis.adapter.HomeCategoryAdapter;
import com.dodomall.ddmall.dduis.bean.DDHomeCategoryBean;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.bean.DDProductPageBean;
import com.dodomall.ddmall.dduis.bean.HomeCategoryDataBean;

import com.dodomall.ddmall.dduis.custom.FilterLayoutView;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.DDHomeService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import rx.functions.Action1;
import rx.functions.Func1;


public class DDHomeCategoryFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.recycler_data)
    RecyclerView dataView;

    //一级分类数据
    DDHomeCategoryBean categoryBean = null;
    //二级分类数据
    ArrayList<CategoryBean> subCategorys = new ArrayList<>();

    HomeCategoryAdapter adapter = null;
    DDHomeService homeService = null;

    int Default_Page = 1;
    int page = Default_Page;
    int pageSize = 15;

    //搜索条件
    String s_categorysId = "";
    String s_minPrice = "";
    String s_maxPrice = "";
    int s_orderBy = -1;
    int s_orderType = 0;
    int s_isRush = 0;
    int s_isFreeShip = 0;

    public void setCategoryBean(DDHomeCategoryBean categoryBean) {
        this.categoryBean = categoryBean;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_category, container, false);
        ButterKnife.bind(this, view);
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        adapter = new HomeCategoryAdapter(getContext());

        initView();

        loadData();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    void initView() {

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);

        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);

        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);

        GridLayoutManager gManager = new GridLayoutManager(getContext(), 4);
        gManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //获取这个位置的View类型
                int type = adapter.getItemViewType(position);
                if (type == HomeCategoryDataBean.Type.Menu.ordinal()) {
                    return 1;
                } else {
                    return 4;
                }
            }
        });
        dataView.setLayoutManager(gManager);
        adapter.setHasStableIds(false);
        adapter.setFilterListener(new FilterLayoutView.FilterListener() {
            @Override
            public void onFilterChanged(String categoryIds, boolean isFreeShip, boolean isRush, String minPrice, String maxPrice) {
                DLog.d("fg.onFilterChanged:" + categoryIds + "，" + isFreeShip + "," + isFreeShip + "," + minPrice + "，" + maxPrice);
                s_categorysId = categoryIds;

                s_isFreeShip = isFreeShip ? 1 : 0;
                s_isRush = isRush ? 1 : 0;

                s_minPrice = minPrice;
                s_maxPrice = maxPrice;

                loadOnePage();
            }

            @Override
            public void onSortChanged(int orderBy, int orderType) {
                DLog.d("fg.onSortChanged:" + orderBy + "，" + orderType);
                s_orderBy = orderBy;
                s_orderType = orderType;

                loadOnePage();
            }
        });
        dataView.setAdapter(adapter);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadOnePage();
    }

    void loadOnePage() {
        page = Default_Page;
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.finishLoadMore(150, true, false);
        loadData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page++;
        loadSpuData();
    }

    /**
     * 加载二级分类数据
     */
    void loadData() {
        DLog.i("加载二级分类数据");
        //获取二级分类数据
        if (categoryBean == null) return;
        String categoryId = categoryBean.getCategoryId();
        APIManager.startRequest(homeService.getSubCategory(categoryId + ""), new RequestListener<ArrayList<CategoryBean>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ArrayList<CategoryBean> result) {
                super.onSuccess(result);
                subCategorys = result;
                DLog.i("加载二级分类完成");
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
            }

            @Override
            public void onComplete() {
                loadSpuData();
            }
        });

    }

    /**
     * 加载商品数据
     */
    void loadSpuData() {
        DLog.i("加载商品数据");
        if (categoryBean == null) return;

        //将用户数据的价格转为分回传服务器
        long minPrice = ConvertUtil.yuan2cent(s_minPrice);
        String minValue = minPrice > -1 ? minPrice + "" : "";
        long maxPrice = ConvertUtil.yuan2cent(s_maxPrice);
        String maxValue = maxPrice > -1 ? maxPrice + "" : "";

        //根据指定的条件搜索商品列表
        APIManager.startRequest(homeService.getCategoryProductByFilter(
                categoryBean.getCategoryId(),
                s_categorysId,
                page,
                pageSize,
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

                adapter.setFilterData(
                        s_categorysId,
                        s_minPrice,
                        s_maxPrice,
                        s_orderBy,
                        s_orderType,
                        s_isRush,
                        s_isFreeShip
                );

                if (page == Default_Page) {
                    adapter.setSpuData(result.getDatas());
                } else {
                    adapter.appendSpuData(result.getDatas());
                }

                int nowCount = adapter.getSpuSize();
                long total = result.getTotalRecord();

                boolean hasMore = !(nowCount < total);

                if (page == Default_Page) {
                    refreshLayout.finishRefresh();
                } else {
                    refreshLayout.finishLoadMore();
                }

                refreshLayout.setNoMoreData(hasMore);
//                if (hasMore) {
//                    refreshLayout.finishLoadMoreWithNoMoreData();
//                } else {

//                }
//                    refreshLayout.finishLoadMore(150, true, hasMore);


                DLog.i("loadSpuData:");
                DLog.i("\tpage=>" + page);
                DLog.i("\tnowSize=>" + nowCount);
                DLog.i("\tpageSize=>" + result.getDatas().size());
                DLog.i("\ttotalSize=>" + total);
                DLog.i("\tisNoMoreData=>" + hasMore);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {


                render();
            }
        });
    }

    void render() {

        adapter.setBannerData(categoryBean.getIndexBannerBeanList());
        adapter.setMenuData(subCategorys);

        adapter.showData();
    }
}
