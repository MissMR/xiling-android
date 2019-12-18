package com.xiling.ddmall.module.team;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseActivity;

import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.team
 * @since 2017-07-05
 */
public class TeamMemberActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_tab_page);
        ButterKnife.bind(this);
    }
}
