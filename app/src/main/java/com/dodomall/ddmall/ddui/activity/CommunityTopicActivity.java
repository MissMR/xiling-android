package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.fragment.CommunityTopicFragment;
import com.dodomall.ddmall.shared.basic.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/11
 * 热门话题
 */
@Deprecated
public class CommunityTopicActivity extends BaseActivity {
    @BindView(R.id.fl_container)
    FrameLayout flContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_topic);
        ButterKnife.bind(this);
        showHeader("热门话题");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fl_container, CommunityTopicFragment.newInstance(false));
        transaction.commit();
    }

}
