package com.xiling.ddmall.shared.component.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CommunityDataBean;
import com.xiling.ddmall.ddui.manager.CommunityImageMaker;
import com.xiling.ddmall.ddui.manager.PosterMaker;
import com.xiling.ddmall.ddui.tools.AppTools;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.util.ClipboardUtil;
import com.xiling.ddmall.shared.util.CommonUtil;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.ShareUtilsNew;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.esafirm.rxdownloader.RxDownloader;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import rx.Observer;

import static android.content.Context.CLIPBOARD_SERVICE;

/**
 * @author Jigsaw
 * @date 2018/8/22
 */
public class DDMShareDialog extends Dialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_content)
    TextView tvContent;

    @BindView(R.id.ll_qrcode)
    LinearLayout llQRCode;

    @BindView(R.id.ll_copy)
    LinearLayout llCopy;

    @BindView(R.id.llRow2Panel)
    LinearLayout row2Panel;

    private Activity mActivity;
    private UMWeb mWeb;
    private UMShareListener mUmShareListener;
    private File mFile;
    private String mDialogTitle;
    private String mDialogContent = "只要你的好友通过你的链接购买此商品，你就能赚到至少%s元的佣金哦~";
    private String mCanMakeMoney;

    //商品数据
    private SkuInfo mSkuInfo = null;

    /**
     * 是否是社区分享模式
     */
    boolean isCommunityMode = false;
    /**
     * 是否是社区分享模式 - 产品模式
     */
    boolean isCommunityProductMode = false;

    //发圈数据
    CommunityDataBean mCommunity = null;

    //短链接
    private String shortUrl = "";

    private final WeakReference<Context> weak;
    private RxDownloader mRxDownload;

    public DDMShareDialog(Activity activity, String title, String desc, String logoUrl, String link, UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mWeb = new UMWeb(link);
        mUmShareListener = umShareListener;
        mWeb.setTitle(title);
        mWeb.setDescription(desc);
        mWeb.setThumb(new UMImage(activity, logoUrl));

        tvContent.getText();
    }

    public DDMShareDialog(Activity activity, Share shareObject, UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mWeb = new UMWeb(shareObject.getLink());
        mUmShareListener = umShareListener;
        mWeb.setTitle(shareObject.getTitle());
        mWeb.setDescription(shareObject.getDesc());
        mWeb.setThumb(new UMImage(activity, shareObject.getLogo()));
        mCanMakeMoney = shareObject.canMakeMoney;
        mDialogTitle = "赚" + shareObject.getCanMakeMoney();
        mDialogContent = String.format(mDialogContent, shareObject.getCanMakeMoney());
        mSkuInfo = shareObject.getSkuInfo();
    }

    public DDMShareDialog(Activity activity, File imgFile, UMShareListener umShareListener) {
        this(activity);
        mActivity = activity;
        mFile = imgFile;
        mUmShareListener = umShareListener;
    }

    public DDMShareDialog(Activity activity, CommunityDataBean bean, UMShareListener umShareListener) {
        this(activity);
        this.mActivity = activity;
        this.mCommunity = bean;
        this.mUmShareListener = umShareListener;
        //社区分享模式
        isCommunityMode = true;
    }

    private DDMShareDialog(Context context) {
        super(context, R.style.Theme_Light_Dialog);

        weak = new WeakReference<Context>(context);
        mRxDownload = new RxDownloader(weak.get());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_ddm);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        //社区模式
        if (isCommunityMode) {
            if (isCommunityProductMode) {
                //有商品信息
                //获得赚的数据
                mCanMakeMoney = "" + ConvertUtil.cent2yuanNoZero(mSkuInfo.rewardPrice);
                mDialogTitle = "赚" + mCanMakeMoney;
                //设置标题栏
                tvTitle.setText(mDialogTitle);
                tvContent.setText(SpannableStringUtils.getBuilder("只要你的好友通过你的链接购买此商品，你就能赚到至少")
                        .append(mCanMakeMoney)
                        .setForegroundColor(getContext().getResources().getColor(R.color.ddm_red))
                        .append("元的佣金哦~")
                        .create());
            } else {
                //无商品信息
                tvTitle.setTextColor(Color.parseColor("#333333"));
                tvTitle.setText("选择分享渠道");
                tvContent.setText(" ");
                tvContent.setVisibility(View.GONE);
                llQRCode.setVisibility(View.GONE);
                llCopy.setVisibility(View.GONE);
                row2Panel.setVisibility(View.GONE);
            }
        } else {
            //非社区模式 - 商品模式
            tvTitle.setText(mDialogTitle);
            tvContent.setText(SpannableStringUtils.getBuilder("只要你的好友通过你的链接购买此商品，你就能赚到至少")
                    .append(mCanMakeMoney)
                    .setForegroundColor(getContext().getResources().getColor(R.color.ddm_red))
                    .append("元的佣金哦~")
                    .create());
        }
    }

    /**
     * 社区分享 - 商品模式
     */
    public void setCommunityProduct(SkuInfo skuInfo) {
        //由上一层传入的模式
        isCommunityProductMode = true;
        //商品信息
        mSkuInfo = skuInfo;

        String url = mSkuInfo.getProductUrl();
        mWeb = new UMWeb(url);
        mWeb.setTitle(skuInfo.name);
        mWeb.setDescription(skuInfo.desc);
        mWeb.setThumb(new UMImage(mActivity, skuInfo.thumbURL));
    }

    /**
     * 处理社区部分的分享数据
     */
    public void shareCommunityData(SHARE_MEDIA media) {

        String content = StringUtil.html2Text(mCommunity.getContent());

        String videoUrl = mCommunity.getVideoPlayUrl();
        ArrayList<String> images = mCommunity.getImagesUrlList();
        if (!TextUtils.isEmpty(videoUrl)) {
            String thumbUrl = mCommunity.getVideoImageUrl();
            //有视频数据
            ShareUtilsNew.shareVideo(mActivity, videoUrl, WebViewActivity.ShareText.DEFAULT_SHARE_TITLE, "" + thumbUrl, "" + content + "" + shortUrl, media, mUmShareListener);
        } else if (images != null && images.size() > 0) {
            //有图片数据
            ArrayList<String> iData = new ArrayList<String>();
            iData.addAll(images);
            progressFirstImage(iData, media);
        } else {
            //没有多媒体数据
            ShareUtilsNew.shareText(mActivity, content + "" + shortUrl, media, mUmShareListener);
        }
    }

    /**
     * 处理第一张图片
     *
     * @param images 网络图片数组
     * @param media  分享方式
     */
    public void progressFirstImage(final ArrayList<String> images, final SHARE_MEDIA media) {

        //移除分享朋友圈的多条数据
        if ((media == SHARE_MEDIA.WEIXIN_CIRCLE) && images.size() > 1) {
            String firstImage = images.get(0);
            images.clear();
            images.add(firstImage);
        }

        if (images.size() > 0) {
            String firstUrl = images.get(0);

            CommunityImageMaker maker = new CommunityImageMaker(getContext());
            maker.setMode(CommunityImageMaker.Mode.Round);
            maker.setSkuInfo(mSkuInfo);
            maker.setMergeImageUrl("" + firstUrl);
            maker.setListener(new CommunityImageMaker.CommunityImageMakeListener() {
                @Override
                public void onCommunityImageMakeStart() {
                    ToastUtil.showLoading(getContext());
                }

                @Override
                public void onCommunityImageMakeReady(final String pathToImage) {
                    //如果有多张图片
                    if (images.size() > 1) {
                        //那就移除第一张
                        images.remove(0);
                        //去下载剩下的图片
                        downloadOtherImage(images, new DownloadMultipleImageListener() {
                            @Override
                            public void onDownloadStart() {

                            }

                            @Override
                            public void onDownloadFinish(ArrayList<File> files) {
                                //图片下载完成后重新填充进来第一张图片
                                files.add(0, new File(pathToImage));
                                //然后批量分享
                                shareMultipleImages(files, media);
                            }

                            @Override
                            public void onDownloadError(String error) {

                            }
                        });
                    } else {
                        //是单张图片
                        ArrayList<File> files = new ArrayList<>();
                        files.add(new File(pathToImage));
                        //直接去分享
                        shareMultipleImages(files, media);
                    }
                }

                @Override
                public void onCommunityImageMakeError(String error) {
                    ToastUtil.error("" + error);
                    ToastUtil.hideLoading();
                }
            });
            maker.make();
        } else {
            ToastUtil.error("没有图片无法分享");
        }
    }

    public interface DownloadMultipleImageListener {

        void onDownloadStart();

        void onDownloadFinish(ArrayList<File> files);

        void onDownloadError(String error);
    }

    /**
     * 下载其他图片
     */
    public void downloadOtherImage(ArrayList<String> images, final DownloadMultipleImageListener listener) {

        final ArrayList<String> files = new ArrayList<>();
        if (listener != null) {
            listener.onDownloadStart();
        }
        Single<List<String>> downloads = Observable.fromIterable(images).flatMap(new Function<String, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(String imgUrl) throws Exception {
                String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                String folder = "community";
                String path = PosterMaker.DataRootPath + folder + "/" + fileName;
                files.add(path);

                String downloadPath = PosterMaker.dst + folder;
                File downloadFolder = new File(downloadPath);
                if (!downloadFolder.exists()) {
                    downloadFolder.mkdirs();
                }

                return mRxDownload.download(imgUrl, fileName, downloadPath, "image/jpg", false);
            }
        }).toList();
        downloads.observeOn(Schedulers.newThread()).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<String>>() {
            @Override
            public void accept(List<String> strings) throws Exception {
                ToastUtil.hideLoading();
                ArrayList<File> realFiles = new ArrayList<>();
                for (int i = 0; i < files.size(); i++) {
                    File file = new File(files.get(i));
                    realFiles.add(file);
                }
                if (listener != null) {
                    listener.onDownloadFinish(realFiles);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                if (listener != null) {
                    listener.onDownloadError("" + throwable.getMessage());
                }
            }
        });
    }

    public void shareMultipleImages(ArrayList<File> images, final SHARE_MEDIA media) {
        ToastUtil.hideLoading();
        if (weak.get() != null) {
            String content = mCommunity.getFormatContent();
            if (media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ShareUtilsNew.shareMultiplePictureToTimeLine(weak.get(), content, images);
            } else if (media == SHARE_MEDIA.WEIXIN) {
                ShareUtilsNew.shareMultiplePictureToUi(weak.get(), content, images);
            } else if (media == SHARE_MEDIA.QQ) {
                ShareUtilsNew.shareMultiplePictureToQQ(weak.get(), images);
            }
            //WARNING 使用微信调起逻辑启动的没有回调，所以只要调起就直接认为是成功
            if (mUmShareListener != null) {
                mUmShareListener.onStart(media);
                mUmShareListener.onResult(media);
            }
        } else {
            DLog.e("WeakContext is null!");
        }
        ClipboardUtil.setPrimaryClip(mCommunity.getFormatContent() + "" + shortUrl);
    }

    protected void share(SHARE_MEDIA media) {

        if (media == SHARE_MEDIA.WEIXIN || media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            if (!AppTools.isAppInstall(getContext(), AppTools.PKG_Wechat)) {
                ToastUtil.error("未安装微信");
                return;
            }
        } else if (media == SHARE_MEDIA.QQ) {
            if (!AppTools.isAppInstall(getContext(), AppTools.PKG_QQ)) {
                ToastUtil.error("未安装QQ");
                return;
            }
        } else {
            ToastUtil.error("未知分享模式");
            return;
        }

        if (isCommunityMode) {
            shareCommunityData(media);
        } else {
            //通用型分享
            if (mFile != null) {
                ShareUtils.shareImg(mActivity, mFile, media, mUmShareListener);
            } else {
                new ShareAction(mActivity)
                        .setPlatform(media)
                        .withMedia(mWeb)
                        .setCallback(mUmShareListener)
                        .share();
            }
        }
        dismiss();
    }

    @OnClick({R.id.iv_close, R.id.ll_wechat, R.id.ll_wechat_circle, R.id.ll_qq, R.id.ll_qrcode, R.id.ll_copy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.ll_wechat:
                share(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.ll_wechat_circle:
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case R.id.ll_qq:
                share(SHARE_MEDIA.QQ);
                break;
            case R.id.ll_qrcode:
                if (SessionUtil.getInstance().isLogin()) {
                    if (mSkuInfo != null) {
                        RxPermissions rxPermissions = new RxPermissions(mActivity);
                        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .subscribe(new Observer<Permission>() {
                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        ToastUtil.error("申请存储权限失败，请前往APP应用设置中打开此权限");
                                    }

                                    @Override
                                    public void onNext(Permission permission) {
                                        if (permission.granted) {
                                            showProductQrCodeDialog();
                                        } else if (permission.shouldShowRequestPermissionRationale) {
                                            ToastUtil.error("缺少存储权限，请同意位置权限的申请");
                                        } else {
                                            ToastUtil.error("缺少存储权限，请前往APP应用设置中打开此权限");
                                        }
                                    }
                                });


                    } else {
                        ToastUtil.error("数据异常");
                    }
                } else {
                    ToastUtil.error("用户未登录");
                    DLog.w("用户未登录");
                }

                dismiss();
                break;
            case R.id.ll_copy:
                ClipboardManager myClipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData myClip = ClipData.newPlainText("product", mWeb.toUrl());
                myClipboard.setPrimaryClip(myClip);
                ToastUtil.success("链接已复制");
                dismiss();
                break;
        }
    }

    DDMProductQrCodeDialog qrCodeDialog = null;

    private void showProductQrCodeDialog() {
        qrCodeDialog = new DDMProductQrCodeDialog(mActivity, mSkuInfo, mWeb, new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
                qrCodeDialog.dismiss();
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
        qrCodeDialog.show();
    }

    public static class Share {

        private SkuInfo skuInfo;

        private String title;
        private String desc;
        private String logo;
        private String link;
        private String canMakeMoney;

        private boolean isCommunityMode = false;

        public Share() {
        }

        public Share(String title, String desc, String logo, String link, String canMakeMoney) {
            this.title = title;
            this.desc = desc;
            this.logo = logo;
            this.link = link;
            this.canMakeMoney = canMakeMoney;
        }

        public Share(String title, String desc, String logo, String link) {
            this.title = title;
            this.desc = desc;
            this.logo = logo;
            this.link = link;
        }

        public SkuInfo getSkuInfo() {
            return skuInfo;
        }

        public void setSkuInfo(SkuInfo skuInfo) {
            this.skuInfo = skuInfo;
        }

        public String getCanMakeMoney() {
            return canMakeMoney;
        }

        public void setCanMakeMoney(String canMakeMoney) {
            this.canMakeMoney = canMakeMoney;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
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

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public boolean isCommunityMode() {
            return isCommunityMode;
        }

        public void setCommunityMode(boolean communityMode) {
            isCommunityMode = communityMode;
        }
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
}
