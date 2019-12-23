package com.xiling.shared.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.xiling.BuildConfig;
import com.xiling.R;

public class CommonUtil {

    private static int SCREEN_SIZE_WIDTH = -1;
    private static int SCREEN_SIZE_HEIGHT = -1;

    public static int getScreenWidth(Context context) {
        if (SCREEN_SIZE_WIDTH <= 0 || SCREEN_SIZE_HEIGHT <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.getDefaultDisplay().getMetrics(dm);
            }
            SCREEN_SIZE_WIDTH = dm.widthPixels;
            SCREEN_SIZE_HEIGHT = dm.heightPixels;
        }

        Configuration conf = context.getResources().getConfiguration();
        switch (conf.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                return SCREEN_SIZE_WIDTH > SCREEN_SIZE_HEIGHT ? SCREEN_SIZE_WIDTH
                        : SCREEN_SIZE_HEIGHT;
            case Configuration.ORIENTATION_PORTRAIT:
                return SCREEN_SIZE_WIDTH < SCREEN_SIZE_HEIGHT ? SCREEN_SIZE_WIDTH
                        : SCREEN_SIZE_HEIGHT;
            default:
                Logger.e("can't get screen width!");
        }
        return SCREEN_SIZE_WIDTH;
    }

    public static String getAppName(Context context) {
        if ("debug".equalsIgnoreCase(BuildConfig.BUILD_TYPE)) {
            return context.getResources().getString(R.string.appNameDebug);
        }
        return context.getResources().getString(R.string.app_name);
    }

    public static void initDialogWindow(Window window, int gravity) {
        if (window != null) {
            window.setGravity(gravity);
            window.setWindowAnimations(R.style.BottomDialogStyle);
            window.getDecorView().setPadding(0, 0, 0, 0);
            //获得window窗口的属性
            WindowManager.LayoutParams lp = window.getAttributes();
            //设置窗口宽度为充满全屏
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            //设置窗口高度为包裹内容
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            //将设置好的属性set回去
            window.setAttributes(lp);
        }
    }
}
