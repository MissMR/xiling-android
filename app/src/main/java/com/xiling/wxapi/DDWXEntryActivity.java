package com.xiling.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.xiling.ddui.tools.DLog;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

public class DDWXEntryActivity extends WXCallbackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    //该方法执行umeng登陆的回调的处理
    @Override
    public void a(com.umeng.weixin.umengwx.b b) {

        DLog.d("a:" + b.a);
        DLog.d("b:" + b.b);
        DLog.d("c:" + b.c);
        DLog.d("d:" + b.d);
        if (b.c != null && b.d != null) {
            super.a(b);
            DLog.e("pass UMENG SDK!");
        } else {
            DLog.e("break UMENG SDK send code!");
        }
    }

    @Override
    protected void a(Intent intent) {
        super.a(intent);
    }

    //在onResume中处理从微信授权通过以后不会自动跳转的问题，手动结束该页面
    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
