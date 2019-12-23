package com.xiling.module.community;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * @author Stone
 * @time 2018/1/10  13:28
 * @desc ${TODD}
 */

public class BetterPtrClassicFrameLayout extends PtrClassicFrameLayout {
    public BetterPtrClassicFrameLayout(Context context) {
        super(context);
    }

    public BetterPtrClassicFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BetterPtrClassicFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                this.requestDisallowInterceptTouchEvent(false);
                disableWhenHorizontalMove(true);
                break;
            default:
        }
        if (disallowInterceptTouchEvent) {
            return dispatchTouchEventSupper(e);
        }
        return super.dispatchTouchEvent(e);
    }

    private boolean disallowInterceptTouchEvent = false;

    @Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        disallowInterceptTouchEvent = disallowIntercept;

        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }


}
