package com.xiling.module.splash;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.blankj.utilcode.utils.SPUtils;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.module.MainActivity;
import com.xiling.shared.basic.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class GuideFragment extends BaseFragment {


    @BindView(R.id.ivImage)
    ImageView mIvImage;
    Unbinder unbinder;
    @BindView(R.id.ivText)
    ImageView mIvText;
    @BindView(R.id.ivGoMain)
    ImageView mIvGoMain;
    boolean isEnd;

    public static GuideFragment newInstance(int imgRes,boolean isEnd) {
        Bundle args = new Bundle();
        args.putInt("imgRes", imgRes);
        args.putBoolean("isEnd",isEnd);
        GuideFragment fragment = new GuideFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        int imgRes = getArguments().getInt("imgRes");
        isEnd = getArguments().getBoolean("isEnd");
        mIvImage.setImageResource(imgRes);

        if (isEnd){
            mIvGoMain.setVisibility(View.VISIBLE);
            mIvText.setVisibility(View.VISIBLE);
        }
    }

    private void startAnima() {
        AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(getContext(), R.anim.guide_anim);
        mIvText.startAnimation(alphaAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ivGoMain)
    public void onViewClicked() {
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}
