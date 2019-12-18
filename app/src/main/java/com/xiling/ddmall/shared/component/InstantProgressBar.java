package com.xiling.ddmall.shared.component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.xiling.ddmall.R;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/4.
 */
public class InstantProgressBar extends FrameLayout {


    private ImageView mIvProgress;
    private TextView mTvProgress;

    public InstantProgressBar(@NonNull Context context) {
        super(context, null);
    }

    public InstantProgressBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.layout_instant_progress, this);
        mIvProgress = (ImageView) findViewById(R.id.ivProgress);
        mTvProgress = (TextView) findViewById(R.id.tvProgress);
    }

    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        mTvProgress.setText(progress + "%");
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int width = layoutParams.width;
        int height = layoutParams.height;
        LogUtils.e("宽敞" + width + "   " + height);
        int progressWidth = width * progress / 100;
        mIvProgress.setLayoutParams(new LayoutParams(progressWidth, ViewGroup.LayoutParams.MATCH_PARENT));

        mTvProgress.setTextSize(ConvertUtils.px2dp(height) * 2 / 3);
    }
}
