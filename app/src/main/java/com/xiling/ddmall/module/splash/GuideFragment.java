package com.xiling.ddmall.module.splash;


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
import com.xiling.ddmall.BuildConfig;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.MainActivity;
import com.xiling.ddmall.shared.basic.BaseFragment;

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

    public static GuideFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt("index", index);
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
        int index = getArguments().getInt("index");
        switch (index) {
            case 1:
                mIvImage.setImageResource(R.mipmap.guide_img_1);
                break;
            case 2:
                mIvImage.setImageResource(R.mipmap.guide_img_2);
                break;
            case 3:
                mIvImage.setImageResource(R.mipmap.guide_img_3);
                mIvGoMain.setVisibility(View.VISIBLE);
                mIvText.setVisibility(View.VISIBLE);
                startAnima();
                break;
            default:
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
        SPUtils spUtils = new SPUtils(SplashActivity.class.getName() + "_" + BuildConfig.VERSION_NAME);
        spUtils.putBoolean("oneStart", true);
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }
}
