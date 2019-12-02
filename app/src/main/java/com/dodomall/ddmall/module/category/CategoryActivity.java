package com.dodomall.ddmall.module.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.category.adapter.ProductListAdapter;
import com.dodomall.ddmall.module.search.SearchActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.dodomall.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.layoutSwitchBtn)
    protected ImageView mLayoutSwitchBtn;
    private PageManager mPageManager;
    private LinearLayoutManager mSingleColumnLayoutManager;
    private GridLayoutManager mDoubleColumnLayoutManager;
    private ListDividerDecoration mListDividerDecoration;
    private ProductListAdapter mAdapter;
    private IProductService mProductService;
    private String mCategoryId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_layout);
        getParam();
        ButterKnife.bind(this);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        mSingleColumnLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDoubleColumnLayoutManager = new GridLayoutManager(this, 2);
        mListDividerDecoration = new ListDividerDecoration(this);
        mAdapter = new ProductListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        try {
            mPageManager = PageManager.getInstance()
                    .setSwipeRefreshLayout(mRefreshLayout)
                    .setRecyclerView(mRecyclerView)
                    .setLayoutManager(mSingleColumnLayoutManager)
                    .setRequestListener(this)
                    .build(this);
        } catch (PageManager.PageManagerException e) {
            e.printStackTrace();
        }
    }

    void getParam() {
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mCategoryId = getIntent().getExtras().getString("categoryId");
        }
        if (mCategoryId == null || mCategoryId.isEmpty()) {
            ToastUtil.error("参数错误");
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPageManager.onRefresh();
    }

    @OnClick(R.id.layoutSwitchBtn)
    protected void switchLayout() {
        mLayoutSwitchBtn.setSelected(!mLayoutSwitchBtn.isSelected());
        if (mLayoutSwitchBtn.isSelected()) {
            mRecyclerView.setLayoutManager(mDoubleColumnLayoutManager);
            mRecyclerView.removeItemDecoration(mListDividerDecoration);
            mAdapter.setColumns(2);
        } else {
            mRecyclerView.setLayoutManager(mSingleColumnLayoutManager);
            mRecyclerView.addItemDecoration(mListDividerDecoration);
            mAdapter.setColumns(1);
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.searchView)
    protected void viewSearch() {
        startActivity(new Intent(this, SearchActivity.class));
    }

    @OnClick(R.id.backBtn)
    protected void onBack() {
        finish();
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mProductService.getCategoryProductList(mCategoryId, page), new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(this) {
            @Override
            public void onSuccess(PaginationEntity<SkuInfo, Object> result) {
                if (page == 1) {
                    mAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mAdapter.addItems(result.list);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }
}
