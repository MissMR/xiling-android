package com.dodomall.ddmall.ddui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.utils.SPUtils;
import com.dodomall.ddmall.BuildConfig;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.module.splash.GuideActivity;
import com.dodomall.ddmall.module.splash.SplashActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseSubscriber;
import com.dodomall.ddmall.shared.bean.Splash;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IAdService;
import com.google.gson.Gson;
import com.zjm.zviewlibrary.splash.model.SplashModel;
import com.zjm.zviewlibrary.splash.view.SplashFrame;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/30
 * 启动页
 */
public class DDSplashActivity extends BaseActivity {

    @BindView(R.id.tv_hint)
    TextView tvHint;

    private String hint = "阿里云提供计算服务\nv %s";

    int delay = 2 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_ddsplash);
        ButterKnife.bind(this);

        tvHint.setText(String.format(hint, BuildConfig.VERSION_NAME));

        SPUtils spUtils = new SPUtils(SplashActivity.class.getName() + "_" + BuildConfig.VERSION_NAME);
        boolean oneStart = spUtils.getBoolean("oneStart");
        if (!oneStart) {
            //开启引导页
            jumpToWelcome();
            return;
        } else {
            jumpToMain();
//            getSplashData();
        }
    }

    public void getSplashData() {
        //获取网络数据
        IAdService adService = ServiceManager.getInstance().createService(IAdService.class);
        execute(adService.getSplashAd(), new BaseSubscriber<Splash>() {
            @Override
            public void onNext(Splash splash) {
                super.onNext(splash);
                //显示广告数据
                SplashModel model = new SplashModel(splash.backUrl, splash.event, splash.target);
                if (model != null) {
                    if (TextUtils.isEmpty(model.imgUrl)) {
                        //清空上次存储的图片数据
                        clearFlashImage();
                        jumpToMain();
                    } else {
                        jumpToAds(new Gson().toJson(model));
                    }
                } else {
                    jumpToMain();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //发生错误直接跳转首页
                jumpToMain();
            }
        });
    }


    void clearFlashImage() {
        SplashModel emptyModel = new SplashModel("", "", "");
//        SplashFrame.cacheData(SplashActivity.this, emptyModel);
        SharedPreferences splashSP = context.getSharedPreferences("splash", 0);
        splashSP.edit().putString("SplashModel", (new Gson()).toJson(emptyModel)).commit();
    }

    public void jumpToWelcome() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(DDSplashActivity.this, GuideActivity.class));
                finish();
            }
        }, delay);
    }

    public void jumpToAds(final String json) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashActivity.navTo(DDSplashActivity.this, json);
                finish();
            }
        }, delay);
    }

    public void jumpToMain() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(DDSplashActivity.this, MainActivity.class));
                finish();
            }
        }, delay);
    }
}
