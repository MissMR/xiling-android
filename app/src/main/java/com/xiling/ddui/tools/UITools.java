package com.xiling.ddui.tools;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.ImageInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class UITools {

    public static void clearViewPagerCache(FragmentManager fragmentManager, ViewPager viewPager) {
        if (viewPager.getAdapter() != null) {
            //获取FragmentManager实现类的class对象,这里指的就是FragmentManagerImpl
            Class<? extends FragmentManager> aClass = fragmentManager.getClass();
            try {
                //1.获取其mAdded字段
                Field f = aClass.getDeclaredField("mAdded");
                f.setAccessible(true);
                //强转成ArrayList
                ArrayList<Fragment> list = (ArrayList) f.get(fragmentManager);
                //清空缓存
                list.clear();

                //2.获取mActive字段
                f = aClass.getDeclaredField("mActive");
                f.setAccessible(true);
                //强转成SparseArray
                SparseArray<Fragment> array = (SparseArray) f.get(fragmentManager);
                //清空缓存
                array.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /***
     *  通过imageWidth 的宽度，自动适应高度
     *
     *  @param simpleDraweeView view
     *  @param imagePath
     *  @param imageWidth width
     *  @param mContext context
     *  */
    public static void setImageWithAutoHeight(final SimpleDraweeView simpleDraweeView, String imagePath, final int imageWidth, Context mContext) {

        Uri uri = Uri.parse(imagePath);

        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, @javax.annotation.Nullable ImageInfo imageInfo, @javax.annotation.Nullable Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);

                if (imageInfo == null) {
                    return;
                }

                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();

//                int viewWidth = width > imageWidth ? imageWidth : width;
//                int viewHeight = width > imageWidth ? (int) ((float) (imageWidth * height) / (float) width) : height;

                int viewWidth = imageWidth;
                int viewHeight = (int) ((float) (imageWidth * height) / (float) width);

                ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
                layoutParams.width = viewWidth;
                layoutParams.height = viewHeight;
                simpleDraweeView.setLayoutParams(layoutParams);

                simpleDraweeView.setPadding(0, 0, 0, 0);

                simpleDraweeView.setMinimumWidth(viewWidth);
                simpleDraweeView.setMinimumHeight(viewHeight);

                simpleDraweeView.setMaxWidth(viewWidth);
                simpleDraweeView.setMaxHeight(viewHeight);

                simpleDraweeView.setTop(0);
                simpleDraweeView.setBottom(0);


                simpleDraweeView.invalidate();
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                throwable.printStackTrace();
            }
        }).setUri(uri).setAutoPlayAnimations(true).build();
        clearImageCache(imagePath);
        simpleDraweeView.setController(controller);
    }

    public static void clearImageCache(String imagePath) {
        Uri uri = Uri.parse(imagePath);
        // 清除Fresco缓存
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        imagePipeline.evictFromMemoryCache(uri);
        imagePipeline.evictFromDiskCache(uri);
        imagePipeline.evictFromCache(uri);
    }


    public static void setGifUrl(SimpleDraweeView sdView, String url) {
        Uri uri = Uri.parse(url);
        DLog.d("setGifUrl:" + uri);
        DraweeController mGifController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        sdView.setController(mGifController);
    }

    public static void addTextViewDeleteLine(TextView textView) {
        if (textView == null) return;
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    /**
     * 递归查找父控件下的所有TextView
     *
     * @param rootView 父级控件 必须是ViewGroup
     */
    public static ArrayList<TextView> findAllChildTextView(ViewGroup rootView) {
        ArrayList<TextView> views = new ArrayList<>();
        if (rootView != null) {
            int childCount = rootView.getChildCount();
            if (childCount > 0) {
                for (int i = 0; i < childCount; i++) {
                    View view = rootView.getChildAt(i);
                    if (view != null) {
                        if (view instanceof ViewGroup) {
                            views.addAll(findAllChildTextView((ViewGroup) view));
                        } else if (view instanceof TextView) {
                            views.add((TextView) view);
                        }
                    }
                }
            }
        }
        return views;
    }

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    /**
     * 跳转系统浏览器
     *
     * @param context 上下文
     * @param url     要跳转的地址
     */
    public static void jumpSystemBrowser(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 高斯模糊一个图片并设置到指定的View
     */
    public static void blur(Context context, Bitmap bkg, ImageView view, int radius) {
        if (bkg != null) {
            int scaleFactor = 8;
            Bitmap overlay = Bitmap.createScaledBitmap(bkg, bkg.getWidth() / scaleFactor, bkg.getHeight() / scaleFactor, false);
            Blurry.with(context).radius(radius).from(overlay).into(view);
        }
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
//        int screenHeight = dm.heightPixels;
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
//        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        return screenHeight;
    }

    public static Bitmap getViewBitmap(View v) {
        DLog.i("getViewBitmap");
        ToastUtil.showLoading(v.getContext());
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(Color.WHITE);
        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);
        return bitmap;
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

}
