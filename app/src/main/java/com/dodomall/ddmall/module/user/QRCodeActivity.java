package com.dodomall.ddmall.module.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.service.HtmlService;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.InvitationCode;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.dialog.ShareDialog;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IInviteService;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ShareUtilsNew;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.user
 * @since 2017-08-03
 * 邀请有奖
 */
public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.rl_root)
    RelativeLayout mRlRoot;
    @BindView(R.id.qrCodeIv)
    protected SimpleDraweeView mQrCodeIv;
    @BindView(R.id.tvCopy)
    ImageView mTvCopy;
    @BindView(R.id.tvShare)
    TextView mTvShare;
    @BindView(R.id.tvinviteCode)
    TextView mTvinviteCode;
    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.ivBuyStore)
    ImageView mIvBuyStore;
    @BindView(R.id.layoutQrCode)
    CardView mLayoutQrCode;
    @BindView(R.id.layoutStore)
    LinearLayout mLayoutStore;
    @BindView(R.id.layoutVip)
    LinearLayout mLayoutVip;

    private InvitationCode mInvitationCode;
    private boolean isStoreMaster = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        showHeader();
        setTitle("邀请有奖");
        setLeftBlack();
        initData();
    }

    private void initData() {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        if (loginUser == null) {
            return;
        }
        if (loginUser.vipType > 0) {

            isStoreMaster = true;

            mLayoutStore.setVisibility(View.VISIBLE);
            mLayoutVip.setVisibility(View.GONE);

            FrescoUtil.setImageSmall(mIvAvatar, loginUser.avatar);
            IInviteService inviteService = ServiceManager.getInstance().createService(IInviteService.class);
            APIManager.startRequest(inviteService.getInvitationCode(), new BaseRequestListener<InvitationCode>(this) {
                @Override
                public void onSuccess(InvitationCode result) {
                    mInvitationCode = result;
                    FrescoUtil.setImage(mQrCodeIv, mInvitationCode.imgUrl);
                    mTvinviteCode.setText(result.inviteCode);
                }
            });
        } else {
            mLayoutStore.setVisibility(View.GONE);
            mLayoutVip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
    }

    @OnClick(R.id.qrCodeIv)
    protected void preview() {
        if (mInvitationCode == null) {
            return;
        }
//        ArrayList<String> images = Lists.newArrayList(mInvitationCode.imgUrl);
        File saveFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        startActivity(BGAPhotoPreviewActivity.newIntent(this, saveFile, mInvitationCode.imgUrl));
//        ImageUtil.previewImage(this, images, 0);
    }

    @OnClick(R.id.tvCopy)
    public void onMTvinviteCodeClicked() {
        ShareUtilsNew.clipData(this, mInvitationCode.inviteCode);
    }

    @OnLongClick(R.id.rl_root)
    public boolean onSaveImage() {
        if (!isStoreMaster) {
            return true;
        }
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("确认保存图片到本地？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Bitmap viewBitmap = getViewBitmap(mRlRoot);
                        File savedImageFile = getSaveFile();
                        saveBitmapFile(viewBitmap, savedImageFile, true);
                    }
                }).create();
        dialog.show();
        return true;
    }

    @OnClick(R.id.ivBuyStore)
    public void onViewClicked() {
        HtmlService.startBecomeStoreMasterActivity(this);
    }

    private void showShareDialog(File file) {
        new ShareDialog(this, file, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtil.showLoading(QRCodeActivity.this);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtil.hideLoading();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                ToastUtil.error("分享出错:" + throwable.getMessage());
                ToastUtil.hideLoading();
            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {
                ToastUtil.hideLoading();
            }
        }).show();
    }

    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);
        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);
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

    public void saveBitmapFile(Bitmap bitmap, final File saveFile, boolean isShowMsg) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            if (isShowMsg) {
                ToastUtil.success("保存成功");
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(saveFile));
            sendBroadcast(intent);
        } catch (IOException e) {
            ToastUtil.error("保存失败");
            e.printStackTrace();
        }
    }

    private File getSaveFile() {
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                + "/" + AppUtils.getAppName(this);
        File file = new File(dirPath, System.currentTimeMillis() + ".jpg");
        FileUtils.createOrExistsDir(dirPath);
        return file;
    }


}
