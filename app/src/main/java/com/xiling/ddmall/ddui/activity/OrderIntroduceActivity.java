package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;

/**
 * @author Jigsaw
 * @date 2018/9/19
 * 推广订单
 * 空页面
 */
public class OrderIntroduceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_introduce);
        showHeader("推广订单");
    }
}
