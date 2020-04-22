package com.xiling.ddui.tools;

import android.app.Activity;
import android.content.Context;

import com.huantansheng.easyphotos.EasyPhotos;

/**
 * @author 逄涛
 * 相册与拍照
 */
public class PhotoTools {
    public static void openAlbum(Activity activity, int requestCode) {
        EasyPhotos.createAlbum(activity, true, GlideEngine.getInstance())
                .setFileProviderAuthority("com.xiling.fileProvider")
                .setPuzzleMenu(false)
                .start(requestCode);
    }

    public static void photogaph(Activity activity, int requestCode) {
        EasyPhotos.createCamera(activity)
                .setFileProviderAuthority("com.xiling.fileProvider")
                .start(requestCode);
    }
}
