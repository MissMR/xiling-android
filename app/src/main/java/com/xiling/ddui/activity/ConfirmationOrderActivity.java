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
import com.xiling.ddui.bean.DDCouponBean;
import com.xiling.ddui.bean.OrderDetailBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.custom.DDCouponSelectorDialog;
import com.xiling.ddui.custom.popupwindow.CouponSelectorDialog;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.module.address.AddressListActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 确认订单页面
 */
public class ConfirmationOrderActivity extends BaseActivity {
    public static final String SKULIST = "skuList";
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

    private boolean isBalance = false;
    private final String DEFULT_BALANCE = "使用 ¥0";
    private String useBalance = DEFULT_BALANCE;
    private double totlaPrice = 0;
    private AccountInfo accountInfo = null;

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
        // getConfirmOrder();
    }

    private void setAddress(AddressListBean.DatasBean address) {
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
                    useBalance = "使用 ¥" + result.getTotalPrice();
                    totlaPrice = result.getTotalPrice();
                    if (accountInfo != null) {
                        if (isBalance) {
                            tvBalanceUse.setText(useBalance);
                            NumberHandler.setPriceText(0, tvNeedPrice, tvNeedPriceDecimal);
                        } else {
                            tvBalanceUse.setText(DEFULT_BALANCE);
                            NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
                        }
                    } else {
                        NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
                    }

                    tvGoodsPrice.setText("¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalRetailPrice()));
                    tvDiscountPrice.setText("-¥" + NumberHandler.reservedDecimalFor2(result.getGoodsTotalDiscountPrice()));
                    tvCouponPrice.setText("-¥" + NumberHandler.reservedDecimalFor2(result.getCouponReductionPrice()));
                    tvFreightPrice.setText("+¥" + NumberHandler.reservedDecimalFor2(result.getFreight()));
                    tvBalancePrice.setText("-¥" + NumberHandler.reservedDecimalFor2(result.getTotalPrice()));

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


    @OnClick({R.id.btn_address, R.id.switch_balance, R.id.btn_coupon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_address://更换地址
                Intent intent = new Intent(this, AddressListActivity.class);
                intent.putExtra("action", Key.SELECT_ADDRESS);
                startActivityForResult(intent, 0);
                break;
            case R.id.switch_balance: // 使用账户余额
                isBalance = !isBalance;
                view.setSelected(isBalance);
                if (isBalance) {
                    tvBalanceUse.setText(useBalance);
                    NumberHandler.setPriceText(0, tvNeedPrice, tvNeedPriceDecimal);
                } else {
                    tvBalanceUse.setText(DEFULT_BALANCE);
                    NumberHandler.setPriceText(totlaPrice, tvNeedPrice, tvNeedPriceDecimal);
                }

                break;
            case R.id.btn_coupon:// 选择优惠券
                CouponSelectorDialog dialog = new CouponSelectorDialog(this, skuList);
                if (!TextUtils.isEmpty(mCouponId)){
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

                break;
        }

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


}
