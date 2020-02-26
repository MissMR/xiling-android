package com.xiling.module.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.xiling.R;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.NoData;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PageManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAddressService;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.Constants.PAGE_SIZE;

/**
 * pt
 * 收货地址列表
 */
public class AddressListActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.refreshLayout)
    protected SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.noDataLayout)
    protected NoData mNoDataLayout;

    private AddressAdapter mAddressAdapter;
    private boolean isSelectAddress = false;
    private IAddressService mAddressService;
    private String mDrawId;

    int page = 1;
    int total = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        initAction();
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestAddress();
    }

    private void initView() {
        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mAddressAdapter = new AddressAdapter(this);
        mAddressAdapter.setOnEditListener(new AddressAdapter.OnEditListener() {
            @Override
            public void onEdit(AddressListBean.DatasBean address) {
                Intent intent = new Intent(context, AddressFormActivity.class);
                intent.putExtra("action", Key.EDIT_ADDRESS);
                intent.putExtra("addressId", address.getAddressId());
                context.startActivity(intent);
            }
        });

        if (isSelectAddress) {
            mAddressAdapter.setOnItemListener(new AddressAdapter.OnItemListener() {
                @Override
                public void onItemListener(AddressListBean.DatasBean address) {
                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    setResult(0, intent);
                    finish();
                }
            });
        }
        mRecyclerView.setAdapter(mAddressAdapter);
        mNoDataLayout.setTextView("您还没有添加收货地址哦～");
        mNoDataLayout.setReload("新增收货地址", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToCreateNewAddress();
            }
        });
    }


    private void initAction() {
        showHeader();
        Intent intent = getIntent();
        if (intent == null) {
            isSelectAddress = false;
        } else if (intent.getExtras() == null) {
            isSelectAddress = false;
        } else {
            String action = getIntent().getExtras().getString("action");
            isSelectAddress = action != null && action.equals(Key.SELECT_ADDRESS);
            mDrawId = getIntent().getStringExtra("drawId");
        }
        setTitle("收货地址");

        showHeaderRightText("新增地址", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                jumpToCreateNewAddress();
            }
        });
    }

    void jumpToCreateNewAddress() {
        startActivity(new Intent(AddressListActivity.this, AddressFormActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        setLeftBlack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void requestAddress() {
        APIManager.startRequest(mAddressService.getAddressList(page, PAGE_SIZE), new BaseRequestListener<AddressListBean>() {
            @Override
            public void onSuccess(AddressListBean result) {

                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();

                if (page == 1) {
                    mAddressAdapter.removeAllItems();
                }
                total = result.getTotalPage();

                // 如果已经到最后一页了，关闭上拉加载
                if (page >= total) {
                    mRefreshLayout.setEnableLoadMore(false);
                } else {
                    mRefreshLayout.setEnableLoadMore(true);
                }

                mAddressAdapter.addItems(result.getDatas());
                mNoDataLayout.setVisibility(result.getDatas().size() > 0 ? View.GONE : View.VISIBLE);
                mRefreshLayout.setVisibility(result.getDatas().size() > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mRefreshLayout.finishRefresh();
                mRefreshLayout.finishLoadMore();
            }
        });
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (page < total) {
            page++;
            requestAddress();
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        requestAddress();
    }
}
