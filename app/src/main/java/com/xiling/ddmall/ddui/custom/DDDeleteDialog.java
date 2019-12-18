package com.xiling.ddmall.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiling.ddmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDDeleteDialog extends Dialog {

    private View.OnClickListener listener = null;
    private View.OnClickListener mOnNegativeClickListener;
    private String title = "";
    private String content = "";

    private String left = "";
    private String right = "";


    public DDDeleteDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public DDDeleteDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDDeleteDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_delete);
        ButterKnife.bind(this);

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(content)) {
            tvContent.setText(content);
            tvContent.setVisibility(View.VISIBLE);
        } else {
            tvContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(left)) {
            btnConfirm.setText(left);
        }

        if (!TextUtils.isEmpty(right)) {
            btnCancel.setText(right);
        }
    }

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @BindView(R.id.btnCancel)
    Button btnCancel;


    @OnClick({R.id.iv_close, R.id.btnCancel})
    void onClosePressed(View v) {
        dismiss();
        if (mOnNegativeClickListener != null) {
            mOnNegativeClickListener.onClick(v);
        }
    }

    @OnClick(R.id.btnConfirm)
    void onConfirmPressed() {
        if (listener != null) {
            listener.onClick(btnConfirm);
        }
        dismiss();
    }

    public void setOnNegativeClickListener(View.OnClickListener listener) {
        this.mOnNegativeClickListener = listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setButtonName(String left, String right) {
        this.left = left;
        this.right = right;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public void setContent(String text) {
        this.content = text;
    }


}
