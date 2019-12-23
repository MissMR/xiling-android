package com.xiling.ddui.tools;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiling.R;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.util.DensityUtil;

/**
 * created by Jigsaw at 2019/4/19
 */
public class DDRefreshHeader extends LinearLayout implements RefreshHeader {


    private ImageView mImageView;
    private AnimationDrawable doorOpenDrawable;
    private AnimationDrawable doorLoadingDrawable;

    public DDRefreshHeader(Context context) {
        this(context, null);
    }

    public DDRefreshHeader(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        setGravity(Gravity.CENTER);
        mImageView = new ImageView(context);
        mImageView.setImageResource(R.drawable.anim_door_open);
        doorOpenDrawable = (AnimationDrawable) mImageView.getDrawable();
        doorLoadingDrawable = (AnimationDrawable) ContextCompat.getDrawable(getContext(), R.drawable.anim_door_loading);
        addView(mImageView, -1, -1);
        setMinimumHeight(DensityUtil.dp2px(60));
    }

    @NonNull
    public View getView() {
        return this;//真实的视图就是自己，不能返回null
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null
    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout layout, int height, int maxDragHeight) {
        startAnimate();//开始动画
    }

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        DLog.i("onFinish");
        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnimate();//停止动画
            }
        }, 550);
        return 600;//
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullDownToRefresh:
                mImageView.setImageDrawable(doorOpenDrawable);
                break;
            case Refreshing:
                break;
            case ReleaseToRefresh:
                mImageView.setImageDrawable(doorOpenDrawable);
                break;
            case PullDownCanceled:
            case PullUpCanceled:
                mImageView.setImageDrawable(doorOpenDrawable);
                break;
            default:
        }
    }

    private void startAnimate() {
        mImageView.setImageDrawable(doorOpenDrawable);
        if (doorOpenDrawable.isRunning()) {
            doorOpenDrawable.stop();
        }
        if (doorLoadingDrawable.isRunning()) {
            doorLoadingDrawable.stop();
        }
        doorOpenDrawable.start();

        mImageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mImageView.setImageDrawable(doorLoadingDrawable);
                doorLoadingDrawable.start();
            }
        }, getDoorOpenDrawableDuration());
    }

    private int getDoorOpenDrawableDuration() {
        int duration = 0;
        for (int i = 0; i < doorOpenDrawable.getNumberOfFrames(); i++) {
            duration += doorOpenDrawable.getDuration(i);
        }
        return duration;
    }

    private void stopAnimate() {
        DLog.i("stopAnim");
        if (doorOpenDrawable.isRunning()) {
            doorOpenDrawable.stop();
        }
        if (doorLoadingDrawable.isRunning()) {
            doorLoadingDrawable.stop();
        }
        mImageView.setImageDrawable(doorOpenDrawable);
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {

    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }
}
