package com.xiling.dduis.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

public class AvatarDemoMaker {


    public static final int RES_SIZE = 30;
    public static final String PATH_HEADER = "asset:///";
    public static final String PATH_ASSET = PATH_HEADER + "avatar/";
    public static final String PATH_ASSET_MORE = PATH_ASSET + "fake_avatar_more.png";

    /**
     * 生成指定size图片数，若size>maxSize，第一张图片换位三个点“更多”
     *
     * @param size    生成图片数
     * @param maxSize 最大生成图片数
     * @return
     */
    public static String[] randomAvatarWithFirstMoreImage(int size, int maxSize) {
        String[] result = randomAvatar(size > maxSize ? maxSize : size);
        if (size > 1 && size >= maxSize) {
            // 第一项改为三个点
            result[0] = PATH_ASSET_MORE;
        }
        return result;
    }

    public static String[] randomAvatar(int size) {
        size = size > RES_SIZE ? RES_SIZE : size;
        HashMap<String, String> response = new HashMap<>();
        for (; response.size() < size; ) {
            int s = new Random().nextInt(RES_SIZE);
            String path = PATH_ASSET + "fake_avatar_" + s + ".png";
            response.put("" + s, path);
        }
        return response.values().toArray(new String[0]);
    }

    /**
     * 将所有的SimpleDraweeView都设置上图片
     */
    public static void setDemoAvatar(SimpleDraweeView... draweeViews) {
        int size = draweeViews.length;
        String[] res = randomAvatar(size);
        for (int i = 0; i < size; i++) {
            SimpleDraweeView view = draweeViews[i];
            String url = res[i % RES_SIZE];
            view.setImageURI("" + url);
        }
    }

    public static void setVisibilitys(View[] views, int size, int maxLength) {
        int viewSize = views.length;
        if (maxLength > viewSize) {
            maxLength = viewSize;
        }
        if (size < maxLength) {
            maxLength = size;
        }
        for (int i = 0; i < viewSize; i++) {
            View v = views[i];
            int index = viewSize - i - 1;
            if (index > -1 && i < maxLength) {
                v.setVisibility(View.VISIBLE);
            } else {
                v.setVisibility(View.GONE);
            }
        }
    }

    public static void setDemoAvatarDrawable(ImageView... imageViews) {
        int size = imageViews.length;
        String[] res = randomAvatar(size);
        for (int i = 0; i < size; i++) {
            ImageView view = imageViews[i];
            String url = res[i % RES_SIZE];
            url = url.replace(PATH_HEADER, "");
            Context context = view.getContext();
            view.setBackground(loadImageFromAsserts(context, url));
        }
    }


    /**
     * 从assets 文件夹中读取图片
     */
    public static Drawable loadImageFromAsserts(final Context ctx, String fileName) {
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            //解流到bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            //圆角
            BitmapDrawable bd = new BitmapDrawable(ctx.getResources(), getCircleImage(bitmap));
            return bd;
        } catch (IOException e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (OutOfMemoryError e) {
            if (e != null) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            if (e != null) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 获取一个圆形图片
     */
    public static Bitmap getCircleImage(Bitmap bitmap) {

        int size = bitmap.getWidth();
        int radius = size / 2;

        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        Bitmap target = Bitmap.createBitmap(size, size, bitmap.getConfig());
        /**
         * 产生一个同样大小的画布
         */
        Canvas canvas = new Canvas(target);
        /**
         * 首先绘制圆形
         */
        canvas.drawCircle(radius, radius, radius, paint);
        /**
         * 使用SRC_IN
         */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        /**
         * 给Canvas加上抗锯齿标志
         */
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        /**
         * 绘制图片
         */
        canvas.drawBitmap(bitmap, 0, 0, paint);

        //回收资源
        try {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (Exception e) {

        }

        return target;
    }

}
