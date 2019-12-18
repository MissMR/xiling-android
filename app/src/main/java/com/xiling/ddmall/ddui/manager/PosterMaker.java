package com.xiling.ddmall.ddui.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.xiling.ddmall.ddui.activity.SharePosterActivity;
import com.xiling.ddmall.ddui.bean.PosterBean;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.ImageTools;
import com.xiling.ddmall.ddui.tools.QRCodeUtil;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.sobot.chat.utils.MD5Util;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadStatus;

public class PosterMaker {

    boolean isReCreate = true;

    public static final String BG_SHARE_BAG = "bg_share_bag.jpg";
    public static final String BG_SHARE_FANS = "bg_share_fans.jpg";

    //默认图片
    public static final String DefaultIcon = "icon_default.png";

    public interface PosterMakeListener {
        void onPosterMakeStart();

        void onUserAvatarCreate(String pathToFile);

        void onUserQRCreate(String pathToFile);

        void onPosterCreate(String pathToFile);

        void onPosterMakeError(String error);

        void onPosterMakeEnd();
    }

    Context context = null;
    int mode = SharePosterActivity.PosterType.POSTER;
    ArrayList<PosterBean> posters = new ArrayList<>();
    ArrayList<PosterBean> downloads = new ArrayList<>();
    PosterMakeListener listener = null;

