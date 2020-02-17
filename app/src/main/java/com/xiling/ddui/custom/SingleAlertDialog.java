package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单个按钮的dialog
 */
public class SingleAlertDialog extends Dialog {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn_ok)
    TextView btnOk;

    String title,message,okTitle;
    View.OnClickListener okClickListener;

    public SingleAlertDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public SingleAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_single);
        ButterKnife.bind(this);
        if (!TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }else{
            tvTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(message)){
            tvMessage.setText(message);
        }
        if (!TextUtils.isEmpty(okTitle)){
            btnOk.setText(okTitle);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (okClickListener != null){
                    okClickListener.onClick(v);
                }
            }
        });

    }

    public SingleAlertDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public SingleAlertDialog setMessage(String message) {
        this.message = message;
        return this;
    }

    public SingleAlertDialog setOnClickListener(String btnName, final View.OnClickListener onClickListener) {
        this.okTitle = btnName;
        this.okClickListener = onClickListener;
        return this;
    }


}
