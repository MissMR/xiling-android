package com.dodomall.ddmall.module.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.SPUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.DDHomeBanner;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseSubscriber;
import com.dodomall.ddmall.shared.bean.Splash;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IAdService;
import com.google.gson.Gson;
import com.zjm.zviewlibrary.splash.model.SplashModel;
import com.zjm.zviewlibrary.splash.view.CountDownTextViewView;
import com.zjm.zviewlibrary.splash.view.SplashFrame;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.splash
 * @since 2017-08-03
 */
public class SplashActivity extends BaseActivity {

    @BindView(R.id.layoutContent)
    LinearLayout mLayoutContent;

    String delayEvent = "";
    String delayTarget = "";
    boolean isPressed = false;

    private static String kJson = "";

    public static void navTo(Context context, String json) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.putExtra(kJson, json);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        String json = getIntent().getStringExtra(kJson);
        SplashModel model = new Gson().fromJson(json, SplashModel.class);

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (model != null) {
            if (model.imgUrl == null) {
                jumpToMain();
            } else {
                SplashFrame.cacheData(SplashActivity.this, model);
                showFlashImage();
            }
        }
    }

    void showFlashImage() {
        SplashFrame splashFrame = new SplashFrame(this, -1, new SplashFrame.OnSplashActionListener() {
            @Override
            public void onImageClick(String s, String s1) {
                DLog.d("onImageClick:" + s + "," + s1);
                isPressed = true;
                delayEvent = s;
                delayTarget = s1;
                jumpToMain();
            }

            @Override
            public void onHide() {
                if (!isPressed) {
                    jumpToMain();
                }
            }
        }, new CountDownTextViewView(this));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mLayoutContent.addView(splashFrame, layoutParams);
    }

    public void jumpToMain() {

        //先跳转首页
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        //接着继续处理跳转逻辑
        if (!TextUtils.isEmpty(delayEvent) && !TextUtils.isEmpty(delayTarget)) {
            onAdPressed(delayEvent, delayTarget);
        }
        //关闭当前界面
        finish();
    }

    /**
     * 广告被点击
     *
     * @param event
     * @param target
     */
    public void onAdPressed(String event, String target) {
        DDHomeBanner.process(context, event, target);
    }

}
