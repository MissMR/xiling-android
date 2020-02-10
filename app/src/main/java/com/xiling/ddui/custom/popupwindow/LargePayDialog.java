package com.xiling.ddui.custom.popupwindow;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xiling.R;
import com.xiling.ddui.activity.UploadCredentalsActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author 逄涛
 * 大额支付
 */
public class LargePayDialog extends Dialog {

    Context mContext;

    String key = "";
    String type ="";

    public LargePayDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
        mContext = context;
    }


    public LargePayDialog(@NonNull Context context,String type, String key) {
        this(context, R.style.DDMDialog);
        mContext = context;
        this.type = type;
        this.key = key;
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
        initView();
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
                mContext.startActivity(intent);
                dismiss();
                break;
        }

        dismiss();
    }


}
