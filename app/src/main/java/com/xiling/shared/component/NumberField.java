package com.xiling.shared.component;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.tools.ViewUtil;
import com.xiling.shared.component.dialog.EditNumberDialog;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumberField extends LinearLayout {

    @BindView(R.id.minusBtn)
    protected ImageView mMinusBtn;
    @BindView(R.id.plusBtn)
    protected ImageView mPlusBtn;
    @BindView(R.id.valueTv)
    protected EditText mValueTv;

    private int mMin = 1;
    private int mMax = 999;

    public int getmValue() {
        return mValue;
    }

    private int mValue = 1;
    private OnValueChangeLister mListener;

    public NumberField(Context context) {
        super(context);
        initView();
    }

    public NumberField(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.cmp_number_field_layout, this);
        ButterKnife.bind(this, view);

        mValueTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String size = charSequence.toString();
                int mSize = 0;
                if (!TextUtils.isEmpty(size)) {
                    mSize = Integer.valueOf(size);
                }

                if (mSize != mValue) {
                    setValue(mSize);
                    mValueTv.setSelection(mValueTv.getText().length());//将光标移至文字末尾
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setButtonsEnabled();
    }

    public void setViewSize(int size) {
        int rSize = ConvertUtil.dip2px(size);

        ViewGroup.LayoutParams param1 = mMinusBtn.getLayoutParams();
        param1.width = rSize;
        param1.height = rSize;
        mMinusBtn.setLayoutParams(param1);

        ViewGroup.LayoutParams param2 = mPlusBtn.getLayoutParams();
        param2.width = rSize;
        param2.height = rSize;
        mPlusBtn.setLayoutParams(param2);
    }

    /**
     * 默认数据
     */
    public void setDefaultValue(int value) {
        this.mValue = value;
        mValueTv.setText("" + this.mValue);
        setButtonsEnabled();
    }


    /**
     * 设置数据有效区间
     */
    public void setLimit(int min, int max) {
        mMin = min;
        mMax = max;
        if (mValue > mMax) {
            mValue = mMax;
        } else if (mValue < mMin) {
            mValue = mMin;
        }
        mValueTv.setText("" + this.mValue);
        //   setValue(mValue);
        setButtonsEnabled();
    }

    public void setValue(int value) {
        if (value > mMax) {
            ToastUtil.error("商品数量已经达到最大值");
        }
        if (value < mMin) {
            ToastUtil.error("商品数量已经达到最小值");
        }

        value = value < mMin ? mMin : value;
        this.mValue = value <= mMax ? value : mMax;

        mValueTv.setText("" + this.mValue);
        if (this.mListener != null) {
            this.mListener.changed(this.mValue);
        }
        setButtonsEnabled();
    }

    /**
     * adapter 刷新时用
     * @param value
     * @param isListener
     */
    public void setValue(int value, boolean isListener) {
       // value = value < mMin ? mMin : value;
     //   this.mValue = value <= mMax ? value : mMax;

        mValueTv.setText("" + this.mValue);
        if (isListener && this.mListener != null) {
            this.mListener.changed(this.mValue);
        }
        setButtonsEnabled();
    }


    private void setButtonsEnabled() {
        this.mMinusBtn.setEnabled(this.mValue > mMin);
        this.mPlusBtn.setEnabled(this.mValue < mMax);
    }

    public void setOnChangeListener(OnValueChangeLister listener) {
        this.mListener = listener;
    }

    public void setEditable(boolean editable) {
        mValueTv.setEnabled(editable);
    }

    @OnClick(R.id.minusBtn)
    protected void onMinus() {
        mValue--;
        setValue(mValue);
    }

    @OnClick(R.id.plusBtn)
    protected void onPlus() {
        mValue++;
        setValue(mValue);
    }

}
