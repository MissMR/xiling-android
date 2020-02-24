package com.xiling.ddui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.component.NoData;
import com.xiling.shared.manager.APIManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/9/15
 * 常规list activity
 * BaseAdapter
 */
public abstract class DDBaseListActivity<T> extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    protected int mPage = 1;
    protected int mSize = 10;

    protected BaseAdapter mAdapter;
    @BindView(R.id.noDataLayout)
    NoData noDataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddlist);
        ButterKnife.bind(this);
        noDataLayout.setTextView("暂无数据");
        initData();
        initView();
        getList();
    }

    public void setNoDataLayout(String text) {
        noDataLayout.setTextView(text);
    }

    private void initData() {
        mAdapter = getBaseAdapter();
    }

    private void initView() {
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

    }

    private void getList() {
        if (null == getApiObservable()) {
            return;
        }
        APIManager.startRequest(getApiObservable(), new BaseRequestListener<ListResultBean<T>>() {
            @Override
            public void onSuccess(ListResultBean<T> result) {
                super.onSuccess(result);
                finishRefresh();
                if (mPage >= result.getTotalPage()) {
                    mSmartRefreshLayout.setEnableLoadMore(false);
                } else {
                    mSmartRefreshLayout.setEnableLoadMore(true);
                }
                List<T> list = result.getDatas();
                if (mPage == 1) {
                    mAdapter.getItems().clear();
                    if (null != list && list.size() > 0) {
                        mAdapter.addItems(list);
                        noDataLayout.setVisibility(View.GONE);
                    } else {
                        noDataLayout.setVisibility(View.VISIBLE);
                    }
                } else {
                    mAdapter.addItems(list);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                finishRefresh();
            }
        });
    }


    protected abstract Observable<RequestResult<ListResultBean<T>>> getApiObservable();

    protected abstract BaseAdapter getBaseAdapter();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 1;
        mAdapter.getItems().clear();
        mAdapter.notifyDataSetChanged();
        getList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        getList();
    }

    protected void finishRefresh() {
        if (mPage == 1) {
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

}
