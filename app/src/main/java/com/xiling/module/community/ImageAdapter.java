package com.xiling.module.community;


import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class ImageAdapter {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        loadImage(view,url,null,null);
    }

    @BindingAdapter({"imageUrl", "placeHolder"})
    public static void loadImage(ImageView view, String url,Drawable placeHolder) {
        loadImage(view,url,null,placeHolder);
    }

    @BindingAdapter({"imageUrl", "error", "placeHolder"})
    public static void loadImage(ImageView view, String url, Drawable error, Drawable placeHolder) {
        if (TextUtils.isEmpty(url)) {
            if (error != null) {
                view.setImageDrawable(error);
            } else if (placeHolder != null) {
                view.setImageDrawable(placeHolder);
            } else {
//                view.setImageDrawable(d);
            }

            return;
        }

        RequestCreator creator = Picasso.with(view.getContext()).load(url);
        if (error != null) {
            creator.error(error);
        } else {
            //set default error image
//            creator.placeholder(R.drawable.img_avatar);
        }

        if (placeHolder != null) {
            creator.placeholder(placeHolder);
        } else {
            //set default placeholder
//            creator.placeholder(R.drawable.img_avatar);
        }

        creator.fit().centerCrop().into(view);
    }

    @BindingAdapter({"preview"})
    public static void preview(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    /*@BindingAdapter({"bind:fsImageUrl"})
    public static void loadFixedDimenImage(ImageView view, String url, int widthDp, int heightDP) {
        float density = view.getContext().getResources().getDisplayMetrics().density;
        int width = view.getResources().getDisplayMetrics().widthPixels;
        if (widthDp != -1) {
            width = (int) (widthDp * density);
        }

        int height = (int) (heightDP * density);
        Picasso.with(view.getContext()).load(url).resize(width,height).centerCrop().into(view);
    }*/

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource){
        imageView.setImageResource(resource);
    }
}
