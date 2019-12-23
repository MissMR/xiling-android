package com.xiling.ddui.manager;

import android.app.Activity;

import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.HProductPostBean;
import com.xiling.ddui.custom.DDResDownloadDialog;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ImgDownLoadUtils;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;

/**
 * 多商品合成图片分享和下载管理器
 * <p>
 * 1. 传入发圈行数据
 * 2. 下载所有的普通图片
 * 3. 合成所有的商品图片
 * 4-1. 批量保存到相册
 * 4-2. 用户选择后分享到微信
 */
public class DDCommunityShareManager {

    Activity context = null;
    CommunityDataBean data = null;
    CommunityShareManager.CommunityShareListener listener = null;

    public CommunityShareManager.CommunityShareListener getListener() {
        return listener;
    }

    public void setListener(CommunityShareManager.CommunityShareListener listener) {
        this.listener = listener;
    }

    public DDCommunityShareManager(Activity context, CommunityDataBean data) {
        this.context = context;
        this.data = data;
    }

    private void resetSelectStatus() {
        ArrayList<HProductPostBean> ds = data.getPostImages();
        //默认全选
        for (HProductPostBean d : ds) {
            d.setSelect(true);
        }
    }

    /**
     * 显示分享对话框
     */
    public void show() {
        resetSelectStatus();
        ClipboardUtil.setPrimaryClip(data.getShareText());

        DDShareWXDialog dialog = new DDShareWXDialog(context);
        dialog.setMode(DDShareWXDialog.Mode.Community);
        dialog.setCommunity(data);
        dialog.setListener(listener);
        dialog.show();
    }

    /**
     * 开始下载
     */
    public void download() {
        resetSelectStatus();
        ClipboardUtil.setPrimaryClip(StringUtil.html2Text(data.getShareText()));

        if (data.hasVideo()) {
            if ("1".equals(data.getCanDownVideoFlag())) {
                DLog.i("下载视频...");
                ImgDownLoadUtils.saveVideo2Local(data.getVideoUrl(), context, fileListener);
            } else {
                ToastUtil.hideLoading();
                showResultDialog(DDResDownloadDialog.DownloadMode.Video, false);
                //复制文本也要增加一次下载次数
                if (listener != null) {
                    listener.onDownloadSuccess();
                }
            }
        } else if (data.hasImage()) {
            DDProductPosterManager manager = new DDProductPosterManager(context);
            manager.setData(data);
            manager.setListener(new DDProductPosterManager.ProductPosterListener() {
                @Override
                public void onPosterMakerStart() {
                    DLog.i("开始合成海报");
                }

                @Override
                public void onPosterTaskStart(String url) {
                    DLog.i("开始合成海报任务:" + url);
                }

                @Override
                public void onPosterTaskSuccess(String url, String poster) {
                    DLog.i("合成海报:" + url + " 到> " + poster);
                    //单任务合成完成后直接保存到相册
                    String name = StringUtil.md5(url);
                    ImgDownLoadUtils.copyFileToAlbum(context, poster, name);
                }

                @Override
                public void onPosterMakerSuccess(ArrayList<String> posters) {
                    DLog.i("合成海报完成");
                    if (listener != null) {
                        listener.onDownloadSuccess();
                    }
                    fileListener.onFileSaveFinish(ImgDownLoadUtils.FileType.Image);
                }

                @Override
                public void onPosterMakerError(String desc) {
                    DLog.i("合成海报失败:" + desc);
                    ToastUtil.error("" + desc);
                }
            });
            manager.makePoster();
        } else {
            showResultDialog(DDResDownloadDialog.DownloadMode.Text, false);
            if (listener != null) {
                listener.onDownloadSuccess();
            }
        }
    }

    ImgDownLoadUtils.FileSaveListener fileListener = new ImgDownLoadUtils.FileSaveListener() {

        @Override
        public void onFileSaveStart() {
            DLog.d("onFileSaveStart");
            ToastUtil.showLoading(context);
        }

        @Override
        public void onFileSaveFinish(ImgDownLoadUtils.FileType fileType) {
            DLog.d("onFileSaveFinish");
            ToastUtil.hideLoading();
            if (fileType == ImgDownLoadUtils.FileType.Image) {
                showResultDialog(DDResDownloadDialog.DownloadMode.Image, true);
            } else if (fileType == ImgDownLoadUtils.FileType.Video) {
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
     * 结果提示框 - 下载专用
     */
    private void showResultDialog(DDResDownloadDialog.DownloadMode mode, boolean resStatus) {
        DDResDownloadDialog dialog = new DDResDownloadDialog(context);
        dialog.setMode(mode, resStatus);
        dialog.show();
    }

}
