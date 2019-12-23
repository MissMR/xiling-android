package com.xiling.ddui.tools;

import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;

/**
 * created by Jigsaw at 2019/3/15
 */
public class ProductSuggestSnapHelper extends PagerSnapHelper {
    private static final int PAGE_COUNT = 3;
    private OnPageChangeListener mOnPageChangeListener;

    @Override
    public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        DLog.i("position:" + super.findTargetSnapPosition(layoutManager, velocityX, velocityY) + " x:" + velocityX);

        int position = super.findTargetSnapPosition(layoutManager, velocityX, velocityY);
        // 0  3  6
        // 1  4  7
        int mid = 1 + PAGE_COUNT * (position / PAGE_COUNT);
        DLog.i("mid:" + mid);
        if (velocityX > 0) {
            // 右滑
            position = mid + PAGE_COUNT;
        } else {
            // 左滑
            position = mid;
        }
        DLog.i("position after : " + position);

        if (getOnPageChangeListener() != null) {
            getOnPageChangeListener().onPageChange(position / 3);
        }

        return position;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return mOnPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public interface OnPageChangeListener {
        void onPageChange(int index);
    }
}
