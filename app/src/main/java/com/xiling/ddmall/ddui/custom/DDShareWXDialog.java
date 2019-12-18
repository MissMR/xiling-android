package com.xiling.ddmall.ddui.custom;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.CommunityDataBean;
import com.xiling.ddmall.ddui.bean.HProductPostBean;
import com.xiling.ddmall.ddui.manager.CommunityShareManager;
import com.xiling.ddmall.ddui.manager.DDProductPosterMaker;
import com.xiling.ddmall.ddui.manager.DDProductPosterManager;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.decoration.GridSpacingItemDecoration;
import com.xiling.ddmall.shared.util.ClipboardUtil;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.ImgDownLoadUtils;
import com.xiling.ddmall.shared.util.ShareUtils;
import com.xiling.ddmall.shared.util.ShareUtilsNew;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDShareWXDialog extends Dialog {

    public enum Mode {
        //分享单张本地图片资源
        SingleImage,
        //分享链接
        Link,
        //网络图片
        WebImage,
        //发圈素材
        Community,
    }

    //分享模式
    private Mode mode = Mode.SingleImage;

    //要分享的图片资源本地路径
    private String imgPath = "";

    //要分享的链接的4要素
    private String title = "";
    private String desc = "";
    private String thumbUrl = "";
    private int thumbId = R.mipmap.ic_launcher;
    private String linkUrl = "";
    private ImageSelectorAdapter mImageSelectorAdapter;

    //发圈素材
    private CommunityDataBean community = null;
    private CommunityShareManager.CommunityShareListener listener = null;

    public void setListener(CommunityShareManager.CommunityShareListener listener) {
        this.listener = listener;
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA share_media) {
            DLog.d("share image onStart：" + share_media);
            if (listener != null) {
                listener.onShareStart();
            }
        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {
            DLog.d("share image onResult：" + share_media);
            if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                ToastUtil.success("分享成功");
            }
            if (listener != null) {
                listener.onShareSuccess();
            }
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
    };

    @BindView(R.id.shareFriendBtn)
    RelativeLayout shareFriendBtn;

    @BindView(R.id.shareCircleBtn)
    RelativeLayout shareCircleBtn;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @OnClick(R.id.topPanel)
    void onTopPanelPressed() {
        dismiss();
    }

    @OnClick(R.id.selectPanel)
    void onSelectPanelPressed() {

    }

    @BindView(R.id.selectPanel)
    LinearLayout selectPanel;

    Activity activity = null;

    public DDShareWXDialog(@NonNull Activity context) {
        super(context, R.style.WXShareDialog);
        this.activity = context;
    }


    public DDShareWXDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DDShareWXDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_wx);
        ButterKnife.bind(this);
        initWindow();
        setCanceledOnTouchOutside(true);
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

        if (mode == Mode.Community) {
            initRecyclerView();
        }

    }


    private void initRecyclerView() {
        if (!community.hasVideo()) {
            if (community.hasImage()) {
                mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
                mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, ConvertUtil.dip2px(6), false));
                if (community != null) {
                    mImageSelectorAdapter = new ImageSelectorAdapter(community.getPostImages());
                    mImageSelectorAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            HProductPostBean bean = mImageSelectorAdapter.getItem(position);
                            bean.setSelect(!bean.isSelect());
                            DLog.i("" + bean.getUrl() + ":" + bean.isSelect());
                            mImageSelectorAdapter.notifyDataSetChanged();
                        }
                    });
                    mRecyclerView.setAdapter(mImageSelectorAdapter);
                }
                //大于1个资源显示
                if (community.getPostUrls().size() > 1) {
                    selectPanel.setVisibility(View.VISIBLE);
                } else {
                    selectPanel.setVisibility(View.GONE);
                }
            } else {
                selectPanel.setVisibility(View.GONE);
            }
        } else {
            selectPanel.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.shareFriendBtn)
    void onShareFriendPressed() {
        DLog.i("onShareFriendPressed");
        boolean status = share(SHARE_MEDIA.WEIXIN);
        if (status) {
            dismiss();
        }
    }

    @OnClick(R.id.shareCircleBtn)
    void onShareCirclePressed() {
        DLog.i("onShareCirclePressed");
        boolean status = share(SHARE_MEDIA.WEIXIN_CIRCLE);
        if (status) {
            dismiss();
        }
    }

    @OnClick(R.id.iv_close)
    void onCloseClick() {
        dismiss();
    }

    public boolean share(SHARE_MEDIA way) {
        ToastUtil.hideLoading();
        if (mode == Mode.SingleImage) {
            shareSingleImage(way);
        } else if (mode == Mode.Link) {
            shareLink(way);
        } else if (mode == Mode.WebImage) {
            shareWebImage(way);
        } else if (mode == Mode.Community) {
            String content = community.getShareText();
            title = WebViewActivity.ShareText.DEFAULT_SHARE_TITLE;
            if (community.hasVideo()) {
                //视频模式
                linkUrl = community.getVideoUrl();
                thumbUrl = community.getVideoImageUrl();
                ShareUtilsNew.shareVideo(activity, linkUrl, title, "" + thumbUrl, "" + content + "", way, shareListener);
            } else if (community.hasImage()) {
                //图片模式
                return shareCommunity(content, way);
            } else {
                //文本模式
                ShareUtilsNew.shareText(activity, content + "", way, shareListener);
            }
        }
        return true;
    }

    private void shareWebImage(SHARE_MEDIA way) {
        ShareUtils.shareWebImg(activity, imgPath, way, shareListener);
    }

    private void shareSingleImage(SHARE_MEDIA way) {
        File mFile = new File(imgPath);
        ShareUtils.shareImg(activity, mFile, way, shareListener);
    }

    private void shareLink(SHARE_MEDIA way) {
        DLog.i("shareLink:\ntitle:" + title + "\ndesc:" + desc + "\nthumbUrl:" + thumbUrl + "\nlinkUrl:" + linkUrl + "\n==>" + way);
        if (TextUtils.isEmpty(thumbUrl) && thumbId > 0) {
            ShareUtilsNew.share(activity, title, desc, thumbId, linkUrl, way);
        } else {
            ShareUtilsNew.share(activity, title, desc, thumbUrl, linkUrl, way);
        }
    }

    private boolean shareCommunity(String content, SHARE_MEDIA way) {

        //复制到黏贴板
        ClipboardUtil.setPrimaryClip(content);

        List<HProductPostBean> allData = mImageSelectorAdapter.getData();
        List<HProductPostBean> selected = new ArrayList<>();
        for (HProductPostBean item : allData) {
            if (item.isSelect()) {
                selected.add(item);
            }
        }
        DLog.i("选中图片数量：" + selected.size());
        if (selected.size() > 0) {
            if (selected.size() == 1) {
                DLog.d("选中1张图片直接调用微信SDK");
                HProductPostBean bean = selected.get(0);
                if (bean.getType() == HProductPostBean.PostType.Product) {
                    DLog.i("选中的单张图片是商品图片，调用合成图片后再分享本地图片");
                    shareSingleCommunity(bean, way);
                } else {
                    imgPath = bean.getUrl();
                    DLog.i("选中的单张图片是普通发圈素材，直接使用分享网络图片:" + imgPath);
                    shareWebImage(way);
                }
            } else {
                //处理分享多张的逻辑
                shareMultiCommunity(way);
            }
            return true;
        } else {
            ToastUtil.error("至少选择1张图片");
            return false;
        }
    }

    void shareMultiCommunity(final SHARE_MEDIA way) {
        DDProductPosterManager manager = new DDProductPosterManager(activity);
        manager.setData(community);
        ToastUtil.showLoading(activity);
        if (way == SHARE_MEDIA.WEIXIN_CIRCLE) {
            DLog.d("选中多张图片，且是朋友圈,直接保存本地，并提示");
            manager.setListener(new DDProductPosterManager.ProductPosterListener() {
                @Override
                public void onPosterMakerStart() {
                    DLog.i("多张图片到朋友圈 Start");
                }

                @Override
                public void onPosterTaskStart(String url) {
                    DLog.i("多张图片到朋友圈 Task:" + url);
                }

                @Override
                public void onPosterTaskSuccess(String url, String poster) {
                    DLog.i("多张图片到朋友圈 Task Success:" + url + "  > " + poster);
                    //单任务合成完成后直接保存到相册
                    String name = StringUtil.md5(url);
                    ImgDownLoadUtils.copyFileToAlbum(activity, poster, name);
                }

                @Override
                public void onPosterMakerSuccess(ArrayList<String> posters) {
                    DLog.i("多张图片到朋友圈 Success:" + posters.size());
                    showResultDialog(DDResDownloadDialog.DownloadMode.Image, true);
                    ToastUtil.hideLoading();
                }

                @Override
                public void onPosterMakerError(String desc) {
                    DLog.e("多张图片到朋友圈 Error:" + desc);
                    ToastUtil.error("" + desc);
                    ToastUtil.hideLoading();
                }
            });
            manager.makePoster();
        } else {
            DLog.d("选中多张图片，发送给朋友,保存本地并发送");
            manager.setListener(new DDProductPosterManager.ProductPosterListener() {
                @Override
                public void onPosterMakerStart() {
                    DLog.i("多张图片到朋友 Start");
                }

                @Override
                public void onPosterTaskStart(String url) {
                    DLog.i("多张图片到朋友 Task:" + url);
                }

                @Override
                public void onPosterTaskSuccess(String url, String poster) {
                    DLog.i("多张图片到朋友 Task Success:" + url + "  > " + poster);
                }

                @Override
                public void onPosterMakerSuccess(ArrayList<String> posters) {
                    DLog.i("多张图片到朋友 Success:" + posters.size());
                    ArrayList<File> files = new ArrayList<>();
                    for (String s : posters) {
                        files.add(new File(s));
                    }
                    shareMultipleImages(files, way);
                    ToastUtil.hideLoading();
                }

                @Override
                public void onPosterMakerError(String desc) {
                    DLog.e("多张图片到朋友 Error:" + desc);
                    ToastUtil.error("" + desc);
                    ToastUtil.hideLoading();
                }
            });
            manager.makePoster();
        }
    }

    public void shareMultipleImages(ArrayList<File> images, SHARE_MEDIA media) {
        ToastUtil.hideLoading();
        String content = community.getFormatContent();
        if (media == SHARE_MEDIA.WEIXIN_CIRCLE) {
            ShareUtilsNew.shareMultiplePictureToTimeLine(activity, content, images);
        } else if (media == SHARE_MEDIA.WEIXIN) {
            ShareUtilsNew.shareMultiplePictureToUi(activity, content, images);
        } else if (media == SHARE_MEDIA.QQ) {
            ShareUtilsNew.shareMultiplePictureToQQ(activity, images);
        }
        //WARNING 使用微信调起逻辑启动的没有回调，所以只要调起就直接认为是成功
        if (shareListener != null) {
            shareListener.onStart(media);
            shareListener.onResult(media);
        }
        ClipboardUtil.setPrimaryClip(community.getShareText());
    }

    void shareSingleCommunity(HProductPostBean item, final SHARE_MEDIA way) {
        DDProductPosterMaker maker = new DDProductPosterMaker(getContext());
        maker.setData(item);
        maker.setListener(new DDProductPosterMaker.DDProductPostMakerListener() {
            @Override
            public void onPostMakerStart(String url) {
                DLog.i("单张合成商品图开始:" + url);
                ToastUtil.showLoading(activity);
            }

            @Override
            public void onPostMakerDownloadUserInfo(String url, String file) {
                DLog.i("单张合成商品图 下载用户图片:" + url + "=>" + file);
            }

            @Override
            public void onPostMakerDownloadProductInfo(String url, String file) {
                DLog.i("单张合成商品图 下载产品图片:" + url + "=>" + file);
            }

            @Override
            public void onPostMakerSuccess(String url, String file) {
                DLog.i("单张合成商品图 合成完成:" + url + "=>" + file);
                ToastUtil.hideLoading();
                imgPath = file;
                shareSingleImage(way);
            }

            @Override
            public void onPostMakerError(String desc) {
                DLog.i("单张合成商品图发生错误:" + desc);
                ToastUtil.hideLoading();
                ToastUtil.error("" + desc);
            }
        });
        maker.render();
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
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

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public UMShareListener getShareListener() {
        return shareListener;
    }

    public void setShareListener(UMShareListener shareListener) {
        this.shareListener = shareListener;
    }

    public CommunityDataBean getCommunity() {
        return community;
    }

    public void setCommunity(CommunityDataBean community) {
        this.community = community;
    }

    public int getThumbId() {
        return thumbId;
    }

    public void setThumbId(int thumbId) {
        this.thumbId = thumbId;
    }

    public static class ImageSelectorAdapter extends BaseQuickAdapter<HProductPostBean, BaseViewHolder> {

        public ImageSelectorAdapter(@Nullable List<HProductPostBean> data) {
            super(R.layout.item_image_seletor, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, HProductPostBean item) {
            ((SimpleDraweeView) helper.getView(R.id.sdv_img)).setImageURI(item.toString());
            helper.itemView.setSelected(item.isSelect());
//            view.setSelected(!view.isSelected());
        }

    }

    /**
     * 结果提示框 - 下载专用
     */
    private void showResultDialog(DDResDownloadDialog.DownloadMode mode, boolean resStatus) {
        DDResDownloadDialog dialog = new DDResDownloadDialog(activity);
        dialog.setMode(mode, resStatus);
        dialog.show();
    }
}
