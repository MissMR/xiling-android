package com.xiling.ddmall.module.order;

import android.content.Intent;
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
import com.xiling.ddmall.shared.component.dialog.WJDialog;
import com.xiling.ddmall.shared.decoration.ListDividerDecoration;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.OrderService;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.util.CSUtils;
import com.xiling.ddmall.shared.util.ConvertUtil;

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
 * 退款详情
 */
public class RefundDetailActivity extends BaseActivity {

    @BindView(R.id.tvRefundType1)
    TextView mTvRefundType1;
    @BindView(R.id.tvRefundType2)
    TextView mTvRefundType2;
    @BindView(R.id.layoutRefundType)
    LinearLayout mLayoutRefundType;
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
    @BindView(R.id.tvRefundReason)
    TextView mTvRefundReason;
    @BindView(R.id.tvRefundMoney)
    TextView mTvRefundMoney;
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
    @BindView(R.id.rvProduct)
    RecyclerView mRvProduct;
    @BindView(R.id.tvItemEdit)
    TextView mTvItemEdit;
    @BindView(R.id.tvItemInput)
    TextView mTvItemInput;
    @BindView(R.id.tvItemCancel)
    TextView mTvItemCancel;
    @BindView(R.id.bottomLayout)
    LinearLayout mBottomLayout;
    @BindView(R.id.tvStoreInputExpress)
    TextView mTvStoreInputExpress;
    @BindView(R.id.layoutProduct)
    LinearLayout mLayoutProduct;
    @BindView(R.id.tvRefundTagTitle)
    TextView mTvRefundTagTitle;
    @BindView(R.id.tvRefundTagReason)
    TextView mTvRefundTagReason;
    @BindView(R.id.tvRefundTagMoney)
    TextView mTvRefundTagMoney;
    @BindView(R.id.tvRefundTagRemark)
    TextView mTvRefundTagRemark;
    @BindView(R.id.tvEditExpress)
    TextView mTvEditExpress;

