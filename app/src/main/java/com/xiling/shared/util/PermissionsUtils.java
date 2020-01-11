package com.xiling.shared.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;


import com.blankj.utilcode.utils.DeviceUtils;
import com.xiling.BuildConfig;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/19.
 */
public class PermissionsUtils {
    private final static String HUAWEI = "Huawei";
    private final static String XIAOMI = "Xiaomi";
    private final static String OPPO = "OPPO";
    private final static String VIVO = "vivo";

    public static void goPermissionsSetting(Activity activity) {
        switch (DeviceUtils.getManufacturer()) {
            case XIAOMI:
//                goXiaomiPermission(activity);
                break;
            case OPPO:
//                goOPPOPermission(activity);
                break;
            case VIVO:
                break;
        }
    }

    private static void goXiaomiPermission(Activity activity) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
        activity.startActivity(intent);
    }

    private static void goOPPOPermission(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }
}
