package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;


public class DecimalEditText extends android.support.v7.widget.AppCompatEditText {

    private static final int DEFAULT_DECIMAL_NUMBER = 2;
    /**
     * 保留小数点后多少位
     */
    private int mDecimalNumber = DEFAULT_DECIMAL_NUMBER;


    public DecimalEditText(Context context) {
        super(context);
        init();
    }

    public DecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setLongClickable(false);
        setFilters(new InputFilter[]{new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String lastInputContent = dest.toString();

                if (".".equals(source) && lastInputContent.length() == 0) {
                    return "0.";
                }

                if (lastInputContent.contains(".")) {
                    int index = lastInputContent.indexOf(".");
                    if (dend - index >= mDecimalNumber + 1) {
                        return "";
                    }
                }

                return null;
            }
        }});
    }

    public int getDecimalNumber() {
        return mDecimalNumber;
    }

    public void setDecimalNumber(int decimalNumber) {
        mDecimalNumber = decimalNumber;
    }

    public void setMaxValue(long maxValue) {
        setFilters(new InputFilter[]{new NumRangeInputFilter(maxValue)});
    }
}