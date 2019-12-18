package com.xiling.ddmall.dduis.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.config.UIConfig;
import com.xiling.ddmall.ddui.custom.SquareDraweeView;
import com.xiling.ddmall.ddui.manager.PosterMaker;
import com.xiling.ddmall.ddui.manager.ShortUrlMaker;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.ImageTools;
import com.xiling.ddmall.ddui.tools.QRCodeUtil;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.dduis.base.AvatarDemoMaker;
import com.xiling.ddmall.dduis.bean.DDRushHeaderBean;
import com.xiling.ddmall.dduis.bean.DDRushSpuBean;
import com.xiling.ddmall.dduis.bean.DDRushSpuPageBean;
import com.xiling.ddmall.dduis.custom.SaleProgressView;
import com.xiling.ddmall.module.community.DateUtils;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.DDHomeService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.esafirm.rxdownloader.RxDownloader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class DDShareRushDialog extends Dialog {

    public interface ScreenShotListener {
        void onScreenShotStart();

        void onScreenShot(Bitmap file);

        void onScreenShotError(String error);
    }

    Activity activity = null;
    DDRushHeaderBean headerBean = null;

    public void setHeaderBean(DDRushHeaderBean headerBean) {
        this.headerBean = headerBean;
    }

    public DDShareRushDialog(@NonNull Activity context) {
        super(context, R.style.WXShareDialog);
        this.activity = context;

        weak = new WeakReference<Context>(context);
        mRxDownload = new RxDownloader(weak.get());
    }

    public DDShareRushDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDShareRushDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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

    @BindView(R.id.layout_screenshot)
    LinearLayout screenshotLayout;

    @BindView(R.id.layout_spu)
    LinearLayout spuLayout;

    @BindView(R.id.iv_qr_code)
    SimpleDraweeView sdQRCode;

    @OnClick(R.id.layout_share_wx)
    void onShareWXPressed() {
        DLog.i("onShareWXPressed");

        //TIPS 分享到微信好友逻辑变更为分享链接
        //只有点击保存图片的时候才生成图片

//        makeScreenShot(new ScreenShotListener() {
//            @Override
//            public void onScreenShotStart() {
//                ToastUtil.showLoading(getContext());
//            }
//
//            @Override
//            public void onScreenShot(Bitmap file) {
//                ToastUtil.hideLoading();
//                saveOrShare(file, false);
//            }
//
//            @Override
//            public void onScreenShotError(String error) {
//                ToastUtil.error("" + error);
//                ToastUtil.hideLoading();
//            }
//
//        });


        //生成二维码
        String url = HtmlService.buildRushH5Url(headerBean.getFlashSaleId());
        ShortUrlMaker.share().create(url, new ShortUrlMaker.ShortUrlListener() {
            @Override
            public void onUrlCreate(final String url) {

                String rushId = headerBean.getFlashSaleId();
                ToastUtil.showLoading(getContext());
                APIManager.startRequest(homeService.getRushListData(rushId, 1, 10), new RequestListener<DDRushSpuPageBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(DDRushSpuPageBean result) {
                        super.onSuccess(result);
                        data = result;

                        Date time = DateUtils.parseDateString(headerBean.getStartTime());
                        String timeText = DateUtils.getFormatTextFromDate(time, "MM月dd日 HH:mm");
                        String title = "" + timeText + "场 " + headerBean.getStatusText(headerBean.isSelected());
                        String desc = "限时抢购，价格美丽！答应我，先点开再说！";
                        String thumbUrl = "";

                        if (data.getDatas().size() > 0) {
                            thumbUrl = data.getDatas().get(0).getThumbUrlForShopNow();
                        }

                        ShareUtils.share(activity, title, desc, thumbUrl, url, new UMShareListener() {
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

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.error(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        ToastUtil.hideLoading();
                    }
                });


            }

            @Override
            public void onUrlCreateError(String error) {
                ToastUtil.error("生成链接失败");
            }
        });


    }

    @OnClick(R.id.layout_share_save)
    void onSavePressed() {
        DLog.i("onSavePressed");
        makeScreenShot(new ScreenShotListener() {
            @Override
            public void onScreenShotStart() {
                ToastUtil.showLoading(getContext());
            }

            @Override
            public void onScreenShot(Bitmap file) {
                ToastUtil.hideLoading();
                saveOrShare(file, true);
            }

            @Override
            public void onScreenShotError(String error) {
                ToastUtil.error("" + error);
                ToastUtil.hideLoading();
            }

        });
    }

    public void saveOrShare(Bitmap bitmap, boolean isSave) {

        String path = PosterMaker.DataRootPath + "rush_data/";
        String file = path + "full.jpg";

        File parent = new File(path);
        if (!parent.exists()) {
            boolean mdStatus = parent.mkdirs();
            DLog.d("创建合成文件目录:" + path + "," + mdStatus);
        } else {
            DLog.i("已存在合成文件目录:" + path);
        }

        File shareFile = new File(file);
        if (!shareFile.exists()) {
            try {
                boolean cfStatus = shareFile.createNewFile();
                DLog.d("创建新的合成文件:" + file + "," + cfStatus);
            } catch (Exception e) {
                DLog.d("创建新的合成文件:" + file + ",发生错误:" + e.getLocalizedMessage());
                e.printStackTrace();
            }
        } else {
            DLog.i("已存在合成文件:" + file);
        }

        if (isSave) {
            //存到相册
            ImageTools.saveBitmapToAlbum(getContext(), bitmap, "full_" + System.currentTimeMillis() + ".jpg");
            ToastUtil.success("保存成功");

            dismiss();
        } else {
            //存到SD存储
            ImageTools.saveBitmapToSD(bitmap, file, 85, false);
            dismiss();
            //调用分享
            ShareUtils.shareImg(activity, shareFile, SHARE_MEDIA.WEIXIN, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                    DLog.i("onStart:" + share_media);
                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {
                    DLog.i("onResult:" + share_media);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                    DLog.i("onError:" + share_media);
                    throwable.printStackTrace();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media) {
                    DLog.i("onCancel:" + share_media);
                }
            });
        }

    }

    @OnClick(R.id.layout_share_save)
    void onShareSaveImagePressed() {
        DLog.i("onShareSaveImagePressed");
    }

    @OnClick(R.id.btn_cancel)
    void onCancelButtonPressed() {
        this.dismiss();
    }

    DDHomeService homeService = null;

    private WeakReference<Context> weak;
    private RxDownloader mRxDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeService = ServiceManager.getInstance().createService(DDHomeService.class);
        setContentView(R.layout.dialog_share_rush);
        ButterKnife.bind(this);
        initWindow();
    }

    /*
     * 以下是动态合图逻辑
     */

    /**
     * 执行截屏
     */
    public void makeScreenShot(ScreenShotListener listener) {
        DLog.i("makeScreenShot");
        listener.onScreenShotStart();
        //开始获取最新数据
        getRushData(listener);
    }


    DDRushSpuPageBean data = null;

    public void getRushData(final ScreenShotListener listener) {
        String rushId = headerBean.getFlashSaleId();
        ToastUtil.showLoading(getContext());
        APIManager.startRequest(homeService.getRushListData(rushId, 1, 10), new RequestListener<DDRushSpuPageBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDRushSpuPageBean result) {
                super.onSuccess(result);
                data = result;
                //下载数据所需要的资源
                downloadProductImages(listener, result.getDatas());
            }

            @Override
            public void onError(Throwable e) {
                listener.onScreenShotError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void downloadProductImages(final ScreenShotListener listener, ArrayList<DDRushSpuBean> spus) {
        Single<List<String>> downloads = io.reactivex.Observable.fromIterable(spus)
                .flatMap(new Function<DDRushSpuBean, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(DDRushSpuBean spuBean) throws Exception {
                        String imgUrl = spuBean.getThumbUrlForShopNow();

                        String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
                        String folder = "rush_data";
                        String downloadPath = PosterMaker.dst + folder;
                        File downloadFolder = new File(downloadPath);
                        if (!downloadFolder.exists()) {
                            downloadFolder.mkdirs();
                        }

                        return mRxDownload.download(imgUrl, fileName, downloadPath, "image/jpg", false);
                    }
                }).toList();
        downloads.observeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<String>>() {
                    @Override
                    public void accept(List<String> strings) throws Exception {
                        ToastUtil.hideLoading();
                        StringBuffer buffer = new StringBuffer();
                        for (String s : strings) {
                            buffer.append("" + s + "\n");
                        }
                        DLog.i("" + buffer);
                        render(listener, strings);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DLog.e("下载图片发生错误");
                        listener.onScreenShotError(throwable.getLocalizedMessage());
                    }
                });
    }

    private void render(final ScreenShotListener listener, List<String> strings) {
        //清空上次的残留数据
        spuLayout.removeAllViews();
        //渲染数据到view
        ArrayList<DDRushSpuBean> spus = data.getDatas();
        HashMap<String, String> fileNames = new HashMap<>();
        for (String path : strings) {
            String fileName = path.substring(path.lastIndexOf("/") + 1);
            fileNames.put(fileName, path);
        }
        int len = spus.size();
        for (int i = 0; i < len; i++) {
            DDRushSpuBean spu = spus.get(i);
            String imgUrl = spu.getThumbUrlForShopNow();
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
            String filePath = fileNames.get(fileName);
            spuLayout.addView(buildProductView(spu, filePath));
        }
        //生成二维码
        String url = HtmlService.buildRushH5Url(headerBean.getFlashSaleId());
        ShortUrlMaker.share().create(url, new ShortUrlMaker.ShortUrlListener() {
            @Override
            public void onUrlCreate(String url) {
                String folder = "rush_data";
                String path = PosterMaker.DataRootPath + folder + "/qr.png";
                QRCodeUtil.createQRImage(url, 240, 240, null, path);
                setDiskImageToView(sdQRCode, path);
                screenshot(listener);
            }

            @Override
            public void onUrlCreateError(String error) {
                listener.onScreenShotError(error);
            }
        });
    }

    int second = 0;

    Handler hd = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 0) {
                ScreenShotListener listener = (ScreenShotListener) msg.obj;
                Bitmap bitmap = UITools.loadBitmapFromView(screenshotLayout);
                if (listener != null) {
                    if (bitmap != null) {
                        listener.onScreenShot(bitmap);
                    } else {
                        listener.onScreenShotError("生成文件失败");
                    }
                }
            }
        }
    };

    public void screenshot(final ScreenShotListener listener) {
        Message msg = hd.obtainMessage(0, listener);
        hd.sendMessageDelayed(msg, 100);
    }

    public View buildProductView(DDRushSpuBean spu, String filePath) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_rush_screenshot, null, false);

        TextView tvName = view.findViewById(R.id.tv_spu);
        SquareDraweeView ivThumb = view.findViewById(R.id.sd_thumb);
        TextView saleCountTextView = view.findViewById(R.id.tv_count);
        TextView priceTextView = view.findViewById(R.id.tv_price);
        TextView marketPriceTextView = view.findViewById(R.id.tv_price_market);
        UITools.addTextViewDeleteLine(marketPriceTextView);

        SimpleDraweeView sdAvatar01 = view.findViewById(R.id.sd_avatar_01);
        SimpleDraweeView sdAvatar02 = view.findViewById(R.id.sd_avatar_02);
        SimpleDraweeView sdAvatar03 = view.findViewById(R.id.sd_avatar_03);
        AvatarDemoMaker.setDemoAvatarDrawable(sdAvatar01, sdAvatar02, sdAvatar03);

        sdAvatar01.setVisibility(View.GONE);
        sdAvatar02.setVisibility(View.GONE);
        sdAvatar03.setVisibility(View.GONE);

        int saleCount = (int) spu.getSaleCount();
        if (saleCount > 0) {
            switch (saleCount) {
                default:
                case 3:
                    sdAvatar03.setVisibility(View.VISIBLE);
                case 2:
                    sdAvatar02.setVisibility(View.VISIBLE);
                case 1:
                    sdAvatar01.setVisibility(View.VISIBLE);
                    break;
            }
        }

        SaleProgressView progressBar = view.findViewById(R.id.pb_progress);

        //设置缩略图
        setDiskImageToView(ivThumb, filePath);

        //设置名称
        tvName.setText("" + spu.getProductName());
        saleCountTextView.setText("已抢" + ConvertUtil.formatWan(spu.getSaleCount()) + "件");

        priceTextView.setTextColor(UIConfig.COLOR_RUSH_VIP);
        priceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spu.getMinScorePrice()));
        marketPriceTextView.setText("￥" + ConvertUtil.cent2yuanNoZero(spu.getMaxSalePrice()));

        long totalCount = saleCount + spu.getInventory();
        progressBar.setTotalAndCurrentCount((int) totalCount, (int) saleCount);

        return view;
    }

    /**
     * 给ImageView设置本地图片
     */
    public void setDiskImageToView(ImageView imageView, String diskImagePath) {
        String path = diskImagePath.replace("file://", "");
        Bitmap bmThumb = ImageTools.getSmallBitmap(path);
        BitmapDrawable bd = new BitmapDrawable(getContext().getResources(), bmThumb);
        imageView.setBackground(bd);
    }

}
