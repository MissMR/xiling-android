package com.xiling.module.instant;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.bean.InstantData;
import com.xiling.shared.component.CountDown;

import java.util.Date;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/2.
 */
public class InstantBannerView extends LinearLayout{

    private SimpleDraweeView mIvBanner;
    private CountDown mCountDown;
    TextView mTvStatus;

    public InstantBannerView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        View inflate = inflate(getContext(), R.layout.view_instant_banner, this);
        mIvBanner = (SimpleDraweeView) inflate.findViewById(R.id.ivBanner);
        mCountDown = (CountDown) inflate.findViewById(R.id.countDownView);
        mTvStatus = (TextView) inflate.findViewById(R.id.tvStatus);
    }

    public void setData(InstantData.SecondKill secondKill){
        mIvBanner.setImageURI(secondKill.image);

        Date date = new Date();
        Date startDate = TimeUtils.string2Date(secondKill.startDate);
        Date endDate = TimeUtils.string2Date(secondKill.endDate);

        if (date.getTime() > endDate.getTime()) {
            mTvStatus.setText("已结束");
            mTvStatus.setVisibility(VISIBLE);
            mCountDown.setVisibility(GONE);
        } else if (date.getTime() < startDate.getTime()) {
            mTvStatus.setText("距开始还剩:");
            startDownTime(startDate.getTime() - date.getTime());
        } else {
            mTvStatus.setText("距离结束:");
            startDownTime(endDate.getTime() - date.getTime());
        }
    }

    private void startDownTime(long time) {
        mCountDown.setTimeLeft(time, null);
    }
}
