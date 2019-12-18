package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;

public class WechatHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_help);
        showHeader("帮助");
    }
}
