package com.xiling.ddmall.dduis.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDGeneralDialog extends Dialog {

    public enum ButtonStyle {
        Gray,
        Red
    }

    public interface DDGeneralDialogClickListener {
        void onPressed(DDGeneralDialog dialog, View view);
    }

    public DDGeneralDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
    }

    public DDGeneralDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDGeneralDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = ConvertUtil.dip2px(10); //设置居于底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @BindView(R.id.btn_single)
    TextView btnSingle;

    @BindView(R.id.btn_left)
    TextView btnLeft;

    @BindView(R.id.btn_right)
    TextView btnRight;

    @BindView(R.id.tv_content)
    TextView contentTextView;

    @OnClick(R.id.btn_single)
    void onSinglePressed() {
        if (singlePressedEvent != null) {
            singlePressedEvent.onPressed(this, btnSingle);
        } else {
            DLog.w("singlePressedEvent is null.");
        }
    }

    @OnClick(R.id.btn_left)
    void onLeftPressed() {
        if (leftPressedEvent != null) {
            leftPressedEvent.onPressed(this, btnLeft);
        } else {
            DLog.w("leftPressedEvent is null.");
        }
    }

    @OnClick(R.id.btn_right)
    void onRightPressed() {
        if (rightPressedEvent != null) {
            rightPressedEvent.onPressed(this, btnRight);
        } else {
            DLog.w("rightPressedEvent is null.");
        }
    }

    @OnClick(R.id.panel_main)
    void onMainPressed() {
        dismiss();
    }

    @OnClick(R.id.panel_dialog)
    void onDialogPressed() {
        DLog.w("onDialogPressed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_general);
        initWindow();
        ButterKnife.bind(this);

        contentTextView.setText(content);

        btnSingle.setVisibility(isSingle ? View.VISIBLE : View.GONE);
        btnLeft.setVisibility(isSingle ? View.GONE : View.VISIBLE);
        btnRight.setVisibility(isSingle ? View.GONE : View.VISIBLE);

        if (!isSingle) {
            btnSingle.setVisibility(View.GONE);
            btnLeft.setVisibility(View.VISIBLE);
            btnRight.setVisibility(View.VISIBLE);

            btnLeft.setText(leftText);
            btnLeft.setTextColor(Color.parseColor(leftButtonStyle == ButtonStyle.Red ? "#ff4646" : "#999999"));

            btnRight.setText(rightText);
            btnRight.setTextColor(Color.parseColor(rightButtonStyle == ButtonStyle.Red ? "#ff4646" : "#999999"));
        } else {
            btnSingle.setVisibility(View.VISIBLE);
            btnLeft.setVisibility(View.GONE);
            btnRight.setVisibility(View.GONE);

            btnSingle.setText(singleText);
            btnSingle.setTextColor(Color.parseColor(singleButtonStyle == ButtonStyle.Red ?"#ff4646" : "#999999"));
        }


    }

    ////////////////////////////////链式调用部分/////////////////////////////////////
    String content = "";

    boolean isSingle;

    ButtonStyle leftButtonStyle;
    String leftText;

    ButtonStyle rightButtonStyle;
    String rightText;

    ButtonStyle singleButtonStyle;
    String singleText;

    DDGeneralDialogClickListener leftPressedEvent = null;
    DDGeneralDialogClickListener rightPressedEvent = null;
    DDGeneralDialogClickListener singlePressedEvent = null;

    public DDGeneralDialog setMessage(String text) {
        content = text;
        return this;
    }

    public DDGeneralDialog setLeftButton(ButtonStyle style, String text, DDGeneralDialogClickListener listener) {
        leftButtonStyle = style;
        leftText = text;
        leftPressedEvent = listener;
        return this;
    }

    public DDGeneralDialog setRightButton(ButtonStyle style, String text, DDGeneralDialogClickListener listener) {
        rightButtonStyle = style;
        rightText = text;
        rightPressedEvent = listener;
        return this;
    }

    public DDGeneralDialog setSingleButton(ButtonStyle style, String text, DDGeneralDialogClickListener listener) {
        singleButtonStyle = style;
        singleText = text;
        singlePressedEvent = listener;
        return this;
    }

    public DDGeneralDialog setButtonMode(boolean isSingle) {
        this.isSingle = isSingle;
        return this;
    }

    public void setLeftPressedEvent(DDGeneralDialogClickListener leftPressedEvent) {
        this.leftPressedEvent = leftPressedEvent;
    }

    public void setRightPressedEvent(DDGeneralDialogClickListener rightPressedEvent) {
        this.rightPressedEvent = rightPressedEvent;
    }

    public void setSinglePressedEvent(DDGeneralDialogClickListener singlePressedEvent) {
        this.singlePressedEvent = singlePressedEvent;
    }
}
