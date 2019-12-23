package com.xiling.ddui.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.xiling.ddui.service.HtmlService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.ddui.tools.QRCodeUtil;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.User;
import com.xiling.shared.util.SessionUtil;
import com.esafirm.rxdownloader.RxDownloader;

import java.io.File;
import java.lang.ref.WeakReference;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class CommunityImageMaker {

    public enum Mode {
        Round,/*发圈资源*/
        Greeting,/*早晚安语*/
    }

    public interface CommunityImageMakeListener {
        void onCommunityImageMakeStart();

        void onCommunityImageMakeReady(String pathToImage);

        void onCommunityImageMakeError(String error);
    }

    public static final int q = 80;

    //App在SD内部存储中的相对路径
    public static String dst = "/data/com.dodomall.ddmall/data/";
    //App在SD内存储存的绝对路径
    public static String DataRootPath = Environment.getExternalStorageDirectory() + dst;

    static String ext = ".jpg";

    //头像文件的存储文件夹
    static String avatarFilePath = DataRootPath;
    //头像文件的存储文件名字
    static String avatarFileName = "avatar" + ext;

    static String QRPath = DataRootPath;
    //二维码文件的绝对路径
    static String QRPathToFile = QRPath + "qr" + ext;

    //产品大图的存储文件夹
    static String productFilePath = DataRootPath;
    //产品大图的存储文件名字
    static String productFileName = "product" + ext;

    //产品大图的存储文件夹
    static String greetingFilePath = DataRootPath;
    //产品大图的存储文件名字
    static String greetingFileName = "greeting" + ext;

    //二维码描述图片
    static String greetingQrDescPath = DataRootPath;
    static String greetingQrDescFileName = "gQRDesc" + ext;

    //模式 - 用来确定合成的样式和数据
    private Mode mode = Mode.Round;

    //商品信息
    private SkuInfo skuInfo = null;
    //用户信息
    private User user = null;

    //合并图片地址
    private String mergeImageUrl = "";

    private Context context = null;
    private CommunityImageMakeListener mListener = null;

    private WeakReference<Context> weak;
    private RxDownloader mRxDownload;

    public CommunityImageMaker(Context context) {
        this.context = context;
        weak = new WeakReference<Context>(context);
        mRxDownload = new RxDownloader(weak.get());
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public SkuInfo getSkuInfo() {
        return skuInfo;
    }

    public void setSkuInfo(SkuInfo skuInfo) {
        this.skuInfo = skuInfo;
    }

    public String getMergeImageUrl() {
        return mergeImageUrl;
    }

    public void setMergeImageUrl(String mergeImageUrl) {
        this.mergeImageUrl = mergeImageUrl;
    }

    public CommunityImageMakeListener getListener() {
        return mListener;
    }

    public void setListener(CommunityImageMakeListener listener) {
        this.mListener = listener;
    }

    //合成方式
    private int eventMode = 0;
    //合成URL地址
    private String eventUrl = "";

    public int getEventMode() {
        return eventMode;
    }

    public void setEventMode(int eventMode) {
        this.eventMode = eventMode;
    }

    public String getEventUrl() {
        return eventUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }

    /**
     * 开始生成
     * <p>
     * 发圈素材的合并
     * =================
     * 1.生成短链接地址
     * 2.获取用户信息
     * 3.生成二维码图片
     * 4.下载用户头像图片
     * 5.下载商品大图图片
     * 6.合并三张图片
     * 7.写文本
     * <p>
     * 早晚安语的合并
     * =================
     * 1.生成短链接地址
     * 2.获取用户信息
     * 3.生成二维码图片
     * 4.生成AppLogo图片
     * 5.合并三张图片
     * 6.写文本
     */
    public void make() {
        //用户登录检查
        if (!SessionUtil.getInstance().isLogin()) {
            DLog.i("用户未登录");
            return;
        }
        //取出最新的用户信息
        user = SessionUtil.getInstance().getLoginUser();

        if (mListener != null) {
            mListener.onCommunityImageMakeStart();
        }

        //构造原始链接地址
        String url = "";
        //发圈逻辑
        if (mode == Mode.Round) {
            if (skuInfo == null) {
                //没有商品 - 用户邀请码
                url = HtmlService.BESHOPKEPPER + "?inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode;
            } else {
                //存在商品 - 商品详情带邀请码
                url = skuInfo.getProductUrl();
            }
            //生成短链接
            ShortUrlMaker.share().create(url, new ShortUrlMaker.ShortUrlListener() {
                @Override
                public void onUrlCreate(String url) {
                    DLog.d("onUrlCreate:" + url);
                    if (mode == Mode.Round) {
                        createRoundImage(url);
                    } else if (mode == Mode.Greeting) {
                        createGreetingImage(url);
                    } else {
                        DLog.e("未识别的业务模式");
                    }
                }

                @Override
                public void onUrlCreateError(String error) {
                    DLog.e("onUrlCreateError:" + error);
                    if (mListener != null) {
                        mListener.onCommunityImageMakeError("生成短链接失败:" + error);
                    }
                }
            });
        } else if (mode == Mode.Greeting) {
            if (TextUtils.isEmpty(this.eventUrl)) {
                url = HtmlService.BESHOPKEPPER + "?inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode;
            } else {
                url = this.eventUrl;
            }
            if (eventMode == 0) {
                downloadImage(new DownloadImageListener() {
                    @Override
                    public void onImageDownloadStart() {

                    }

                    @Override
                    public void onImageDownloadProgress(long progress) {

                    }

                    @Override
                    public void onImageDownloadFinish(String url, String pathToFile) {
                        if (TextUtils.isEmpty(pathToFile)) {
                            //返回路径为空
                            if (mListener != null) {
                                mListener.onCommunityImageMakeError("下载图片失败，路径为空");
                            }
                        } else {
                            //成功
                            if (mListener != null) {
                                mListener.onCommunityImageMakeReady(pathToFile);
                            }
                        }
                    }

                    @Override
                    public void onImageDownloadError(String error) {
                        if (mListener != null) {
                            mListener.onCommunityImageMakeError("下载图片失败");
                        }
                    }
                });
            } else {
                createGreetingImage(url);
            }

        }
    }

    /**
     * 生成发圈图片
     *
     * @param shortUrl 短链接
     */
    private void createRoundImage(String shortUrl) {
        final String tag = "Create Round ";
        createQR(shortUrl, ImageTools.roundQRSize, new DownloadImageListener() {
            @Override
            public void onImageDownloadStart() {
                DLog.i(tag + "QR Image start");
            }

            @Override
            public void onImageDownloadProgress(long progress) {
                DLog.i(tag + "QR Image progress:" + progress);
            }

            @Override
            public void onImageDownloadFinish(String url, final String pathToFile) {
                DLog.i(tag + "QR Image finish:" + pathToFile);
                String avatarUrl = user.avatar;
                createUserHeader(avatarUrl, new DownloadImageListener() {
                    @Override
                    public void onImageDownloadStart() {
                        DLog.i(tag + "User Avatar Image start");
                    }

                    @Override
                    public void onImageDownloadProgress(long progress) {
                        DLog.i(tag + "User Avatar Image progress:" + progress);
                    }

                    @Override
                    public void onImageDownloadFinish(String url, String pathToFile) {
                        DLog.d(tag + "User Avatar Image finish:" + pathToFile);
                        //下载发圈素材图片
                        downloadImage(mergeImageUrl, productFilePath, productFileName, ImageTools.roundImageSize, ImageTools.roundImageSize, new DownloadImageListener() {

                            @Override
                            public void onImageDownloadStart() {
                                DLog.i(tag + "Image start");
                            }

                            @Override
                            public void onImageDownloadProgress(long progress) {
                                DLog.i(tag + "Image progress:" + progress);
                            }

                            @Override
                            public void onImageDownloadFinish(String url, String pathToFile) {
                                DLog.d(tag + "Image finish:" + pathToFile);
                                //合并图片
                                drawRoundImage();
                            }

                            @Override
                            public void onImageDownloadError(String error) {
                                DLog.e(tag + "Image error:" + error);
                                if (mListener != null) {
                                    mListener.onCommunityImageMakeError("" + error);
                                }
                            }
                        });
                    }

                    @Override
                    public void onImageDownloadError(String error) {
                        DLog.e(tag + "User Avatar Image error:" + error);
                        if (mListener != null) {
                            mListener.onCommunityImageMakeError("下载用户头像失败");
                        }
                    }
                });
            }

            @Override
            public void onImageDownloadError(String error) {
                DLog.e(tag + "QR Image error:" + error);
                if (mListener != null) {
                    mListener.onCommunityImageMakeError("二维码生成失败");
                }
            }
        });
    }

    /**
     * 生成早晚安语图片
     *
     * @param shortUrl 短链接
     */
    private void createGreetingImage(String shortUrl) {
        final String tag = "Create Greeting";
        //生成二维码图片
        createQR(shortUrl, ImageTools.greetingQRSize, new DownloadImageListener() {
            @Override
            public void onImageDownloadStart() {

            }

            @Override
            public void onImageDownloadProgress(long progress) {

            }

            @Override
            public void onImageDownloadFinish(String url, String pathToFile) {
                DLog.i(tag + " QR Image finish:" + pathToFile);
                downloadImage(new DownloadImageListener() {

                    @Override
                    public void onImageDownloadStart() {
                        DLog.i(tag + "Image start");
                    }

                    @Override
                    public void onImageDownloadProgress(long progress) {
                        DLog.i(tag + "Image progress:" + progress);
                    }

                    @Override
                    public void onImageDownloadFinish(String url, String pathToFile) {
                        DLog.i(tag + "Image finish:" + pathToFile);

                        AssetsCarry assetsCarry = AssetsCarry.getInstance(context);
                        assetsCarry.setAssetCopyCallback(new AssetsCarry.AssetCopyCallback() {
                            @Override
                            public void onAssetCopySuccess() {
                                DLog.i("二维码描述图片复制完成");
                                //合并图片
                                drawGreetingImage();
                            }

                            @Override
                            public void onAssetCopyFailed(String error) {
                                DLog.e("二维码描述图片复制出错:" + error);
                                if (mListener != null) {
                                    mListener.onCommunityImageMakeError("生成早晚安语资源失败");
                                }
                            }
                        });
                        assetsCarry.copyAssetsToSD("greeting_qr_desc.png", greetingQrDescPath + greetingQrDescFileName);
                    }

                    @Override
                    public void onImageDownloadError(String error) {
                        DLog.i(tag + "Image error:" + error);
                        if (mListener != null) {
                            mListener.onCommunityImageMakeError("下载早晚安语图片失败");
                        }
                    }
                });
            }

            @Override
            public void onImageDownloadError(String error) {
                DLog.i(tag + "QR error:" + error);
                if (mListener != null) {
                    mListener.onCommunityImageMakeError("" + error);
                }
            }
        });
    }

    private void downloadImage(DownloadImageListener downloadImageListener) {
        final String tag = "downloadImage";
        //下载早晚安语图片
        downloadImage(mergeImageUrl, greetingFilePath, greetingFileName, ImageTools.greetingImageWidth, ImageTools.greetingImageHeight, downloadImageListener);
    }

    /**
     * 生成二维码图片
     *
     * @param shortUrl 短链接地址
     * @param listener 生成回调
     */
    void createQR(String shortUrl, int size, DownloadImageListener listener) {
        DLog.d("createQR:" + shortUrl);

        File folder = new File(QRPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        boolean result = QRCodeUtil.createQRImage(shortUrl, size, size, null, QRPathToFile);
        if (listener != null) {
            if (result) {
                DLog.i("二维码生成成功");
                listener.onImageDownloadFinish(shortUrl, QRPathToFile);
            } else {
                DLog.w("二维码生成失败");
                listener.onImageDownloadError("二维码生成失败");
            }
        } else {
            DLog.w("createQr has no listener for status:" + result);
        }
    }

    /**
     * 生成用户头像
     */
    void createUserHeader(String headerUrl, DownloadImageListener listener) {
        int headerSize = 140;
        DLog.d("下载用户头像:" + headerUrl);
        downloadImage(headerUrl, avatarFilePath, avatarFileName, headerSize, headerSize, listener);
    }


    /**
     * 合并发圈 - 画图
     */
    private void drawRoundImage() {

        String avatarPathToFile = avatarFilePath + avatarFileName;
        String thumbPathToFile = productFilePath + productFileName;

        DLog.d("drawRoundImage start");
        DLog.d("=======================");
        DLog.i("avatarPathToFile:" + avatarPathToFile);
        DLog.i("thumbPathToFile:" + thumbPathToFile);
        DLog.i("qrIcon:" + QRPathToFile);
        DLog.d("=======================");

        try {
            String greetingFile = ImageTools.mergeRoundImage(thumbPathToFile, QRPathToFile, avatarPathToFile, user.nickname, user.invitationCode, skuInfo, 80);
            if (TextUtils.isEmpty(greetingFile)) {
                //返回路径为空
                if (mListener != null) {
                    mListener.onCommunityImageMakeError("合并图片图片失败:路径为空");
                }
            } else {
                //成功
                if (mListener != null) {
                    mListener.onCommunityImageMakeReady(greetingFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //发生了异常
            if (mListener != null) {
                mListener.onCommunityImageMakeError("合并图片失败:" + e.getMessage());
            }
        }

    }

    /**
     * 合并早晚安语 - 画图
     */
    private void drawGreetingImage() {

        String greetingPathToFile = greetingFilePath + greetingFileName;
        String greetingQRDescPathToFile = greetingQrDescPath + greetingQrDescFileName;

        DLog.d("drawGreetingImage start");
        DLog.d("=======================");
        DLog.i("greetingImage:" + greetingPathToFile);
        DLog.i("qrIcon:" + QRPathToFile);
        DLog.d("=======================");

        try {
            String greetingFile = ImageTools.mergeGreetingImage(greetingPathToFile, QRPathToFile, greetingQRDescPathToFile, 80);
            if (TextUtils.isEmpty(greetingFile)) {
                //返回路径为空
                if (mListener != null) {
                    mListener.onCommunityImageMakeError("合并图片图片失败:路径为空");
                }
            } else {
                //成功
                if (mListener != null) {
                    mListener.onCommunityImageMakeReady(greetingFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //发生了异常
            if (mListener != null) {
                mListener.onCommunityImageMakeError("合并图片失败:" + e.getMessage());
            }
        }

    }

    public interface DownloadImageListener {

        void onImageDownloadStart();

        void onImageDownloadProgress(long progress);

        void onImageDownloadFinish(String url, String pathToFile);

        void onImageDownloadError(String error);
    }

    /***
     * 下载图片到本地并剪切到指定大小
     *
     * @param url 下载地址
     * @param path 保存路径
     * @param fileName 保存文件名
     * @param width 图片宽度
     * @param height 图片高度
     * @param listener 事件回调
     */
    public void downloadImage(String url, String path, String fileName, final int width, final int height, final DownloadImageListener listener) {
        final String pathToFile = path + fileName;
        DLog.i("will download image to " + pathToFile);

        File folder = new File(path);
        if (!folder.exists()) {
            DLog.w("init folder file.");
            folder.mkdirs();
        }

        File file = new File(pathToFile);
        if (file.exists()) {
            DLog.w("delete exist file.");
            file.delete();
        }

        final String downloadUrl = "" + url;
        if (TextUtils.isEmpty(downloadUrl)) {
            DLog.w("图片URL为空,直接复制默认图片 > " + pathToFile);
            copyDefaultIconToPath(pathToFile, listener);
        } else {
            RxDownload.getInstance(context)
                    .download(downloadUrl, fileName, path)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DownloadStatus>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            if (listener != null) {
                                listener.onImageDownloadStart();
                            }
                        }

                        @Override
                        public void onNext(DownloadStatus downloadStatus) {
                            long progress = downloadStatus.getPercentNumber();
                            if (listener != null) {
                                listener.onImageDownloadProgress(progress);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (listener != null) {
                                listener.onImageDownloadError("下载失败:" + e.getMessage());
                            }
                        }

                        @Override
                        public void onComplete() {
                            DLog.i("download url : " + downloadUrl + " complete.");
                            ImageTools.cutImage(pathToFile, width, height);
                            if (listener != null) {
                                listener.onImageDownloadFinish(downloadUrl, pathToFile);
                            }
                        }
                    });
        }
    }

    /**
     * 默认图片
     */
    static final String DefaultIcon = "icon_default.png";

    void copyDefaultIconToPath(final String dstFile, final DownloadImageListener listener) {
        AssetsCarry assetsCarry = AssetsCarry.getInstance(context);
        assetsCarry.setAssetCopyCallback(new AssetsCarry.AssetCopyCallback() {
            @Override
            public void onAssetCopySuccess() {
                DLog.d("onAssetCopySuccess");
                if (listener != null) {
                    listener.onImageDownloadFinish("", dstFile);
                }
            }

            @Override
            public void onAssetCopyFailed(String error) {
                DLog.d("onAssetCopyFailed：" + error);
                if (listener != null) {
                    listener.onImageDownloadError("" + error);
                }
            }
        });
        assetsCarry.copyAssetsToSD(DefaultIcon, dstFile);
    }

}
