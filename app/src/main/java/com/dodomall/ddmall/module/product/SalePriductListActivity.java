package com.dodomall.ddmall.module.product;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.category.adapter.ProductListAdapter;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.decoration.ListDividerDecoration;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalePriductListActivity extends BaseActivity implements PageManager.RequestListener {

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
        setTitle("特卖会场");
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
                mIProductService.getSaleSkuList(page, 20),
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
