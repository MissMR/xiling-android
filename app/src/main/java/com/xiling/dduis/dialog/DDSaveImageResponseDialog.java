package com.xiling.dduis.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.xiling.R;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDSaveImageResponseDialog extends Dialog {

    Activity activity = null;

    public DDSaveImageResponseDialog(@NonNull Activity context) {
        super(context, R.style.WXShareDialog);
        this.activity = context;
    }

    public DDSaveImageResponseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDSaveImageResponseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @OnClick({R.id.iv_close, R.id.layout_empty})
    void onClosePressed() {
        dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_image_response);
        ButterKnife.bind(this);
        initWindow();
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

    @OnClick(R.id.layout_share_friend)
    void onShareFriendPressed() {
        File img = new File(path);
        ShareUtils.shareImg(activity, img, SHARE_MEDIA.WEIXIN, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtil.success("分享成功");
                dismiss();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }

    @OnClick(R.id.layout_share_circle)
    void onShareCirclePressed() {
        File img = new File(path);
        ShareUtils.shareImg(activity, img, SHARE_MEDIA.WEIXIN_CIRCLE, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                DLog.d("share image onStart：" + share_media);

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                DLog.d("share image onResult：" + share_media);
                ToastUtil.success("分享成功");
                dismiss();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                DLog.d("share image onError：" + share_media);
                ToastUtil.error("分享失败");

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                DLog.d("share image onCancel：" + share_media);
                ToastUtil.error("分享已取消");

            }
        });
    }

    private String path = "";

    public void setPath(String path) {
        this.path = path;
    }


}
