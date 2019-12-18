package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CashWithdrawRecordBean;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/9/13
 * 提现记录详情页
 */
public class CashWithdrawDetailActivity extends BaseActivity {


    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_withdraw_state)
    TextView tvWithdrawState;
    @BindView(R.id.tv_bank_name)
    TextView tvBankName;
    @BindView(R.id.tv_bank_card_number)
    TextView tvBankCardNumber;
    @BindView(R.id.tv_withdraw_number)
    TextView tvWithdrawNumber;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_remark)
    TextView tvRemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_withdraw_detail);
        ButterKnife.bind(this);
        initView();

        APIManager.startRequest(ServiceManager.getInstance().createService(IUserService.class)
                        .getWithdrawInfo(getIntent().getStringExtra(Constants.Extras.ID)),
                new BaseRequestListener<CashWithdrawRecordBean>(this) {
                    @Override
                    public void onSuccess(CashWithdrawRecordBean result) {
                        super.onSuccess(result);
                        setWithdrawInfo(result);
                    }
                });
    }

    private void initView() {
        showHeader("提现记录");
    }

    private void setWithdrawInfo(CashWithdrawRecordBean withdrawInfo) {
        tvMoney.setText(ConvertUtil.centToCurrency(this, withdrawInfo.getWithdrawalAmount()));
        tvWithdrawState.setText(withdrawInfo.getAuditStatusStr());
        tvBankName.setText(withdrawInfo.getBank());
        tvBankCardNumber.setText(withdrawInfo.getBankCardNo());
        tvWithdrawNumber.setText(withdrawInfo.getWithdrawalNumber());
        tvBalance.setText(ConvertUtil.centToCurrency(this, withdrawInfo.getAfterWithdrawalBlance()));
        tvTime.setText(withdrawInfo.getApplyTime());
        StringBuffer stringBuffer = new StringBuffer();
        if (!TextUtils.isEmpty(withdrawInfo.getAuditRemark())) {
            stringBuffer.append(withdrawInfo.getAuditRemark() + "\n");
        }
        if (!TextUtils.isEmpty(withdrawInfo.getMakeRemark())) {
            stringBuffer.append(withdrawInfo.getMakeRemark());
        }
        tvRemark.setText(stringBuffer.toString());
    }
}
