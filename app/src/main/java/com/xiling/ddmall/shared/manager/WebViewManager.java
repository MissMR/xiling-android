package com.xiling.ddmall.shared.manager;

import android.content.Context;
import android.net.Uri;

import java.lang.ref.WeakReference;

public class WebViewManager {
    private static WebViewManager instance;
    private static WeakReference<Context> weakContext;

    private WebViewManager(Context context) {
        Context applicationContext = context.getApplicationContext();
        weakContext = new WeakReference<>(applicationContext);
    }

    public static WebViewManager getInstance(Context context) {
        if (weakContext == null) {
            instance = null;
        }
        if (null != instance && weakContext.get() == null) {
            instance = null;
        }
        if (null == instance) {
            synchronized (WebViewManager.class) {
                if (null == instance) {
                    instance = new WebViewManager(context);
                }
            }
        }
        return instance;
    }

    public void convertURI(String uri) {
        Uri u = Uri.parse(uri);
    }
}
