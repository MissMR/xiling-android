package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.OrderSkuAdapter;
import com.xiling.ddui.bean.AccountInfo;
import com.xiling.ddui.bean.AddressListBean;
import com.xiling.ddui.bean.CouponBean;
import com.xiling.ddui.bean.OrderAddBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.custom.popupwindow.CouponSelectorDialog;
import com.xiling.ddui.custom.popupwindow.PayPopWindow;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.address.AddressListActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.shared.Constants.ORDER_WAIT_PAY;
import static com.xiling.shared.constant.Event.FINISH_ORDER;
import static com.xiling.shared.service.contract.IPayService.PAY_TYPE_ORDER;

/**
 * @author 逄涛
 * 确认订单页面
 */
public class ConfirmationOrderActivity extends BaseActivity {
    public static final String SKULIST = "skuList";
    public static final String ORDER_SOURCE = "orderSource";
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_balance_use)
    TextView tvBalanceUse;
    @BindView(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_coupon_price)
    TextView tvCouponPrice;
    @BindView(R.id.tv_balance_price)
    TextView tvBalancePrice;
    @BindView(R.id.tv_freight_price)
    TextView tvFreightPrice;
    @BindView(R.id.tv_need_price)
    TextView tvNeedPrice;
    @BindView(R.id.tv_need_price_decimal)
    TextView tvNeedPriceDecimal;
    @BindView(R.id.iv_arrow2)
    ImageView ivArrow2;
    @BindView(R.id.tv_coupon)
    TextView tvCoupon;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.switch_balance)
    ImageView switchBalance;
    @BindView(R.id.tv_identity_price)
    TextView tvIdentityPrice;
    private String mCouponId = "";


    ArrayList<SkuListBean> skuList;
    IOrderService mOrderService;
    IAddressService mAddressService;
    INewUserService mUserService;

    @BindView(R.id.iv_default)
    ImageView ivDefault;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.tv_address_detail)
    TextView tvAddressDetail;
    @BindView(R.id.tv_contacts)
    TextView tvContacts;
    @BindView(R.id.btn_address)
    RelativeLayout btnAddress;

    OrderSkuAdapter orderSkuAdapter;
    @BindView(R.id.iv_arrow)
    ImageView ivArrow;
    @BindView(R.id.recycler_sku)
    RecyclerView recyclerSku;

    private AddressListBean.DatasBean mAddress;
    private boolean isBalance = false;
    private String balancePassword = "";
    private double useBalance = 0;
    private double totlaPrice = 0;
    private AccountInfo accountInfo = null;
    private int orderSource = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_order);
        ButterKnife.bind(this);
        setTitle("确认订单");
        setLeftBlack();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        if (getIntent() != null) {
            skuList = getIntent().getParcelableArrayListExtra(SKULIST);
            orderSource = getIntent().getIntExtra(ORDER_SOURCE, 1);
        }


        if (skuList == null) {
            ToastUtil.error("商品列表为空");
            return;
        }


        recyclerSku.setLayoutManager(new LinearLayoutManager(this));
        orderSkuAdapter = new OrderSkuAdapter();
        recyclerSku.setAdapter(orderSkuAdapter);

        getAccountInfo();
        getDefaultAddress();
    }

    private void setAddress(AddressListBean.DatasBean address) {
        mAddress = address;
        if (address == null) {
            llAddress.setVisibility(View.GONE);
            tvContacts.setVisibility(View.GONE);
        } else {
            llAddress.setVisibility(View.VISIBLE);
            tvContacts.setVisibility(View.VISIBLE);

            ivDefault.setVisibility(address.getIsDefault() == 1 ? View.VISIBLE : View.GONE);
            tvAddress.setText(address.getFullRegion());
            tvAddressDetail.setText(address.getDetail());
            tvContacts.setText(address.getContactAndPhone());
        }
    }

    /**
     * 获取默认地址
     */
    private void getDefaultAddress() {
        APIManager.startRequest(mAddressService.getDefaultAddress(), new BaseRequestListener<AddressListBean.DatasBean>() {
            @Override
            public void onSuccess(AddressListBean.DatasBean result) {
                setAddress(result);
            }

            @Override
            public void onError(Throwable e) {
                setAddress(null);
            }
        });
    }


    private void upDataBalance() {
        if (isBalance) {
            tvBalanceUse.setVisibility(View.VISIBLE);
            tvBalanceUse.setText("使用 ¥" + useBalance);
            NumberHandler.setPriceText(totlaPrice - useBalance, tvNeedPrice, tvNeedPriceDecimal);
            tvBalancePrice.setText("-¥" + useBalance);
        } else {
            tvBalanceUse.setText("使用 ¥" + NumberHandler.reservedDecimalFor2(0));
            tvBalanceUse.setVisibility(View.GONE);
            tvBalancePrice.setText("-¥" + NumberHandler.reservedDecimalFor2(0));
            NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
        }
    }


    /**
     * 获取确认订单数据
     */
    private void getConfirmOrder(final AccountInfo accountInfo) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", skuList);
        if (!TextUtils.isEmpty(mCouponId)) {
            params.put("couponId", mCouponId);
        }
        APIManager.startRequest(mOrderService.getConfirmOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<OrderDetailBean>(this) {
            @Override
            public void onSuccess(OrderDetailBean result) {
                super.onSuccess(result);
                orderSkuAdapter.setNewData(result.getStores());

                if (result != null) {
                    useBalance = result.getTotalPrice();
                    totlaPrice = result.getTotalPrice();
                    if (accountInfo != null) {
                        //如果余额小于商品总价，使用金额为余额
                        if (useBalance > accountInfo.getBalance()) {
                            useBalance = accountInfo.getBalance();
                        }
                    } else {
                        NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
                    }
                    upDataBalance();
                    switch (UserManager.getInstance().getUserLevel()) {
                        case 0:
                            //注册会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_register);
                            break;
                        case 10:
                            //普通会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_ordinary);
                            break;
                        case 20:
                            //vip会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_vip);
                            break;
                        case 30:
                            //黑卡会员
                            tvIdentityPrice.setBackgroundResource(R.drawable.bg_price_black);
                            break;
                    }

                    tvGoodsPrice.setText("¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalRetailPrice()));
                    tvDiscountPrice.setText("¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalPrice()));
                    tvCouponPrice.setText("-¥" + NumberHandler.reservedDecimalFor2(result.getCouponReductionPrice()));
                    tvFreightPrice.setText("+¥" + NumberHandler.reservedDecimalFor2(result.getFreight()));
                    // tvBalancePrice.setText("-¥" + NumberHandler.reservedDecimalFor2(useBalance));

                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    /**
     * 获取账户信息（余额）
     */
    private void getAccountInfo() {
        APIManager.startRequest(mUserService.getAccountInfo(), new BaseRequestListener<AccountInfo>() {
            @Override
            public void onSuccess(AccountInfo result) {
                super.onSuccess(result);
                if (result != null) {
                    tvBalance.setText("共 ¥" + NumberHandler.reservedDecimalFor2(result.getBalance()));
                    accountInfo = result;
                    getConfirmOrder(result);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("账户信息获取失败");
                getConfirmOrder(null);
            }
        });
    }


    private void switchBalance(View view) {
        isBalance = !isBalance;
        view.setSelected(isBalance);
        upDataBalance();
    }


    /**
     * 校验是否设置余额密码
     */
    private void hasPassword(final View view) {
        APIManager.startRequest(mUserService.hasPassword(), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    PayPopWindow payPopWindow = new PayPopWindow(context);
                    payPopWindow.setOnPasswordEditListener(new PayPopWindow.OnPasswordEditListener() {
                        @Override
                        public void onEditFinish(String password) {
                            //在此校验输入的密码
                            checkPassword(password, view);
                        }
                    });
                    payPopWindow.show();
                } else {
                    startActivity(new Intent(context, TransactionPasswordActivity.class));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @OnClick({R.id.btn_address, R.id.switch_balance, R.id.btn_coupon, R.id.btn_send})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_address://更换地址
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra("action", Key.SELECT_ADDRESS);
                startActivityForResult(intent, 0);
                break;
            case R.id.switch_balance: // 使用账户余额
                if (accountInfo.getBalance() == 0) {
                    return;
                }
                if (!isBalance) {
                    //打开
                    //先校验是否有支付密码，如果没有跳转设置密码的界面
                    hasPassword(view);
                } else {
                    //关闭
                    switchBalance(view);
                    balancePassword = "";
                }

                break;
            case R.id.btn_coupon:// 选择优惠券
                getCouponList();
                break;
            case R.id.btn_send:
                //提交订单
                subOrder();
                break;

        }

    }

    ICouponService mCouponService;

    private void getCouponList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("products", skuList);
        if (mCouponService == null) {
            mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
        }
        APIManager.startRequest(mCouponService.getCouponList(APIManager.buildJsonBody(params)), new BaseRequestListener<List<CouponBean>>() {
            @Override
            public void onSuccess(List<CouponBean> result) {
                if (result != null && result.size() > 0) {
                    CouponSelectorDialog dialog = new CouponSelectorDialog(context, result);
                    if (!TextUtils.isEmpty(mCouponId)) {
                        dialog.setSelectId(mCouponId);
                    }

                    dialog.setOnCouponSelectListener(new CouponSelectorDialog.OnCouponSelectListener() {
                        @Override
                        public void onCouponSelected(CouponBean couponBean) {
                            mCouponId = couponBean.getId();
                            getConfirmOrder(accountInfo);
                            tvCoupon.setText(couponBean.getName());
                        }
                    });
                    dialog.show();
                } else {
                    ToastUtil.error("暂无可用优惠券");
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }


    /**
     * 提交订单
     */
    private void subOrder() {

        if (mAddress == null) {
            ToastUtil.error("请选择收货地址");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("useBalance", isBalance);
        params.put("balance", isBalance ? useBalance * 100 : 0);
        if (isBalance) {
            params.put("balancePassword", balancePassword);
        }
        params.put("products", skuList);
        params.put("device", 1);
        params.put("addressId", mAddress.getAddressId());
        if (!TextUtils.isEmpty(mCouponId)) {
            params.put("couponId", mCouponId);
        }
        params.put("orderSource", orderSource);

        APIManager.startRequest(mOrderService.addOrder(APIManager.buildJsonBody(params)), new BaseRequestListener<OrderAddBean>(this) {
            @Override
            public void onSuccess(OrderAddBean result) {
                super.onSuccess(result);
                //通知购物车，刷新
                EventBus.getDefault().post(new EventMessage(FINISH_ORDER));
                //订单生成后，根据订单编号查询订单信息
                getOrderDetails(result.getOrderId());
            }

            @Override
            public void onError(Throwable e, String businessCode) {
                super.onError(e);

                if (!TextUtils.isEmpty(businessCode)) {
                    if (businessCode.equals("un-auth")) {
                        D3ialogTools.showAlertDialog(context, e.getMessage(), "实名认证", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(context, RealAuthActivity.class));
                            }
                        }, "取消下单", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        });
                    }
                } else {
                    ToastUtil.error(e.getMessage());
                }


            }
        });
    }

    private void getOrderDetails(final String orderId) {
        APIManager.startRequest(mOrderService.getOrderDetails(orderId), new BaseRequestListener<XLOrderDetailsBean>() {
            @Override
            public void onSuccess(XLOrderDetailsBean result) {
                super.onSuccess(result);
                if (result.getOrderStatusUs().equals(ORDER_WAIT_PAY)) {
                    //如果是待支付，跳转收银台
                    XLCashierActivity.jumpCashierActivity(context, PAY_TYPE_ORDER, result.getPayMoney(), result.getWaitPayTimeMilli(), result.getOrderId());
                } else {
                    //已支付，跳转订单详情
                    XLOrderDetailsActivity.jumpOrderDetailsActivity(context, orderId);
                    finish();
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            AddressListBean.DatasBean address = data.getParcelableExtra("address");
            if (address != null) {
                setAddress(address);
            }
        }

    }

    /**
     * 校验余额密码
     *
     * @param password
     */
    private void checkPassword(final String password, final View switchView) {
        APIManager.startRequest(mUserService.checkBalancePassword(password), new BaseRequestListener<Object>(context) {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                switchBalance(switchView);
                balancePassword = password;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                balancePassword = "";
                D3ialogTools.showAlertDialog(context, e.getMessage(), "忘记密码", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(context, TransactionPasswordActivity.class));
                    }
                }, "重新输入", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PayPopWindow payPopWindow = new PayPopWindow(context);
                        payPopWindow.setOnPasswordEditListener(new PayPopWindow.OnPasswordEditListener() {
                            @Override
                            public void onEditFinish(String password) {
                                //在此校验输入的密码
                                checkPassword(password, switchView);
                            }
                        });
                        payPopWindow.show();
                    }
                });

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        switch (message.getEvent()) {
            case FINISH_ORDER:
                finish();
                break;
        }
    }

}
