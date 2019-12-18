package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.WithdrawBean;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.component.dialog.DDMDialog;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SpanUtils;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 店主提现界面
 */
public class CashWithdrawActivity extends BaseActivity {

    @BindView(R.id.tv_btn_next)
    TextView tvBtnNext;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_max_withdraw)
    TextView tvMaxWithdraw;
    @BindView(R.id.sdv_bank)
    SimpleDraweeView sdvBank;
    @BindView(R.id.tv_bank_card)
    TextView tvBankCard;

    private IUserService mIUserService;
    private WithdrawBean mWithdrawBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdraw);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData() {
        mIUserService = ServiceManager.getInstance().createService(IUserService.class);
        getWithdrawInfo();
    }

    private void getWithdrawInfo() {
        APIManager.startRequest(mIUserService.getWithdrawInfo(), new BaseRequestListener<WithdrawBean>(this) {
            @Override
            public void onSuccess(WithdrawBean result) {
                super.onSuccess(result);
                mWithdrawBean = result;
                setWithdrawInfo();
            }
        });
    }

    private void setWithdrawInfo() {
        FrescoUtil.setImageSmall(sdvBank, mWithdrawBean.getBankImgUrl());
        tvBankCard.setText(mWithdrawBean.getBankName() + "(" + mWithdrawBean.getBankCardCode() + ")");
        String hint = "";
        if (mWithdrawBean.getMinWithdrawAmount() > 0) {
            hint = "最低提现" + ConvertUtil.cent2yuan(mWithdrawBean.getMinWithdrawAmount()) + "元 ";
        }
        if (mWithdrawBean.getMaxWithdrawAmount() > -1) {
            hint += "最高提现" + ConvertUtil.cent2yuan(mWithdrawBean.getMaxWithdrawAmount()) + "元";
        }
        etMoney.setHint(TextUtils.isEmpty(hint) ? "请输入提现金额" : hint);

        long money = Long.valueOf(mWithdrawBean.getBalance());
        tvMaxWithdraw.setText(ConvertUtil.centToCurrency(this, money));
    }

    private void initView() {
        ToastUtil.showLoading(this);
        tvBtnNext.setEnabled(false);
        showHeader("提现");
        getHeaderLayout().setRightText("提现记录");
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CashWithdrawActivity.this, CashWithdrawRecordActivity.class));
            }
        });
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                    }
                }

                tvBtnNext.setEnabled(!TextUtils.isEmpty(etMoney.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.tv_btn_next)
    public void onNextClicked() {
        if (mWithdrawBean == null) {
            return;
        }

        APIManager.startRequest(mIUserService.checkWidthdrawNum(), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                performNextStep();
            }
        });

    }

    private void performNextStep() {
        if (checkMoneyEnable()) {
            Long money = null;
            try {
                money = ConvertUtil.stringMoney2Long(etMoney.getText().toString().trim());
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(this, CashWithdrawSecurityActivity.class)
                    .putExtra(Constants.Extras.WITHDRAW, money)
                    .putExtra(Constants.Extras.QUESTION, mWithdrawBean.getSafeProblem()));
        }
    }

    private boolean checkMoneyEnable() {
        if (TextUtils.isEmpty(etMoney.getText())) {
            ToastUtil.error("请输入提现金额");
            return false;
        }
        String hint = "";
        boolean result = false;
        long money = 0;
        try {
            money = ConvertUtil.stringMoney2Long(etMoney.getText().toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            ToastUtil.error("请输入正确的金额");
        }
        if (money < mWithdrawBean.getMinWithdrawAmount()) {
            hint = "提现金额小于最低提现金额";
        } else if (money >= mWithdrawBean.getMinWithdrawAmount()
                && money <= Long.valueOf(mWithdrawBean.getBalance())) {
            result = true;
        }
        if (money > Long.valueOf(mWithdrawBean.getBalance())) {
            hint = "提现金额大于可提现金额";
        } else if (mWithdrawBean.getMaxWithdrawAmount() > 0 && money > mWithdrawBean.getMaxWithdrawAmount()) {
            hint = "提现金额大于最高提现金额";
        }

        if (mWithdrawBean.getMinWithdrawAmount() == -1 && 0 == money) {
            hint = "提现金额需大于0元";
        }

        if (!TextUtils.isEmpty(hint)) {
            ToastUtil.error(hint);
            result = false;
        }
        return result;
    }

    @OnClick({R.id.tv_btn_withdraw_all, R.id.tv_withdraw_introduce})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_btn_withdraw_all:
                if (checkCanWithdrawAll()) {
                    etMoney.setText(ConvertUtil.cent2yuan2(mWithdrawBean.getBalance()));
                }
                break;
            case R.id.tv_withdraw_introduce:
                showIntroduceDialog();
                break;
        }
    }

    private boolean checkCanWithdrawAll() {
        if (mWithdrawBean == null) {
            return false;
        }

        if (mWithdrawBean.checkCanAllWithDraw()) {
            // 可以全部提现
            return true;
        }
        String dialogContent;
        if (mWithdrawBean.getBalance() < mWithdrawBean.getMinWithdrawAmount()) {
            // 小于最小提现金额
            dialogContent = "可提现金额小于最小提现金额！";
            new DDMDialog(this)
                    .setTitle("提示")
                    .setContent(dialogContent).setPositiveButton("确认", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).show();
        } else {
            // 大于最大提现金额
            etMoney.setText(ConvertUtil.cent2yuan2(mWithdrawBean.getMaxWithdrawAmount()));
        }
        return false;
    }

    private void showIntroduceDialog() {
        new DDMDialog(this)
                .setTitle("提现说明")
                .setPositiveButton("我知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .setContent(getDialogContent())
                .show();
    }

    private CharSequence getDialogContent() {
        SpanUtils spanUtils = new SpanUtils();
        spanUtils.append("提现有什么要求？\n");
        spanUtils.append("会员的实名认证姓名必须与提现银行卡姓名保持一致，并且银行卡信息完整。");
        setSpanSmallSize(spanUtils);
        if (mWithdrawBean.getMinWithdrawAmount() > 0) {
            spanUtils.append("最低金额为" + ConvertUtil.cent2yuan(mWithdrawBean.getMinWithdrawAmount()) + "元!");
            setSpanSmallSize(spanUtils);
        }
        if (mWithdrawBean.getMaxWithdrawAmount() > 0) {
            spanUtils.append("最高金额为" + ConvertUtil.cent2yuan(mWithdrawBean.getMaxWithdrawAmount()) + "元！");
            setSpanSmallSize(spanUtils);
        }
        spanUtils.append("\n");
        spanUtils.append("提现什么时候可以到账?\n");
        spanUtils.append("一般为审核通过后工作日内24小时会到账，请留意银行通知！\n");
        setSpanSmallSize(spanUtils);

        spanUtils.append("提现有次数限制吗？\n");
        spanUtils.append("每天只有2次提现机会。");
        setSpanSmallSize(spanUtils);

        return spanUtils.create();
    }

    private void setSpanSmallSize(SpanUtils spanUtils) {
        spanUtils.setForegroundColor(getResources().getColor(R.color.ddm_gray_dark))
                .setFontSize(12, true);
    }
}
