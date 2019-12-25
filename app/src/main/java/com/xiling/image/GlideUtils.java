package com.xiling.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class GlideUtils {

    public static void loadImage(Context context,ImageView imageView, String url){
        Glide.with(context).load(url).into(imageView);
    }

}
