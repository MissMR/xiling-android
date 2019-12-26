package com.xiling.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.activity.CategorySecondActivity;
import com.xiling.ddui.adapter.CategoryAdapter;
import com.xiling.ddui.adapter.CategoryBrandAdapter;
import com.xiling.ddui.adapter.CategoryNavigationAdapter;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.image.GlideUtils;
import com.xiling.module.search.SearchActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 首页分类
 */
public class DDCategoryFragment extends BaseFragment {

    @BindView(R.id.tv_search)
    TextView mTvSearch;

    @BindView(R.id.rv_category_nav)
    RecyclerView mRvCategoryNav;

    @BindView(R.id.rv_category)
    RecyclerView rvCategory;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    ArrayList<TopCategoryBean> topCategoryList;
    ArrayList<SecondCategoryBean.SecondCategoryListBean> secondCategoryList;

    Unbinder unbinder;
    @BindView(R.id.sdv_category_banner)
    SimpleDraweeView sdvCategoryBanner;
    @BindView(R.id.tv_category)
    TextView tvCategory;
    @BindView(R.id.ll_brand)
    LinearLayout llBrand;
    @BindView(R.id.rv_category_brand)
    RecyclerView rvCategoryBrand;


    private IProductService mProductService;
    private CategoryNavigationAdapter mCategoryNavigationAdapter;
    private CategoryAdapter mCategoryAdapter;
    private CategoryBrandAdapter categoryBrandAdapter;
    private int childPosition = 0;

    public DDCategoryFragment() {
    }

    public static DDCategoryFragment newInstance() {
        DDCategoryFragment fragment = new DDCategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ddcategory, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {

        mCategoryNavigationAdapter = new CategoryNavigationAdapter();
        mCategoryNavigationAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCategoryNavigationAdapter.setmActiveIndex(position);
                childPosition = position;
                if (topCategoryList.size() > childPosition) {
                    getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                }
            }
        });

        mRvCategoryNav.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCategoryNav.setAdapter(mCategoryNavigationAdapter);


        mCategoryAdapter = new CategoryAdapter();
        rvCategory.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 8), ScreenUtils.dip2px(getActivity(), 10)));
        rvCategory.setAdapter(mCategoryAdapter);
        rvCategory.setLayoutManager(new GridLayoutManager(mContext, 3));

        mCategoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CategorySecondActivity.jumpCategorySecondActivity(mContext,topCategoryList.get(childPosition).getCategoryName(),topCategoryList.get(childPosition).getCategoryId(),secondCategoryList,position);
            }
        });

        categoryBrandAdapter = new CategoryBrandAdapter();
        rvCategoryBrand.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 8), ScreenUtils.dip2px(getActivity(), 10)));
        rvCategoryBrand.setAdapter(categoryBrandAdapter);
        rvCategoryBrand.setLayoutManager(new GridLayoutManager(mContext, 2));

        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (topCategoryList.size() > childPosition) {
                    getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                }
            }
        });


    }


    /**
     * 获取一级分类列表（左侧）
     */
    private void getTopCategory() {
        APIManager.startRequest(mProductService.getTopCategory(), new BaseRequestListener<ArrayList<TopCategoryBean>>(getActivity()) {
            @Override
            public void onSuccess(ArrayList<TopCategoryBean> result) {
                super.onSuccess(result);
                topCategoryList = result;
                mCategoryNavigationAdapter.setNewData(result);
                childPosition = 0;
                if (topCategoryList.size() > childPosition) {
                    getSecondCategory(topCategoryList.get(childPosition).getCategoryId());
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private void getSecondCategory(String nodeId) {
        APIManager.startRequest(mProductService.getSecondCategory(nodeId), new BaseRequestListener<SecondCategoryBean>(getActivity()) {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(SecondCategoryBean result) {
                super.onSuccess(result);
                try {
                    mSmartRefreshLayout.finishRefresh();
                    if (result != null) {
                        GlideUtils.loadImage(mContext, sdvCategoryBanner, topCategoryList.get(childPosition).getBannerUrl());
                        tvCategory.setText(topCategoryList.get(childPosition).getCategoryName());
                        if (result.getSecondCategoryList() != null) {
                            secondCategoryList = result.getSecondCategoryList();
                            mCategoryAdapter.setNewData(result.getSecondCategoryList());
                        } else {
                            mCategoryAdapter.setNewData(new ArrayList<SecondCategoryBean.SecondCategoryListBean>());
                        }

                        if (result.getBrandBeanList() != null && result.getBrandBeanList().size() > 0) {
                            llBrand.setVisibility(View.VISIBLE);
                            categoryBrandAdapter.setNewData(result.getBrandBeanList());
                        } else {
                            llBrand.setVisibility(View.GONE);
                        }


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mSmartRefreshLayout.finishRefresh();
            }
        });
    }

    @OnClick({R.id.tv_search})
    void onClickSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @Override
    public void onResume() {
        super.onResume();
        getTopCategory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
