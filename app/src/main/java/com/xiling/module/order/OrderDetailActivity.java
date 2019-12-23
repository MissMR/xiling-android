package com.xiling.module.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.manager.FreeEventManager;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.order.adapter.OrderItemAdapter;
import com.xiling.module.pay.PayMsg;
import com.xiling.module.store.ReceiveRefundGoodsActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Order;
import com.xiling.shared.bean.OrderProduct;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.common.AdvancedCountdownTimer;
import com.xiling.shared.common.ImageAdapter;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.decoration.ListDividerDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.OrderService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.util.CSUtils;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017-07-05
 * 订单详情页
 */
public class OrderDetailActivity extends BaseActivity {

    @BindView(R.id.tvScore)
    TextView mTvScore;
    @BindView(R.id.layoutScore)
    LinearLayout mLayoutScore;
    @BindView(R.id.itemShit)
    TextView mItemShit;
    @BindView(R.id.tvAgressRefundGoods)
    TextView mTvAgressRefundGoods;
    @BindView(R.id.tvAgressRefundMoney)
    TextView mTvAgressRefundMoney;
    @BindView(R.id.tvReceiveRefundGodds)
    TextView mTvReceiveRefundGodds;
    @BindView(R.id.layoutConpon)
    LinearLayout mLayoutConpon;
    @BindView(R.id.itemCancelRefundMoney)
    TextView mItemCancelRefundMoney;
    @BindView(R.id.itemCancelRefundGoods)
    TextView mItemCancelRefundGoods;
    @BindView(R.id.itemCheckGroupBuy)
    TextView mItemCheckGroupBuy;
    @BindView(R.id.itemGoGroupBuy)
    TextView mItemGoGroupBuy;
    @BindView(R.id.layoutMoney)
    LinearLayout mLayoutMoney;
    @BindView(R.id.tvSellerBuyerPayMoney)
    TextView mTvSellerBuyerPayMoney;
    @BindView(R.id.rvSellerProduct1)
    RecyclerView mRvSellerProduct1;
    @BindView(R.id.tvSellerFeight1)
    TextView mTvSellerFeight1;
    @BindView(R.id.tvSellerRecevieMoney)
    TextView mTvSellerRecevieMoney;
    @BindView(R.id.rvSellerProduct2)
    RecyclerView mRvSellerProduct2;
    @BindView(R.id.tvSellerFeight2)
    TextView mTvSellerFeight2;
    @BindView(R.id.layoutSeller)
    LinearLayout mLayoutSeller;
    @BindView(R.id.tvMoneyTag)
    TextView mTvMoneyTag;
    @BindView(R.id.tvOrderExpressCode)
    TextView mTvOrderExpressCode;
    @BindView(R.id.layoutOrderExpressCode)
    LinearLayout mLayoutOrderExpressCode;
    @BindView(R.id.tvCS)
    TextView mTvCS;
    private IOrderService mOrderService;
    private Order mOrder;
    @BindView(R.id.statusTv)
    protected TextView mStatusTv;
    @BindView(R.id.refundGoodTipsTv)
    protected TextView mRefundGoodTipsTv;
    @BindView(R.id.orderCodeTv)
    protected TextView mOrderCodeTv;

    @BindView(R.id.addRefundGoodExpressBtn)
    protected TextView mAddRefundGoodInfoBtn;

    @BindView(R.id.refundLayout)
    protected LinearLayout mRefundLayout;
    @BindView(R.id.refundReasonLabelTv)
    protected TextView mRefundReasonLabelTv;
    @BindView(R.id.refundReasonValueTv)
    protected TextView refundReasonValueTv;

    @BindView(R.id.refundApplyMoneyLabelTv)
    protected TextView refundApplyMoneyLabelTv;
    @BindView(R.id.refundApplyMoneyValueTv)
    protected TextView refundApplyMoneyValueTv;
    @BindView(R.id.refundMoneyLayout)
    protected LinearLayout mRefundMoneyLayout;
    @BindView(R.id.refundMoneyLabelTv)
    protected TextView refundMoneyLabelTv;
    @BindView(R.id.refundMoneyValueTv)
    protected TextView refundMoneyValueTv;

