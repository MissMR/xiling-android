package com.dodomall.ddmall.shared.basic;

import android.support.annotation.NonNull;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.basic
 * @since 2017-06-18
 */
public class BaseGestureImpl implements GestureDetector.OnGestureListener {

    private final OnFlingListener mListener;

    public BaseGestureImpl(@NonNull OnFlingListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        int minWidth = 120;
        int minSpeed = 0;
        float distanceDown = motionEvent1.getY() - motionEvent.getY();
        float distanceUp = motionEvent.getY() - motionEvent1.getY();
        if (distanceDown > minWidth && Math.abs(v) > minSpeed) {
            Logger.e("onFling-" + "向下滑动");
            mListener.onMoveDown();
        } else if (distanceUp > minWidth && Math.abs(v) > minSpeed) {
            Logger.e("onFling-" + "向上滑动");
            mListener.onMoveUp();
        }
        return true;
    }

    public interface OnFlingListener {
        void onMoveUp();
        void onMoveDown();
    }
}
