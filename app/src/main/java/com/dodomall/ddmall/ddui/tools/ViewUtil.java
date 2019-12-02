package com.dodomall.ddmall.ddui.tools;

import android.view.View;

/**
 * created by Jigsaw at 2018/11/25
 */
public class ViewUtil {
    private static final int DELAY_TIME = 1 * 1000;

    public static void setViewClickedDelay(final View view) {
        setViewClickedDelay(view, DELAY_TIME);
    }

    public static void setViewClickedDelay(final View view, int delayTime) {
        if (view == null || delayTime <= 0) {
            return;
        }

        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.setEnabled(true);
                }
            }
        }, delayTime);
    }
}