    private String mRefundId;
    private IOrderService mService;
    private List<OrderProduct> products = new ArrayList<>();
    private RefundsOrder mRefundOrder;
    private ImageAdapter mImageAdapter;
    private OrderItemAdapter mProductAdapter;
    private RefundsOrder.ApiRefundOrderBeanEntity mRefundOrderBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_detail);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(mService.getRefundDetail(mRefundId), new BaseRequestListener<RefundsOrder>(this) {

            @Override
            public void onSuccess(RefundsOrder result) {
                super.onSuccess(result);
                mRefundOrder = result;
                mRefundOrderBean = result.apiRefundOrderBean;
                setRefundTypeLayout();
                setRefundStoreLayout();
                setRefundInfoLayout();
                setProductLayout();
                setBottomBtns();
                setTypeView();
            }
        });
    }

    private void setTypeView() {
        if (mRefundOrderBean.refundType == 2 || mRefundOrderBean.refundType == 4) {
            setTitle("退款详情");
            mTvRefundTagTitle.setText("退款信息");
            mTvRefundTagReason.setText("退款原因：");
            mTvRefundTagMoney.setText("申请退款金额：");
            mTvRefundTagRemark.setText("退款说明：");
        } else {
            setTitle("退货详情");
            mTvRefundTagTitle.setText("退货信息");
            mTvRefundTagReason.setText("退货原因：");
            mTvRefundTagMoney.setText("申请退货金额：");
            mTvRefundTagRemark.setText("退货说明：");
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
        mRvProduct.setAdapter(mProductAdapter);

        mRvProduct.addItemDecoration(new ListDividerDecoration(this));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setSmoothScrollbarEnabled(false);
        mRvRefundImages.setLayoutManager(gridLayoutManager);
        mImageAdapter = new ImageAdapter(this);
        mRvRefundImages.setAdapter(mImageAdapter);
    }

    private void getIntentData() {
        mRefundId = getIntent().getStringExtra(Config.INTENT_KEY_ID);
    }

    private void setRefundTypeLayout() {
        mTvRefundType1.setText(mRefundOrder.apiRefundOrderBean.mePromptDetail);
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

        if (mRefundOrder.apiRefundOrderBean.refundStatus == 1) {
            mTvStoreInputExpress.setVisibility(View.VISIBLE);
            mTvStoreExpress.setVisibility(View.GONE);
        } else {
            mTvStoreInputExpress.setVisibility(View.GONE);
            mTvStoreExpress.setVisibility(View.VISIBLE);

            String expressName = mRefundOrder.apiRefundOrderBean.refundGoodsExpressName;
            final String expressCode = mRefundOrder.apiRefundOrderBean.refundGoodsExpressCode;
            String format = String.format("%s <font color=\"#3333ff\">%s</font>", expressName, expressCode);
            mTvStoreExpress.setText(Html.fromHtml(format));
            mTvStoreExpress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OrderService.checkExpress(RefundDetailActivity.this, expressCode, mRefundOrder.apiRefundOrderBean.expressType);
                }
            });

            if (mRefundOrderBean.refundStatus == 2) {
                mTvEditExpress.setVisibility(View.VISIBLE);
            }
        }
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
        if (mRefundOrderBean.isRefundMoney()) {
            switch (mRefundOrderBean.refundStatus) {
                case 0:
                    mTvItemEdit.setVisibility(View.VISIBLE);
                    mTvItemCancel.setVisibility(View.VISIBLE);
                    break;
                default:
            }
        } else {
            switch (mRefundOrderBean.refundStatus) {
                case 0:
                    mTvItemEdit.setVisibility(View.VISIBLE);
                    mTvItemCancel.setVisibility(View.VISIBLE);
                    break;
                case 1:
                case 2:
                    mTvItemCancel.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void editRefunds(RefundsOrder refundsOrder) {
        if (refundsOrder.apiRefundOrderBean.isRefundMoney()) {
            OrderService.editRefundMoneyActivity(this, refundsOrder.apiRefundOrderBean.orderCode, refundsOrder.apiRefundOrderBean.refundId);
        } else {
            OrderService.addOrEditRefundOrder(this, refundsOrder.apiRefundOrderBean.orderCode, refundsOrder.orderProducts, refundsOrder.apiRefundOrderBean.refundId);
        }
    }

    private void cancelRefunds(final String refundId) {
        final WJDialog wjDialog = new WJDialog(this);
        wjDialog.show();
        wjDialog.setTitle("撤回申请");
        if (mRefundOrderBean.isRefundMoney()) {
            wjDialog.setContentText("撤回后，该退款单将被关闭");
        } else {
            wjDialog.setContentText("撤回后，该退货单将被关闭");
        }
        wjDialog.setCancelText("取消");
        wjDialog.setConfirmText("确定");
        wjDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wjDialog.dismiss();
                OrderService.cancelRefundExt(RefundDetailActivity.this, refundId);
            }
        });
    }

    private void inputRefundsInfo(String refundId) {
        Intent intent = new Intent(this, RefundExpressActivity.class);
        intent.putExtra("refundId", refundId);
        startActivity(intent);
    }

    @OnClick({R.id.tvStoreInputExpress, R.id.tvEditExpress, R.id.tvItemCS, R.id.tvItemEdit, R.id.tvItemCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvStoreInputExpress:
            case R.id.tvEditExpress:
                inputRefundsInfo(mRefundOrder.apiRefundOrderBean.refundId);
                break;
            case R.id.tvItemCS:
                CSUtils.start(this, "退款退货用户");
                break;
            case R.id.tvItemEdit:
                editRefunds(mRefundOrder);
                break;
            case R.id.tvItemCancel:
                cancelRefunds(mRefundOrder.apiRefundOrderBean.refundId);
                break;
        }
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
