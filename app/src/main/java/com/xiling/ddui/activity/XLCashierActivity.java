package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.ddui.adapter.XLBankPayAdapter;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.bean.XLOrderDetailsBean;
import com.xiling.ddui.custom.D3ialogTools;
import com.xiling.ddui.service.IBankService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.module.community.DateUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收银台
 */
public class XLCashierActivity extends BaseActivity {
    public static final int ADD_BAND_CODE =1000;
    public static final String ORDER_DETAILS = "orderDetails";
    private IBankService mBankService;


    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_price_decimal)
    TextView tvPriceDecimal;

    XLOrderDetailsBean orderDetailsBean;
    @BindView(R.id.recycler_bank)
    RecyclerView recyclerBank;
    XLBankPayAdapter bankAdapter;
    List<BankListBean> bankListBeans = new ArrayList<>();
    @BindView(R.id.btn_pay)
    TextView btnPay;
    boolean isAdd = false;

    public static void jumpCashierActivity(Context context, XLOrderDetailsBean orderDetailsBean) {
        Intent intent = new Intent(context, XLCashierActivity.class);
        intent.putExtra(ORDER_DETAILS, orderDetailsBean);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlcashier);
        ButterKnife.bind(this);
        setTitle("收银台");
        setLeftBlack(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                D3ialogTools.showAlertDialog(context, "是否要放弃支付  订单会保留45分钟，请尽快支付", "继续支付", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }, "确认离开", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
        mBankService = ServiceManager.getInstance().createService(IBankService.class);


        if (getIntent() != null) {
            orderDetailsBean = getIntent().getParcelableExtra(ORDER_DETAILS);
        }

        if (orderDetailsBean != null) {
            NumberHandler.setPriceText(orderDetailsBean.getPayMoney(), tvPrice, tvPriceDecimal);
            startCountDown();
        }

        initBank();
        getBankList();
    }

    /**
     * 启动倒计时
     */
    private void startCountDown() {
        CountDownTimer countDownTimer = new CountDownTimer(orderDetailsBean.getWaitPayTimeMilli(), 1000) {
            @Override
            public void onTick(long l) {
                DateUtils.setCountDownTimeStrng(l, tvHour, tvMinute, tvSecond);
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }

    private void initBank() {
        bankListBeans.clear();
        BankListBean wxBank = new BankListBean();
        wxBank.setBankLogoGround(R.drawable.icon_pay_wx);
        wxBank.setBankName("微信支付");
        bankListBeans.add(wxBank);

        BankListBean zfbBank = new BankListBean();
        zfbBank.setBankLogoGround(R.drawable.icon_pay_zfb);
        zfbBank.setBankName("支付宝");
        bankListBeans.add(zfbBank);

        if (bankAdapter == null) {
            recyclerBank.setLayoutManager(new LinearLayoutManager(this));
            bankAdapter = new XLBankPayAdapter(bankListBeans);
            recyclerBank.setAdapter(bankAdapter);
            bankAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    bankAdapter.setSelectPosition(position);
                    btnPay.setText(bankListBeans.get(position).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(orderDetailsBean.getPayMoney()));
                }
            });
        }

    }

    private void getBankList() {
        APIManager.startRequest(mBankService.getBankCardList(), new BaseRequestListener<List<BankListBean>>(this) {

            @Override
            public void onSuccess(List<BankListBean> result) {
                super.onSuccess(result);

                if (result.size() > 0) {
                    initBank();
                    int position = bankListBeans.size();
                    bankListBeans.addAll(result);
                    if (!isAdd) {
                        bankAdapter.setSelectPosition(position);
                        btnPay.setText(bankListBeans.get(position).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(orderDetailsBean.getPayMoney()));
                    } else {
                        bankAdapter.setNewData(bankListBeans);
                    }
                } else {
                    btnPay.setText(bankListBeans.get(0).getBankName() + " ¥" + NumberHandler.reservedDecimalFor2(orderDetailsBean.getPayMoney()));
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }


    @OnClick({R.id.btn_pay, R.id.btn_add_bank, R.id.btn_pay_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_pay:
                break;
            case R.id.btn_add_bank:
                startActivityForResult(new Intent(context, XLAddBankActivity.class), 0);
                break;
            case R.id.btn_pay_type:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ADD_BAND_CODE) {
            getBankList();
        }
    }
}
