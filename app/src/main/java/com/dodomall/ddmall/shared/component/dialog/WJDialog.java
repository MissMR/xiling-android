package com.dodomall.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-07
 */
public class WJDialog extends Dialog {

    @BindView(R.id.titleTv)
    protected TextView mTitleTv;
    @BindView(R.id.contentTv)
    protected TextView mContentTv;
    @BindView(R.id.cancelBtn)
    protected TextView mCancelBtn;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;

    protected View.OnClickListener mCancelListener;
    protected View.OnClickListener mConfirmListener;

    public WJDialog(Context context) {
        this(context, 0);
    }

    private WJDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        mTitleTv.setText(charSequence);
        mTitleTv.setVisibility(View.VISIBLE);
    }

    @Override
    public void setTitle(@StringRes int titleId) {
        String string = getContext().getResources().getString(titleId);
        setTitle(string);
    }

    public void hideCancelBtn() {
        mCancelBtn.setVisibility(View.GONE);
    }

    public void setCancelText(CharSequence charSequence) {
        mCancelBtn.setText(charSequence);
        setCancelable(true);
    }

    public void setConfirmText(CharSequence charSequence) {
        mConfirmBtn.setText(charSequence);
    }

    public void setContentText(CharSequence charSequence) {
        mContentTv.setText(charSequence);
    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
        mCancelBtn.setVisibility(cancelable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setOnCancelListener(@Nullable OnCancelListener listener) {
    }

    public void setOnCancelListener(@Nullable View.OnClickListener listener) {
        mCancelListener = listener;
        setCancelable(true);
    }

    public void setOnConfirmListener(@Nullable View.OnClickListener listener) {
        mConfirmListener = listener;
    }

    @OnClick(R.id.cancelBtn)
    protected void onClose(View view) {
        if (mCancelListener == null) {
            dismiss();
        } else {
            mCancelListener.onClick(view);
        }
    }

    @OnClick(R.id.confirmBtn)
    protected void onConfirm(View view) {
        if (mConfirmListener == null) {
            dismiss();
        } else {
            mConfirmListener.onClick(view);
        }
    }
}
