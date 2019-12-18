package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-06-18
 */
public class MyScrollView extends ScrollView {

    private boolean isAtTop = false;
    private boolean isAtBottom = false;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {

        View view = getChildAt(getChildCount() - 1);
        int d = view.getBottom();
        d -= (getHeight() + getScrollY());
        if (d == 0) {
            isAtBottom = true;
            //you are at the end of the list in scrollview
            //do what you wanna do here
        } else {
            isAtBottom = false;
            super.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public boolean isAtBottom() {
        return isAtBottom;
    }

    public boolean isAtTop() {
        return getScrollY() == 0;
    }
}