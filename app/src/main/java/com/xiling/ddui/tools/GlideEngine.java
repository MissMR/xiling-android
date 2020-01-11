package com.xiling.ddui.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huantansheng.easyphotos.engine.ImageEngine;
import com.xiling.image.GlideUtils;

public class GlideEngine implements ImageEngine {
    //单例
    private static GlideEngine instance = null;
    //单例模式，私有构造方法
    private GlideEngine() {
    }
    //获取单例
    public static GlideEngine getInstance() {
        if (null == instance) {
            synchronized (GlideEngine.class) {
                if (null == instance) {
                    instance = new GlideEngine();
                }
            }
        }
        return instance;
    }


    @Override
    public void loadPhoto(Context context, String photoPath, ImageView imageView) {
        GlideUtils.loadImage(context,imageView,photoPath);
    }

    @Override
    public void loadGifAsBitmap(Context context, String gifPath, ImageView imageView) {

    }

    @Override
    public void loadGif(Context context, String gifPath, ImageView imageView) {

    }

    @Override
    public Bitmap getCacheBitmap(Context context, String path, int width, int height) throws Exception {
        return null;
    }
}
