package com.xiling.ddmall.ddui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.component.NoData;
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
 * 为了适配 老的 适配器 com.dodomall.ddmall.shared.basic.BaseAdapter
 */
public abstract class DDBaseListFragment<T> extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.no_data)
    NoData mNoData;

    Unbinder unbinder;
    private boolean isLoaded = false;
    protected int mPage = 1;
    protected int mSize = 10;

    protected BaseAdapter mAdapter;

    private OnRequestSuccessListener mOnRequestSuccessListener;

    public DDBaseListFragment() {
        // Required empty public constructor
    }

    public void setOnRequestSuccessListener(OnRequestSuccessListener listener) {
        mOnRequestSuccessListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ddlist, null);
        unbinder = ButterKnife.bind(this, rootView);
        init();
        initView();
        return rootView;
    }

    protected abstract void init();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && !isLoaded) {
            ToastUtil.showLoading(getContext());
            refresh();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getUserVisibleHint() && !isLoaded) {
            ToastUtil.showLoading(getContext());
            refresh();
        }
    }

    private void initView() {
        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mAdapter = getBaseAdapter();

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
                finishRefresh();
                List<T> list = result.getDatas();
                if (null != list && list.size() > 0) {
                    mAdapter.addItems(list);
                    showNoData(false);
                } else {
                    if (mPage > 0) {
                        if (mPage == 1) {
                            mAdapter.getItems().clear();
                            mAdapter.notifyDataSetChanged();
                            showNoData(true);
                        }
                        mSmartRefreshLayout.setNoMoreData(true);
                    }
                }
                if (mOnRequestSuccessListener != null) {
                    mOnRequestSuccessListener.onRequestSuccess();
                }
                isLoaded = true;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                if (checkNull(mSmartRefreshLayout)) {
                    return;
                }
                finishRefresh();
                isLoaded = true;
            }
        });
    }

    private void showNoData(boolean isShowNoData) {
        mNoData.setVisibility(isShowNoData ? View.VISIBLE : View.GONE);
        mRecyclerView.setVisibility(isShowNoData ? View.GONE : View.VISIBLE);
    }

    private boolean checkNull(Object o) {
        return null == o;
    }

    protected abstract Observable<RequestResult<ListResultBean<T>>> getApiObservable();

    protected abstract BaseAdapter getBaseAdapter();

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        refresh();
    }

    public void refresh() {
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
            ToastUtil.hideLoading();
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    public interface OnRequestSuccessListener {
        void onRequestSuccess();
    }
}
