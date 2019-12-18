package com.xiling.ddmall.module.order;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.order.adapter.OrderItemAdapter;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.OrderProduct;
import com.xiling.ddmall.shared.bean.RefundsOrder;
import com.xiling.ddmall.shared.common.ImageAdapter;
import com.xiling.ddmall.shared.decoration.ListDividerDecoration;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.OrderService;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/24 .
 */
public class SellerRefundDetailActivity extends BaseActivity {

    @BindView(R.id.tvRefundType1)
    TextView mTvRefundType1;
    @BindView(R.id.tvRefundType2)
    TextView mTvRefundType2;
    @BindView(R.id.layoutRefundType)
    LinearLayout mLayoutRefundType;
    @BindView(R.id.tvRefundTagTitle)
    TextView mTvRefundTagTitle;
    @BindView(R.id.tvRefundTagReason)
    TextView mTvRefundTagReason;
    @BindView(R.id.tvRefundReason)
    TextView mTvRefundReason;
    @BindView(R.id.tvRefundTagMoney)
    TextView mTvRefundTagMoney;
    @BindView(R.id.tvRefundMoney)
    TextView mTvRefundMoney;
    @BindView(R.id.tvRefundTagRemark)
    TextView mTvRefundTagRemark;
    @BindView(R.id.tvRefundRemark)
    TextView mTvRefundRemark;
    @BindView(R.id.rvRefundImages)
    RecyclerView mRvRefundImages;
    @BindView(R.id.tvRefundDate)
    TextView mTvRefundDate;
    @BindView(R.id.tvRefundCode)
    TextView mTvRefundCode;
    @BindView(R.id.tvRefundOrderCode)
    TextView mTvRefundOrderCode;
    @BindView(R.id.layoutRefundInfo)
    LinearLayout mLayoutRefundInfo;
    @BindView(R.id.tvBuyerName)
    TextView mTvBuyerName;
    @BindView(R.id.rvProduct)
    RecyclerView mRvProduct;
    @BindView(R.id.layoutProduct)
    LinearLayout mLayoutProduct;
    @BindView(R.id.tvOrderCode)
    TextView mTvOrderCode;
    @BindView(R.id.layoutOrder)
    LinearLayout mLayoutOrder;
    @BindView(R.id.bottomLayout)
    LinearLayout mBottomLayout;
    @BindView(R.id.tvItemStoreRefuse)
    TextView mTvItemStoreRefuse;
    @BindView(R.id.tvItemStoreAgree)
    TextView mTvItemStoreAgree;
    @BindView(R.id.tvItemStoreFinish)
    TextView mTvItemStoreFinish;
    @BindView(R.id.tvStoreName)
    TextView mTvStoreName;
    @BindView(R.id.tvStorePhone)
    TextView mTvStorePhone;
    @BindView(R.id.tvStoreAddress)
    TextView mTvStoreAddress;
    @BindView(R.id.tvStoreExpress)
    TextView mTvStoreExpress;
    @BindView(R.id.layoutStore)
    LinearLayout mLayoutStore;

