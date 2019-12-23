package com.xiling.module.lottery;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.R;
import com.xiling.module.lottery.adapter.WinnerAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.LotteryWinner;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NoData;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ILotteryService;
import com.xiling.shared.util.RvUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WinnerListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.noDataLayout)
    NoData mNoDataLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;

    private ILotteryService mService;
    private ArrayList<LotteryWinner> mDatas = new ArrayList<>();
    private WinnerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner_list);
        ButterKnife.bind(this);
        initView();
        getData(true);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        setTitle("中奖记录");
        setLeftBlack();

        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getData(true);
            }
        });

        RvUtils.configRecycleView(this, mRecyclerView);
        mAdapter = new WinnerAdapter(mDatas);
        NoData noData = new NoData(this);
        noData.setTextView("您还没有获得奖品哦");
        mAdapter.setEmptyView(noData);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(WinnerListActivity.this, WinnerDetailsActivity.class);
                intent.putExtra("data", mDatas.get(position));
                startActivity(intent);
//                EventBus.getDefault().postSticky(mDatas.get(position));
            }
        });
    }

    private void getData(final boolean isRefresh) {
        if (mService == null) {
            mService = ServiceManager.getInstance().createService(ILotteryService.class);
        }
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        APIManager.startRequest(
                mService.getWinnerList(Constants.PAGE_SIZE, mDatas.size() / Constants.PAGE_SIZE + 1),
                new BaseRequestListener<PaginationEntity<LotteryWinner, Object>>(mRefreshLayout) {
                    @Override
                    public void onSuccess(PaginationEntity<LotteryWinner, Object> result) {
                        super.onSuccess(result);
                        if (isRefresh) {
                            mDatas.clear();
                        }
                        mDatas.addAll(result.list);
                        mAdapter.notifyDataSetChanged();
                        if (result.list.size() < Constants.PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();
                    }
                }
        );
    }

    @Override
    public void onRefresh() {
        getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddress(EventMessage message) {
        if (message.getEvent() == Event.acceptPrizeSuccess) {
            onRefresh();
        }
    }
}
