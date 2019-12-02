package com.dodomall.ddmall.module.balance;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.module.deal.DealFirstActivity;
import com.dodomall.ddmall.module.transfer.StepFirstActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Balance;
import com.dodomall.ddmall.shared.bean.CommonExtra;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.UserService;
import com.dodomall.ddmall.shared.service.contract.IBalanceService;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

public class BalanceListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.noDataLayout)
    protected View mNoDataLayout;
    private IBalanceService mBalanceService;
    private BalanceAdapter mBalanceAdapter;
    private PageManager mPageManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_list_layout);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.red));
        }

        mBalanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        mBalanceAdapter = new BalanceAdapter(this);
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("零钱包");
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mBalanceService.getBalanceList(page), new BaseRequestListener<PaginationEntity<Balance, CommonExtra>>(mRefreshLayout) {
            @Override
            public void onSuccess(PaginationEntity<Balance, CommonExtra> result) {
                if (page == 1) {
                    mBalanceAdapter.getItems().clear();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mBalanceAdapter.setHeaderData(result.ex);
                mBalanceAdapter.addItems(result.list);
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

    @OnClick(R.id.transferTv)
    public void onMTransferTvClicked() {
        if (SessionUtil.getInstance().getLoginUser().authStatus != AppTypes.AUTH_STATUS.SUCESS) {
            ToastUtil.error("未进行实名认证，无法转账");
            return;
        }
        UserService.checkHasPassword(this, new UserService.HasPasswordListener() {
            @Override
            public void onHasPassword() {
                startActivity(new Intent(BalanceListActivity.this, StepFirstActivity.class));
            }
        });
    }

    @OnClick(R.id.withdrawalTv)
    public void onMWithdrawalTvClicked() {
        if (SessionUtil.getInstance().getLoginUser().authStatus != AppTypes.AUTH_STATUS.SUCESS) {
            ToastUtil.error("未进行实名认证，无法提现");
            return;
        }
        UserService.checkHasPassword(this, new UserService.HasPasswordListener() {
            @Override
            public void onHasPassword() {
                startActivity(new Intent(BalanceListActivity.this, DealFirstActivity.class));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MsgStatus status) {
        switch (status.getAction()) {
            case MsgStatus.ACTION_DEAL_SUCESS:
                mPageManager.onRefresh();
                break;
            default:
                break;
        }
    }
}
