package com.xiling.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

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
                .into(imageView);
    }


    public static void loadImage(Context context, ImageView imageView, String url) {
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
