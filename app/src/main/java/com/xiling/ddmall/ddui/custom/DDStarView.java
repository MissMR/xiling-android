package com.xiling.ddmall.ddui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2019/3/19
 */
public class DDStarView extends LinearLayout {
    private int mValue = 5;
    private int mStarDrawableRes = R.drawable.icon_fav_selector;
    private int mTextSize = 15;
    private int mTextColor;
    private int mStarWidth = 12;
    private TextView mTextView;
    private LinearLayout mLlStarContainer;

    private int mOrientation = VERTICAL;

    public DDStarView(Context context) {
        super(context);
        init(null);
    }

    public DDStarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public DDStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attributeSet) {
        mTextColor = ContextCompat.getColor(getContext(), R.color.ddm_red);
        if (attributeSet != null) {
            TypedArray ta = getContext().obtainStyledAttributes(attributeSet, R.styleable.DDStarView);
            mValue = ta.getInt(R.styleable.DDStarView_value, mValue);
            mStarDrawableRes = ta.getResourceId(R.styleable.DDStarView_starDrawable, R.drawable.icon_fav_selector);
            mTextColor = ta.getColor(R.styleable.DDStatusButton_textColor, mTextColor);
            mTextSize = ta.getDimensionPixelSize(R.styleable.DDStarView_textSize, mTextSize);
            mOrientation = ta.getInt(R.styleable.DDStarView_orientation, VERTICAL);
        }

        setOrientation(mOrientation);
        setGravity(Gravity.CENTER);

        mTextView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = getDefaultLayoutParams();
        mTextView.setLayoutParams(layoutParams);
        mTextView.setTextColor(mTextColor);
        mTextView.setTextSize(mTextSize);
        mTextView.setText("5.0");

        mLlStarContainer = new LinearLayout(getContext());
        mLlStarContainer.setOrientation(HORIZONTAL);
        mLlStarContainer.setLayoutParams(getDefaultLayoutParams());
        for (int i = 0; i < 5; i++) {
            View view = getItemView();
            view.setSelected(true);
            mLlStarContainer.addView(view);
        }

        if (mOrientation == HORIZONTAL) {
            addView(mLlStarContainer);
            LinearLayout.LayoutParams lp = (LayoutParams) mTextView.getLayoutParams();
            lp.leftMargin = ConvertUtil.dip2px(4);
            mTextView.setLayoutParams(lp);
            addView(mTextView);
        } else {
            addView(mTextView);
            addView(mLlStarContainer);
        }
    }

    @NonNull
    private ImageView getItemView() {
        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource(mStarDrawableRes);
        LayoutParams lp = getDefaultLayoutParams();
        lp.width = ConvertUtil.dip2px(mStarWidth);
        lp.height = ConvertUtil.dip2px(mStarWidth);
        imageView.setLayoutParams(lp);
        int padding = ConvertUtil.dip2px(1.5f);
        imageView.setPadding(padding, padding, padding, padding);
        return imageView;
    }

    private LinearLayout.LayoutParams getDefaultLayoutParams() {
        return new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public void setValue(int value) {
        this.mValue = value;
        this.mTextView.setText(String.format("%s.0", value));
        for (int i = 0; i < mLlStarContainer.getChildCount(); i++) {
            mLlStarContainer.getChildAt(i).setSelected(value > i);
        }
    }


}
