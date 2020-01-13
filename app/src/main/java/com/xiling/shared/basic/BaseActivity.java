package com.xiling.shared.basic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.api.MApiActivity;
import com.xiling.ddui.config.AppConfig;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.module.auth.Config;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.constant.Event;
import com.xiling.shared.util.ActivityController;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.stetho.common.LogUtil;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


abstract public class BaseActivity extends AppCompatActivity implements SensorEventListener {

    protected FrameLayout mBaseContentLayout;
    private HeaderLayout mHeaderLayout;
    protected InputMethodManager mInputMethodManager;
    private SwipeRefreshLayout mLayoutRefresh;
    private Dialog mProgressDialog;
    private CompositeDisposable compositeSubscription;
    private Disposable mDisposable;
    private View mBarPading;

    public Context context = null;
    public Bundle mReenterData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("BaseActivity",getClass().getSimpleName());
        context = this;
        ActivityController.getInstance().pushActivity(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (isNeedLogin() && !UserManager.getInstance().isLogin()) {
            EventBus.getDefault().post(new EventMessage(Event.goToLogin));
        }

        if (AppConfig.DEBUG) {
            initSensor();
        }

    }

    SensorManager mSensorManager = null;
    Sensor mAccelerometerSensor = null;

    void initSensor() {
        //获取 SensorManager 负责管理传感器
        mSensorManager = ((SensorManager) getSystemService(SENSOR_SERVICE));
        if (mSensorManager != null) {
            //获取加速度传感器
            mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    /**
     * 设置一个状态
     */
    public void setStatusBarView(View view) {
        int statusBarHeight = UITools.getStatusBarHeight(this);
//        DLog.d("statusBarHeight:" + statusBarHeight);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        view.setLayoutParams(params);
    }

    public void clearViewHeight(View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        view.setLayoutParams(params);
    }

    public void darkStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public void writeStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }

    public void makeStatusBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //透明状态栏
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }


    public void makeNavigationBarTranslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        } else {
            LogUtil.w("this device not support navigation!");
        }
    }

    protected boolean isNeedLogin() {
        return false;
    }

    public SwipeRefreshLayout getLayoutRefresh() {
        return mLayoutRefresh;
    }

    public void setLayoutRefresh(SwipeRefreshLayout layoutRefresh) {
        mLayoutRefresh = layoutRefresh;
        mLayoutRefresh.setColorSchemeResources(R.color.red);
    }

    public Dialog getProgressDialog() {
        if (mProgressDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.loading_layout, null);
            mProgressDialog = new Dialog(this, R.style.LoadingDialog);
            mProgressDialog.setContentView(view, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            mProgressDialog.setCancelable(true);
        }
        return mProgressDialog;
    }


    @Override
    protected void onStart() {
        try {
            super.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        Bugtags.onResume(this);

        if (mSensorManager != null && mAccelerometerSensor != null) {
            mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_UI);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // 强制界面在重新回来的时候隐藏所有加载进度框 荆汉青 2018/11/23 9:15
        ToastUtil.hideLoading();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
//        Bugtags.onPause(this);
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.getInstance().popActivity(this);
        ToastUtil.hideLoading();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        unSubscribe();
    }

    boolean isShake = false;
    float TARGET_VALUE = 17;

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
        if (null != data) {
            // 共享元素  拿到回传的参数
            DLog.i("onActivityReenter " + data.toString());
            mReenterData = data.getExtras();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (!AppConfig.DEBUG) {
            return;
        }

        String clazzName = this.getClass().getSimpleName();
        if (!TextUtils.isEmpty(clazzName)) {
            if (clazzName.equals(MApiActivity.class.getSimpleName())) {
                return;
            }
        }
        int type = sensorEvent.sensor.getType();
        if (type == Sensor.TYPE_ACCELEROMETER) {
            //获取三个方向值
            float[] values = sensorEvent.values;
            float x = values[0];
            float y = values[1];
            float z = values[2];
            if (Math.abs(x) > TARGET_VALUE && !isShake) {
                isShake = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("调试");
                builder.setMessage("选择类型：");
                builder.setNeutralButton("API", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(context, MApiActivity.class));
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        isShake = false;
                    }
                });
                builder.show();
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeMessageWithEventBus(EventMessage message) {
        switch (message.getEvent()) {
            case toastErrorMessage:
                ToastUtil.error((String) message.getData());
                break;
            case hideLoading:
                ToastUtil.hideLoading();
                break;
            case showLoading:
                ToastUtil.showLoading(this);
                break;
            case networkConnected:
                Logger.e("网络已连接");
                break;
            case networkDisconnected:
                Logger.e("网络已断开");
                break;
            case showAlert:
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setTitle("原生弹窗")
                        .setPositiveButton("确定", null)
                        .setMessage((String) message.getData())
                        .create().show();
                break;
            default:
                break;
        }
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(R.layout.base_layout);
        initBaseViews();
        getLayoutInflater().inflate(layoutResID, mBaseContentLayout, true);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(R.layout.base_layout);
        initBaseViews();
        mBaseContentLayout.addView(view);
    }

    private void initBaseViews() {
        mBaseContentLayout = (FrameLayout) findViewById(R.id.baseContentLayout);
        mHeaderLayout = (HeaderLayout) findViewById(R.id.headerLayout);
        mBarPading = findViewById(R.id.barPading);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mHeaderLayout.setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
        mHeaderLayout.setTitle(titleId);
    }

    public void setBarPadingHeight(int height) {
        mBarPading.getLayoutParams().height = height;
//        mBarPading.setMinimumHeight(height);
    }

    public void setBarPadingColor(int color) {
        mBarPading.setBackgroundColor(color);
    }

    public void setTitleNoLine() {
        mHeaderLayout.setNoLine();
    }


    public void setLeftBlack(View.OnClickListener backListener) {
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
        mHeaderLayout.setOnLeftClickListener(backListener);
    }

    public void setLeftBlack() {
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
        mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void setLeftWhite() {
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_white);
        mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void showHeader() {
        mHeaderLayout.show();
    }

    public void hideHeader() {
        mHeaderLayout.hide();
    }

    public void showHeader(CharSequence title, boolean isBlack) {
        showHeader();
        setTitle(title);
        if (isBlack) {
            setLeftBlack();
        }
    }

    public void showHeader(CharSequence title) {
        showHeader(title, true);
    }

    public void showHeaderRightText(CharSequence right, View.OnClickListener listener) {
        mHeaderLayout.setRightText(right);
        mHeaderLayout.setOnRightClickListener(listener);
    }

    public HeaderLayout getHeaderLayout() {
        return mHeaderLayout;
    }

    // zjm 网络请求封装
    public void execute(Observable observable, BaseSubscriber subscriber) {
        observable = observable
                .map(new HttpResultFunc())
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        observable.subscribe(subscriber);
        addSubscribe(subscriber.getDisposable());
    }


    public static class HttpResultFunc<T> implements Function<RequestResult<T>, T> {
        @Override
        public T apply(@NonNull RequestResult<T> result) throws Exception {
            if (result.isSuccess()) {
                return result.data;
            } else if (result.isNotLogin()) {
                EventBus.getDefault().post(new EventMessage(Event.goToLogin));
                throw new RuntimeException(Config.NET_MESSAGE.NO_LOGIN);
            } else {
                throw new RuntimeException(result.message);
            }
        }
    }

    public void addSubscribe(Disposable disposable) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeDisposable();
        }
        compositeSubscription.add(disposable);
    }

    public void unSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.clear();
        }
    }


    /**
     * EditText获取焦点并显示软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
    }

    public boolean isFinished() {
        return isFinishing() || isDestroyed();
    }
}