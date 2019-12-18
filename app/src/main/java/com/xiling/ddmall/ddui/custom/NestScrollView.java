package com.xiling.ddmall.ddui.custom;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

public class NestScrollView extends NestedScrollView {

    private OnScrollChangedCallback mOnScrollChangedCallback;

    public NestScrollView(Context context) {
        super(context);
    }

    public NestScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    float touchX = 0;
    float touchY = 0;
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                touchX = event.getX();
//                touchY = event.getY();
//                DLog.i("down:" + touchX + "," + touchY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float dx = touchX - event.getX();
//                float dy = touchY - event.getY();
//                DLog.i("move:" + dx + "," + dy);
//                if (dx > dy) {
//                    return false;
//                }
//            case MotionEvent.ACTION_UP:
//                touchX = 0;
//                touchY = 0;
//                DLog.i("up:" + touchX + "," + touchY);
//                break;
//        }
//        DLog.d("onInterceptTouchEvent:" + event.getAction() + "," + event.getX() + "," + event.getY());
//        return super.onTouchEvent(event);
//    }

//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        DLog.d("dispatchTouchEvent:" + event.getAction() + "," + event.getX() + "," + event.getY());
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                touchX = event.getX();
//                touchY = event.getY();
//                DLog.i("down:" + touchX + "," + touchY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float dx = touchX - event.getX();
//                float dy = touchY - event.getY();
//                DLog.i("move:" + dx + "," + dy);
//                if (dx > dy) {
//                    return false;
//                }
//            case MotionEvent.ACTION_UP:
//                touchX = 0;
//                touchY = 0;
//                DLog.i("up:" + touchX + "," + touchY);
//                break;
//        }
//        return super.dispatchTouchEvent(event);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        DLog.d("onTouchEvent:" + event.getAction() + "," + event.getX() + "," + event.getY());
//        return super.onTouchEvent(event);
//    }


    int lastX = 0;
    int lastY = 0;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        DLog.d("onScrollChanged:" + l + "," + t);
        lastX = l;
        lastY = t;
        if (mOnScrollChangedCallback != null) {
            mOnScrollChangedCallback.onScroll(l, t);
        }
    }

    public void setOnScrollChangedCallback(OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    public interface OnScrollChangedCallback {
        void onScroll(int dx, int dy);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
//        DLog.i("DDScrollView onLayout changed:" + changed + ",l:" + l + ",t:" + t + ",r:" + r + ",b:" + b);
        if (changed) {
            scrollTo(0, 0);
        }
    }
}
