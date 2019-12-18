package com.xiling.ddmall.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.ddmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDResultDialog extends Dialog {

    public DDResultDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public DDResultDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDResultDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @BindView(R.id.ivTopIcon)
    ImageView ivTopIcon;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.btnConfirm)
    Button btnConfirm;

    @OnClick(R.id.iv_close)
    void onClosePressed() {
        dismiss();
    }

    @OnClick(R.id.btnConfirm)
    void onConfirmPressed() {
        if (listener != null) {
            listener.onClick(btnConfirm);
        }
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_result);
        ButterKnife.bind(this);
        reload();
    }

    public void reload() {
        ivTopIcon.setImageResource(status ? R.mipmap.icon_dialog_top_success : R.mipmap.icon_dialog_top_failure);
        tvTitle.setText(title);
        tvContent.setText(content);
        btnConfirm.setText(confirmText);
        ivTopIcon.setVisibility(isRemoveTop ? View.GONE : View.VISIBLE);
    }

    private boolean status = true;
    private String title = "";
    private String content = "";
    private String confirmText = "";
    private View.OnClickListener listener = null;
    private boolean isRemoveTop = false;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void removeTopImage() {
        isRemoveTop = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConfirmText() {
        return confirmText;
    }

    public void setConfirmText(String confirmText) {
        this.confirmText = confirmText;
    }

    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
