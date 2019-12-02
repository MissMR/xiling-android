package com.dodomall.ddmall.ddui.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.dodomall.ddmall.R;
import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import in.srain.cube.views.ptr.indicator.PtrTensionIndicator;

public class DDMPtrUIHandler extends FrameLayout implements PtrUIHandler {

    public DDMPtrUIHandler(@NonNull Context context) {
        super(context);
        init();
    }

    public DDMPtrUIHandler(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DDMPtrUIHandler(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setBackground(int color) {
        view.setBackgroundColor(color);
    }

    public void setTextColor(int color) {
        titleView.setTextColor(color);
    }

    View view;
    HTextView titleView;

    private PtrTensionIndicator mPtrTensionIndicator;
    PtrFrameLayout mPtrFrameLayout;

    public void init() {
        Context context = getContext();
        view = LayoutInflater.from(context).inflate(R.layout.layout_ptr_header, this, false);
        addView(view);
        titleView = view.findViewById(R.id.hTextView);
        titleView.setAnimateType(HTextViewType.SCALE);
    }

    public void setup(PtrFrameLayout ptrFrameLayout) {
        mPtrFrameLayout = ptrFrameLayout;
        mPtrTensionIndicator = new PtrTensionIndicator();
        mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
//        DLog.d("onUIReset");
        titleView.reset("");
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
//        DLog.d("onUIRefreshPrepare");
        titleView.animateText("正在为您加载");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
//        DLog.d("onUIRefreshBegin");
        titleView.animateText("精彩即将呈现");
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
//        DLog.d("onUIRefreshComplete");
        titleView.reset("");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//        DLog.d("onUIPositionChange");
        final int mOffsetToRefresh = frame.getOffsetToRefresh();
        final int currentPos = ptrIndicator.getCurrentPosY();
        final int lastPos = ptrIndicator.getLastPosY();


        if (currentPos < mOffsetToRefresh) {
            //未到达刷新线
            if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {

            }
        } else if (currentPos > mOffsetToRefresh) {
            //到达或超过刷新线
            if (isUnderTouch && status == PtrFrameLayout.PTR_STATUS_PREPARE) {

            }
        }

    }
}
