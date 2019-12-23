package com.xiling.ddui.custom;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.text.Html;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.xiling.MyApplication;

/**
 * created by Jigsaw at 2018/10/20
 */
public class DDImageGetter implements Html.ImageGetter {
    @Override
    public Drawable getDrawable(String source) {

        final LevelListDrawable drawable = new LevelListDrawable();
        Glide.with(MyApplication.getInstance()).load(source).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if (resource != null) {
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(resource);
                    drawable.addLevel(1, 1, bitmapDrawable);
                    drawable.setBounds(0, 0, resource.getWidth(), resource.getHeight());
                    drawable.setLevel(1);
                }
            }
        });
        return drawable;
    }
}
