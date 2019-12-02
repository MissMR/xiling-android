package com.dodomall.ddmall.dduis.dialog;

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
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.manager.PosterMaker;
import com.dodomall.ddmall.ddui.tools.ImageTools;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.shared.util.ShareUtils;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.dodomall.ddmall.shared.util.WebViewUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 浏览器调用 带预览分享 对话框 的结果对话框
 *
 * @see com.dodomall.ddmall.dduis.dialog.DDWebViewShareImageDialog
 */
public class DDWebViewShareImageResponseDialog extends Dialog {

    public enum ShareMode {
        //微信好友
        Wechat_Friend,
        //微信朋友圈
        WeChat_TimeLine,
        //本地磁盘
        Local_Disk
    }

    Activity mActivity = null;

    public DDWebViewShareImageResponseDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
        this.mActivity = (Activity) context;
    }

    public DDWebViewShareImageResponseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDWebViewShareImageResponseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    String filePath = "";

    @BindView(R.id.tv_title)
    TextView titleTextView;

    @BindView(R.id.tv_response)
    TextView responseTextView;

    @OnClick({R.id.iv_close, R.id.layout_top})
    void onClosePressed() {
        dismiss();
    }

    @OnClick(R.id.layout_wx_friend)
    void onShareFriendPressed() {
        if (mode == ShareMode.Wechat_Friend || mode == ShareMode.WeChat_TimeLine) {
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
                    // bugId 2846 fixed.
                    ToastUtil.success("分享成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });
        } else {
            ShareUtils.shareImg(mActivity, new File(filePath), SHARE_MEDIA.WEIXIN, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    dismiss();
                    // bugId 2846 fixed.
                    ToastUtil.success("分享成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });
        }
    }

    @OnClick(R.id.layout_wx_circle)
    void onShareCirclePressed() {
        if (mode == ShareMode.Wechat_Friend || mode == ShareMode.WeChat_TimeLine) {
            ShareUtils.shareImg(mActivity, new File(filePath), SHARE_MEDIA.WEIXIN_CIRCLE, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    dismiss();

                    // bugId 2846 fixed.
                    ToastUtil.success("分享成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });
        } else {
            ShareUtils.shareImg(mActivity, new File(filePath), SHARE_MEDIA.WEIXIN_CIRCLE, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    dismiss();
                    // bugId 2846 fixed.
                    ToastUtil.success("分享成功");
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_web_view_share_image_response);
        initWindow();
        ButterKnife.bind(this);

        bindView();
    }

    public void bindView() {
        if (mode == ShareMode.Wechat_Friend || mode == ShareMode.WeChat_TimeLine) {
            titleTextView.setText("分享成功");
            responseTextView.setText("99%的小伙伴表示:\n分享到3个群成功率更高!");
        } else {
            titleTextView.setText("保存成功");
            responseTextView.setText("继续将推广海报分享至微信吧");
        }

        filePath = PosterMaker.DataRootPath + "/wv_share";
        File thumbFile = new File(filePath);
        if (thumbFile.exists()) {
            thumbFile.delete();
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
        }
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

    DDWebViewShareImageDialog.DDWebViewShareImageData shareData = null;
    private ShareMode mode = ShareMode.Local_Disk;

    public void setShareData(DDWebViewShareImageDialog.DDWebViewShareImageData shareData) {
        this.shareData = shareData;
    }

    public void setMode(ShareMode mode) {
        this.mode = mode;
    }
}
