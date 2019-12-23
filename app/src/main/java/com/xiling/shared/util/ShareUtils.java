package com.xiling.shared.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.shared.component.dialog.DDMShareDialog;
import com.xiling.shared.component.dialog.ShareDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/8.
 */
public class ShareUtils {

    public static void showDDMShareDialog(final Activity activity, DDMShareDialog.Share shareObject) {
        if (StringUtils.isEmpty(shareObject.getDesc())) {
            shareObject.setDesc("    ");
        }
        DDMShareDialog dialog = new DDMShareDialog(activity, shareObject, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.e("onStart   ");
                ToastUtil.showLoading(activity);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                LogUtils.e("onResult   ");
                ToastUtil.hideLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                LogUtils.e("onError   ");
                ToastUtil.hideLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                LogUtils.e("onCancel   ");
                ToastUtil.hideLoading();
            }

        });
        dialog.show();
    }

    public static void showShareDialog(final Activity activity, String title, String desc, String logoUrl, String linke) {
        if (StringUtils.isEmpty(desc)) {
            desc = "     ";
        }
        new ShareDialog(activity, title, desc, logoUrl, linke, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                LogUtils.e("onStart   ");

                ToastUtil.showLoading(activity);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                LogUtils.e("onResult   ");

                ToastUtil.hideLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                LogUtils.e("onError   ");

                ToastUtil.hideLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                LogUtils.e("onCancel   ");

                ToastUtil.hideLoading();
            }

        }).show();
    }

    public static void share(Activity activity, String title, String desc, String logoUrl, String link) {
        share(activity, title, desc, logoUrl, link, null);
    }

    public static void share(Activity activity, String title, String desc, String logoUrl, String link, final UMShareListener listener) {
        UMWeb web = new UMWeb(link);
        web.setTitle(title);
        web.setDescription(desc);
        web.setThumb(new UMImage(activity, logoUrl));
//        showLoading();
        new ShareAction(activity).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                Log.e("err", "start");
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                Log.e("err", "result");
//                hideLoading();
                if (listener != null) {
                    listener.onResult(share_media);
                }
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                Log.e("err", "err" + throwable.getMessage());
//                hideLoading();
                if (listener != null) {
                    listener.onError(share_media, throwable);
                }
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
//                hideLoading();
                if (listener != null) {
                    listener.onCancel(share_media);
                }
            }
        }).withMedia(web).share();
    }

    /**
     * 分享图片到指定平台
     */
    public static void shareImg(Activity activity, File file, SHARE_MEDIA media, UMShareListener umShareListener) {
        UMImage image = new UMImage(activity, file);

        //添加压缩方式
//        image.compressStyle = UMImage.CompressStyle.QUALITY;

        //增加缩略图
        Bitmap bitmap = ImageTools.getSmallBitmap(file.getPath());
        UMImage thumb = new UMImage(activity, bitmap);
        image.setThumb(thumb);

        new ShareAction(activity).setPlatform(media).setCallback(umShareListener).withMedia(image).share();
    }

    /**
     * 分享网络图片到指定平台
     *
     * @param activity        界面
     * @param imgUrl          图片地址
     * @param media           分享渠道
     * @param umShareListener 回调事件
     */
    public static void shareWebImg(Activity activity, String imgUrl, SHARE_MEDIA media, UMShareListener umShareListener) {
        UMImage image = new UMImage(activity, imgUrl);
//        UMImage thumb = new UMImage(activity, imgUrl);
//        image.setThumb(thumb);
        image.setThumb(image);
        new ShareAction(activity).setPlatform(media).setCallback(umShareListener).withMedia(image).share();
    }
}
