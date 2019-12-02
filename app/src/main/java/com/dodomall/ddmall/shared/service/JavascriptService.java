package com.dodomall.ddmall.shared.service;

import android.support.annotation.NonNull;
import android.webkit.JavascriptInterface;

import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;

import org.greenrobot.eventbus.EventBus;

public class JavascriptService {

    @JavascriptInterface
    public void alert() {
        this.alert("");
    }

    @JavascriptInterface
    public void alert(@NonNull String message) {
        EventBus.getDefault().post(new EventMessage(Event.showAlert, message));
    }
}
