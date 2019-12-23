package com.xiling.module.address;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NoData;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PageManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.ILotteryService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressListActivity extends BaseActivity implements PageManager.RequestListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.noDataLayout)
    protected NoData mNoDataLayout;

    @BindView(R.id.btnAddAddress)
    Button btnAddAddress;

    @OnClick(R.id.btnAddAddress)
    void onAddAddressPressed() {
        jumpToCreateNewAddress();
    }

    private AddressAdapter mAddressAdapter;
    private boolean isSelectAddress = false;
    private IAddressService mAddressService;
    private PageManager mPageManager;
    private boolean mIsLottery;
    private String mDrawId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        ButterKnife.bind(this);
        initAction();

        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);

        mAddressAdapter = new AddressAdapter(this, isSelectAddress, mIsLottery);
        mRecyclerView.setAdapter(mAddressAdapter);
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

        mNoDataLayout.setImgRes(R.mipmap.no_data_order);
        mNoDataLayout.setTextView("您还没有添加收货地址");
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
            mIsLottery = getIntent().getBooleanExtra("isLottery", false);
            mDrawId = getIntent().getStringExtra("drawId");
        }
        setTitle(isSelectAddress ? "选择收货地址" : "管理收货地址");
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
//        getHeaderLayout().setRightDrawable(R.drawable.icon_add);
//        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                jumpToCreateNewAddress();
//            }
//        });
        mPageManager.onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectAddress(EventMessage message) {
        if (message.getEvent() == Event.selectAddress) {
            Intent intent = getIntent();
            intent.putExtra("address", (Address) message.getData());
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (message.getEvent() == Event.deleteAddress) {
            mPageManager.onRefresh();
        } else if (message.getEvent() == Event.saveAddress) {
            mPageManager.onRefresh();
        } else if (message.getEvent() == Event.selectLotteryAddress) {
            Address address = (Address) message.getData();
            if (address != null) {
                acceptPrize(address);
            }
        }
    }

    /**
     * 领取奖品
     *
     * @param address
     */
    private void acceptPrize(final Address address) {
        ILotteryService service = ServiceManager.getInstance().createService(ILotteryService.class);
        APIManager.startRequest(
                service.acceptPrize(mDrawId, address.addressId),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        EventBus.getDefault().post(new EventMessage(Event.acceptPrizeSuccess, address));
                        ToastUtil.success("领取成功");
                        finish();
                    }
                }
        );
    }

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mAddressService.getAddressList(page), new BaseRequestListener<PaginationEntity<Address, Object>>(mRefreshLayout) {

            @Override
            public void onSuccess(PaginationEntity<Address, Object> result) {
                if (page == 1) {
                    mAddressAdapter.removeAllItems();
                }
                mPageManager.setLoading(false);
                mPageManager.setTotalPage(result.totalPage);
                mAddressAdapter.addItems(result.list);
                mNoDataLayout.setVisibility(result.total > 0 ? View.GONE : View.VISIBLE);
                mRefreshLayout.setVisibility(result.total > 0 ? View.VISIBLE : View.GONE);
                btnAddAddress.setVisibility(result.total > 0 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mPageManager.setLoading(false);
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mPageManager.setLoading(false);
                mRefreshLayout.setRefreshing(false);
            }
        });
    }

}
