package com.xiling.shared.util;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.SharedElementCallback;
import android.text.TextUtils;
import android.view.View;

import com.xiling.ddui.activity.DDPhonePreviewActivity;
import com.xiling.ddui.tools.BGANinePhotoLayoutUtils;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-06-14
 */
public class ImageUtil {

    public static void previewImage(Context context, ArrayList<String> images, int position) {
        if (images.size() == 1) {
            context.startActivity(BGAPhotoPreviewActivity.newIntent(context, null, images.get(0)));
        } else if (images.size() > 1) {
            context.startActivity(BGAPhotoPreviewActivity.newIntent(context, null, images, position));
        }
    }

    /**
     * 九宫格控件共享元素
     *
     * @param context
     * @param ninePhotoLayout
     * @param images
     * @param position
     */
    public static void previewImageWithTransition(Context context, final BGANinePhotoLayout ninePhotoLayout, ArrayList<String> images, int position) {
        List<View> list = BGANinePhotoLayoutUtils.getItemViews(ninePhotoLayout);
        if (list != null) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    list.get(position), "share");
            context.startActivity(DDPhonePreviewActivity.newIntent(context, null, images, position), optionsCompat.toBundle());
            if (context instanceof BaseActivity) {
                final BaseActivity baseActivity = (BaseActivity) context;
                ActivityCompat.setExitSharedElementCallback((Activity) context, new SharedElementCallback() {
                    @Override
                    public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                        if (baseActivity.mReenterData != null) {
                            int index = baseActivity.mReenterData.getInt("index", 0);
                            DLog.i("reenter index : " + index);
                            View newSharedElement = BGANinePhotoLayoutUtils.getItemViews(ninePhotoLayout).get(index);
                            if (newSharedElement != null) {
                                names.clear();
                                names.add("share");
                                sharedElements.clear();
                                sharedElements.put("share", newSharedElement);
                            }
                            baseActivity.mReenterData = null;
                        }
                    }
                });
            }
        }
    }

    /**
     * 普通view共享元素
     *
     * @param context
     * @param shareView
     * @param url
     */
    public static void previewImageWithTransition(Context context, View shareView, String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        ActivityCompat.setExitSharedElementCallback((Activity) context, null);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                shareView, "share");
        context.startActivity(DDPhonePreviewActivity.newIntent(context, null, url), optionsCompat.toBundle());

        fixNotDisplayBug((Activity) context);

    }

    // 修复fresco结合共享元素过度动画 偶尔不显示图片内容的bug
    private static void fixNotDisplayBug(Activity context) {
        context.setExitSharedElementCallback(new android.app.SharedElementCallback() {
            @Override
            public void onSharedElementEnd(List<String> sharedElementNames,
                                           List<View> sharedElements,
                                           List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements,
                        sharedElementSnapshots);
                for (View view : sharedElements) {
                    if (view instanceof SimpleDraweeView) {
                        view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
