package com.dodomall.ddmall.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.adapter.DDCommunityDataAdapter;
import com.dodomall.ddmall.ddui.bean.CommunityDataBean;
import com.dodomall.ddmall.module.collect.CollectAdapter;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICollectService;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DDLikeDataFragment extends BaseFragment implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected NoData mNoDataLayout;

    private DDCommunityDataAdapter mDataAdapter;

    private ICollectService mCollectService;
    private PageManager mPageManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_list_layout, container, false);
        ButterKnife.bind(this, view);

        mCollectService = ServiceManager.getInstance().createService(ICollectService.class);

        mDataAdapter = new DDCommunityDataAdapter(getContext());
        //设置为喜欢模式
        mDataAdapter.setMode(DDCommunityDataAdapter.Mode.Like);

        mRecyclerView.setAdapter(mDataAdapter);

        try {
            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setSwipeRefreshLayout(mRefreshLayout)
                    .setRequestListener(this)
                    .build(getContext());
        } catch (PageManager.PageManagerException e) {
            ToastUtil.error("PageManager 初始化失败");
        }

        mNoDataLayout.setImgRes(R.mipmap.no_data_collect);
        mNoDataLayout.setTextView("亲，你还没有喜欢的素材哦");

        return view;
    }

    boolean isLoadData = false;

    @Override
    public void onStart() {
        super.onStart();
        if (!isLoadData) {
            mPageManager.onRefresh();
            isLoadData = true;
        }
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mCollectService.getLikeDataList(page), new BaseRequestListener<PaginationEntity<CommunityDataBean, Object>>(mRefreshLayout) {
            @Override
            public void onSuccess(PaginationEntity<CommunityDataBean, Object> result) {
                if (page == 1) {
                    mDataAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mDataAdapter.addItems(result.list);
                mNoDataLayout.setVisibility(result.total > 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
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
