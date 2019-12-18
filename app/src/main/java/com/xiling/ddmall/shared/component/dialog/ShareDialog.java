package com.xiling.ddmall.shared.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.ShareObject;
import com.xiling.ddmall.shared.util.CommonUtil;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-07-05
 */
public class ShareDialog extends Dialog {

    private ShareObject mShareObject;
    private Activity mActivity;
    private UMWeb mWeb;
    private UMShareListener mUmShareListener;
    private File mFile;

    public ShareDialog(Activity activity, String title, String desc, String logoUrl, String linke, UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mWeb = new UMWeb(linke);
        mUmShareListener = umShareListener;
        mWeb.setTitle(title);
        mWeb.setDescription(desc);
        mWeb.setThumb(new UMImage(activity, logoUrl));
    }

    public ShareDialog(Activity activity, File imgFile,UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mFile = imgFile;
        mUmShareListener = umShareListener;
    }

    private ShareDialog(Context context) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
    }

    @OnClick(R.id.shareCircleBtn)
    protected void shareToCircle() {
        if (mFile != null) {
            ShareUtils.shareImg(mActivity,mFile,SHARE_MEDIA.WEIXIN_CIRCLE,mUmShareListener);
        } else {
            new ShareAction(mActivity)
                    .setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                    .withMedia(mWeb)
                    .setCallback(mUmShareListener)
                    .share();
        }
        dismiss();
    }

    @OnClick(R.id.shareFriendBtn)
    protected void shareToFriend() {
        if (mFile!=null) {
            ShareUtils.shareImg(mActivity,mFile,SHARE_MEDIA.WEIXIN,mUmShareListener);
        }else {
            new ShareAction(mActivity)
                    .setPlatform(SHARE_MEDIA.WEIXIN)
                    .withMedia(mWeb)
                    .setCallback(mUmShareListener)
                    .share();
        }
        dismiss();
    }

    @OnClick(R.id.cancelBtn)
    protected void onClose() {
        dismiss();
    }

}
