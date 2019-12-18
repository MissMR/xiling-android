package com.xiling.ddmall.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.ProductAdapter;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.OrderPayStatusBean;
import com.xiling.ddmall.ddui.custom.PayResultView;
import com.xiling.ddmall.ddui.manager.CSManager;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.bean.DDProductBean;
import com.xiling.ddmall.module.MainActivity;
import com.xiling.ddmall.module.order.OrderDetailActivity;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.OrderService;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.service.contract.IProductService;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2019/1/22
 * 支付结果页
 */
public class PayResultActivity extends DDListActivity<DDProductBean> implements PayResultView.OnPayResultActionClickListener {

    public static final int PAY_SUCCESS = 0x00;
    public static final int PAY_FAIL = 0x01;

    private int mParamPayResult = PAY_SUCCESS;
    private String mOrderCode;


    private int mWaitTime = 2 * 1000;
    private PayResultView mPayResultView;
    private IOrderService mOrderService;

    private OrderPayStatusBean mOrderPayStatusBean;

    public static void start(Context context, String orderCode, boolean isPaySuccess) {
        context.startActivity(new Intent(context, PayResultActivity.class)
                .putExtra(Constants.Extras.ORDER_CODE, orderCode)
                .putExtra(Constants.Extras.PAY_RESULT, isPaySuccess ? PAY_SUCCESS : PAY_FAIL));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        initView();
    }

    private void initView() {
        showHeader("支付结果");
        mSmartRefreshLayout.setEnableRefresh(false);
        mSmartRefreshLayout.setEnableLoadMore(false);

        mPayResultView = new PayResultView(this);
        mAdapter.addHeaderView(mPayResultView);

        initHeader();

    }

    private void initHeader() {
        if (mParamPayResult == PAY_SUCCESS) {
            mPayResultView.setPayResultStatus(PayResultView.PAY_SUCCESS_WITH_ORDER_DEALING);
            mPayResultView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getOrderStatus();
                }
            }, mWaitTime);
        } else {
            mPayResultView.setPayResultStatus(PayResultView.PAY_FAIL);
        }

        mPayResultView.setOnPayResultActionClickListener(this);
    }

    private void getOrderStatus() {
        APIManager.startRequest(mOrderService.getOrderStatus(mOrderCode), new BaseRequestListener<OrderPayStatusBean>(this) {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(OrderPayStatusBean result) {
                super.onSuccess(result);
                mOrderPayStatusBean = result;
                if (result.isOrderPaySuccess()) {
                    initOrderPaySuccess(result);
                } else {
                    mPayResultView.setPayResultStatus(PayResultView.PAY_SUCCESS_WITH_ORDER_FAIL);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mPayResultView.setPayResultStatus(PayResultView.PAY_SUCCESS_WITH_ORDER_FAIL);
            }
        });
    }

    private void initOrderPaySuccess(OrderPayStatusBean result) {
        checkUpdateUserInfo();
        refresh();
        mPayResultView.setOrderPaySuccess(result);
        mSmartRefreshLayout.setEnableLoadMore(true);
        addAdapterSecondHeader(result);
    }

    private void checkUpdateUserInfo() {
        DLog.i("checkUpdateUserInfo");
        User user = SessionUtil.getInstance().getLoginUser();
        if (!user.isStoreMaster() && mOrderPayStatusBean.isStoreGift()) {
            DLog.i("checkUpdateUserInfo：成为店主");
            user.vipType = 4;
            SessionUtil.getInstance().setLoginUser(user);
            EventBus.getDefault().post(new EventMessage(Event.becomeStoreMaster));
        }
    }

    private void addAdapterSecondHeader(OrderPayStatusBean orderPayStatusBean) {
        if (mAdapter.getHeaderLayoutCount() > 0 && mAdapter.getHeaderLayout().getChildCount() < 2) {
            mAdapter.addHeaderView(getBannerDividerHeader(orderPayStatusBean));
        }
    }

    @Override
    public void onOrderClick() {
        OrderDetailActivity.start(this, mOrderCode);
    }

    @Override
    public void onHomeClick() {
        MainActivity.goBack(this, 0);
    }

    @Override
    public void onRefreshClick() {
        ToastUtil.showLoading(this);
        getOrderStatus();
    }

    @Override
    public void onPayClick() {
        OrderService.viewPayActivity(context, mOrderCode);
    }

    @Override
    public void onServiceClick() {
        CSManager.share().jumpToChat(context, "");
    }

    @Override
    protected boolean autoRefresh() {
        return false;
    }

    @Override
    protected Observable<RequestResult<ListResultBean<DDProductBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IProductService.class).getSuggestProduct(mPage, 20);
    }

    @Override
    protected BaseQuickAdapter<DDProductBean, ? extends BaseViewHolder> getBaseQuickAdapter() {
        return new ProductAdapter();
    }

    private void getIntentData() {
        mParamPayResult = getIntent().getIntExtra(Constants.Extras.PAY_RESULT, PAY_SUCCESS);
        mOrderCode = getIntent().getStringExtra(Constants.Extras.ORDER_CODE);
        if (mParamPayResult == PAY_SUCCESS && TextUtils.isEmpty(mOrderCode)) {
            ToastUtil.error("订单号不能为空");
            finish();
        }
    }

    private View getBannerDividerHeader(OrderPayStatusBean orderPayStatusBean) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pay_divider_banner, mAdapter.getHeaderLayout(), false);
        final boolean isMaster = SessionUtil.getInstance().getLoginUser().isStoreMaster();
        SimpleDraweeView imgBanner = view.findViewById(R.id.sdv_banner);
        if (isMaster && !orderPayStatusBean.isShowActivityBanner()) {
            imgBanner.setVisibility(View.GONE);
            imgBanner.setImageURI(orderPayStatusBean.getImageUrl());
        } else {
            imgBanner.setImageResource(R.mipmap.banner_pay_result_normal);
        }

        imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jumpUrl(PayResultActivity.this, isMaster ? HtmlService.getMasterFollowerActivityURL() : HtmlService.BESHOPKEPPER);
            }
        });
        return view;
    }


}
