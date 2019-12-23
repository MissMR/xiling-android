package com.xiling.ddui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.bigkoo.convenientbanner.ConvenientBanner;

/**
 * created by Jigsaw at 2018/11/1
 * 方形banner  以宽度为边长
 * 商品详情页banner
 */
public class DDSquareBanner extends ConvenientBanner {
    public DDSquareBanner(Context context) {
        super(context);
    }

    public DDSquareBanner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DDSquareBanner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DDSquareBanner(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        int dimen = getDefaultSize(0, widthMeasureSpec);
        setMeasuredDimension(dimen, dimen);
    }
}
