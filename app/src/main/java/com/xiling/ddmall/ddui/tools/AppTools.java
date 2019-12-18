package com.xiling.ddmall.ddui.tools;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationManagerCompat;

import com.xiling.ddmall.shared.util.ToastUtil;

import java.util.List;

public class AppTools {

    /**
     * QQ包名
     */
    public static final String PKG_QQ = "com.tencent.mobileqq";
    /*微信包名*/
    public static final String PKG_Wechat = "com.tencent.mm";

    //判断是否安装了微信,QQ,QQ空间
    public static boolean isAppInstall(Context context, String mType) {
        // 获取PackageManager
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(mType)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 启动微信App
     *
     * @param context 上下文对象
     */
    public static void startMicroMsgApp(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName component = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
        intent.setComponent(component);
        context.startActivity(intent);
    }

    public static void openApp(Context context, String packageName, String className) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName component = new ComponentName(packageName, className);
        intent.setComponent(component);
        context.startActivity(intent);
    }

    /**
     * 是否开启了推送权限
     */
    public static boolean isEnableNotification(Context content) {
        NotificationManagerCompat notification = NotificationManagerCompat.from(content);
        boolean isEnabled = notification.areNotificationsEnabled();
        return isEnabled;
    }

    /**
     * 跳转到应用的设置
     */
    public static void jumpToAppSettings(Context context) {
        String SETTINGS_ACTION = "android.settings.APPLICATION_DETAILS_SETTINGS";
        try {
            Intent intent = new Intent()
                    .setAction(SETTINGS_ACTION)
                    .setData(Uri.fromParts("package",
                            context.getApplicationContext().getPackageName(), null));
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtil.error("无法跳转到设置,请手动开启消息推送权限");
        }
    }

}
