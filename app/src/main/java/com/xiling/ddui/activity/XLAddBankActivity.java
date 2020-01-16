package com.xiling.ddui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.BankListBean;
import com.xiling.ddui.custom.popupwindow.BankSelectDialog;
import com.xiling.ddui.service.IBankService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xiling.ddui.activity.XLCashierActivity.ADD_BAND_CODE;

public class XLAddBankActivity extends BaseActivity {
    IBankService iBankService;
    @BindView(R.id.tv_bank)
    TextView tvBank;
    @BindView(R.id.et_card_number)
    EditText etCardNumber;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_identity)
    EditText etIdentity;
    @BindView(R.id.etPhone)
    EditText etPhone;

    private BankListBean mBankBean;
    private String cardNumber = "";
    private String cardHolderName = "";
    private String identity = "";
    private String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xladd_bank);
        ButterKnife.bind(this);
        setTitle("添加银行卡");
        setLeftBlack();
        iBankService = ServiceManager.getInstance().createService(IBankService.class);
    }

    BankSelectDialog bankSelectDialog;

    @OnClick({R.id.btn_bank, R.id.btn_submit})
    public void onViewClicked(View view) {
        ViewUtil.setViewClickedDelay(view);
        switch (view.getId()) {
            case R.id.btn_bank:
                if (bankSelectDialog == null) {
                    bankSelectDialog = new BankSelectDialog(this);
                    bankSelectDialog.setOnBankSelectListener(new BankSelectDialog.OnBankSelectListener() {
                        @Override
                        public void onBankSelect(BankListBean bankListBean) {
                            mBankBean = bankListBean;
                            tvBank.setText(mBankBean.getBankName());
                        }
                    });
                }
                bankSelectDialog.show();

                break;
            case R.id.btn_submit:
                if (checkData()) {
                    addBank();
                }
                break;
        }
    }

    /**
     * 校验提交数据
     */
    private boolean checkData() {
        if (mBankBean == null) {
            return false;
        }
        cardNumber = etCardNumber.getText().toString();
        String cardError = PhoneNumberUtil.checkBankNumber(cardNumber);
        if (!TextUtils.isEmpty(cardError)) {
            ToastUtil.error(cardError);
            return false;
        }
        cardHolderName = etName.getText().toString();
        if (TextUtils.isEmpty(cardHolderName)) {
            ToastUtil.error("请输入持卡人姓名");
            return false;
        }

        identity = etIdentity.getText().toString();
        String errorDentity = PhoneNumberUtil.checkIDNumber(identity);
        if (!TextUtils.isEmpty(errorDentity)) {
            ToastUtil.error(errorDentity);
            return false;
        }

        phone = etPhone.getText().toString();
        if (!PhoneNumberUtil.checkPhoneNumber(phone)) {
            ToastUtil.error("请正确输入手机号");
        }
        return true;
    }


    private void addBank() {
        HashMap<String, String> params = new HashMap();
        params.put("bankId", mBankBean.getBankId());
        params.put("bankCardNumber", cardNumber);
        params.put("cardUser", cardHolderName);
        params.put("cardIdentity", identity);
        params.put("cardPhone", phone);

        APIManager.startRequest(iBankService.addBank(params), new BaseRequestListener<Object>(context) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                setResult(ADD_BAND_CODE);
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

}
