package com.xiling.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.utils.StringUtils;
import com.xiling.R;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditNumberDialog extends Dialog {

    @BindView(R.id.minusBtn)
    protected ImageView mMinusBtn;
    @BindView(R.id.plusBtn)
    protected ImageView mPlusBtn;
    @BindView(R.id.valueEt)
    protected EditText mValueEt;

    private int mMin = 1;
    private int mMax = 9999;
    private int mValue = 1;
    private OnValueChangeLister mListener;

    public EditNumberDialog(Context context, int value, int min, int max) {
        this(context, 0);
        this.mValue = value;
        this.mMax = Math.min(max, 9999);
        this.mMin = Math.max(min, 1);
    }

    private EditNumberDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_number);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        initViews();
        setButtonsEnabled();
        mValueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int value =0;
                if (!StringUtils.isEmpty(mValueEt.getText().toString())) {
                    value = Integer.valueOf(mValueEt.getText().toString());
                }

                mValue = value;
                setButtonsEnabled();
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (!Integer.valueOf(editable.toString()).equals(mValue)) {
//                    mValueEt.setText(String.valueOf(mValue));
//                }
            }
        });
    }

    private void initViews() {
        mValueEt.setText(String.valueOf(this.mValue));
    }

    public void setValue(int value) {
        this.mValue = value;
        mValueEt.setText(String.valueOf(this.mValue));
        setButtonsEnabled();
    }

    private void setButtonsEnabled() {
        this.mMinusBtn.setEnabled(mMin < this.mValue);
        this.mPlusBtn.setEnabled(mMax > this.mValue);
    }

    public void setOnChangeListener(OnValueChangeLister listener) {
        this.mListener = listener;
    }

    public void setValues(int value, int min, int max) {
        this.mMin = min;
        this.mMax = max;
        this.setValue(value);
    }

    @OnClick(R.id.minusBtn)
    protected void onMinus() {
        this.mValue--;
        setValue(this.mValue);
    }

    @OnClick(R.id.plusBtn)
    protected void onPlus() {
        this.mValue++;
        setValue(this.mValue);
    }

    @OnClick(R.id.confirmBtn)
    protected void confirmToChange() {
        if (!StringUtils.isEmpty(mValueEt.getText().toString())) {
            setValue(Integer.valueOf(mValueEt.getText().toString()));
            if (this.mListener != null) {
                this.mListener.changed(this.mValue);
            }
            dismiss();
        }
    }

    @OnClick(R.id.cancelBtn)
    protected void onClose() {
        dismiss();
    }

}
