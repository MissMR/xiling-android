package com.xiling.module.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.R;
import com.xiling.ddui.activity.PayResultActivity;
import com.xiling.ddui.bean.DDCouponBean;
import com.xiling.ddui.custom.DDCouponSelectorDialog;
import com.xiling.ddui.service.IDDCouponService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.module.address.AddressListActivity;
import com.xiling.module.pay.adapter.PayAdapter;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.CartItem;
import com.xiling.shared.bean.CartStore;
import com.xiling.shared.bean.Freight;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderResponse;
import com.xiling.shared.bean.SkuAmount;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMDialog;
import com.xiling.shared.constant.Action;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.decoration.SpacesItemDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.service.contract.IFreightService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.AliPayUtils;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WePayUtils;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * @author Jigsaw
 * @date 2019/6/14
 * 重构 删除积分/团购 等无用代码
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.addressArrowIv)
    ImageView mAddressArrowIv;
    @BindView(R.id.addressLayout)
    LinearLayout mAddressLayout;
    @BindView(R.id.couponLayout)
    LinearLayout mCouponLayout;
    @BindView(R.id.addressInfoLayout)
    protected RelativeLayout mAddressInfoLayout;
    @BindView(R.id.selectAddressTv)
    protected TextView mSelectAddressTv;
    @BindView(R.id.contactsTv)
    protected TextView mContactsTv;
    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.addressDetailTv)
    protected TextView mDetailTv;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.remarkEt)
    protected EditText mRemarkEt;
    @BindView(R.id.couponTv)
    protected TextView mCouponTv;
    @BindView(R.id.freightTv)
    protected TextView mFreightTv;
    @BindView(R.id.totalTv)
    protected TextView mTotalTv;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;

    private ICartService mCartService;
    private IProductService mProductService;
    private IAddressService mAddressService;
    private IOrderService mOrderService;
    private IFreightService mFreightService;
    private IDDCouponService mIDDCouponService;
    private Address mAddress;
    private PayAdapter mPayAdapter;

    private long mFreight = 0L;
    private DDCouponBean mCoupon;
    // 订单单号 可用来判断是否已下单
    private String mOrderCode;
    private Order mOrder;

    private String mFrom;
    private long mTotalPrice;

    private List<DDCouponBean> mDDCouponBeanList;


    // 是否选择了微信支付 为了判断手机装了分身应用时弹出选择微信的确认框时，
    // 若选择了取消收不到系统的回调，所以添加此字段用来判断，并在onResume里处理相关回调
    private boolean isSelectedWechatPay = false;
    // 第一次resume是弹出系统分身选择框，第二次是选择完分身
    private int mWechatPayResumed = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);

        showHeader();
        setTitle("结算付款");
        setLeftBlack();

        WePayUtils.initWePay();

        mCartService = ServiceManager.getInstance().createService(ICartService.class);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        mFreightService = ServiceManager.getInstance().createService(IFreightService.class);
        mIDDCouponService = ServiceManager.getInstance().createService(IDDCouponService.class);

        mPayAdapter = new PayAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(10)));
        mRecyclerView.setAdapter(mPayAdapter);
        mRecyclerView.setNestedScrollingEnabled(false);

        checkParamsAndLoadData();

        if (mAddress == null) {
            loadDefaultAddress();
        } else {
            loadFreight();
            initAddressView();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void checkParamsAndLoadData() {
        if (getIntent() == null || getIntent().getExtras() == null) {
            ToastUtil.error("请选择产品");
            finish();
        }

        Bundle extras = getIntent().getExtras();
        mFrom = extras.getString("from", "");
        mAddress = (Address) extras.getSerializable(Constants.Extras.ADDRESS);
        try {
            switch (mFrom) {
                case "cart": // 购物车结算下单
                    ArrayList<String> list = extras.getStringArrayList("skuIds");
                    if (list == null) {
                        throw new Exception();
                    }
                    loadCartListBySkuIds(list);
                    break;
                case "buy": // 立刻购买下单
                    SkuAmount skuAmount = (SkuAmount) extras.getSerializable("skuAmount");
                    if (skuAmount == null) {
                        throw new Exception();
                    }
                    loadCartListBySkuAmount(skuAmount);
                    break;
                default: //
                    throw new Exception();
            }
        } catch (Exception e) {
            ToastUtil.error("请选择产品");
            finish();
        }
    }

    private void loadCartListBySkuIds(ArrayList<String> list) {
        APIManager.startRequest(mCartService.getListBySkuIds(Joiner.on(",").join(list)), new BaseRequestListener<List<CartStore>>(this) {
            @Override
            public void onSuccess(List<CartStore> result) {
                mPayAdapter.setItems(result);
                Logger.e("从购物车进来，当前列表：" + mPayAdapter.getItems().size());
                initAddressView();
                loadFreight();
                initTotalView();
            }
        });
    }

    private void loadCartListBySkuAmount(final SkuAmount skuAmount) {
        APIManager.startRequest(mProductService.getSkuById(skuAmount.skuId), new BaseRequestListener<SkuInfo>(this) {

            @Override
            public void onSuccess(SkuInfo skuInfo) {
                mPayAdapter.setItems(getFormatCartStoresFromSkuInfo(skuInfo, skuAmount));
                Logger.e("从立即购买进来，当前列表：" + mPayAdapter.getItems().size());

                initAddressView();
                loadFreight();
                initTotalView();

            }
        });
    }

    @NonNull
    private ArrayList<CartStore> getFormatCartStoresFromSkuInfo(SkuInfo skuInfo, SkuAmount skuAmount) {
        CartStore cartStore = new CartStore();
        cartStore.id = skuInfo.storeId;
        cartStore.name = skuInfo.storeName;

        Gson gson = new Gson();
        CartItem cartItem = gson.fromJson(gson.toJson(skuInfo), CartItem.class);
        cartItem.amount = skuAmount.amount;
        cartStore.products.add(cartItem);
        cartItem.amount = skuAmount.amount;
        cartItem.flashSaleId = skuAmount.getFlashSaleId();

        ArrayList<CartStore> cartStores = new ArrayList<>();
        cartStores.add(cartStore);
        return cartStores;
    }

    /**
     * 设置优惠券文字,无可用优惠券传入 null，其他根据需要传入 string
     *
     * @param text 如果传入 null，则显示【无可用优惠券】，并使用灰色字体
     */
    private void setCouponText(String text) {
        if (StringUtils.isEmpty(text)) {
            mCouponTv.setText("请选择优惠券");
//            mCouponTv.setTextColor(getResources().getColor(R.color.text_gray));
        } else {
            mCouponTv.setText(text);
            mCouponTv.setTextColor(getResources().getColor(R.color.redPrice));
        }
    }

    private void loadDefaultAddress() {

    }

    private void loadFreight() {
        if (mAddress == null || mPayAdapter.getItemCount() == 0) {
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("addressId", mAddress.addressId);
        params.put("products", convertProducts());
        params.put("isGroup", 0);

        APIManager.startRequest(mFreightService.calculate(APIManager.buildJsonBody(params)), new BaseRequestListener<Freight>(this) {
            @Override
            public void onSuccess(Freight result) {
                mFreight = result.freight;
                if (mFreight > 0) {
                    mFreightTv.setText(ConvertUtil.centToCurrency(PayActivity.this, mFreight));
                    mFreightTv.setTextColor(getResources().getColor(R.color.ddm_red));
                } else {
                    mFreightTv.setText("包邮");
                    mFreightTv.setTextColor(getResources().getColor(R.color.text_black));
                }
                initTotalView();
            }
        });
    }

    private void getAvailableCouponList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", convertProducts());

        APIManager.startRequest(mIDDCouponService.getAvailableCouponList(APIManager.buildJsonBody(params)),
                new BaseRequestListener<List<DDCouponBean>>(this) {
                    @Override
                    public void onSuccess(List<DDCouponBean> result) {
                        super.onSuccess(result);
                        mDDCouponBeanList = result;
                        showCouponSelectDialog();
                    }
                });
    }

    private List<SkuAmount> convertProducts() {
        List<SkuAmount> products = new ArrayList<>();
        for (CartStore cartStore : mPayAdapter.getItems()) {
            if (cartStore.products == null) {
                continue;
            }
            for (CartItem product : cartStore.products) {
                SkuAmount skuAmount = new SkuAmount(product.skuId, product.amount);
                skuAmount.setFlashSaleId(product.flashSaleId);
                skuAmount.setActivityType(TextUtils.isEmpty(product.flashSaleId) ? 1 : 2);
                products.add(skuAmount);
            }
        }
        return products;
    }

    private void initAddressView() {
        if (mAddress == null) {
            mSelectAddressTv.setVisibility(View.VISIBLE);
            mAddressInfoLayout.setVisibility(View.GONE);
        } else {
            mSelectAddressTv.setVisibility(View.GONE);
            mAddressInfoLayout.setVisibility(View.VISIBLE);

            mContactsTv.setText(String.format("收货人：%s", mAddress.contacts));
            mPhoneTv.setText(mAddress.phone);
            mDetailTv.setText("收货地址：" + mAddress.getFullAddress());
        }
    }


    /**
     * 计算最后的总价，并且设置到合计
     */
    private void initTotalView() {
        mTotalPrice = 0L;
        mTotalPrice += getCartStoreTotal();
        if (mCoupon != null) {
            mTotalPrice -= mCoupon.getReducedPrice();
            mTotalPrice = mTotalPrice >= 0 ? mTotalPrice : 0;
        }
        mTotalPrice += mFreight;
        mTotalTv.setText(ConvertUtil.centToCurrency(this, mTotalPrice));

    }


    /**
     * 获取购物车商品的总价格
     *
     * @return
     */
    private long getCartStoreTotal() {
        long total = 0L;
        for (CartStore cartStore : mPayAdapter.getItems()) {
            total += cartStore.getTotal();
        }
        return total;
    }


    @Override
    protected void onResume() {
        super.onResume();
        DLog.i("onResume : isOrderCreated = " + isOrderCreated() + "  " + new Date().toString());

        if (isSelectedWechatPay) {
            mWechatPayResumed++;
            if (mWechatPayResumed > 1) {
                DLog.d("微信支付，微信双开，选择微信时，用户取消支付");
//                EventUtil.viewOrderList(this, Order.ORDER_WAIT_PAY);
                startPayResultActivity(false);
                isSelectedWechatPay = false;
                mWechatPayResumed = 0;
                finish();
            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            if (requestCode == Action.SELECT_PAY_WAY && isOrderCreated()) {
                // 取消选择 支付方式
                EventUtil.viewOrderDetail(this, mOrderCode);
                finish();
            }
            return;
        }
        switch (requestCode) {
            case Action.SELECT_ADDRESS:
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mRecyclerView.getLayoutParams();
                layoutParams.setMargins(0, 0, 0, 0);
                mRecyclerView.setLayoutParams(layoutParams);

                Address address = (Address) data.getExtras().get("address");
                if (address != null) {
                    mAddress = address;
                    initAddressView();
                    loadFreight();
                }
                break;
            case Action.SELECT_PAY_WAY:
                int payType = data.getIntExtra(Constants.Extras.PAY_TYPE, -1);
                if (payType == -1) {
                    ToastUtil.error("请先选择支付方式");
                } else {
                    goPayOrder(mOrder, payType);
                }
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.addressLayout)
    protected void selectAddress() {
        Intent intent = new Intent(PayActivity.this, AddressListActivity.class);
        intent.putExtra("action", Key.SELECT_ADDRESS);
        startActivityForResult(intent, Action.SELECT_ADDRESS);
    }

    @OnClick(R.id.couponLayout)
    protected void selectCoupon() {

        if (mDDCouponBeanList == null) {
            getAvailableCouponList();
            return;
        }

        showCouponSelectDialog();

    }

    private void showCouponSelectDialog() {
        if (mDDCouponBeanList == null || mDDCouponBeanList.isEmpty()) {
            setCouponText("暂无可用优惠券");
            ToastUtil.error("暂无可用优惠券");
            return;
        }
        new DDCouponSelectorDialog(this, mDDCouponBeanList)
                .setOnCouponSelectListener(new DDCouponSelectorDialog.OnCouponSelectListener() {
                    @Override
                    public void onCouponSelected(DDCouponBean couponBean) {
                        mCoupon = couponBean;
                        setCouponText(mCoupon == null ? "" : "-" + ConvertUtil.centToCurrency(PayActivity.this, mCoupon.getReducedPrice()));
                        initTotalView();
                    }
                })
                .show();
    }

    @OnClick(R.id.confirmBtn)
    protected void onClickConfirm() {
        if (mAddress == null || mAddress.addressId == null || mAddress.addressId.isEmpty()) {
            ToastUtil.error("请选择收货地址");
            return;
        }
        ViewUtil.setViewClickedDelay(mConfirmBtn, 500);
        ToastUtil.showLoading(this);
        addOrder();
    }

    // 下单操作
    private void addOrder() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("addressId", mAddress.addressId);
        params.put("products", convertProducts());
        params.put("orderFrom", 1);
        params.put("couponId", mCoupon == null ? "" : mCoupon.getId());
        params.put("remark", mRemarkEt.getText().toString());
        String url = "order/add";
        params.put("fromCart", "cart".equals(mFrom) ? "1" : "0");

        Observable<RequestResult<Order>> observable = mOrderService
                .create(url, APIManager.buildJsonBody(params))
                .flatMap(new Function<RequestResult<OrderResponse>, ObservableSource<RequestResult<Order>>>() {
                    @Override
                    public ObservableSource<RequestResult<Order>> apply(RequestResult<OrderResponse> result) throws Exception {
                        if (result.isFail()) {
                            throw new RuntimeException(result.message);
                        }
                        return mOrderService.getOrderByCode(result.data.orderCode);
                    }
                });
        APIManager.startRequest(observable, new BaseRequestListener<Order>(this) {
            @Override
            public void onSuccess(Order result) {
                super.onSuccess(result);
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                mOrder = result;
                mOrderCode = result.orderMain.orderCode;
                EventBus.getDefault().post(new EventMessage(Event.createOrderSuccess));
                startActivityForResult(new Intent(PayActivity.this, PayDialogActivity.class)
                        .putExtra(PayDialogActivity.PARAM_KEY_ONLY_SELECT, ""), Action.SELECT_PAY_WAY);
            }

            @Override
            public void onError(Throwable e) {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                ToastUtil.hideLoading();
                new DDMDialog(PayActivity.this)
                        .setContent(e.getMessage())
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }).show();
            }
        });
    }

    /**
     * 是否已创建订单
     *
     * @return true已创建订单
     */
    private boolean isOrderCreated() {
        return !TextUtils.isEmpty(mOrderCode);
    }

    private void goPayOrder(Order order, int payWay) {
        ToastUtil.showLoading(this);
        switch (payWay) {
            case AppTypes.PAY_TYPE.WECHAT:
                isSelectedWechatPay = true;
                WePayUtils.wePay(this, order.orderMain.totalMoney, order.orderMain.orderCode);
                break;
            case AppTypes.PAY_TYPE.ALI:
                AliPayUtils.pay(this, ConvertUtil.cent2yuanNoZero(order.orderMain.totalMoney), order.orderMain.orderCode);
                break;
            default:
                ToastUtil.error("请选择支付方式");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(PayMsg msgStatus) {
        switch (msgStatus.getAction()) {
            case PayMsg.ACTION_WXPAY_SUCCEED:
                isSelectedWechatPay = false;
            case PayMsg.ACTION_ALIPAY_SUCCEED:
            case PayMsg.ACTION_WEBPAY_SUCCEED:
                EventBus.getDefault().post(new EventMessage(Event.paySuccess));
                startPayResultActivity(true);
                ToastUtil.success("支付成功");
                finish();
                break;
            case PayMsg.ACTION_BALANCE_SUCCEED:
                finish();
                break;
            case PayMsg.ACTION_WXPAY_FAIL:
                isSelectedWechatPay = false;
            case PayMsg.ACTION_ALIPAY_FAIL:
            case PayMsg.ACTION_WEBPAY_FAIL:
                ToastUtils.showShortToast(msgStatus.message);
                startPayResultActivity(false);
                //                EventUtil.viewOrderList(this, Order.ORDER_WAIT_PAY);
                finish();
                break;
            default:
        }
    }

    private void startPayResultActivity(boolean isPaySuccess) {
        PayResultActivity.start(this, mOrderCode, isPaySuccess);
    }

}