    public PosterMaker(Context context) {
        this.context = context;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public void setData(ArrayList<PosterBean> posters) {
        this.posters = posters;
        this.downloads.addAll(posters);
    }

    public void setListener(PosterMakeListener listener) {
        this.listener = listener;
    }

    /**
     * 开始生成海报
     */
    public void generate() {
        if (listener != null) {
            listener.onPosterMakeStart();
            DLog.d("封面海报生成开始...");
        }
        if (this.posters.size() > 0) {
            createUserIcon();
        } else {
            if (listener != null) {
                DLog.d("封面海报生成完成");
                listener.onPosterMakeEnd();
            }
        }
    }

    public static String dst = "/data/com.dodomall.ddmall/data/";
    public static String DataRootPath = Environment.getExternalStorageDirectory() + dst;

    String avatarUrl = "";
    String nickName = "";

    static String avatarFilePath = DataRootPath;
    static String QRFilePath = DataRootPath + "qr";

    static String postPath = DataRootPath + "poster/";

    public static String getPosterFile(int mode, String url) {
        if (mode == SharePosterActivity.PosterType.POSTER) {
            return getPosterFile(url);
        } else {
            return getAssetFile(url);
        }
    }

    public static String getPosterFile(String url) {
        File rootPath = new File(postPath);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        String name = getPosterFileName(url);
        return postPath + "" + name;
    }

    public static String getAssetFile(String url) {
        File rootPath = new File(DataRootPath);
        if (!rootPath.exists()) {
            rootPath.mkdirs();
        }
        String name = getPosterFileName(url);
        return DataRootPath + "" + name;
    }

    public static String getPosterFileName(String url) {
        String name = MD5Util.encode(url);
        return name;
    }

    /**
     * 生成用户头像
     */
    void createUserIcon() {
        User user = SessionUtil.getInstance().getLoginUser();
        if (user != null) {
            avatarUrl = user.avatar;
            nickName = user.nickname;
            if (!TextUtils.isEmpty(avatarUrl)) {
                DLog.i("下载用户头像=>" + avatarFilePath);
                RxDownload.getInstance(context)
                        .download(avatarUrl, "avatar", avatarFilePath)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<DownloadStatus>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                DLog.i("开始下载用户头像");
                            }

                            @Override
                            public void onNext(DownloadStatus downloadStatus) {
                                DLog.i("下载用户头像:" + downloadStatus.getPercentNumber());
                                if (downloadStatus.getPercentNumber() == 100) {
                                    DLog.i("下载用户头像完成");
                                    ImageTools.cutImage(avatarFilePath + "avatar", 132, 132);
                                    if (listener != null) {
                                        listener.onUserAvatarCreate("" + avatarFilePath + "avatar");
                                    }
                                    createUserQR();
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                DLog.e("下载用户头像失败");
                                if (listener != null) {
                                    listener.onPosterMakeError("下载用户头像失败:" + e.getMessage());
                                }
                            }

                            @Override
                            public void onComplete() {
                                DLog.i("下载用户头像结束");
                            }
                        });
            } else {
                if (listener != null) {
                    listener.onPosterMakeError("下载用户头像失败");
                }
            }
        }
    }

    public static int PosterQRSize = 300;

    /**
     * 生成用户二维码
     */
    void createUserQR() {

        String url = "" + HtmlService.WEB_REGISTER;
        if (mode == SharePosterActivity.PosterType.BAG) {
            url = "" + HtmlService.SHARE_BADGE;
        } else if (mode == SharePosterActivity.PosterType.FANS) {
            url = "" + HtmlService.SHARE_FANS;
        }
        url += url.contains("?") ? "&" : "?";
        url += "inviteCode=" + SessionUtil.getInstance().getLoginUser().invitationCode;

        final String finalUrl = url;

        ShortUrlMaker.share().

                create(url, new ShortUrlMaker.ShortUrlListener() {
                    @Override
                    public void onUrlCreate(String shortUrl) {

                        boolean result = QRCodeUtil.createQRImage(shortUrl, PosterQRSize, PosterQRSize, null, QRFilePath);
                        if (result) {
                            DLog.i("二维码生成完成");
                            if (listener != null) {
                                listener.onUserQRCreate("" + QRFilePath);
                            }
                            createPoster();
                        } else {
                            DLog.i("二维码生成失败");
                            if (listener != null) {
                                listener.onPosterMakeError("二维码生成失败");
                            }
                        }
                    }

                    @Override
                    public void onUrlCreateError(String error) {
                        DLog.i("获取短链接失败,使用原Url生成二维码 err:" + error);
                        boolean result = QRCodeUtil.createQRImage(finalUrl, PosterQRSize, PosterQRSize, null, QRFilePath);
                        if (result) {
                            DLog.i("二维码生成完成");
                            if (listener != null) {
                                listener.onUserQRCreate("" + QRFilePath);
                            }
                            createPoster();
                        } else {
                            DLog.i("二维码生成失败");
                            if (listener != null) {
                                listener.onPosterMakeError("二维码生成失败");
                            }
                        }
                    }
                });


    }

    /**
     * 生成海报
     */
    void createPoster() {
        if (mode == SharePosterActivity.PosterType.POSTER) {
            //专属海报
            if (downloads.size() > 0) {
                PosterBean poster = downloads.get(0);
                String imgUrl = poster.getImgUrl();
                final String filePath = getPosterFile(imgUrl);
                final File file = new File(filePath);

                //判断是否要重新生成
                if (isReCreate) {
                    if (file.exists()) {
                        file.delete();
                    }
                }

                downloads.remove(0);

                if (!file.exists()) {
                    String name = getPosterFileName(imgUrl);
                    DLog.i("下载封面:" + imgUrl + "=>" + postPath + "" + name);

                    RxDownload.getInstance(context)
                            .download(imgUrl, "" + name, postPath)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<DownloadStatus>() {
                                @Override
                                public void onSubscribe(Disposable d) {
                                    DLog.i("封面开始下载");
                                }

                                @Override
                                public void onNext(DownloadStatus downloadStatus) {
                                    DLog.i("封面下载:" + downloadStatus.getPercentNumber());
                                    if (downloadStatus.getPercentNumber() == 100) {
                                        makeFinalPoster(filePath);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    DLog.i("封面下载失败:" + e.getMessage());
                                    if (listener != null) {
                                        listener.onPosterMakeError("海报下载失败:" + e.getMessage());
                                    }
                                }

                                @Override
                                public void onComplete() {
                                    createPoster();
                                }
                            });
                } else {
                    createPoster();
                }
            } else {
                DLog.w("所有封面下载完成");
                if (listener != null) {
                    listener.onPosterMakeEnd();
                }
            }
        } else if (mode == SharePosterActivity.PosterType.BAG) {
            //创业礼包
            downloadImageFormAssets(BG_SHARE_BAG);
        } else if (mode == SharePosterActivity.PosterType.FANS) {
            //专属粉丝
            downloadImageFormAssets(BG_SHARE_FANS);
        }
    }

    /**
     * 从资源文件中复制出背景图片并合成
     */
    public void downloadImageFormAssets(final String fileName) {
        final String name = getPosterFileName(fileName);
        String filePath = getPosterFile(mode, fileName);
        File file = new File(filePath);

        if (isReCreate) {
            if (file.exists()) {
                file.delete();
            }
        }

        if (!file.exists()) {
            AssetsCarry assetsCarry = AssetsCarry.getInstance(context);
            assetsCarry.setAssetCopyCallback(new AssetsCarry.AssetCopyCallback() {
                @Override
                public void onAssetCopySuccess() {
                    makeFinalPoster(DataRootPath + "" + name);
                }

                @Override
                public void onAssetCopyFailed(String error) {
                    if (listener != null) {
                        listener.onPosterMakeError("海报下载失败:" + error);
                    }
                }
            });
            assetsCarry.copyAssetsToSD(fileName, dst + "" + name);
        } else {
            if (listener != null) {
                listener.onPosterMakeEnd();
            }
        }
    }

    /**
     * 合并图片
     */
    public void makeFinalPoster(String background) {
        DLog.i("开始合成...");
        try {

            //统一压缩质量到80%
            int compress = 80;

            if (mode == SharePosterActivity.PosterType.POSTER) {
                ImageTools.mergePoster(background, avatarFilePath + "avatar", QRFilePath, nickName, compress);
            } else {
                ImageTools.mergeShare(background, QRFilePath, compress);
            }

            if (listener != null) {
                if (mode == SharePosterActivity.PosterType.POSTER) {
                    listener.onPosterCreate(background);
                } else {
                    listener.onPosterCreate(background);
                    listener.onPosterMakeEnd();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            if (listener != null) {
                listener.onPosterMakeError("海报合成失败:" + e.getMessage());
            }
        }
    }
}
