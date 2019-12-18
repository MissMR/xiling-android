package com.xiling.ddmall.module.balance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Balance;
import com.xiling.ddmall.shared.bean.DealDetail;
import com.xiling.ddmall.shared.bean.OrderProduct;
import com.xiling.ddmall.shared.bean.PayDetail;
import com.xiling.ddmall.shared.bean.Transfer;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IBalanceService;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/20.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.module.balance
 * @since 2017/6/20 上午11:07
 */

public class BalanceDetailActivity extends BaseActivity {

    @BindView(R.id.titleTv)
    protected TextView mTitleTv;
    @BindView(R.id.moneyTv)
    protected TextView mMoneyTv;
    @BindView(R.id.timeTitleTv)
    protected TextView mTimeTitleTv;
    @BindView(R.id.timeTv)
    protected TextView mTimeTv;
    @BindView(R.id.typeTitleTv)
    protected TextView mTypeTitleTv;
    @BindView(R.id.typeTv)
    protected TextView mTypeTv;
    @BindView(R.id.transferTitleTv)
    protected TextView mTransferTitleTv;
    @BindView(R.id.transferTv)
    protected TextView mTransferTv;
    @BindView(R.id.remarkTitleTv)
    protected TextView mRemarkTitleTv;
    @BindView(R.id.remarkTv)
    protected TextView mRemarkTv;
    @BindView(R.id.transferLayout)
    protected LinearLayout mTransferLayout;
    @BindView(R.id.remarkLayout)
    protected LinearLayout mRemarkLayout;
    @BindView(R.id.orderDetailLayout)
    protected LinearLayout mOrderDetailLayout;

