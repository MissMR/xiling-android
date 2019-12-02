package com.dodomall.ddmall.ddui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2018/11/1
 */
public class DDTagView extends AppCompatTextView {
    private GradientDrawable mGradientDrawable;

    public DDTagView(Context context) {
        this(context, null);
        DLog.i("DDTagView 1");
    }

    public DDTagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
        DLog.i("DDTagView Constructor has 2 params.");
    }

    public DDTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
        DLog.i("DDTagView Constructor has 3 params.");
    }

    private void initView(AttributeSet attrs) {
        mGradientDrawable = new GradientDrawable();
        //设置圆角值
        mGradientDrawable.setCornerRadius(ConvertUtil.dip2px(8));
        int color = getResources().getColor(R.color.ddm_red);
        if (null != attrs) {
            final TypedArray a = getContext().obtainStyledAttributes(
                    attrs, R.styleable.DDTagView);
            color = a.getColor(R.styleable.DDTagView_tag_background, getResources().getColor(R.color.ddm_red));
        }
        mGradientDrawable.setColor(color);
    }

    public void setTagBackground(String colorString) {
        int color;
        try {
            color = Color.parseColor(colorString);
        } catch (Exception e) {
            e.printStackTrace();
            DLog.e("colorString 不合法：colorString = " + colorString);
            return;
        }

        setTagBackground(color);

    }

    public void setTagBackground(@ColorInt int color) {
//        mGradientDrawable.setColor(color);
//        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mGradientDrawable.setCornerRadius(getDefaultSize(0, heightMeasureSpec));
//        setBackground(mGradientDrawable);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

}
