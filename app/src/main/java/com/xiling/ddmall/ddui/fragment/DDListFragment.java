package com.xiling.ddmall.ddui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/9/15
 */
public abstract class DDListFragment<T> extends Fragment implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.rl_content)
    RelativeLayout mRlContent;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    Unbinder unbinder;

    protected int mPage = 1;
    protected int mSize = 10;

    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    // 是否已经加载过的标志
    private boolean mIsDataLoaded = false;
    // 是否已经执行了onCreateView
    private boolean mIsViewCreated = false;
    private boolean mEnableLazyLoad = false;

    public DDListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_ddlist, null);
        unbinder = ButterKnife.bind(this, rootView);
        initView();
        init();
        mIsViewCreated = true;
        return rootView;
    }

    protected abstract void init();

    protected void setEnableLazyLoad(boolean enableLazyLoad) {
        this.mEnableLazyLoad = enableLazyLoad;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initView() {
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mAdapter = getBaseQuickAdapter();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                mIsDataLoaded = true;
                finishRefresh();
                List<T> list = result.getDatas();
                if (null != list && list.size() > 0) {
                    if (mPage == 1 && mAdapter.getEmptyView() != null) {
                        mAdapter.getEmptyView().setVisibility(View.GONE);
                    }
                    mAdapter.addData(list);
                } else {
                    if (mPage > 0) {
                        if (mPage == 1) {
                            if (mAdapter.getEmptyView() != null) {
                                mAdapter.getEmptyView().setVisibility(View.VISIBLE);
                            }
                            mAdapter.getData().clear();
                            mAdapter.notifyDataSetChanged();
                        }
                        mSmartRefreshLayout.setNoMoreData(true);
                    }
                }
                onRequestSuccess(result);
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

    protected void onRequestSuccess(ListResultBean<T> resultBean) {
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLazyRefresh();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        checkLazyRefresh();
    }

    private void checkLazyRefresh() {
        if (mEnableLazyLoad && getUserVisibleHint() && mIsViewCreated && !mIsDataLoaded) {
            // 懒加载
            ToastUtil.showLoading(getActivity());
            refresh();
        }
    }

    private boolean checkNull(Object o) {
        return null == o;
    }

    protected abstract Observable<RequestResult<ListResultBean<T>>> getApiObservable();

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getBaseQuickAdapter();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh();
    }

    public void refresh() {
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
        if (mPage == 1) {
            ToastUtil.hideLoading();
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

}
