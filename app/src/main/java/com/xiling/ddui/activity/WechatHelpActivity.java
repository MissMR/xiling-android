package com.xiling.ddui.activity;

import android.os.Bundle;

import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;

public class WechatHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_help);
        showHeader("帮助");
    }
}
