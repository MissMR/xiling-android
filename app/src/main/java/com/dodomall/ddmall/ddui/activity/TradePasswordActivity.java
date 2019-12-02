package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.UIEvent;
import com.dodomall.ddmall.ddui.custom.D3ialogTools;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.TextTools;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/9/11
 * 交易密码
 */
public class TradePasswordActivity extends BaseActivity {

    @BindView(R.id.cb_password_switch)
    CheckBox cbPasswordSwitch;

    @BindView(R.id.et_trade_password)
    EditText etTradePassword;

    @BindView(R.id.tv_btn_confirm)
    TextView tvBtnConfirm;

    @BindView(R.id.hint01StatusImageView)
    ImageView ivCharStatus;

    @BindView(R.id.hint02StatusImageView)
    ImageView ivLengthStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_password);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvBtnConfirm.setEnabled(false);
        showHeader("设置交易密码", true);
        cbPasswordSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPasswordEditTextState(isChecked);
            }
        });

        //过滤空格和回车
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n")) {
                    return "";
                } else {
                    return null;
                }
            }
        };
        etTradePassword.setFilters(new InputFilter[]{filter});

        etTradePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    checkStatus("" + charSequence.toString(), false);
                } else {
                    ivCharStatus.setVisibility(View.INVISIBLE);
                    ivLengthStatus.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etTradePassword.requestFocus();
        //出键盘
        showSoftInputFromWindow(etTradePassword);

    }

    boolean checkStatus(String password, boolean isToast) {

        boolean status = false;
        String tag = "交易密码";

        int len = password.length();
        if (!(len > 7 && len < 17)) {
            status = false;
            if (isToast) {
                ToastUtil.error(tag + "必须为8-16位");
                return status;
            }
            ivLengthStatus.setImageResource(R.mipmap.icon_password_error);
            ivLengthStatus.setVisibility(View.VISIBLE);
        } else {
            status = true;
            ivLengthStatus.setImageResource(R.mipmap.icon_password_success);
            ivLengthStatus.setVisibility(View.VISIBLE);
        }

        int flag = 0;
        if (TextTools.isHasABC(password)) {
            DLog.i(tag + "已包含字母");
            flag++;
        }
        if (TextTools.isHasDigit(password)) {
            DLog.i(tag + "已包含数字");
            flag++;
        }
        if (TextTools.isHasSpecialChar(password)) {
            DLog.i(tag + "已包含特殊字符");
            flag++;
        }

        if (flag < 2) {
            status = false;
            if (isToast) {
                ToastUtil.error(tag + "必须包含数字、字母、符号中至少2种元素");
                return status;
            }
            ivCharStatus.setImageResource(R.mipmap.icon_password_error);
            ivCharStatus.setVisibility(View.VISIBLE);
            tvBtnConfirm.setEnabled(false);
        } else {
            status = true;
            ivCharStatus.setImageResource(R.mipmap.icon_password_success);
            ivCharStatus.setVisibility(View.VISIBLE);
            tvBtnConfirm.setEnabled(etTradePassword.getText().length() >= 8);
        }

        return status;
    }

    private void setPasswordEditTextState(boolean isShowPassword) {
        if (isShowPassword) {
            // 可见
            etTradePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD); //字符
        } else {
            // 不可见
            etTradePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD); //字符
        }
        etTradePassword.setSelection(etTradePassword.getText().length());
    }

    @OnClick(R.id.tv_btn_confirm)
    public void onViewClicked() {

        String password = etTradePassword.getText().toString();

        if (!(checkStatus(password, true))) {
            return;
        }

        String enc_password = StringUtil.md5(password);
        IUserService iUserService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(iUserService.resetTradePassword(enc_password), new RequestListener<Object>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(Object result, String msg) {
                super.onSuccess(result);
                //        ToastUtil.success("" + msg);
                onResult(msg);
                etTradePassword.setText("");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 设置成功后的逻辑处理
     */
    public void onResult(String msg) {
        D3ialogTools.showSuccess(context, "设置成功", "" + msg, "完成", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                UIEvent event = new UIEvent();
                event.setType(UIEvent.Type.ClosePasswordActivity);
                EventBus.getDefault().post(event);
            }
        });
    }
}
