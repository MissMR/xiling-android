package com.dodomall.ddmall.module.product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.FileUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.dialog.ShareDialog;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/10/16 下午5:39.
 */
public class ProductQrcodeShowActivity extends BaseActivity {

    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.tvTitle)
    TextView mTvTitle;
    @BindView(R.id.ivImg)
    SimpleDraweeView mIvImg;
    @BindView(R.id.ivQrCode)
    ImageView mIvQrCode;
    @BindView(R.id.tvProductName)
    TextView mTvProductName;
    @BindView(R.id.tvProducePrice)
    TextView mTvProducePrice;
    @BindView(R.id.layoutQrCode)
    RelativeLayout mLayoutQrCode;
    private String mImgUrl;
    private String mLinkUrl;
    private String mSkuName;
    private long mPrice;
    private AsyncTask<Void, Void, Bitmap> mExecute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_qrcode_show);
        ButterKnife.bind(this);

        getIntentData();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ToastUtil.hideLoading();
        mExecute.cancel(true);
    }

    private void initData() {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        mTvTitle.setText(String.format("%s  为您推荐", loginUser.nickname));
        FrescoUtil.setImageSmall(mIvAvatar, loginUser.avatar);
        FrescoUtil.setImage(mIvImg, mImgUrl);
        mTvProductName.setText(mSkuName);
        mTvProducePrice.setText(ConvertUtil.centToCurrency(this, mPrice));

        createEnglishQRCodeWithLogo();
    }

    private void initView() {
        setTitle("产品二维码");
        setLeftBlack();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mImgUrl = intent.getStringExtra("imgUrl");
        mLinkUrl = intent.getStringExtra("linkUrl");
        mSkuName = intent.getStringExtra("skuName");
        mPrice = intent.getLongExtra("price", 0);
    }

    @OnClick(R.id.tvSave)
    public void onMTvSaveClicked() {
        ToastUtil.showLoading(this);
        Bitmap viewBitmap = getViewBitmap(mLayoutQrCode);
        File savedImageFile = getSaveFile();
        saveBitmapFile(viewBitmap, savedImageFile, true);
        ToastUtil.hideLoading();
    }

    @OnClick(R.id.tvShare)
    public void onMTvShareClicked() {
        ToastUtil.showLoading(this);
        Bitmap viewBitmap = getViewBitmap(mLayoutQrCode);
        File savedImageFile = getSaveFile();
        saveBitmapFile(viewBitmap, savedImageFile, false);
        Luban.with(this)
                .load(savedImageFile)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                        LogUtils.e("压缩图片开始");
                    }

                    @Override
                    public void onSuccess(File file) {
                        ToastUtil.hideLoading();
                        showShareDialog(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error("压缩图片出错:" + e.getMessage());
                        ToastUtil.hideLoading();
                    }
                }).launch();
    }

    private void showShareDialog(File file) {
        new ShareDialog(this, file, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {
                ToastUtil.showLoading(ProductQrcodeShowActivity.this);
            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtil.success("分享成功");
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

    private File getSaveFile() {
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath()
                + "/"+ AppUtils.getAppName(this);
        File file = new File(dirPath, System.currentTimeMillis() + ".jpg");
        FileUtils.createOrExistsDir(dirPath);
        return file;
    }

    public void saveBitmapFile(Bitmap bitmap, final File saveFile, boolean isShowMsg) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            if (isShowMsg) {
//                Snackbar.make(mLayoutQrCode, "保存图片成功", Snackbar.LENGTH_LONG).setAction("立即查看", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        viewImage(saveFile);
////                        tv.setText("这里可以自己改成想要执行的任务。");
//                    }
//                }).show();
                ToastUtil.success("保存成功");
            }
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(saveFile));
            sendBroadcast(intent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查看图片
     * @param saveFile
     */
    private void viewImage(File saveFile) {

//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(Uri.fromFile(saveFile), "image/*");
//        startActivity(intent);
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

    private void createEnglishQRCodeWithLogo() {
        ToastUtil.showLoading(this);
        mExecute = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(mLinkUrl, BGAQRCodeUtil.dp2px(ProductQrcodeShowActivity.this, 100));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ToastUtil.hideLoading();
                if (bitmap != null) {
                    mIvQrCode.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(ProductQrcodeShowActivity.this, "生成二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}


