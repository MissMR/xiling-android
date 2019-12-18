package com.xiling.ddmall.shared.component.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.manager.PosterMaker;
import com.xiling.ddmall.ddui.manager.ShortUrlMaker;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.ImageTools;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.dduis.bean.DDProductBean;
import com.xiling.ddmall.dduis.bean.DDRushSpuBean;
import com.xiling.ddmall.dduis.dialog.DDSaveImageResponseDialog;
import com.xiling.ddmall.shared.bean.Product;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.util.CommonUtil;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.SpanUtils;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * @author Jigsaw
 * @date 2018/9/13
 * 商品二维码展示
 */
public class DDMProductQrCodeDialog extends Dialog {
    @BindView(R.id.llScreen)
    LinearLayout llScreen;

    @BindView(R.id.avatar)
    SimpleDraweeView sdAvatar;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.ivProductImage)
    SimpleDraweeView sdProductImage;

    @BindView(R.id.tvProductTitle)
    TextView tvProductTitle;

    @BindView(R.id.tvPrice)
    TextView tvPrice;

    @BindView(R.id.tvMarketPrice)
    TextView tvMarketPrice;

    @BindView(R.id.iv_qr_code)
    ImageView ivQRCode;

    @BindView(R.id.iv_product_header)
    ImageView mIvProductHeader;

    @BindView(R.id.tv_reward)
    TextView mTvReward;
    @BindView(R.id.tv_reward_desc)
    TextView tvRewardDesc;

    private Activity mActivity;
    private UMWeb mWeb;
    private UMShareListener mUmShareListener;
    private File mFile;

    private SkuInfo mSkuInfo = null;

    private AsyncTask<Void, Void, Bitmap> mExecute;

    public SkuInfo getSkuInfo() {
        return mSkuInfo;
    }

    public void setSkuInfo(SkuInfo mSkuInfo) {
        this.mSkuInfo = mSkuInfo;
    }

    public DDMProductQrCodeDialog(Activity activity, SkuInfo skuInfo, UMWeb web, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;
        this.setSkuInfo(skuInfo);
        this.mUmShareListener = umShareListener;
        this.mWeb = web;
    }

    public DDMProductQrCodeDialog(Activity activity, SkuInfo skuInfo, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;
        this.setSkuInfo(skuInfo);
        this.mUmShareListener = umShareListener;

        String url = skuInfo.getProductUrl();
        UMWeb mWeb = new UMWeb(url);
        mWeb.setTitle(skuInfo.name);
        mWeb.setDescription(skuInfo.desc);
        mWeb.setThumb(new UMImage(activity, skuInfo.thumbURL));

        this.mWeb = mWeb;
    }


    public DDMProductQrCodeDialog(Activity activity, DDProductBean spu, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;

        SkuInfo skuInfo = SkuInfo.from(spu);
        this.setSkuInfo(skuInfo);

        this.mUmShareListener = umShareListener;

        String url = skuInfo.getProductUrl();
        UMWeb mWeb = new UMWeb(url);
        mWeb.setTitle(skuInfo.name);
        mWeb.setDescription(skuInfo.desc);
        mWeb.setThumb(new UMImage(activity, skuInfo.thumbURL));

        this.mWeb = mWeb;
    }

    public DDMProductQrCodeDialog(Activity activity, Product product, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;

        SkuInfo skuInfo = SkuInfo.from(product);
        this.setSkuInfo(skuInfo);

        this.mUmShareListener = umShareListener;

        String url = skuInfo.getProductUrl();
        UMWeb mWeb = new UMWeb(url);
        mWeb.setTitle(skuInfo.name);
        mWeb.setDescription(skuInfo.desc);
        mWeb.setThumb(new UMImage(activity, skuInfo.thumbURL));

        this.mWeb = mWeb;
    }

    public DDMProductQrCodeDialog(Activity activity, DDRushSpuBean spu, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;

        SkuInfo skuInfo = SkuInfo.from(spu);
        this.setSkuInfo(skuInfo);

        this.mUmShareListener = umShareListener;

        String url = skuInfo.getProductUrl();
        UMWeb mWeb = new UMWeb(url);
        mWeb.setTitle(skuInfo.name);
        mWeb.setDescription(skuInfo.desc);
        mWeb.setThumb(new UMImage(activity, skuInfo.thumbURL));

        this.mWeb = mWeb;
    }

    public DDMProductQrCodeDialog(Activity activity, File imgFile, UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mFile = imgFile;
        mUmShareListener = umShareListener;
    }

    public DDMProductQrCodeDialog(Context context) {
        super(context, R.style.WXShareDialog);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_prodecu_qr_code);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.CENTER);
        setCanceledOnTouchOutside(true);
        setView();
    }


    @OnClick(R.id.llShareDisk)
    void onShareDiskPressed() {
        DLog.i("onShareDiskPressed");
        ToastUtil.showLoading(getContext());
        makeScreenShot(new ScreenShotListener() {
            @Override
            public void onScreenShotEnd(Bitmap bitmap) {
                String name = "" + System.currentTimeMillis();
                if (mSkuInfo != null) {
                    name = mSkuInfo.productId;
                }

                ImageTools.saveBitmapToAlbum(getContext(), bitmap, "" + name);

                String pathToFile = PosterMaker.getPosterFile("" + name);
                DLog.i("saveBitmapToSD：" + pathToFile);
                ImageTools.saveBitmapToSD(bitmap, pathToFile, 80, false);

                ToastUtil.hideLoading();
                if (mUmShareListener != null) {
                    mUmShareListener.onResult(SHARE_MEDIA.MORE);
                }

                dismiss();

                DDSaveImageResponseDialog dialog = new DDSaveImageResponseDialog(mActivity);
                dialog.setPath("" + pathToFile);
                dialog.show();
            }
        });
    }

    @OnClick(R.id.llShareWechat)
    void onShareWeChatPressed() {
        DLog.i("onShareWeChatPressed");
//        shareTo3rdPlatform(SHARE_MEDIA.WEIXIN);

        //TIPS 新产品设计流程为分享到好友直接分享链接,废弃以前的分享逻辑 at 20190418 by hanQ

        String title = "" + mSkuInfo.name;
        String desc = "你那么好看，这个分享一定要看";
        String thumbUrl = "" + mSkuInfo.thumbURL;
        String url = mSkuInfo.getProductUrl();
        ShareUtils.share(mActivity, title, desc, thumbUrl, url, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                ToastUtil.success("分享成功");
                if (mUmShareListener != null) {
                    mUmShareListener.onResult(share_media);
                }

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

    @OnClick(R.id.llShareWechatCircle)
    void onShareWeChatCirclePressed() {
        DLog.i("onShareWeChatCirclePressed");
        shareTo3rdPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
    }

    @OnClick(R.id.llShareQQ)
    void onShareQQPressed() {
        DLog.i("onShareQQPressed");
        shareTo3rdPlatform(SHARE_MEDIA.QQ);
    }

    void shareTo3rdPlatform(final SHARE_MEDIA way) {
        DLog.i("shareTo3rdPlatform：" + way);
        makeScreenShot(new ScreenShotListener() {
            @Override
            public void onScreenShotEnd(Bitmap bitmap) {
                String name = "" + System.currentTimeMillis();
                if (mSkuInfo != null) {
                    name = mSkuInfo.productId;
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
                        if (mUmShareListener != null) {
                            mUmShareListener.onStart(share_media);
                        }
                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        DLog.d("share image onResult：" + share_media);

                        if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                            ToastUtil.success("分享成功");
                        }

                        if (mUmShareListener != null) {
                            mUmShareListener.onResult(share_media);
                        }

                        dismiss();

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        DLog.d("share image onError：" + share_media);
                        ToastUtil.error("分享失败");
                        if (mUmShareListener != null) {
                            mUmShareListener.onError(share_media, throwable);
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        DLog.d("share image onCancel：" + share_media);
                        ToastUtil.error("分享已取消");
                        if (mUmShareListener != null) {
                            mUmShareListener.onCancel(share_media);
                        }
                    }
                });
            }
        });
    }

    /**
     * 设置界面数据
     */
    void setView() {

        if (SessionUtil.getInstance().isLogin()) {
            User user = SessionUtil.getInstance().getLoginUser();
            String avatarUrl = user.avatar;
            sdAvatar.setImageURI(String.valueOf(avatarUrl));
            tvUserName.setText(user.nickname);
        }

        if (mSkuInfo != null) {
            String productName = mSkuInfo.name;
            String productDesc = mSkuInfo.desc;

            String productImageUrl = mSkuInfo.thumbURL;
            if (TextUtils.isEmpty(productImageUrl)) {
                productImageUrl = mSkuInfo.thumb;
            }


            tvProductTitle.setText("" + productName);

            tvPrice.setText(new SpanUtils().append("￥").setFontSize(14, true)
                    .append(ConvertUtil.cent2yuan(mSkuInfo) + "").create());

            //添加删除线
            UITools.addTextViewDeleteLine(tvMarketPrice);
            tvMarketPrice.setText("￥" + ConvertUtil.cent2yuanNoZero(mSkuInfo.marketPrice));

            DLog.d("productImageUrl:" + productImageUrl);
            sdProductImage.setImageURI(String.valueOf(productImageUrl));

            if (mSkuInfo.isStoreGift()) {
                mIvProductHeader.setImageDrawable(getContext().getDrawable(R.mipmap.product_header_gift));
            }

            String reward = ConvertUtil.cent2yuan2(mSkuInfo.rewardPrice);
            mTvReward.setText("赚" + reward + "元");

            SpannableStringBuilder builder = SpannableStringUtils.getBuilder("只要好友通过您分享的链接购买此商品，您至少\n能赚到")
                    .append(reward + "元")
                    .setForegroundColor(ContextCompat.getColor(getContext(), R.color.ddm_red))
                    .append("的佣金~")
                    .create();
            tvRewardDesc.setText(builder);
        }


        ShortUrlMaker.share().create(mWeb.toUrl(), new ShortUrlMaker.ShortUrlListener() {
            @Override
            public void onUrlCreate(String url) {
                createQRImage(url);
            }

            @Override
            public void onUrlCreateError(String error) {
                createQRImage(mWeb.toUrl());
                ToastUtil.error("短链接生成失败,使用默认Url生成二维码 error:" + error);
            }
        });


    }

    /**
     * 生成商品二维码图片
     */
    private void createQRImage(final String url) {
        ToastUtil.showLoading(getContext());
        mExecute = new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
                return QRCodeEncoder.syncEncodeQRCode(url, BGAQRCodeUtil.dp2px(getContext(), 100));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ToastUtil.hideLoading();
                if (bitmap != null) {
                    ivQRCode.setImageBitmap(bitmap);
                } else {
                    ToastUtil.error("生成二维码失败");
                }
            }
        }.execute();
    }

    @OnClick(R.id.tv_btn_cancel)
    public void onViewClicked() {
        dismiss();
    }


    public interface ScreenShotListener {
        void onScreenShotEnd(Bitmap bitmap);
    }

    /**
     * 执行截屏
     */
    public void makeScreenShot(ScreenShotListener listener) {
        DLog.i("makeScreenShot");
        Bitmap bitmap = getViewBitmap(llScreen);
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
}
