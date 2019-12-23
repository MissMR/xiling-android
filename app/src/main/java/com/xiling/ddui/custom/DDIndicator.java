package com.xiling.ddui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.xiling.R;
import com.xiling.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2019/3/18
 */
public class DDIndicator extends RadioGroup {

    private int mIndicatorDrawableResId = R.drawable.indicator_bg_selector_default;
    private int mActiveIndex = 0;

    public DDIndicator(Context context) {
        super(context);
        init(null);
    }

    public DDIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        if (attrs != null) {
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.DDIndicator);
            mIndicatorDrawableResId = attributes.getResourceId(R.styleable.DDIndicator_indicator, R.drawable.indicator_bg_selector_default);
            mActiveIndex = attributes.getInt(R.styleable.DDIndicator_indicator_index_active, 0);
            attributes.recycle();
        }

        setIndicatorCount(3);
    }

    public void setIndicatorCount(int count) {
        removeAllViews();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = getRadioButton();
            radioButton.setId(i);
            if (i == mActiveIndex) {
                radioButton.setChecked(true);
            }
            addView(radioButton);
        }

    }

    private RadioButton getRadioButton() {
        RadioButton radioButton = new RadioButton(getContext());
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = ConvertUtil.dip2px(1.5f);
        layoutParams.rightMargin = ConvertUtil.dip2px(1.5f);
        layoutParams.width = ConvertUtil.dip2px(11);
        layoutParams.height = ConvertUtil.dip2px(2);
        radioButton.setLayoutParams(layoutParams);
        radioButton.setButtonDrawable(null);

        radioButton.setBackgroundResource(mIndicatorDrawableResId);
        radioButton.setEnabled(false);
        return radioButton;
    }

    public void setIndexActive(int index) {
        View view = getChildAt(index);
        if (view == null) {
            return;
        }

        if (view instanceof RadioButton) {
            ((RadioButton) view).setChecked(true);
        }

    }

}
