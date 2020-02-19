package com.xiling.ddui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class AutofitViewPager extends ViewPager {
    public AutofitViewPager(@NonNull Context context) {
        super(context);
    }

    public AutofitViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        View view = getChildAt(getCurrentItem());//注意:这里是有bug的,看文末的解决方式
        if (view != null) {
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }

        setMeasuredDimension(getMeasuredWidth(), measureHeight(heightMeasureSpec, view));
    }

    private int measureHeight(int measureSpec, View view) {
        int result = 0;

        if (view != null) {
            result = view.getMeasuredHeight();
        }


        return result;
    }
}
