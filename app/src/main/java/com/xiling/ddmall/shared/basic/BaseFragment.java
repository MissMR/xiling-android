package com.xiling.ddmall.shared.basic;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xiling.ddmall.module.user.LoginActivity;
import com.xiling.ddmall.shared.bean.event.MsgMain;
import com.xiling.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;

public class BaseFragment extends Fragment {

    protected CharSequence title;

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setTitle(int titleId) {
        setTitle(getResources().getString(titleId));
    }

    public CharSequence getTitle() {
        return this.title;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setViewHeight(View view, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        view.setLayoutParams(params);
    }

    protected boolean isNeedLogin() {
        return false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isNeedLogin() && !SessionUtil.getInstance().isLogin()) {
                EventBus.getDefault().post(new MsgMain(MsgMain.SELECT_HOME));
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        }
    }
}
