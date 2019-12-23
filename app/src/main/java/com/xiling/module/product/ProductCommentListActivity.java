package com.xiling.module.product;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.module.product.adapter.ProductCommentAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.ProductComment;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.decoration.ListDividerDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.RvUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductCommentListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mRefreshLayout;
    private ArrayList<ProductComment> mDatas;
    private ProductCommentAdapter mAdapter;
    private IProductService mService;
    private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_list_layout);
        ButterKnife.bind(this);
        mService = ServiceManager.getInstance().createService(IProductService.class);
        getIntentData();
        initView();
        getData(true);
    }

    private void getIntentData() {
        mId = getIntent().getStringExtra("id");
    }

    private void initView() {
        setTitle("商品评论");
        setLeftBlack();

        mDatas = new ArrayList<>();
        mAdapter = new ProductCommentAdapter(mDatas);
        RvUtils.configRecycleView(this, mRecyclerView, mAdapter);
        mRecyclerView.addItemDecoration(new ListDividerDecoration(this));
        mRefreshLayout.setOnRefreshListener(this);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        APIManager.startRequest(
                mService.getProductComment(mId, mDatas.size() / Constants.PAGE_SIZE + 1, Constants.PAGE_SIZE),
                new BaseRequestListener<PaginationEntity<ProductComment, Object>>(mRefreshLayout) {
                    @Override
                    public void onSuccess(PaginationEntity<ProductComment, Object> result) {
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
}
