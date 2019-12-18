package com.xiling.ddmall.module.community;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.xiling.ddmall.R;

import me.relex.circleindicator.CircleIndicator;


public class BannerView extends FrameLayout {
    private FixedViewPager viewPager;
    private CircleIndicator indicator;

    public BannerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.view_banner,this,true);

        viewPager = (FixedViewPager) view.findViewById(R.id.viewpager);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator);
    }

    public void setAdapter(PagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        viewPager.loop(false);
        viewPager.loop();
        indicator.setViewPager(viewPager);
    }
}
