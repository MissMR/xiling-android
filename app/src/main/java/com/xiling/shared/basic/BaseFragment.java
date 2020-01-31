package com.xiling.shared.basic;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.user.LoginActivity;
import com.xiling.shared.bean.event.MsgMain;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class BaseFragment extends Fragment {

    protected CharSequence title;
    protected Context mContext;

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public void setTitle(int titleId) {
        setTitle(getResources().getString(titleId));
    }

    public CharSequence getTitle() {
        return this.title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("BaseFragment", getClass().getSimpleName());
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
            if (isNeedLogin() && !UserManager.getInstance().isLogin()) {
                startActivity(new Intent(getContext(), LoginActivity.class));
            }
        }
    }

}
