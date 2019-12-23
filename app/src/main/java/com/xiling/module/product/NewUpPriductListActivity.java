package com.xiling.module.product;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.xiling.R;
import com.xiling.module.category.adapter.ProductListAdapter;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.decoration.ListDividerDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PageManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewUpPriductListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;
    @BindView(R.id.rvList)
    RecyclerView mRvList;
    private ImageView mRightImageView;


    private LinearLayoutManager mSingleColumnLayoutManager;
    private GridLayoutManager mDoubleColumnLayoutManager;
    private ListDividerDecoration mListDividerDecoration;
    private ProductListAdapter mAdapter;
    private PageManager mPageManager;
    private IProductService mIProductService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_up_priduct_list);
        ButterKnife.bind(this);

        mIProductService = ServiceManager.getInstance().createService(IProductService.class);
        initView();
    }

    private void initView() {
        setTitle("每周上新");
        setLeftBlack();
        getHeaderLayout().setRightDrawable(R.drawable.layout_switch_selector);
        mRightImageView = getHeaderLayout().getRightImageView();
        mRightImageView.setOnClickListener(new SwitchOnClickListener());

        mSingleColumnLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mDoubleColumnLayoutManager = new GridLayoutManager(this, 2);
        mListDividerDecoration = new ListDividerDecoration(this);
        mAdapter = new ProductListAdapter(this);
        mRvList.setAdapter(mAdapter);
        try {
            mPageManager = PageManager.getInstance()
                    .setSwipeRefreshLayout(mLayoutRefresh)
                    .setRecyclerView(mRvList)
                    .setLayoutManager(mSingleColumnLayoutManager)
                    .setRequestListener(this)
                    .build(this);
        } catch (PageManager.PageManagerException e) {
            e.printStackTrace();
        }
        mPageManager.onRefresh();
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(
                mIProductService.getNewUpSkuList(page, 20),
                new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(mLayoutRefresh) {
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
                        mLayoutRefresh.setRefreshing(false);
                    }
                });
    }

    public class SwitchOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            mRightImageView.setSelected(!mRightImageView.isSelected());
            if (mRightImageView.isSelected()) {
                mRvList.setLayoutManager(mDoubleColumnLayoutManager);
                mPageManager.setLayoutManager(mDoubleColumnLayoutManager);
                mRvList.removeItemDecoration(mListDividerDecoration);
                mAdapter.setColumns(2);
            } else {
                mRvList.setLayoutManager(mSingleColumnLayoutManager);
                mPageManager.setLayoutManager(mSingleColumnLayoutManager);
                mRvList.addItemDecoration(mListDividerDecoration);
                mAdapter.setColumns(1);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

}
