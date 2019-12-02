package com.dodomall.ddmall.ddui.custom;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

public class DDFooterView extends ClassicsFooter {

    public DDFooterView(Context context) {
        super(context);
        initView();
    }

    public DDFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DDFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        REFRESH_FOOTER_NOTHING = "我也是有底线的";
    }

}
