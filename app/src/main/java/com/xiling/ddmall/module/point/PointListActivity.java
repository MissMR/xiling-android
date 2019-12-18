package com.xiling.ddmall.module.point;

import android.content.Intent;
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
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.transfer.StepFirstActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Point;
import com.xiling.ddmall.shared.bean.PointListExtra;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.PageManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IPointService;
import com.xiling.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Chan on 2017/6/17.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.module.balance
 * @since 2017/6/17 下午4:37
 */

public class PointListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected View mNoDataLayout;
    private IPointService mPointService;
    private PointAdapter mBalanceAdapter;
    private PageManager mPageManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_list);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.red));
            QMUIStatusBarHelper.setStatusBarDarkMode(this);
        }

        mPointService = ServiceManager.getInstance().createService(IPointService.class);
        mBalanceAdapter = new PointAdapter(this);
        mRecyclerView.setAdapter(mBalanceAdapter);
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
        setTitle("积分");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_white);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHeaderLayout().makeHeaderRed();
        mPageManager.onRefresh();
    }


    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mPointService.getPointList(page), new BaseRequestListener<PaginationEntity<Point, PointListExtra>>(this) {
            @Override
            public void onSuccess(PaginationEntity<Point, PointListExtra> result) {
                if (page == 1) {
                    mBalanceAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mBalanceAdapter.setHeaderData(result.ex);
                mBalanceAdapter.addItems(result.list);
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

    @OnClick(R.id.tvScoreTransfer)
    public void onViewClicked() {
        Intent intent = new Intent(this, StepFirstActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, AppTypes.TRANSFER.SCORE);
        startActivity(intent);
    }
}
