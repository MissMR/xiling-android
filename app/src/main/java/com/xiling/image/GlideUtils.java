package com.xiling.image;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.xiling.R;

import cn.bumptech.xnglide.load.resource.bitmap.CircleCrop;
import cn.bumptech.xnglide.request.RequestOptions;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class GlideUtils {


    public static void loadHead(Context context, ImageView imageView, String url) {
        RequestOptions mRequestOptions = RequestOptions.circleCropTransform();
        Glide.with(context)
                .load(url)
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .placeholder(R.drawable.bg_image_def)
                .into(imageView);
    }


    /**
     * 清除缓存
     */
    public static void clearImageCache(final Context context) {
        clearImageMemoryCache(context);
        clearImageDiskCache(context);
    }

    /**
     * 清除图片磁盘缓存
     */
    public static void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片内存缓存
     */
    public static void clearImageMemoryCache(Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadImage(Context context, ImageView imageView, String url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageView, int url) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

    public static void loadImageOpenDisk(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }

}
