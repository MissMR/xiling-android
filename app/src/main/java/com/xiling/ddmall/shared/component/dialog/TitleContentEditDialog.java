package com.xiling.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitleContentEditDialog extends Dialog {


    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.tvContent)
    TextView mTvContent;
    @BindView(R.id.cancelBtn)
    TextView mCancelBtn;
    @BindView(R.id.confirmBtn)
    TextView mConfirmBtn;
    @BindView(R.id.etMessage)
    EditText mEtMessage;
    private String mTitle;
    private String mMessage;
    private String mEtHide;
    private OnConfirmListener mListener;

    public TitleContentEditDialog(Context context, String title, String message, String etHide, OnConfirmListener listener) {
        this(context, 0);
        mTitle = title;
        mMessage = message;
        mEtHide = etHide;
        mListener = listener;
    }

    private TitleContentEditDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_title_content_edit_btn);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        initViews();
    }

    private void initViews() {
        mTvTitle.setText(mTitle);
        mTvContent.setText(mMessage);
        mEtMessage.setHint(mEtHide);
    }

    @OnClick(R.id.confirmBtn)
    protected void confirmToChange() {
        if (mListener!=null) {
            mListener.onConfirm(mEtMessage.getText().toString());
        }
        dismiss();
    }

    @OnClick(R.id.cancelBtn)
    protected void onClose() {
        dismiss();
    }

    public interface OnConfirmListener{
        void onConfirm(String etStr);
    }

}
