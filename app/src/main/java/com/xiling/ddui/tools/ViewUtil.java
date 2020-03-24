package com.xiling.ddui.tools;

import android.view.View;
import android.widget.TextView;

import com.xiling.R;

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

        view.setClickable(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (view != null) {
                    view.setClickable(true);
                }
            }
        }, delayTime);
    }


    public static void setCartBadge(int messageSize, TextView tvCartBadge) {
    /*    if (messageSize < 10) {
            tvCartBadge.setBackgroundResource(R.drawable.bg_oval_read);
        } else {
            tvCartBadge.setBackgroundResource(R.drawable.bg_oval_read_more);
        }
        tvCartBadge.setText(messageSize > 99 ? "99" : String.valueOf(messageSize));*/
        tvCartBadge.setVisibility(messageSize > 0 ? View.VISIBLE : View.GONE);
    }

}
