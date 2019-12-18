package com.xiling.ddmall.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDUnLikeDialog extends Dialog {

    Context context = null;

    public DDUnLikeDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
        this.context = context;
    }

    public DDUnLikeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDUnLikeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_unlike);
        ButterKnife.bind(this);
        initWindow();
        setCanceledOnTouchOutside(true);
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = ConvertUtil.dip2px(10); //设置居于底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @OnClick({R.id.emptyView, R.id.cancelTextView})
    void onEmptyViewPressed() {
        DLog.i("onEmptyViewPressed");
        dismiss();
    }

    View.OnClickListener listener = null;

    public void setCancelLikeEvent(View.OnClickListener listener) {
        this.listener = listener;
    }

    @OnClick(R.id.cancelLikeBtn)
    void onCancelLikePressed(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
        dismiss();
    }
}