    @BindView(R.id.refundRemarkLayout)
    protected LinearLayout refundRemarkLayout;
    @BindView(R.id.refundRemarkLabelTv)
    protected TextView refundRemarkLabelTv;
    @BindView(R.id.refundRemarkValueTv)
    protected TextView refundRemarkValueTv;
    @BindView(R.id.imageRecyclerView)
    protected RecyclerView mImageRecyclerView;
    @BindView(R.id.refundExpressCompanyLayout)
    protected LinearLayout refundExpressCompanyLayout;
    @BindView(R.id.refundExpressCompanyValueTv)
    protected TextView refundExpressCompanyValueTv;
    @BindView(R.id.refundExpressCodeLayout)
    protected LinearLayout refundExpressCodeLayout;
    @BindView(R.id.refundExpressCodeValueTv)
    protected TextView refundExpressCodeValueTv;

    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.contactsTv)
    protected TextView mContactsTv;
    @BindView(R.id.addressDetailTv)
    protected TextView mAddressDetailTv;
    @BindView(R.id.storeNameTv)
    protected TextView mStoreNameTv;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.buyerRemarkLayout)
    protected LinearLayout mBuyerRemarkLayout;
    @BindView(R.id.buyerRemarkTv)
    protected TextView mBuyerRemarkTv;
    @BindView(R.id.sellerRemarkLayout)
    protected LinearLayout mSellerRemarkLayout;
    @BindView(R.id.sellerRemarkTv)
    protected TextView mSellerRemarkTv;

    @BindView(R.id.createDateTv)
    protected TextView mCreateDateTv;
    @BindView(R.id.payWayTv)
    protected TextView mPayWayTv;
    @BindView(R.id.productTotalTv)
    protected TextView mProductTotalTv;
    @BindView(R.id.freightTv)
    protected TextView mFreightTv;
    @BindView(R.id.couponTv)
    protected TextView mCouponTv;
    @BindView(R.id.payMoneyTv)
    protected TextView mPayMoneyTv;

    @BindView(R.id.orderBottomLayout)
    protected LinearLayout mOrderBottomLayout;

    @BindView(R.id.itemCancelBtn)
    protected TextView mCancelBtn;
    @BindView(R.id.itemPayBtn)
    protected TextView mPayBtn;
    // 申请退款
    @BindView(R.id.itemApplyRefundMoneyBtn)
    protected TextView mApplyRefundMoneyBtn;
    // 申请退货
    @BindView(R.id.itemApplyRefundGoodsBtn)
    protected TextView mApplyRefundGoodsBtn;
    @BindView(R.id.itemViewExpressBtn)
    protected TextView mViewExpressBtn;
    @BindView(R.id.orderFinishBtn)
    protected TextView mOrderFinishBtn;
    @BindView(R.id.tv_order_timeout)
    TextView mTvOrderTimeout;

    private int mMode;
    private String mOrderCode;
    private AdvancedCountdownTimer mCountdownTimer;

    private boolean mIsFromBuyStep = false;

    public static void start(Context context, String orderCode) {
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderCode", orderCode);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        showHeader();
        setTitle("订单详情");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mOrderCode = intent.getExtras().getString("orderCode");
            // from_action 目前只用来判断是否是从购买流程进入的详情  若传了这个字段 代表是从购买流程进来的
            mIsFromBuyStep = intent.hasExtra(Constants.Extras.FROM_ACTION);
        }
        if (mOrderCode == null || mOrderCode.isEmpty()) {
            ToastUtil.error("参数错误");
            finish();
            return;
        }
        mMode = intent.getIntExtra("mode", 0);
        loadOrderDetail(mOrderCode);
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
        if (mCountdownTimer != null) {
            mCountdownTimer.cancel();
        }
    }


    private void initSellerBottomButtons() {
        switch (mOrder.orderMain.status) {
            case AppTypes.ORDER.STATUS_SELLER_WAIT_SHIP:
                mItemShit.setVisibility(View.VISIBLE);
                break;
            case AppTypes.ORDER.STATUS_SELLER_HAS_SHIP:
                mViewExpressBtn.setVisibility(View.VISIBLE);
                break;
            default:
                mOrderBottomLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void initBottomButtons() {
        if (mOrder.orderMain.status > 0 && mOrder.orderMain.status < 7) {
            mOrderBottomLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < mOrderBottomLayout.getChildCount(); i++) {
                mOrderBottomLayout.getChildAt(i).setVisibility(View.GONE);
            }
            if (mOrder.isGroupOrder()) {
                mItemCheckGroupBuy.setVisibility(View.VISIBLE);
            }
            switch (mOrder.orderMain.status) {
                case AppTypes.ORDER.STATUS_BUYER_WAIT_PAY:
                    mPayBtn.setVisibility(View.VISIBLE);
                    mCancelBtn.setVisibility(View.VISIBLE);
                    break;
                case AppTypes.ORDER.STATUS_BUYER_WAIT_SHIP:
                    if (mOrder.isShowGroupOrderStatus()) {
                        mItemCheckGroupBuy.setVisibility(View.GONE);
                        mItemGoGroupBuy.setVisibility(View.VISIBLE);
                    } else if (!mOrder.isStoreGift() && !mOrder.isOrderFree() && !mOrder.isUseCoupon()) {
                        // 非店主礼包  非0元购 未使用过优惠券 才可退款
                        mApplyRefundMoneyBtn.setVisibility(View.VISIBLE);
                    }
                    break;
                case AppTypes.ORDER.STATUS_BUYER_HAS_SHIP:
                    if (!mOrder.isStoreGift() && !mOrder.isOrderFree() && !mOrder.isUseCoupon()) {
                        // 非店主礼包 非0元购 未使用过优惠券 显示申请退货
                        mApplyRefundGoodsBtn.setVisibility(View.VISIBLE);
                    }
                    mViewExpressBtn.setVisibility(View.VISIBLE);
                    mOrderFinishBtn.setVisibility(View.VISIBLE);
                    mItemCheckGroupBuy.setVisibility(View.GONE);
                    break;
                default:
                    mOrderBottomLayout.setVisibility(View.GONE);
                    break;
            }
        } else {
            mOrderBottomLayout.setVisibility(View.GONE);
        }
    }

    private void loadOrderDetail(String orderCode) {
        APIManager.startRequest(mOrderService.getOrderByCode(orderCode), new BaseRequestListener<Order>(this) {
            @Override
            public void onSuccess(Order result) {
                mOrder = result;
                initOrderProducts();
                initOrderBase();
                initRefundViews();
                initBottomButtons();
                //Jigsaw 隐藏商家客服
                mTvCS.setVisibility(View.GONE);
                checkUpdateUserInfo();
                checkToStartFreeBuyResultActivity();
                if (isOrderWaittingPay() && mCountdownTimer == null) {
                    startTimer(mOrder.orderMain.outTime + 1);
                }
            }

        });
    }

    private void startTimer(int count) {
        if (mCountdownTimer == null) {
            mCountdownTimer = new AdvancedCountdownTimer(count * 60 * 1000, 60 * 1000) {
                @Override
                public void onTick(long millisUntilFinished, int percent) {
                    if (isOrderWaittingPay()) {
                        loadOrderDetail(mOrderCode);
                    } else {
                        mCountdownTimer.cancel();
                    }
                }

                @Override
                public void onFinish() {
                    mCountdownTimer.cancel();
                }
            };
        }
        mCountdownTimer.start();
    }

    private boolean isOrderWaittingPay() {
        return mOrder == null ? false : mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_WAIT_PAY;
    }

    private boolean checkToStartFreeBuyResultActivity() {
        if (mOrder.isOrderFree() && mIsFromBuyStep) {
            FreeEventManager.share().checkStatus(this);
            return true;
        }
        return false;
    }

    private void checkUpdateUserInfo() {
        if (mOrder.isStoreGift() && mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_WAIT_SHIP) {
            // 待发货 而且是店主礼包的订单
            User user = SessionUtil.getInstance().getLoginUser();
            if (!user.isStoreMaster()) {
                user.vipType = 4;
                SessionUtil.getInstance().setLoginUser(user);
                EventBus.getDefault().post(new EventMessage(Event.becomeStoreMaster));
            }
        }
    }


    private void initOrderProducts() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new ListDividerDecoration(this));
        OrderItemAdapter adapter = new OrderItemAdapter(this, mOrder.products);
        adapter.setDetailModel(true);
        adapter.setSellerModel(false);
        mRecyclerView.setAdapter(adapter);
        adapter.setMemberId(mOrder.orderMain.memberId);
        adapter.setCallback(new BaseCallback<Object>() {
            @Override
            public void callback(Object data) {
                if (data instanceof OrderProduct) {
                    if (!mOrder.isOrderFree()) {
                        DDProductDetailActivity.start(OrderDetailActivity.this, ((OrderProduct) data).spuId);
                    }
                }
            }
        });
    }

    private void initOrderBase() {

        mStatusTv.setText(mOrder.orderMain.orderStatusStr);
        if (mOrder.isShowGroupOrderStatus()) {
            mRefundGoodTipsTv.setVisibility(View.VISIBLE);
            Order.GroupInfoEntity groupInfo = mOrder.groupInfo;
            String date = TimeUtils.date2String(TimeUtils.string2Date(groupInfo.expiresDate), "MM月dd日 HH:mm:ss");
            int min = groupInfo.joinMemberNum - groupInfo.payOrderNum;
            mRefundGoodTipsTv.setText(String.format("还差%d人成团，%s截止", min, date));
        }
        mOrderCodeTv.setText(mOrder.orderMain.orderCode);

        if (mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_HAS_SHIP || mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_HAS_RECEIVED) {
            mLayoutOrderExpressCode.setVisibility(View.VISIBLE);
            mTvOrderExpressCode.setText(mOrder.orderMain.expressCode.replace(",", "\n"));
        } else {
            mLayoutOrderExpressCode.setVisibility(View.GONE);
        }

        mPhoneTv.setText(mOrder.orderMain.phone);
        mContactsTv.setText(mOrder.orderMain.contact);
        mAddressDetailTv.setText(mOrder.orderMain.getFullAddress());

        mStoreNameTv.setText(mOrder.storeName);

        if (!(mOrder.orderMain.buyerRemark == null || mOrder.orderMain.buyerRemark.isEmpty())) {
            mBuyerRemarkLayout.setVisibility(View.VISIBLE);
            mBuyerRemarkTv.setText(mOrder.orderMain.buyerRemark);
        }
        if (!(mOrder.orderMain.sellerRemark == null || mOrder.orderMain.sellerRemark.isEmpty())) {
            mSellerRemarkLayout.setVisibility(View.VISIBLE);
            mSellerRemarkTv.setText(mOrder.orderMain.sellerRemark);
        }
        mCreateDateTv.setText(mOrder.orderMain.createDate);
        mPayWayTv.setText(mOrder.orderMain.payTypeStr);
        mProductTotalTv.setText(ConvertUtil.centToCurrency(this, mOrder.orderMain.totalProductMoney));
        mFreightTv.setText(ConvertUtil.centToCurrency(this, mOrder.orderMain.freight));

        if (mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_WAIT_PAY || (mOrder.orderMain.status == AppTypes.ORDER.STATUS_BUYER_HAS_CLOSE && mOrder.orderMain.payMoney < mOrder.orderMain.totalMoney)) {
            mTvMoneyTag.setText("待付款：");
        }
        mPayMoneyTv.setText(ConvertUtil.centToCurrency(this, mOrder.orderMain.totalMoney));

        //优惠券
        if (mOrder.orderMain.discountCoupon > 0) {
            mLayoutConpon.setVisibility(View.VISIBLE);
            mCouponTv.setText(ConvertUtil.centToCurrency(this, -mOrder.orderMain.discountCoupon));
        }

        //积分
        long score = mOrder.orderMain.score;
        if (score > 0 && Config.IS_DISCOUNT) {
            mLayoutScore.setVisibility(View.VISIBLE);
            mTvScore.setText(String.format("%d积分，抵¥%.2f", score, score * 1.0f / 10));
        }

        mTvOrderTimeout.setVisibility(isOrderWaittingPay() ? View.VISIBLE : View.GONE);

        if (isOrderWaittingPay()) {
            mTvOrderTimeout.setText(String.format("剩%s分自动关闭", mOrder.orderMain.outTime));
        }
    }

    private void initRefundViews() {
        if (mOrder.refundOrder == null || mOrder.refundOrder.refundId == null || mOrder.orderMain.status < 5) {
            return;
        }
        mRefundLayout.setVisibility(View.VISIBLE);
        String tag = mOrder.refundOrder.refundType == 1 ? "退货" : "退款";
        if (mOrder.refundOrder.refundType == 1 && mOrder.refundOrder.refundStatus == 1) {
            mRefundGoodTipsTv.setVisibility(View.VISIBLE);
            mAddRefundGoodInfoBtn.setVisibility(View.VISIBLE);
        }
        mRefundReasonLabelTv.setText(tag + "原因：");
        refundReasonValueTv.setText(mOrder.refundOrder.refundReason);
        refundApplyMoneyLabelTv.setText("申请" + tag + "金额：");
        refundApplyMoneyValueTv.setText(ConvertUtil.centToCurrency(this, mOrder.refundOrder.applyRefundMoney));
        if (mOrder.refundOrder.refundStatus >= 4) {
            mRefundMoneyLayout.setVisibility(View.VISIBLE);
            refundMoneyLabelTv.setText("实际" + tag + "金额：");
            refundMoneyValueTv.setText(ConvertUtil.centToCurrency(this, mOrder.refundOrder.refundMoney));
        }
        if (!mOrder.refundOrder.refundRemark.isEmpty()) {
            refundRemarkLayout.setVisibility(View.VISIBLE);
            refundRemarkLabelTv.setText(tag + "说明：");
            refundRemarkValueTv.setText(mOrder.refundOrder.refundRemark);
        }

        if (mOrder.refundOrder.refundType == 1) {
            if (!mOrder.refundOrder.refundGoodsImage.isEmpty()) {
                mImageRecyclerView.setVisibility(View.VISIBLE);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
                gridLayoutManager.setAutoMeasureEnabled(true);
                gridLayoutManager.setSmoothScrollbarEnabled(false);
                mImageRecyclerView.setLayoutManager(gridLayoutManager);
                ImageAdapter imageAdapter = new ImageAdapter(this);
                mImageRecyclerView.setAdapter(imageAdapter);
                imageAdapter.setItems(mOrder.refundOrder.refundGoodsImage);
            }

            if (mOrder.refundOrder.refundStatus > 1) {
                refundExpressCompanyLayout.setVisibility(View.VISIBLE);
                refundExpressCodeLayout.setVisibility(View.VISIBLE);
                refundExpressCompanyValueTv.setText(mOrder.refundOrder.refundGoodsExpressName);
                refundExpressCodeValueTv.setText(mOrder.refundOrder.refundGoodsExpressCode);
            }
        }
    }

    @OnClick(R.id.itemCancelBtn)
    public void cancelOrder() {
        OrderService.cancelOrder(this, mOrder);
    }

    @OnClick(R.id.itemPayBtn)
    public void payOrder() {
        OrderService.viewPayActivity(this, mOrder.orderMain.orderCode);
    }

    @OnClick(R.id.itemApplyRefundMoneyBtn)
    public void applyRefundMoney() {
        OrderService.viewApplyRefundMoneyActivity(this, mOrder);
    }

    @OnClick(R.id.itemApplyRefundGoodsBtn)
    public void applyRefundGoods() {
        OrderService.viewApplyRefundGoodsActivity(this, mOrder);
    }

    @OnClick({R.id.itemViewExpressBtn, R.id.tvOrderExpressCode})
    public void viewExpress() {
        OrderService.viewExpress(this, mOrder);
    }

    @OnClick(R.id.orderFinishBtn)
    public void finishOrder() {
        OrderService.finishOrder(this, mOrder);
    }

    @OnClick(R.id.tvAgressRefundGoods)
    public void onMTvAgressRefundGoodsClicked() {
//        OrderService.showRefundGoodsDialog(this, mOrder);
    }

    @OnClick(R.id.tvAgressRefundMoney)
    public void onMTvAgressRefundMoneyClicked() {
//        OrderService.showRefundMoneyDialog(this, mOrder);
    }

    @OnClick(R.id.tvReceiveRefundGodds)
    public void onMTvReceiveRefundGoddsClicked() {
        // 确认退货
        Intent intent = new Intent(this, ReceiveRefundGoodsActivity.class);
        intent.putExtra("orderCode", mOrder.orderMain.orderCode);
        intent.putExtra("maxPrice", mOrder.canRefundMoney(1));
        startActivity(intent);
    }

    @OnClick(R.id.addRefundGoodExpressBtn)
    public void addRefundGoodExpress() {
        Intent intent = new Intent(this, RefundExpressActivity.class);
        intent.putExtra("refundId", mOrder.refundOrder.refundId);
        startActivity(intent);
    }

    @OnClick(R.id.itemShit)
    public void onViewClicked() {
        OrderService.ship(this, mOrder);
    }

    @OnClick({R.id.itemCancelRefundMoney, R.id.itemCancelRefundGoods})
    public void onViewClicked(View view) {
        OrderService.showCancelRefund(this, mOrder);
    }

    @OnClick({R.id.itemGoGroupBuy, R.id.itemCheckGroupBuy})
    public void itemGoGroupBuy() {
        OrderService.goGroupBuy(this, mOrder);
    }

    @OnClick(R.id.tvCS)
    public void onGoCS() {
        CSUtils.start(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void orderHandler(EventMessage message) {
        if (message.getEvent().equals(Event.cancelOrder)) {
            mOrder.orderMain.status = 0;
            mOrder.orderMain.orderStatusStr = "已关闭";
            initBottomButtons();
            initOrderBase();
        } else if (message.getEvent().equals(Event.finishOrder)) {
            mOrder.orderMain.status = 4;
            mOrder.orderMain.orderStatusStr = "已收货";
            initBottomButtons();
            initOrderBase();
        } else if (message.getEvent().equals(Event.refundExpressSubmit)) {
            loadOrderDetail(mOrder.orderMain.orderCode);
        } else if (message.getEvent().equals(Event.refundOrder)) {
            finish();
            EventUtil.viewOrderDetail(this, mOrder.orderMain.orderCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        switch (msgStatus.getAction()) {
            case MsgStatus.ACTION_REFUND_CHANGE:
                loadOrderDetail(mOrderCode);
                break;
            case MsgStatus.ACTION_STORE_SHIT_SUCCEED:
                finish();
                EventUtil.viewOrderDetail(this, mOrder.orderMain.orderCode);
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(PayMsg msgStatus) {
        switch (msgStatus.getAction()) {
            case PayMsg.ACTION_ALIPAY_SUCCEED:
            case PayMsg.ACTION_WEBPAY_SUCCEED:
            case PayMsg.ACTION_WXPAY_SUCCEED:

                break;
            case PayMsg.ACTION_BALANCE_SUCCEED:
                loadOrderDetail(mOrderCode);
                break;
            case PayMsg.ACTION_WXPAY_FAIL:
            case PayMsg.ACTION_ALIPAY_FAIL:
            case PayMsg.ACTION_WEBPAY_FAIL:
                break;
            default:
        }
    }


}
