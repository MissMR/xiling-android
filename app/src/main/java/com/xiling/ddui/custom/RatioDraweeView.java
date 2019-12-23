package com.xiling.ddui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.xiling.R;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2019/1/4
 * 可以自定义比例的DraweeView
 * 已宽度为基准  高度根据 ratio 比例设置
 * mRatio 宽高比2  高度为宽度的一半
 */
public class RatioDraweeView extends SimpleDraweeView {
    // 宽高比 1080：1920 以宽度为基准
    private float mRatio = -1;

    public RatioDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public RatioDraweeView(Context context) {
        super(context);
    }

    public RatioDraweeView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RatioDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatioDraweeView);
        mRatio = ta.getFloat(R.styleable.RatioDraweeView_ratio, -1);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > -1) {
            int width = getDefaultSize(0, widthMeasureSpec);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (width / mRatio), MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