    private String mRefundId;
    private IOrderService mService;
    private List<OrderProduct> products = new ArrayList<>();
    private RefundsOrder mRefundOrder;
    private ImageAdapter mImageAdapter;
    private OrderItemAdapter mProductAdapter;
    private RefundsOrder.ApiRefundOrderBeanEntity mRefundOrderBean;
    private String mMemberId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_refund_detail);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initData();
        EventBus.getDefault().post(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                mService.getSellerRefundDetail(mRefundId, mMemberId),
                new BaseRequestListener<RefundsOrder>(this) {

                    @Override
                    public void onSuccess(RefundsOrder result) {
                        super.onSuccess(result);
                        mRefundOrder = result;
                        mRefundOrderBean = result.apiRefundOrderBean;
                        setRefundTypeLayout();
                        setRefundStoreLayout();
                        setRefundInfoLayout();
                        setBuyerLayout();
                        setProductLayout();
                        setOrderLayout();
                        setBottomBtns();
                        setTypeView();
                    }
                });
    }

    private void setOrderLayout() {
        mTvOrderCode.setText(mRefundOrderBean.orderCode);
    }

    private void setBuyerLayout() {
        mTvBuyerName.setText("买家：" + mRefundOrderBean.nickName);
    }

    private void setTypeView() {
        if (mRefundOrderBean.isRefundMoney()) {
            setTitle("退款详情");
            mTvRefundTagTitle.setText("退款信息：");
            mTvRefundTagReason.setText("退款原因：");
            mTvRefundTagMoney.setText("申请退款金额：");
            mTvRefundTagRemark.setText("退款说明：");

            mTvItemStoreAgree.setText("同意退款");
            mTvItemStoreRefuse.setText("拒绝退款");
        } else {
            setTitle("退货详情");
            mTvRefundTagTitle.setText("退货信息：");
            mTvRefundTagReason.setText("退货原因：");
            mTvRefundTagMoney.setText("申请退货金额：");
            mTvRefundTagRemark.setText("退货说明：");

            mTvItemStoreAgree.setText("同意退货");
            mTvItemStoreRefuse.setText("拒绝退货");
        }
    }


    private void initView() {
        setTitle("退货详情");
        setLeftBlack();

        mRvProduct.setLayoutManager(new LinearLayoutManager(this));
        mRvProduct.setNestedScrollingEnabled(false);
        mProductAdapter = new OrderItemAdapter(this, products);
        mProductAdapter.setDetailModel(true);
        mProductAdapter.setRefundModel(true);
        mProductAdapter.setSellerModel(true);
        mRvProduct.addItemDecoration(new ListDividerDecoration(this));
        mRvProduct.setAdapter(mProductAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRvRefundImages.setLayoutManager(gridLayoutManager);
        mImageAdapter = new ImageAdapter(this);
        mRvRefundImages.setAdapter(mImageAdapter);
    }

    private void getIntentData() {
        mRefundId = getIntent().getStringExtra(Config.INTENT_KEY_ID);
        mMemberId = getIntent().getStringExtra("memberId");

    }

    private void setRefundTypeLayout() {
        mTvRefundType1.setText(mRefundOrderBean.storePromptDetail);
        mTvRefundType2.setVisibility(View.VISIBLE);
        switch (mRefundOrderBean.refundStatus) {
            case -3:
            case -4:
                mTvRefundType2.setText("拒绝原因：" + mRefundOrderBean.sellerRemark);
                break;
            case 4:
                mTvRefundType2.setText("实际退款金额：" + ConvertUtil.centToCurrency(this, mRefundOrderBean.refundMoney));
                break;
            default:
                mTvRefundType2.setVisibility(View.GONE);
                break;
        }
    }

    private void setRefundStoreLayout() {
        RefundsOrder.StoreBeanEntity storeBean = mRefundOrder.storeBean;
        if (mRefundOrderBean.isRefundMoney() || mRefundOrderBean.refundStatus == 0 || ((mRefundOrderBean.refundStatus == -2 || mRefundOrderBean.refundStatus == -4) && StringUtils.isEmpty(mRefundOrderBean.refundGoodsExpressCode))) {
            mLayoutStore.setVisibility(View.GONE);
            return;
        }
        mTvStoreName.setText(String.format("商家姓名： %s", storeBean.contact));
        mTvStorePhone.setText(String.format("商家电话： %s", storeBean.phone));
        mTvStoreAddress.setText(storeBean.province + storeBean.city + storeBean.district + storeBean.address);


        mTvStoreExpress.setVisibility(View.VISIBLE);
        String expressName = mRefundOrder.apiRefundOrderBean.refundGoodsExpressName;
        final String expressCode = mRefundOrder.apiRefundOrderBean.refundGoodsExpressCode;
        String format;
        if (StringUtils.isEmpty(expressCode)) {
            format = String.format("%s <font color=\"#999999\">%s</font>", expressName, "暂无");
        } else {
            format = String.format("%s <font color=\"#3333ff\">%s</font>", expressName, expressCode);
            mTvStoreExpress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.checkExpress(SellerRefundDetailActivity.this, expressCode, mRefundOrder.apiRefundOrderBean.expressType);
                }
            });
        }
        mTvStoreExpress.setText(Html.fromHtml(format));
    }


    private void setRefundInfoLayout() {
        RefundsOrder.ApiRefundOrderBeanEntity bean = mRefundOrder.apiRefundOrderBean;
        mTvRefundReason.setText(bean.refundReason);
        mTvRefundMoney.setText(ConvertUtil.centToCurrency(this, bean.applyRefundMoney));
        mTvRefundRemark.setText(bean.refundRemark);
        mImageAdapter.setItems(bean.refundGoodsImage);
        mTvRefundDate.setText(bean.createDate);
        mTvRefundCode.setText(bean.refundCode);
        mTvRefundOrderCode.setText(bean.orderCode);
    }

    private void setProductLayout() {
        if (mRefundOrder.orderProducts.size() == 0) {
            mLayoutProduct.setVisibility(View.GONE);
        } else {
            mProductAdapter.removeAllItems();
            mProductAdapter.addItems(mRefundOrder.orderProducts);
        }
    }

    private void setBottomBtns() {
        for (int i = 0; i < mBottomLayout.getChildCount(); i++) {
            mBottomLayout.getChildAt(i).setVisibility(View.GONE);
        }
        switch (mRefundOrderBean.refundStatus) {
            case 0:
                mTvItemStoreAgree.setVisibility(View.VISIBLE);
                mTvItemStoreRefuse.setVisibility(View.VISIBLE);
                break;
            case 2:
                mTvItemStoreRefuse.setVisibility(View.VISIBLE);
                mTvItemStoreFinish.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void refuseRefund() {
        if (mRefundOrderBean.refundType == 1 || mRefundOrderBean.refundType == 3) {
            OrderService.showRefuseRefundGoodsDialog(this, mRefundOrder);
        } else {
            OrderService.showRefuseRefundMoneyDialog(this, mRefundOrder);
        }
    }

    private void agreeRefund() {
        if (mRefundOrderBean.refundType == 1 || mRefundOrderBean.refundType == 3) {
            OrderService.showRefundGoodsDialog(this, mRefundOrder);
        } else {
            OrderService.showRefundMoneyDialog(this, mRefundOrder);
        }
    }

    @OnClick({R.id.tvItemStoreRefuse, R.id.tvItemStoreAgree, R.id.tvItemStoreFinish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvItemStoreRefuse:
                refuseRefund();
                break;
            case R.id.tvItemStoreAgree:
                agreeRefund();
                break;
            case R.id.tvItemStoreFinish:
                OrderService.finishOrder(this, mRefundOrder);
                break;
        }
    }

    @OnClick(R.id.layoutOrder)
    public void onViewClicked() {
        EventUtil.viewOrderDetailBySeller(this, mRefundOrderBean.orderCode);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatus(MsgStatus msgStatus) {
        switch (msgStatus.getAction()) {
            case MsgStatus.ACTION_REFUND_CHANGE:
                initData();
                break;
        }
    }
}
