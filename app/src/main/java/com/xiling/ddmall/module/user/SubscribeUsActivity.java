package com.xiling.ddmall.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.user
 * @since 2017-08-03
 */
public class SubscribeUsActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbuscribe_us);
        showHeader();
        setTitle("关注我们");
        setLeftBlack();
    }
}
