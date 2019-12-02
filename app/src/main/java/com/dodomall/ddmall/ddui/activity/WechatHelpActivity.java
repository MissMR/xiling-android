package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;

public class WechatHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_help);
        showHeader("帮助");
    }
}
