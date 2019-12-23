package com.xiling.shared.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 留言的弹窗
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/4/25 下午10:12.
 */
public class CommentBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    private Context mContext;
    private OnSubmitListener mListener;

    public CommentBottomSheetDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        View contentView = View.inflate(mContext, R.layout.dialog_comment, null);
        ButterKnife.bind(this, contentView);
        setContentView(contentView);
        super.onCreate(savedInstanceState);
    }


    @OnClick(R.id.tvSubmit)
    public void onViewClicked() {
        if (mListener != null) {
            mListener.submit(mEtContent.getText().toString());
        }
    }

    private void setButtonText(String text){
        mTvSubmit.setText(text);
    }

    private void setContent(String content) {
        mEtContent.setText(content);
        mEtContent.setSelection(content.length());
        showSoftKeyboard(mEtContent,mContext);
    }

    public CommentBottomSheetDialog setSubmitListener(OnSubmitListener listener) {
        mListener = listener;
        return this;
    }

    public interface OnSubmitListener {
        void submit(String content);
    }

    public void showSoftKeyboard(View view,Context mContext) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void show(String btnStr,String etStr){
        show();
        setButtonText(btnStr);
        setContent(etStr);
    }
}
