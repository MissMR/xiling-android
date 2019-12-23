package com.xiling.module.community;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.blankj.utilcode.utils.ToastUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.util.SystemBarHelper;
import com.xiling.shared.util.ToastUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;

/**
 * @author Stone
 * @time 2018/1/3  12:14
 * @desc ${TODD}
 */

public abstract class BasicActivity extends BaseActivity {

    private Unbinder mBind;
    protected Context mActivity;
    private View mLoading;
    private FrameLayout mContainer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
        mLoading = findViewById(R.id.flLoading);
        mContainer = (FrameLayout) findViewById(R.id.flContainer);
        getLayoutInflater().inflate(getLayoutResId(), mContainer);
        mActivity = this;
        mBind = ButterKnife.bind(this);
        //设置状态栏颜色
        int statusBarColor = statusBarColor();
        if (statusBarColor != -1) {
            SystemBarHelper.tintStatusBar(this, ContextCompat.getColor(this, statusBarColor));
        }

        //开启黑色状态栏图标模式
        if (enableStatusBarDarkMode()) {
            SystemBarHelper.setStatusBarDarkMode(this);
        }
        if (enableFullScreenToStatusBar()) {
            SystemBarHelper.tintStatusBar(this, Color.TRANSPARENT);
            configFullScreenToStatusBar();
        }
        initViewConfig();
        initDataNew();
        initListener();
    }

    protected void initListener() {

    }

    protected void initDataNew() {

    }

    protected void initViewConfig() {
    }

    protected void setBackGround(int transparent) {
        mContainer.setBackgroundResource(transparent);
    }


    /**
     * @return 返回布局id
     */
    protected abstract int getLayoutResId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    /**
     * 布局是否需要嵌入状态栏里面
     *
     * @return true: 布局嵌入状态栏
     */
    protected boolean enableFullScreenToStatusBar() {
        return false;
    }

    /**
     * 是否开启黑色状态栏图片样式
     * or
     *
     * @return true: 开启
     */
    protected boolean enableStatusBarDarkMode() {
        return true;
    }

    /**
     * 当前Activity嵌入状态栏里面
     */
    private void configFullScreenToStatusBar() {
        getWindow().setBackgroundDrawableResource(android.R.color.white);
        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
        ViewGroup contentView = (ViewGroup) decorView.findViewById(Window.ID_ANDROID_CONTENT);
        View rootView = contentView.getChildAt(0);
        ViewCompat.requestApplyInsets(rootView);
        ViewCompat.setOnApplyWindowInsetsListener(rootView, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                return insets.consumeSystemWindowInsets();
            }
        });
    }

    /**
     * 状态栏颜色
     *
     * @return color res
     */
    @ColorRes
    protected int statusBarColor() {
        return R.color.white;
    }

    public void showLoading() {
        mLoading.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }


    public void showError(Throwable e) {
        ToastUtil.error(e.getMessage());
    }

    public void showToast(String text) {
        ToastUtils.showShortToast( text);
    }

    public void requestPermission(final PermissionListener listener) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean && listener != null) {
                    listener.onSuccess();
                } else {
                    ToastUtils.showShortToast("请打开权限");
                }
            }
        });
    }

    @Override
    public void setTitle(CharSequence title) {
//        super.setTitle(title);
        getHeaderLayout().setTitle(title);
        setLeftBlack();
    }

    public interface PermissionListener {
        void onSuccess();
    }


}
