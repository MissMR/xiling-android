package com.dodomall.ddmall.shared.component;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 头部&导航 布局
 * Created by JayChan on 16/10/14.
 */
public class HeaderLayout extends LinearLayout {

    @BindView(R.id.headerTitleTv)
    protected TextView mHeaderTitleTv;

    @BindView(R.id.headerLeftTv)
    protected TextView mHeaderLeftTv;

    @BindView(R.id.headerRightTv)
    protected TextView mHeaderRightTv;

    @BindView(R.id.headerLeftIv)
    protected ImageView mHeaderLeftIv;

    @BindView(R.id.headerRightIv)
    protected ImageView mHeaderRightIv;

    @BindView(R.id.layout)
    FrameLayout mHeaderLayout;
    @BindView(R.id.rightAnchor)
    View mRightAnchor;

    private View mRootView;

    public HeaderLayout(Context context) {
        super(context);
        initViews();
    }

    public HeaderLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews();
    }

    private void initViews() {
        this.hide();
        mRootView = LayoutInflater.from(getContext()).inflate(R.layout.header_layout, (ViewGroup) getRootView(), true);
        ButterKnife.bind(this, mRootView);
        mHeaderTitleTv.setText("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE) ? R.string.appNameDebug : R.string.appName);
        //默认标题栏白色
        setBackgroundColor(Color.WHITE);
    }

    public void setBackgroundColor(int color) {
        mHeaderLayout.setBackgroundColor(color);
    }

    public void show() {
        this.setVisibility(VISIBLE);
    }

    public void hide() {
        this.setVisibility(GONE);
    }

    public void showRightItem() {
        mHeaderRightTv.setVisibility(View.VISIBLE);
        mHeaderRightIv.setVisibility(View.VISIBLE);
    }

    public void hideRightItem() {
        mHeaderRightTv.setVisibility(View.INVISIBLE);
        mHeaderRightIv.setVisibility(View.INVISIBLE);
    }

    public void setLeftDrawable(Drawable drawable) {
        mHeaderLeftIv.setImageDrawable(drawable);
        mHeaderLeftIv.setVisibility(VISIBLE);
    }

    public void setLeftDrawable(@DrawableRes int resId) {
        if (mHeaderLeftIv == null) {
            return;
        }
        mHeaderLeftIv.setImageResource(resId);
        mHeaderLeftIv.setVisibility(VISIBLE);
    }

    public void setOnLeftClickListener(OnClickListener onClickListener) {
        if (mHeaderLeftIv == null) {
            return;
        }
        mHeaderLeftTv.setOnClickListener(onClickListener);
        mHeaderLeftIv.setOnClickListener(onClickListener);
    }

    public void setRightDrawable(Drawable drawable) {
        mHeaderRightIv.setImageDrawable(drawable);
        mHeaderRightIv.setVisibility(VISIBLE);
    }

    public void setRightDrawable(@DrawableRes int resId) {
        mHeaderRightIv.setImageResource(resId);
        mHeaderRightIv.setVisibility(VISIBLE);
    }

    public ImageView getRightImageView() {
        return mHeaderRightIv;
    }

    public void setRightText(CharSequence charSequence) {
        mHeaderRightTv.setText(charSequence);
        mHeaderRightTv.setVisibility(VISIBLE);
    }

    public void setRightText(@StringRes int resId) {
        String string = getResources().getString(resId);
        setRightText(string);
    }

    public CharSequence getRightText() {
        if (mHeaderRightTv.getVisibility() == GONE) {
            return "";
        }
        return mHeaderRightTv.getText();
    }


    public void setOnRightClickListener(OnClickListener onClickListener) {
        mHeaderRightTv.setOnClickListener(onClickListener);
        mHeaderRightIv.setOnClickListener(onClickListener);
    }

    public void setTitleTextColor(int color) {
        this.mHeaderTitleTv.setTextColor(color);
    }

    public void setTitle(@StringRes int resId) {
        this.mHeaderTitleTv.setText(resId);
        this.show();
    }

    public void setTitle(CharSequence charSequence) {
        this.mHeaderTitleTv.setText(charSequence);
        this.show();
    }

    public void setTitleColor(@ColorRes int colorRes) {
        this.mHeaderTitleTv.setTextColor(getResources().getColor(colorRes));
    }

    public TextView getTitleView() {
        return mHeaderTitleTv;
    }

    public void makeHeaderRed() {
        mRootView.setSelected(true);
        setLeftDrawable(R.drawable.ic_arrow_left_black);
    }

    public void setNoLine() {
        mHeaderLayout.setBackgroundColor(Color.WHITE);
    }

    public View getRightAnchor() {
        return mRightAnchor;
    }

    public void setRightTextColor(int colorRes) {
        mHeaderRightTv.setTextColor(getResources().getColor(colorRes));
    }

}
