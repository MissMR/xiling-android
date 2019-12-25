package com.xiling.dduis.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.dduis.adapter.HomeActivityAdapter;
import com.xiling.dduis.adapter.HomeBrandAdapter;
import com.xiling.dduis.adapter.HomeHotAdapter;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.adapter.HomeTabAdapter;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.BannerManager;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.youth.banner.Banner;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;


public class DDHomeMainFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    Unbinder unbinder;
    DDHomeService homeService;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.recyclerView_hot)
    RecyclerView recyclerViewHot;
    HomeHotAdapter hotAdapter;
    List<HomeDataBean.SecondCategoryListBean> hots = new ArrayList<>();

    @BindView(R.id.banner)
    Banner banner;
    List<String> bannerList = new ArrayList<>();


    @BindView(R.id.recyclerView_tab)
    RecyclerView recyclerViewTab;
    List<HomeDataBean.TabListBean> tabListBeanList = new ArrayList<>();
    HomeTabAdapter homeTabAdapter;

    @BindView(R.id.recyclerView_activity)
    RecyclerView recyclerViewActivity;
    List<HomeDataBean.ActivityListBean> activityBeanList = new ArrayList<>();
    HomeActivityAdapter activityAdapter;

    @BindView(R.id.recyclerView_brand)
    RecyclerView recyclerViewBrand;
    List<HomeDataBean.BrandHotSaleListBean> brandList = new ArrayList<>();
    HomeBrandAdapter brandAdapter;
    int brandPosition = 1, brandSize = 0;
    @BindView(R.id.tv_brandPosition)
    TextView tvBrandPosition;
    @BindView(R.id.tv_brandSize)
    TextView tvBrandSize;
    @BindView(R.id.rel_brand_head)
    RelativeLayout relBrandHead;

    @BindView(R.id.recyclerView_recommend)
    RecyclerView recyclerViewRecommend;
    List<HomeRecommendDataBean.DatasBean> recommendDataList = new ArrayList<>();
    ShopListAdapter recommendAdapter;
    int pageOffset = 1,pageSize = PAGE_SIZE,totalPage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //初始化协议
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        View view = inflater.inflate(R.layout.fragment_s_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        requestData();
        return view;
    }

    private void initView() {
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        recyclerViewHot.setLayoutManager(gridLayoutManager);
        hotAdapter = new HomeHotAdapter(R.layout.item_home_hot, hots);
        recyclerViewHot.setAdapter(hotAdapter);

        LinearLayoutManager tabLayoutManager = new LinearLayoutManager(getActivity());
        tabLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewTab.setLayoutManager(tabLayoutManager);
        homeTabAdapter = new HomeTabAdapter(R.layout.item_home_tab, tabListBeanList);
        recyclerViewTab.setAdapter(homeTabAdapter);

        LinearLayoutManager activityLayoutManager = new LinearLayoutManager(getActivity());
        activityLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewActivity.setLayoutManager(activityLayoutManager);
        activityAdapter = new HomeActivityAdapter(R.layout.item_home_activity, activityBeanList);
        recyclerViewActivity.setAdapter(activityAdapter);


        LinearLayoutManager bannerLayoutManager = new LinearLayoutManager(getActivity());
        bannerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewBrand.setLayoutManager(bannerLayoutManager);
        brandAdapter = new HomeBrandAdapter(R.layout.item_home_brand, brandList);
        recyclerViewBrand.setAdapter(brandAdapter);
        PagerSnapHelper snapHelper = new PagerSnapHelper() {
            @Override
            public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
                int targetPos = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
                //监听滑动到第几个
                brandPosition = targetPos + 1;
                tvBrandPosition.setText(brandPosition + "");
                return targetPos;
            }
        };
        snapHelper.attachToRecyclerView(recyclerViewBrand);

        GridLayoutManager recommendLayoutManager = new GridLayoutManager(getActivity(),2){
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerViewRecommend.setLayoutManager(recommendLayoutManager);
        recyclerViewRecommend.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(),12), ScreenUtils.dip2px(getActivity(),12)));
        recommendAdapter = new ShopListAdapter(R.layout.item_home_recommend,recommendDataList);
        recyclerViewRecommend.setAdapter(recommendAdapter);
    }

    /**
     * 请求数据
     */
    private void requestData() {
        APIManager.startRequest(homeService.getHomeData(), new BaseRequestListener<HomeDataBean>() {
            @Override
            public void onSuccess(HomeDataBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                if (result != null) {
                    //热搜
                    hots = result.getSecondCategoryList();
                    if (hots == null) {
                        hots = new ArrayList<>();
                    }
                    //如果超过四个，只保留四个
                    if (hots.size() > 4) {
                        hots.subList(0, 3);
                    }

                    hotAdapter.setNewData(hots);

                    //轮播图
                    bannerList.clear();
                    if (result.getBannerList() != null) {
                        for (HomeDataBean.BannerListBean bannerBean : result.getBannerList()) {
                            if (bannerBean.getImgUrl() != null) {
                                bannerList.add(bannerBean.getImgUrl());
                            }
                        }
                    }
                    BannerManager.startBanner(banner, bannerList);

                    //tab
                    tabListBeanList.clear();
                    if (result.getTabList() != null) {
                        tabListBeanList = result.getTabList();
                    }
                    homeTabAdapter.setNewData(tabListBeanList);

                    //activity
                    activityBeanList.clear();
                    if (result.getActivityList() != null) {
                        activityBeanList = result.getActivityList();
                    }
                    activityAdapter.setNewData(activityBeanList);

                    //brand

                    if (result.getBrandHotSaleList() != null) {
                        brandList = result.getBrandHotSaleList();
                    }

                    if (brandList == null) {
                        brandList = new ArrayList<>();
                    }
                    if (brandList.size() > 0) {
                        recyclerViewBrand.setVisibility(View.VISIBLE);
                        relBrandHead.setVisibility(View.VISIBLE);
                        brandAdapter.setNewData(brandList);
                        brandSize = brandList.size();
                        tvBrandPosition.setText(brandPosition + "");
                        tvBrandSize.setText(brandSize + "");
                    } else {
                        recyclerViewBrand.setVisibility(View.GONE);
                        relBrandHead.setVisibility(View.GONE);
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
        requestRecommend();
    }

    /**
     * 请求推荐数据
     */
    private void requestRecommend(){
        APIManager.startRequest(homeService.getHomeRecommendData(pageOffset,pageSize), new BaseRequestListener<HomeRecommendDataBean>() {
            @Override
            public void onSuccess(HomeRecommendDataBean result) {
                super.onSuccess(result);
                smartRefreshLayout.finishRefresh();
                smartRefreshLayout.finishLoadMore();
                if (result != null){
                    if (result.getDatas() != null){
                        if (pageOffset == 1){
                            recommendDataList.clear();
                        }
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage){
                            smartRefreshLayout.setEnableLoadMore(false);
                        }else{
                            smartRefreshLayout.setEnableLoadMore(true);
                        }

                        recommendDataList.addAll(result.getDatas());

                        if (pageOffset == 1){
                            recommendAdapter.setNewData(result.getDatas());
                        }else{
                            recommendAdapter.addData(result.getDatas());
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        pageOffset++;
        requestRecommend();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        requestData();
    }
}
