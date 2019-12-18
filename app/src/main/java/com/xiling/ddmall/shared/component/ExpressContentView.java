package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/1.
 */
public class ExpressContentView extends LinearLayout {

    public ExpressContentView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setData(String[] strs) {
        for (String str : strs) {
            TextView textView = new TextView(getContext());
            textView.setText(str);
            addView(textView);
        }
    }
}
