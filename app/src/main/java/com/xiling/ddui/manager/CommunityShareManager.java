package com.xiling.ddui.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.xiling.BuildConfig;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.custom.DDResDownloadDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.component.dialog.DDMShareDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ImgDownLoadUtils;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;

/**
 * @Deprecated 因对接多商品关联，此工具类废弃 at 20190123 by hanQ
 * @see com.xiling.ddui.manager.DDCommunityShareManager
 */
public class CommunityShareManager {

    public interface CommunityShareListener {
        void onShareStart();

        /**
         * 分享成功
         */
        void onShareSuccess();

        /*下载成功*/
        void onDownloadSuccess();
    }

    private interface DataListener {
        void onLoadEnd(SkuInfo data);
    }

    public CommunityShareListener getListener() {
        return listener;
    }

    public void setListener(CommunityShareListener listener) {
        this.listener = listener;
    }

    Activity activity = null;
    CommunityDataBean data = null;
    IProductService iProductService = null;
    CommunityShareListener listener = null;

    public CommunityShareManager(Activity activity, CommunityDataBean data) {
        this.activity = activity;
        this.data = data;
        this.iProductService = ServiceManager.getInstance().createService(IProductService.class);
    }

    /**
     * 结果提示框 - 下载专用
     */
    private void showResultDialog(DDResDownloadDialog.DownloadMode mode, boolean resStatus) {
//        DDResultDialog dialog = new DDResultDialog(activity);
//        dialog.setTitle("" + title);
//        dialog.setContent("" + content);
//        dialog.setConfirmText("确定");
//        dialog.show();
        DDResDownloadDialog dialog = new DDResDownloadDialog(activity);
        dialog.setMode(mode, resStatus);
        dialog.show();
    }

    //是否是下载逻辑
    boolean isDownload = false;

    public String getProductUrl() {
        String url = BuildConfig.BASE_URL + "spu/" + data.getProductId();
        if (UserManager.getInstance().isLogin()) {
            url += "/" + SessionUtil.getInstance().getLoginUser().invitationCode;
        }
        return url;
    }

    /**
     * 下载数据
     */
    public void download() {
        isDownload = true;
        //如果有商品信息则生成短链接并复制到粘贴板
        if (!TextUtils.isEmpty(data.getSkuId())) {
            String url = getProductUrl();
            ShortUrlMaker maker = ShortUrlMaker.share();
            maker.create(url, new ShortUrlMaker.ShortUrlListener() {
                @Override
                public void onUrlCreate(String url) {
                    String content = data.getContent();
                    content += "" + url;
                    ClipboardUtil.setPrimaryClip(StringUtil.html2Text(content));
                    downloadRes();
                }

                @Override
                public void onUrlCreateError(String error) {
                    ToastUtil.error("" + error);
                }
            });
        } else {
            //没有商品信息直接复制内容到粘贴板
            String content = data.getContent();
            ClipboardUtil.setPrimaryClip(StringUtil.html2Text(content));
            downloadRes();
        }

    }

    private void downloadRes() {
        String videoUrl = data.getVideoPlayUrl();
        String videoDownloadUrl = data.getVideoDownUrl();
        if (TextUtils.isEmpty(videoDownloadUrl)) {
            videoDownloadUrl = videoUrl;
        }
        ArrayList<String> images = data.getImagesUrlList();
        if (!TextUtils.isEmpty(videoUrl) && !TextUtils.isEmpty(videoDownloadUrl)) {
            if ("1".equals(data.getCanDownVideoFlag())) {
                DLog.i("下载视频...");
                ImgDownLoadUtils.saveVideo2Local(videoDownloadUrl, activity, fileListener);
            } else {
                ToastUtil.hideLoading();
                showResultDialog(DDResDownloadDialog.DownloadMode.Video, false);

                //复制文本也要增加一次下载次数 at 2018/10/27 by hanQ
                if (listener != null) {
                    listener.onDownloadSuccess();
                }
            }
        } else if (images != null && images.size() > 0) {
            DLog.i("下载图片...");

            final ArrayList<String> iData = new ArrayList<>();
            iData.addAll(images);

            final String firstImageUrl = iData.get(0);

            final CommunityImageMaker maker = new CommunityImageMaker(activity);
            maker.setMergeImageUrl("" + firstImageUrl);
            maker.setListener(new CommunityImageMaker.CommunityImageMakeListener() {
                @Override
                public void onCommunityImageMakeStart() {
                    ToastUtil.showLoading(activity);
                }

                @Override
                public void onCommunityImageMakeReady(String pathToImage) {
                    Context context = activity.getApplicationContext();
                    //保存第一张图片到相册
                    Bitmap firstBitmap = ImageTools.getSmallBitmap(pathToImage);
                    String fileName = firstImageUrl.substring(firstImageUrl.lastIndexOf("/") + 1);
                    ImageTools.saveBitmapToAlbum(context, firstBitmap, fileName);
                    //如果还有剩下的图片
                    if (iData.size() > 1) {
                        //排除第一张
                        ArrayList<String> remainData = new ArrayList<>();
                        remainData.addAll(iData);
                        remainData.remove(0);
                        //下载剩下的图片
                        ImgDownLoadUtils.savePic2Local(remainData, context, fileListener);
                    } else {
                        ToastUtil.hideLoading();
                        if (fileListener != null) {
                            fileListener.onFileSaveFinish(ImgDownLoadUtils.FileType.Image);
                        }
                    }
                }

                @Override
                public void onCommunityImageMakeError(String error) {
                    ToastUtil.hideLoading();
                    ToastUtil.error(error);
                }
            });

            //如果有商品ID，则进入获取商品消息的逻辑
            String skuId = data.getSkuId();
            if (!TextUtils.isEmpty(skuId)) {
                getProductDetail(skuId, new DataListener() {
                    @Override
                    public void onLoadEnd(SkuInfo data) {
                        maker.setSkuInfo(data);
                        maker.make();
                    }
                });
            } else {
                maker.make();
            }


        } else {
            DLog.i("下载文本...");
            if (fileListener != null) {
                fileListener.onFileSaveStart();
                fileListener.onFileSaveFinish(ImgDownLoadUtils.FileType.TextAndLink);
            }
        }
    }

    UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            DLog.i("CommunityShareManager.onStart:" + share_media);
            if (listener != null) {
                listener.onShareStart();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            DLog.i("CommunityShareManager.onResult:" + share_media);
            if (listener != null) {
                listener.onShareSuccess();
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
            DLog.i("CommunityShareManager.onError:" + share_media);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {
            DLog.i("CommunityShareManager.onCancel:" + share_media);
        }
    };

    ImgDownLoadUtils.FileSaveListener fileListener = new ImgDownLoadUtils.FileSaveListener() {

        @Override
        public void onFileSaveStart() {
            DLog.d("onFileSaveStart");
            ToastUtil.showLoading(activity);
        }

        @Override
        public void onFileSaveFinish(ImgDownLoadUtils.FileType fileType) {
            DLog.d("onFileSaveFinish");
            ToastUtil.hideLoading();
            String content = "";

            if (fileType == ImgDownLoadUtils.FileType.Image) {
                content = "图片已保存至相册";
                showResultDialog(DDResDownloadDialog.DownloadMode.Image, true);
            } else if (fileType == ImgDownLoadUtils.FileType.Video) {
                content = "视频已保存至相册";
                showResultDialog(DDResDownloadDialog.DownloadMode.Video, true);
            } else {
                showResultDialog(DDResDownloadDialog.DownloadMode.Text, true);
            }

            //回调下载事件
            if (listener != null) {
                listener.onDownloadSuccess();
            }

        }

        @Override
        public void onFileSaveError(String error) {
            DLog.e("onFileSaveError:" + error);
            ToastUtil.hideLoading();
            ToastUtil.error("下载数据发生错误");
        }
    };

    /**
     * 初始化分享商品数据
     *
     * @param skuId 商品ID
     */
    /**
     * 获取商品详情
     *
     * @param skuId 商品SKU ID
     */
    void getProductDetail(String skuId, final DataListener listener) {
        APIManager.startRequest(iProductService.getSkuById(skuId), new BaseRequestListener<SkuInfo>() {

            @Override
            public void onStart() {
                super.onStart();
                ToastUtil.showLoading(CommunityShareManager.this.activity);
            }

            @Override
            public void onSuccess(SkuInfo data) {
                if (listener != null) {
                    listener.onLoadEnd(data);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                ToastUtil.hideLoading();
            }
        });
    }


    public void show() {

        isDownload = false;

        String skuId = data.getSkuId();
        if (TextUtils.isEmpty(skuId)) {
            //没有商品数据
            DDMShareDialog dialog = new DDMShareDialog(activity, data, shareListener);
            dialog.show();
        } else {
            //有商品数据
            getProductDetail(skuId, new DataListener() {
                @Override
                public void onLoadEnd(final SkuInfo data) {
                    String url = getProductUrl();
                    ShortUrlMaker maker = ShortUrlMaker.share();
                    maker.create(url, new ShortUrlMaker.ShortUrlListener() {
                        @Override
                        public void onUrlCreate(String url) {
                            DDMShareDialog dialog = new DDMShareDialog(activity, CommunityShareManager.this.data, shareListener);
                            //设置商品数据
                            dialog.setCommunityProduct(data);
                            //设置短链接
                            dialog.setShortUrl(url);
                            dialog.show();
                        }

                        @Override
                        public void onUrlCreateError(String error) {
                            ToastUtil.error("" + error);
                        }
                    });


                }
            });
        }
    }


}
