package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * @author pt
 * 商品Fragment
 */
public class ShopFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    @BindView(R.id.recycler_shop)
    RecyclerView recyclerShop;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    Unbinder unbinder;
    private IProductService mProductService;

    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;
    List<HomeRecommendDataBean.DatasBean> shopDataList = new ArrayList<>();
    ShopListAdapter shopAdapter;

    private String categoryId;
    private String secondCategoryId;

    private String minPrice, maxPrice;
    private int isShippingFree;
    private int orderBy;
    private int orderType;
    private String keyWord;
    private String brandId;

    HashMap<String, String> requestMap = new HashMap<>();

    public static ShopFragment newInstance(String categoryId, String secondCategoryId, String brandId, String minPrice, String maxPrice, int isShippingFree, int orderBy, int orderType, String keyWord) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("secondCategoryId", secondCategoryId);
        args.putString("brandId", brandId);
        args.putString("minPrice", minPrice);
        args.putString("maxPrice", maxPrice);
        args.putInt("isShippingFree", isShippingFree);
        args.putInt("orderBy", orderBy);
        args.putInt("orderType", orderType);
        args.putString("keyWord", keyWord);
        fragment.setArguments(args);
        return fragment;
    }

    private void getArgumentsData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            categoryId = arguments.getString("categoryId");
            secondCategoryId = arguments.getString("secondCategoryId");
            brandId = arguments.getString("brandId");
            minPrice = arguments.getString("minPrice");
            maxPrice = arguments.getString("maxPrice");
            keyWord = arguments.getString("keyWord");
            isShippingFree = arguments.getInt("isShippingFree");
            orderBy = arguments.getInt("orderBy");
            orderType = arguments.getInt("orderType");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArgumentsData();
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        unbinder = ButterKnife.bind(this, view);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setOnLoadMoreListener(this);
        smartRefreshLayout.setOnRefreshListener(this);

        GridLayoutManager shopLayoutManager = new GridLayoutManager(getActivity(), 2);

        recyclerShop.setLayoutManager(shopLayoutManager);
        recyclerShop.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 12), ScreenUtils.dip2px(getActivity(), 12)));
        shopAdapter = new ShopListAdapter(R.layout.item_home_recommend, shopDataList);
        recyclerShop.setAdapter(shopAdapter);


        requestShop(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);


        return view;
    }

    /**
     * 请求数据
     */
    public void requestShop(String minPrice, String maxPrice, int isShippingFree, int orderBy, int orderType, String keyWord) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putString("minPrice",minPrice);
            arguments.putString("maxPrice",maxPrice);
            arguments.putString("keyWord",keyWord);
            arguments.putInt("isShippingFree",isShippingFree);
            arguments.putInt("orderBy",orderBy);
            arguments.putInt("orderType",orderType);
        }
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.isShippingFree = isShippingFree;
        this.orderBy = orderBy;
        this.orderType = orderType;
        this.keyWord = keyWord;
        requestMap.clear();
        if (!TextUtils.isEmpty(categoryId)) {
            requestMap.put("categoryId", categoryId);
        }

        if (!TextUtils.isEmpty(secondCategoryId)) {
            requestMap.put("secondCategoryIdList", secondCategoryId+"");
        }

        if (!TextUtils.isEmpty(brandId)) {
            requestMap.put("brandId", brandId);
        }

        if (!TextUtils.isEmpty(minPrice)) {
            requestMap.put("minPrice", minPrice);
        }

        if (!TextUtils.isEmpty(maxPrice)) {
            requestMap.put("maxPrice", maxPrice);
        }
        if (!TextUtils.isEmpty(keyWord)) {
            requestMap.put("keyWord", keyWord);
        }

        requestMap.put("isShippingFree", isShippingFree + "");
        requestMap.put("orderBy", orderBy + "");
        requestMap.put("orderType", orderType + "");
        requestMap.put("pageOffset", pageOffset + "");
        requestMap.put("pageSize", pageSize + "");
        Log.d("requestJSON", "request map = " + requestMap.toString());
        if (mProductService != null && getActivity() != null){
            APIManager.startRequest(mProductService.getProductList(requestMap), new BaseRequestListener<HomeRecommendDataBean>(getActivity()) {
                @Override
                public void onSuccess(HomeRecommendDataBean result) {
                    super.onSuccess(result);
                    smartRefreshLayout.finishRefresh();
                    smartRefreshLayout.finishLoadMore();

                    if (result != null) {
                        if (result.getDatas() != null) {
                            if (pageOffset == 1) {
                                shopDataList.clear();
                            }
                            totalPage = result.getTotalPage();
                            // 如果已经到最后一页了，关闭上拉加载
                            if (pageOffset >= totalPage) {
                                smartRefreshLayout.setEnableLoadMore(false);
                            } else {
                                smartRefreshLayout.setEnableLoadMore(true);
                            }

                            shopDataList.addAll(result.getDatas());

                            if (pageOffset == 1) {
                                shopAdapter.setNewData(result.getDatas());
                            } else {
                                shopAdapter.addData(result.getDatas());
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
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        //上拉加载
        pageOffset++;
        requestShop(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        //下拉刷新
        pageOffset = 1;
        requestShop(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
