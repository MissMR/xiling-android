package com.dodomall.ddmall.module.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Chan on 2017/6/10.
 */

public class AboutUsActivity extends BaseActivity {
    @BindView(R.id.tvName)
    TextView mTvName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus_layout);
        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHeader();
        setTitle("关于我们");
        setLeftBlack();
        mTvName.setText(AppUtils.getAppName(this) + "  v" + AppUtils.getAppVersionName(this));
    }
}