    private IBalanceService mBalanceService;
    private Balance mBalance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_detail_layout);
        mBalanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String id = intent.getExtras().getString("did");
        long typeId = intent.getExtras().getLong("typeId");
        mBalance = (Balance) intent.getSerializableExtra("data");
        if (typeId == 1) {
            mTransferLayout.setVisibility(View.GONE);
            getPayDetail(id);
        } else if (typeId == 3) {
            getPayDetail(id);
        } else if (typeId == 2) {
            getDealDetail(id);
        } else if (typeId == 4 || typeId == 5) {
            getTransferDetail(id);
        } else if (typeId == 6) {
            setBaseView();
        }

    }

    private void setBaseView() {
        mTitleTv.setText(mBalance.statusStr);
        if (mBalance.money > 0) {
            mMoneyTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(mBalance.money)));
            mMoneyTv.setTextColor(getResources().getColor(R.color.red));
        } else {
            mMoneyTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(mBalance.money)));
        }
        mTimeTv.setText(mBalance.createDate);
        mTimeTv.setText("时间");
        mTypeTv.setText(mBalance.typeName);
        mTransferLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("零钱详情");
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getPayDetail(final String did) {
        APIManager.startRequest(mBalanceService.getPayDetail(did), new BaseRequestListener<PayDetail>(this) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(PayDetail payDetail) {
                if (payDetail.payMoney < 0) {
                    initPayDetailView(payDetail);
                } else {
                    mTitleTv.setText("退款成功");
                    mMoneyTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(payDetail.payMoney)));
                    mMoneyTv.setTextColor(getResources().getColor(R.color.red));
                    mTimeTitleTv.setText("退款时间:");
                    mTimeTv.setText(payDetail.createDate);
                    mTransferTv.setText("账户余额");
                }
                mTypeTv.setText(mBalance.typeName);
            }
        });
    }

    @SuppressLint("DefaultLocale")
    private void initPayDetailView(PayDetail payDetail) {
        mTitleTv.setText("支付成功");
        mMoneyTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(payDetail.payMoney)));
        mMoneyTv.setTextColor(getResources().getColor(R.color.text_black));
        mTimeTitleTv.setText("支付时间");
        mRemarkLayout.setVisibility(View.GONE);
        mOrderDetailLayout.setVisibility(View.VISIBLE);
        LinearLayout orderItemLayout = (LinearLayout) mOrderDetailLayout.findViewById(R.id.orderItemLayout);
        TextView mOrderCodeTv = (TextView) mOrderDetailLayout.findViewById(R.id.orderCodeTv);
        TextView mOrderTimeTv = (TextView) mOrderDetailLayout.findViewById(R.id.orderTimeTv);
        mOrderCodeTv.setText(payDetail.order.orderMain.orderCode);
        mOrderTimeTv.setText(payDetail.order.orderMain.createDate);
        orderItemLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        for (OrderProduct product : payDetail.order.products) {
            LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.cmp_order_item, orderItemLayout, false);
            TextView mItemProductNameTv = (TextView) view.findViewById(R.id.itemProductNameTv);
            TextView mItemNumTv = (TextView) view.findViewById(R.id.itemNumTv);
            TextView mItemMoneyTv = (TextView) view.findViewById(R.id.itemMoneyTv);
            mItemProductNameTv.setText(product.name);
            mItemNumTv.setText(product.getAmountString());
            mItemMoneyTv.setText(String.format("单价:%s", ConvertUtil.centToCurrency(this, product.price)));
            orderItemLayout.addView(view);
            for (OrderProduct present : product.presents) {
                orderItemLayout.addView(createPresentView(layoutInflater, orderItemLayout, present));
            }
        }
    }

    private View createPresentView(LayoutInflater layoutInflater, LinearLayout orderItemLayout, OrderProduct present) {
        LinearLayout view = (LinearLayout) layoutInflater.inflate(R.layout.cmp_order_item_presont, orderItemLayout, false);
        TextView mItemProductNameTv = (TextView) view.findViewById(R.id.itemProductNameTv);
        mItemProductNameTv.setText(present.skuName);
        return view;
    }

    private void getDealDetail(final String did) {
        APIManager.startRequest(mBalanceService.getDealDetail(did), new BaseRequestListener<DealDetail>(this) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(DealDetail dealDetail) {
                mTitleTv.setText(dealDetail.dealStatusStr);
                if (dealDetail.applyMoney < 0) {
                    mMoneyTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(dealDetail.applyMoney)));
                    mMoneyTv.setTextColor(getResources().getColor(R.color.text_black));
                    mRemarkLayout.setVisibility(View.GONE);
                } else {
                    mMoneyTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(dealDetail.applyMoney)));
                    mMoneyTv.setTextColor(getResources().getColor(R.color.red));
                    mRemarkLayout.setVisibility(View.GONE);
                }
                mTimeTitleTv.setText("提现时间");
                mTimeTv.setText(dealDetail.applyDate);
                if (dealDetail.account.accountType == 3) {
                    mTypeTv.setText("提现到微信");
                    mTransferTitleTv.setText("微信昵称");
                    mTransferTv.setText(dealDetail.account.wechatUser);
                } else {
                    mTypeTv.setText("提现到银行卡");
                    mTransferTitleTv.setText("银行账号");
                    String code = dealDetail.account.bankAccount.length()<4?"":dealDetail.account.bankAccount.substring(dealDetail.account.bankAccount.length() - 4);
                    mTransferTv.setText(String.format("%s（尾号%s）", dealDetail.account.bankName, code));
                }

            }
        });
    }

    private void getTransferDetail(final String did) {
        APIManager.startRequest(mBalanceService.getTransferDetail(did), new BaseRequestListener<Transfer>(this) {
            @SuppressLint("DefaultLocale")
            @Override
            public void onSuccess(Transfer transfer) {
                mTitleTv.setText(transfer.statusStr);
                if (transfer.transferMoney < 0) {
                    mMoneyTv.setText(String.format("%.2f", ConvertUtil.cent2yuan(transfer.transferMoney)));
                    mMoneyTv.setTextColor(getResources().getColor(R.color.text_black));
                    mTimeTitleTv.setText("转出时间");
                    mTransferTv.setText(String.format("%s(%s)", transfer.inUserName, transfer.inPhone));
                } else {
                    mMoneyTv.setText(String.format("+%.2f", ConvertUtil.cent2yuan(transfer.transferMoney)));
                    mMoneyTv.setTextColor(getResources().getColor(R.color.red));
                    mTimeTitleTv.setText("转入时间");
                    mTransferTv.setText(String.format("%s(%s)", transfer.outUserName, transfer.outPhone));
                }

                mTimeTv.setText(transfer.createDate);
                mTypeTv.setText("转账");
                mTransferTitleTv.setText("对方账户");
                mRemarkTv.setText(transfer.transferMemo);
                if (!com.blankj.utilcode.utils.StringUtils.isEmpty(transfer.transferMemo)) {
                    mRemarkLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
