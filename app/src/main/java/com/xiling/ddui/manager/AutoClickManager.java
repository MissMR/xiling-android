package com.xiling.ddui.manager;

import android.content.Context;

import com.xiling.ddui.bean.AutoClickBean;
import com.xiling.module.page.WebViewActivity;

public class AutoClickManager {

    /**
     * 解析
     */
    public static void pars(Context context, AutoClickBean autoClickBean) {
        switch (autoClickBean.getEvent()) {
            case "link":
                WebViewActivity.jumpUrl(context, autoClickBean.getTarget());
                break;
        }

    }
}
