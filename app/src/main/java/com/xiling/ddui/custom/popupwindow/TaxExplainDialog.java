package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 税费说明
 */
public class TaxExplainDialog extends Dialog {
    Context mContext;
    String tax;
    boolean isDutyFree;//是否包税
    @BindView(R.id.tv_taxation_explain)
    TextView tvTaxationExplain;
    IProductService iProductService;
    public TaxExplainDialog(@NonNull Context context,boolean isDutyFree,String tax) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.tax = tax;
        this.isDutyFree = isDutyFree;
    }

    public TaxExplainDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    public TaxExplainDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tax_explain);
        ButterKnife.bind(this);
        iProductService = ServiceManager.getInstance().createService(IProductService.class);
        initView();
        if (isDutyFree) {
            tvTaxationExplain.setText("该商品价格已包税");
        }else{
            tvTaxationExplain.setText("该商品需缴纳"+tax+"的跨境电商综合税。");
        }
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

    @OnClick({R.id.iv_close, R.id.btn_know})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
            case R.id.btn_know:
                dismiss();
                break;
        }
    }


}
