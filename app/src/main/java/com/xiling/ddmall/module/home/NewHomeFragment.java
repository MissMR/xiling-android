package com.xiling.ddmall.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.module.search.SearchActivity;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.constant.Key;
import com.xiling.ddmall.shared.page.CustomPageFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2018/6/7.
 */
public class NewHomeFragment extends BaseFragment {

    @BindView(R.id.linkToBeShopkeeper)
    LinearLayout mLinkToBeShopkeeper;
    @BindView(R.id.layoutContent2)
    FrameLayout mLayoutContent;
    @BindView(R.id.tvSearch)
    TextView mTvSearch;
    Unbinder unbinder;

    public static NewHomeFragment newInstance() {
        Bundle args = new Bundle();
        NewHomeFragment fragment = new NewHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), SearchActivity.class));
            }
        });

        CustomPageFragment homeFragment = CustomPageFragment.newInstance(Key.PAGE_HOME);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.layoutContent2, homeFragment);
        fragmentTransaction.commit();
    }

//    @OnClick(R.id.tvSearch)
//    protected void clickSearchLayout() {
//        startActivity(new Intent(getContext(), SearchActivity.class));
//    }

    @OnClick(R.id.linkToBeShopkeeper)
    protected void toBeShopKeeper() {
        HtmlService.startBecomeStoreMasterActivity(getActivity());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
