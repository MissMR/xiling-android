package com.xiling.ddmall.module.profit;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.CommonExtra;
import com.xiling.ddmall.shared.bean.Profit;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.PageManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IBalanceService;
import com.xiling.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/16.
 */

public class ProfitListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected View mNoDataLayout;
    private IBalanceService mBalanceService;
    private ProfitAdapter mProfitAdapter;
    private PageManager mPageManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.red));
            QMUIStatusBarHelper.setStatusBarLightMode(this);
        }

        ButterKnife.bind(this);
        mBalanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        mProfitAdapter = new ProfitAdapter(this);
        mRecyclerView.setAdapter(mProfitAdapter);
        try {

            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setSwipeRefreshLayout(mRefreshLayout)
                    .setRequestListener(this)
                    .build(this);
        } catch (PageManager.PageManagerException e) {
            ToastUtil.error("PageManager 初始化失败");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("店多多");
//        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_white);
//        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
        setLeftBlack();
        getHeaderLayout().makeHeaderRed();
        mPageManager.onRefresh();
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mBalanceService.getProfitList(page), new BaseRequestListener<PaginationEntity<Profit, CommonExtra>>(this) {
            @Override
            public void onSuccess(PaginationEntity<Profit, CommonExtra> result) {
                if(page == 1) {
                    mProfitAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mProfitAdapter.setHeaderData(result.ex);
                mProfitAdapter.addItems(result.list);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

}
