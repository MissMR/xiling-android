package com.xiling.ddmall.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.fragment.DDCommunityFragment;
import com.xiling.ddmall.shared.basic.BaseActivity;

/**
 * @author Jigsaw
 * @date 2018/12/10
 * 社区
 */
public class DDCommunityActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddcommunity_activity);
        initView();
    }

    private void initView() {
        showHeader("发圈");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_content, new DDCommunityFragment());
        transaction.commit();
    }
}
