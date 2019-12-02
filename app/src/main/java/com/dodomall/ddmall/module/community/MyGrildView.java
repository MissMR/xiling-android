package com.dodomall.ddmall.module.community;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author Stone
 * @time 2017/12/26  14:32
 * @desc ${TODD}
 */

public class MyGrildView extends GridView {
    public MyGrildView(Context context) {
        super(context);
    }

    public MyGrildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGrildView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(536870911, -2147483648);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}