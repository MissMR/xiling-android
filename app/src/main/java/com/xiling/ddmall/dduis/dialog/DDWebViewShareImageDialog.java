package com.xiling.ddmall.dduis.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.manager.PosterMaker;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.ImageTools;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.xiling.ddmall.shared.util.WebViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 浏览器调用的分享带预览图片的对话框
 */
public class DDWebViewShareImageDialog extends Dialog {

    public static class DDWebViewShareImageData implements Serializable {

        String thumbUrl = "";
        String title = "";
        String desc = "";
        String url = "";
        String base64Data = "";

        public String getThumbUrl() {
            return thumbUrl;
        }

        public void setThumbUrl(String thumbUrl) {
            this.thumbUrl = thumbUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBase64Data() {
            return base64Data;
        }

        public void setBase64Data(String base64Data) {
            this.base64Data = base64Data;
        }
    }

    Activity mActivity = null;

    public DDWebViewShareImageDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
        this.mActivity = (Activity) context;
    }

    public DDWebViewShareImageDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDWebViewShareImageDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    DDWebViewShareImageData shareData = null;

    public void setShareData(DDWebViewShareImageData shareData) {
        this.shareData = shareData;
    }

    @BindView(R.id.sd_thumb)
    SimpleDraweeView thumbView;

    String filePath = "";

    @OnClick({R.id.iv_close, R.id.layout_top, R.id.layout_thumb})
    void onClosePressed() {
        dismiss();
    }

    @OnClick({R.id.sd_thumb})
    void onImagePressed() {
        DLog.i("thumb is pressed.");
    }

    @OnClick(R.id.layout_wx_friend)
    void onShareFriendPressed() {

        String title = "" + shareData.title;
        String desc = "" + shareData.desc;
        String thumbUrl = "" + shareData.thumbUrl;
        String url = "" + WebViewUtil.buildUrl(shareData.url);

        ShareUtils.share(mActivity, title, desc, thumbUrl, url, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                dismiss();

                DDWebViewShareImageResponseDialog dialog = new DDWebViewShareImageResponseDialog(mActivity);
                dialog.setShareData(shareData);
                dialog.setMode(DDWebViewShareImageResponseDialog.ShareMode.Wechat_Friend);
                dialog.show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });

    }

    @OnClick(R.id.layout_wx_circle)
    void onShareCirclePressed() {
        ShareUtils.shareImg(mActivity, new File(filePath), SHARE_MEDIA.WEIXIN_CIRCLE, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                dismiss();

                DDWebViewShareImageResponseDialog dialog = new DDWebViewShareImageResponseDialog(mActivity);
                dialog.setShareData(shareData);
                dialog.setMode(DDWebViewShareImageResponseDialog.ShareMode.WeChat_TimeLine);
                dialog.show();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }

    @OnClick(R.id.layout_save_local)
    void onShareSavePressed() {

        Bitmap bitmap = ImageTools.getSmallBitmap(filePath);
        ImageTools.saveBitmapToAlbum(getContext(), bitmap, "img_" + System.currentTimeMillis());

        DDWebViewShareImageResponseDialog dialog = new DDWebViewShareImageResponseDialog(mActivity);
        dialog.setShareData(shareData);
        dialog.setMode(DDWebViewShareImageResponseDialog.ShareMode.Local_Disk);
        dialog.show();

        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_web_view_share_image);
        initWindow();
        ButterKnife.bind(this);
        if (shareData == null) {
            ToastUtil.error("无效的数据");
            return;
        }
        bindView();
    }

    private void initWindow() {
        Window window = this.getWindow();
        if (window != null) { //设置dialog的布局样式 让其位于底部
            window.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.y = ConvertUtil.dip2px(10); //设置居于底部的距离
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(lp);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public void bindView() {
        filePath = PosterMaker.DataRootPath + "/wv_share";

        File thumbFile = new File(filePath);
        if (thumbFile.exists()) {
            thumbFile.delete();
        }

        File parentFile = thumbFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        try {
            thumbFile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String imageData = shareData.base64Data;
        if (!TextUtils.isEmpty(imageData)) {
            String target = ";base64,";
//            imageData = imageData.replace("data:image/jpeg;base64,", "");
            if (imageData.contains(target)) {
                String[] split = imageData.split(target);
                if (split.length > 0) {
                    imageData = split[split.length - 1];
                }
            }

            Bitmap thumbBitmap = ImageTools.base64ToBitmap(imageData);
            ImageTools.saveBitmapToSD(thumbBitmap, filePath, 98, false);

            int screenWidth = (int) (UITools.getScreenWidth(getContext()) * 0.75);
            UITools.setImageWithAutoHeight(thumbView, "file://" + filePath, screenWidth, getContext());
        }
    }


}
