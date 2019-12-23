package com.xiling.module.community;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.Constants;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/1/5  16:36
 * @desc ${TODD}
 */

public class CommunityListActivity extends BasicActivity {
    @BindView(R.id.container)
    FrameLayout mContainer;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_community_list;
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        TextView titleTv = (TextView) findViewById(R.id.titleTv);
        GroupCategoryModel model= (GroupCategoryModel) getIntent().getSerializableExtra(Constants.Extras.KEY_EXTRAS);
        int intExtra = getIntent().getIntExtra(Constants.Extras.KET_TYPE, 0);
        titleTv.setText(model.getName());
        if(intExtra==0) {
            Fragment listFragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_MATERIAL,model.getCategoryId(), false);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
        }else {
            Fragment listFragment = GroupFragment.instance(GroupFragment.CommunityType.TYPE_VIDEO,model.getCategoryId(), false);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, listFragment).commit();
        }
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
