package com.xiling.ddui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.blankj.utilcode.utils.SPUtils;
import com.google.gson.Gson;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.ddui.bean.SystemConfigBean;
import com.xiling.ddui.service.IConfigService;
import com.xiling.image.GlideUtils;
import com.xiling.module.MainActivity;
import com.xiling.module.auth.Config;
import com.xiling.module.splash.GuideActivity;
import com.xiling.module.splash.SplashActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Splash;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAdService;
import com.xiling.shared.util.ToastUtil;
import com.zjm.zviewlibrary.splash.model.SplashModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Jigsaw
 * @date 2018/10/30
 * 启动页
 */
public class DDSplashActivity extends BaseActivity {
    IConfigService iConfigService;
    @BindView(R.id.iv_splash)
    ImageView ivSplash;

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

        iConfigService = ServiceManager.getInstance().createService(IConfigService.class);
        //获取系统参数
        getSystemConfig();

        SPUtils spUtils = new SPUtils(SplashActivity.class.getName() + "_" + BuildConfig.VERSION_NAME);
        boolean oneStart = spUtils.getBoolean("oneStart");
        if (!oneStart) {
            //开启引导页
            jumpToWelcome();
            return;
        } else {
            //jumpToMain();
            getSplashData();
        }
    }

    /**
     * 系统配置信息
     */
    private void getSystemConfig() {
        APIManager.startRequest(iConfigService.getSystemConfig(), new BaseRequestListener<SystemConfigBean>() {
            @Override
            public void onSuccess(SystemConfigBean result) {
                super.onSuccess(result);
                //保存系统配置信息
                Config.systemConfigBean = result;
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });


    }


    public void getSplashData() {
        //获取网络数据
        IAdService adService = ServiceManager.getInstance().createService(IAdService.class);
        APIManager.startRequest(adService.getSplashAd(7), new BaseRequestListener<List<Splash>>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                //发生错误直接跳转首页
                jumpToMain();
            }

            @Override
            public void onSuccess(List<Splash> result) {
                super.onSuccess(result);
                if (result.size() > 0) {
                    GlideUtils.loadImageALL(context,ivSplash,result.get(0).getBackUrl());
                }

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
