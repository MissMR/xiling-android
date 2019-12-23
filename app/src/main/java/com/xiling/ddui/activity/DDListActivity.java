package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/9/15
 * 常规list activity
 */
public abstract class DDListActivity<T> extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    protected int mPage = 1;
    protected int mSize = 10;

    protected BaseQuickAdapter<T, ? extends BaseViewHolder> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddlist);
        ButterKnife.bind(this);
        initData();
        initView();
        if (autoRefresh()) {
            mSmartRefreshLayout.autoRefresh();
        }
    }

    private void initData() {
        mAdapter = getBaseQuickAdapter();
    }

    private void initView() {
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setAdapter(mAdapter);

    }

    protected void getList() {
        if (null == getApiObservable()) {
            return;
        }
        APIManager.startRequest(getApiObservable(), new BaseRequestListener<ListResultBean<T>>() {
            @Override
            public void onSuccess(ListResultBean<T> result) {
                super.onSuccess(result);
                if (checkNull(mSmartRefreshLayout)) {
                    return;
                }
                onRequestSuccess(result);
                finishRefresh();
                List<T> list = result.getDatas();
                if (null != list && list.size() > 0) {
                    mAdapter.addData(list);
                    mSmartRefreshLayout.setNoMoreData(false);
                } else {
                    if (mPage > 0) {
                        if (mPage == 1) {
                            mAdapter.getData().clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        mSmartRefreshLayout.setNoMoreData(true);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (checkNull(mSmartRefreshLayout)) {
                    return;
                }
                finishRefresh();
            }
        });
    }

    private boolean checkNull(Object o) {
        return null == o;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected boolean autoRefresh() {
        return true;
    }

    protected void onRequestSuccess(ListResultBean<T> result) {

    }

    protected abstract Observable<RequestResult<ListResultBean<T>>> getApiObservable();

    protected abstract BaseQuickAdapter<T, ? extends BaseViewHolder> getBaseQuickAdapter();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh();
    }

    protected void refresh() {
        mPage = 1;
        mAdapter.getData().clear();
        getList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        getList();
    }

    protected void finishRefresh() {
        ToastUtil.hideLoading();
        if (mPage == 1) {
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

}
