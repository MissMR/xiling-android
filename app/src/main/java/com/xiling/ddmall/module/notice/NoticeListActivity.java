package com.xiling.ddmall.module.notice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.notice.adapter.NoticeAdapter;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.NoticeListModel;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.INotesService;
import com.xiling.ddmall.shared.util.RvUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRvList;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mLayoutRefresh;

    private ArrayList<NoticeListModel.DatasEntity> mDatas = new ArrayList<>();

    private NoticeAdapter mAdapter;
    private INotesService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);
        ButterKnife.bind(this);
        initView();
        mService = ServiceManager.getInstance().createService(INotesService.class);
        getListData(true);
    }

    private void initView() {
        initTitle();

        mLayoutRefresh.setOnRefreshListener(this);
        mAdapter = new NoticeAdapter(mDatas);
        RvUtils.configRecycleView(this, mRvList, mAdapter);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getListData(false);
            }
        });
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                itemClick(mDatas.get(position));
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getListData(false);
            }
        });

    }

    private void itemClick(NoticeListModel.DatasEntity datasEntity) {
        Intent intent = new Intent(this, NoticeDetailsActivity.class);
        intent.putExtra("id", datasEntity.notesId);
        startActivity(intent);
    }

    private void initTitle() {
        setTitle("公告");
        setLeftBlack();
    }

    private void getListData(boolean isRefresh) {
        mLayoutRefresh.setRefreshing(true);
        if (isRefresh) {
            mDatas.clear();
        }
        APIManager.startRequest(
                mService.getList(
                        Constants.PAGE_SIZE,
                        mDatas.size() / Constants.PAGE_SIZE + 1),
                new BaseRequestListener<NoticeListModel>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(NoticeListModel model) {
                        mDatas.addAll(model.datas);
                        if (model.datas.size() < Constants.PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();
                    }
                });
    }


    @Override
    public void onRefresh() {
        getListData(true);
    }
}
