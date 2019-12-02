package com.dodomall.ddmall.module.foot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.product.event.MsgProduct;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IFootService;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FootListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected View mNoDataLayout;
    private IFootService mFootService;
    private FootAdapter mFootAdapter;
    private PageManager mPageManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);
        ButterKnife.bind(this);
        mFootService = ServiceManager.getInstance().createService(IFootService.class);
        mFootAdapter = new FootAdapter(this);
        mRecyclerView.setAdapter(mFootAdapter);
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("足迹");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPageManager.onRefresh();
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mFootService.getFootList(page), new BaseRequestListener<PaginationEntity<SkuInfo, Object>>(mRefreshLayout) {
            @Override
            public void onSuccess(PaginationEntity<SkuInfo, Object> result) {
                if (page == 1) {
                    mFootAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mFootAdapter.addItems(result.list);
                mNoDataLayout.setVisibility(result.total > 0 ? View.GONE : View.VISIBLE);
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

    private void delFoot(final SkuInfo skuInfo) {
        APIManager.startRequest(mFootService.delViewRecord(skuInfo.productId), new BaseRequestListener<Object>(mRefreshLayout) {
            @Override
            public void onSuccess(Object result) {
                mFootAdapter.getItems().remove(skuInfo);
                mFootAdapter.notifyDataSetChanged();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MsgProduct msgProduct) {
        switch (msgProduct.getAction()) {
            case MsgProduct.DEL_VIEW_HOSTORY:
                delFoot(msgProduct.getSkuInfo());
                break;
        }
    }


}
