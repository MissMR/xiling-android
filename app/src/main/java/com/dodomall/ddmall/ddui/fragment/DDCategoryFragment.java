package com.dodomall.ddmall.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDCategoryActivity;
import com.dodomall.ddmall.ddui.adapter.CategoryAdapter;
import com.dodomall.ddmall.ddui.adapter.CategoryNavigationAdapter;
import com.dodomall.ddmall.ddui.bean.CategoryBean;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.module.search.SearchActivity;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Jigsaw
 * @date 2018/12/10
 * 首页分类
 */
public class DDCategoryFragment extends BaseFragment {

    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.rv_category_nav)
    RecyclerView mRvCategoryNav;
    @BindView(R.id.rv_category)
    RecyclerView mRvCategory;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    Unbinder unbinder;

    private IProductService mProductService;
    private CategoryNavigationAdapter mCategoryNavigationAdapter;
    private CategoryAdapter mCategoryAdapter;
    private boolean mDataLoaded = false;

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
                mCategoryNavigationAdapter.setCategoryActive(position);
                ((LinearLayoutManager) mRvCategory.getLayoutManager()).scrollToPositionWithOffset(position, 0);
            }
        });

        mRvCategoryNav.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCategoryNav.setAdapter(mCategoryNavigationAdapter);


        mCategoryAdapter = new CategoryAdapter();
        mCategoryAdapter.setOnCategoryGridItemClickListener(new CategoryAdapter.OnCategoryGridItemClickListener() {
            @Override
            public void onCategoryGridItemClick(BaseQuickAdapter<CategoryBean, BaseViewHolder> adapter, CategoryBean parentCategory, View view, int position) {
                CategoryBean bean = adapter.getItem(position);
                DDCategoryActivity.jumpTo(getContext(), bean.getCategoryId(), parentCategory.getCategoryName(), position);
            }
        });

        mRvCategory.setAdapter(mCategoryAdapter);
        mRvCategory.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvCategory.setNestedScrollingEnabled(false);
        mRvCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCategoryNavigationAdapter.setCategoryActive(getCategoryActiveIndex(dy));
            }
        });

        mSmartRefreshLayout.setEnableLoadMore(false);
        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getCategoryList();
            }
        });

        mSmartRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                DLog.i("rv : " + mRvCategory.getHeight());
                DLog.i("sr : " + mSmartRefreshLayout.getHeight());
                mCategoryAdapter.setHeightThreshold(mSmartRefreshLayout.getHeight());
            }
        });

    }

    private int getCategoryActiveIndex(int dy) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRvCategory.getLayoutManager();
        return linearLayoutManager.findFirstVisibleItemPosition();
    }

    private void getCategoryList() {
        APIManager.startRequest(mProductService.getCategorys(), new BaseRequestListener<ArrayList<CategoryBean>>(getActivity()) {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(ArrayList<CategoryBean> result) {
                super.onSuccess(result);
                try {
                    mSmartRefreshLayout.finishRefresh();
                    mCategoryNavigationAdapter.replaceData(result);
                    mCategoryAdapter.replaceData(result);
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

    @OnClick({R.id.tv_search, R.id.fl_search})
    void onClickSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !mDataLoaded) {
            getCategoryList();
            mDataLoaded = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getCategoryList();
        mDataLoaded = true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
