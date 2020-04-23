package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.UploadCredentalsActivity;
import com.xiling.ddui.bean.BankInfoBean;
import com.xiling.ddui.service.IBankService;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 大额支付
 */
public class LargePayDialog extends Dialog {

    Context mContext;

    String key = "";
    String type = "";
    String weekSize = "";
    IBankService iBankService;
    @BindView(R.id.tv_bank_info)
    TextView tvBankInfo;

    public LargePayDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }

    public LargePayDialog(@NonNull Context context, String type, String key, String weekSize) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.type = type;
        this.key = key;
        this.weekSize = weekSize;
    }


    public LargePayDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public LargePayDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay_large);
        ButterKnife.bind(this);
        iBankService = ServiceManager.getInstance().createService(IBankService.class);
        initView();
        APIManager.startRequest(iBankService.getBankInfo(), new BaseRequestListener<BankInfoBean>() {

            @Override
            public void onSuccess(BankInfoBean result) {
                super.onSuccess(result);
                tvBankInfo.setText("喜领-银行账户：" + result.getAccount());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

        });


    }


    private void initView() {
        initWindow();

    }


    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.iv_close, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.btn_submit:
                Intent intent = new Intent(mContext, UploadCredentalsActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("type", type);
                intent.putExtra("weekSize", weekSize);
                mContext.startActivity(intent);
                dismiss();
                break;
        }

        dismiss();
    }


}
