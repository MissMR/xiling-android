package com.xiling.shared.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xiling.R;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.manager.PosterMaker;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.dialog.DDSaveImageResponseDialog;
import com.xiling.image.GlideUtils;
import com.xiling.shared.util.CommonUtil;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class XLProductQrCodeDialog extends Dialog {

    Activity mActivity;
    ProductNewBean productNewBean;
    @BindView(R.id.img_thumb)
    ImageView imgThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_qr)
    ImageView ivQr;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_market_price)
    TextView tvMarketPrice;
    @BindView(R.id.RelScreen)
    RelativeLayout relScreen;

    private String sharedTitle = "";
    private String sharedDes = "描述内容待定";
    private String sharedThumb = "";
    private String sharedUrl = "";
    Unbinder unbinder;
    public XLProductQrCodeDialog(@NonNull Context context) {
        super(context, R.style.WXShareDialog);
    }

    public XLProductQrCodeDialog(Activity actiity, ProductNewBean productNewBean) {
        super(actiity);
        this.mActivity = actiity;
        this.productNewBean = productNewBean;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prodecu_qr_xl);
        unbinder =  ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        initSharedData();
        initView();
    }

    private void initView() {
        tvMarketPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        if (productNewBean != null) {
            if (productNewBean.getImages() != null && productNewBean.getImages().size() > 0) {
                GlideUtils.loadImage(mActivity, imgThumb, productNewBean.getImages().get(0));
            }

            if (!TextUtils.isEmpty(productNewBean.getProductName())) {
                tvTitle.setText(productNewBean.getProductName());
            }

            tvPrice.setText("¥ " + NumberHandler.reservedDecimalFor2(productNewBean.getMinPrice()));
            tvMarketPrice.setText("¥ " + NumberHandler.reservedDecimalFor2(productNewBean.getMinMarketPrice()));
        }
        createQRImage(sharedUrl);
    }

    private void initSharedData() {
        if (productNewBean != null) {
            sharedTitle = productNewBean.getProductName();
            if (productNewBean.getImages() != null && productNewBean.getImages().size() > 0) {
                sharedThumb = productNewBean.getImages().get(0);
            }
            sharedUrl = productNewBean.getThumbUrl();
        }

    }


    @OnClick({R.id.llShareWechat, R.id.llShareWechatCircle, R.id.llShareDisk, R.id.tv_btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llShareWechat:
                ShareUtils.share(mActivity, sharedTitle, sharedDes, sharedThumb, sharedUrl, new UMShareListener() {
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
                        ToastUtil.success("分享失败");
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                });
                dismiss();
                break;
            case R.id.llShareWechatCircle:
                shareTo3rdPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                dismiss();
                break;
            case R.id.llShareDisk:
                DLog.i("onShareDiskPressed");
                ToastUtil.showLoading(getContext());
                makeScreenShot(new DDMProductQrCodeDialog.ScreenShotListener() {
                    @Override
                    public void onScreenShotEnd(Bitmap bitmap) {
                        String name = "" + System.currentTimeMillis();
                        if (productNewBean != null) {
                            name = productNewBean.getProductId();
                        }

                        ImageTools.saveBitmapToAlbum(getContext(), bitmap, "" + name);

                        String pathToFile = PosterMaker.getPosterFile("" + name);
                        DLog.i("saveBitmapToSD：" + pathToFile);
                        ImageTools.saveBitmapToSD(bitmap, pathToFile, 80, false);

                        ToastUtil.hideLoading();


                        dismiss();

                        DDSaveImageResponseDialog dialog = new DDSaveImageResponseDialog(mActivity);
                        dialog.setPath("" + pathToFile);
                        dialog.show();
                    }
                });


                break;
            case R.id.tv_btn_cancel:
                dismiss();
                break;
        }
    }


    /**
     * 执行截屏
     */
    public void makeScreenShot(DDMProductQrCodeDialog.ScreenShotListener listener) {
        DLog.i("makeScreenShot");
        Bitmap bitmap = getViewBitmap(relScreen);
        if (listener != null) {
            if (bitmap != null) {
                listener.onScreenShotEnd(bitmap);
            } else {
                ToastUtil.error("生成文件失败");
//                listener.onScreenShotError();
            }
        }
    }

    private Bitmap getViewBitmap(View v) {
        DLog.i("getViewBitmap");
        ToastUtil.showLoading(getContext());
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

    void shareTo3rdPlatform(final SHARE_MEDIA way) {
        DLog.i("shareTo3rdPlatform：" + way);
        makeScreenShot(new DDMProductQrCodeDialog.ScreenShotListener() {
            @Override
            public void onScreenShotEnd(Bitmap bitmap) {
                String name = "" + System.currentTimeMillis();
                if (productNewBean != null) {
                    name = productNewBean.getProductId();
                }

                String pathToFile = PosterMaker.getPosterFile("" + name);
                File file = new File(pathToFile);

                DLog.i("saveBitmapToSD：" + pathToFile);
                ImageTools.saveBitmapToSD(bitmap, pathToFile, 80, false);

                ToastUtil.hideLoading();
                ShareUtils.shareImg(mActivity, file, way, new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {
                        DLog.d("share image onStart：" + share_media);
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        DLog.d("share image onResult：" + share_media);

                        if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            ToastUtil.success("分享成功");
                        }

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
        });
    }


    /**
     * 生成商品二维码图片
     */
    private void createQRImage(final String url) {
        ToastUtil.showLoading(getContext());
         new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(getContext(), 100));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ToastUtil.hideLoading();
                if (bitmap != null) {
                    ivQr.setImageBitmap(bitmap);
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        }.execute();
    }

}
