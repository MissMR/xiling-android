package com.xiling.ddui.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 禁止scrollView 子控件获取焦点时自动滚动
 */
public class ProhibitAutoScrollView extends ScrollView {
    public ProhibitAutoScrollView(Context context) {
        super(context);
    }

    public ProhibitAutoScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProhibitAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ProhibitAutoScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }
}
