package com.xiling.ddui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.bean.HelloBean;
import com.xiling.ddui.custom.DDResDownloadDialog;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.custom.DDStatusButton;
import com.xiling.ddui.manager.CommunityImageMaker;
import com.xiling.ddui.service.ICommunityService;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ImageUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * created by Jigsaw at 2018/10/11
 */
public class CommunityHelloAdapter extends BaseQuickAdapter<HelloBean, BaseViewHolder> implements View.OnClickListener {

    private ICommunityService mCommunityService;

    public CommunityHelloAdapter() {
        super(R.layout.item_community_hello);
        init();
    }

    public CommunityHelloAdapter(List<HelloBean> list) {
        super(R.layout.item_community_hello, list);
        init();
    }

    private void init() {
        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HelloBean item) {

        helper.setText(R.id.tv_time_day, item.getPublish_time_day() + "");
        helper.setText(R.id.tv_time_month, item.getPublish_time_month() + "月");

        String title = item.getType() == 1 ? "早安问候" : "晚安问候";
        helper.setText(R.id.tv_title, title);

        helper.setText(R.id.tv_content, StringUtil.html2Text(item.getContent()));
        helper.getView(R.id.tv_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardUtil.setPrimaryClip(StringUtil.html2Text(item.getContent()));
                ToastUtil.success("文案已复制");
                return true;
            }
        });


        final SimpleDraweeView simpleDraweeView = helper.getView(R.id.sdv_img);
        if (TextUtils.isEmpty(item.getImages_url())) {
            simpleDraweeView.setVisibility(View.GONE);
        } else {
            simpleDraweeView.setImageURI(item.getImages_url());
            simpleDraweeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageUtil.previewImageWithTransition(mContext, simpleDraweeView, item.getImages_url());
                }
            });
        }

        DDStatusButton shareView = helper.getView(R.id.dsb_share);
        shareView.setStatusCount(item.getTotalShareCount());
        shareView.setTag(item);
        shareView.setOnClickListener(this);

        DDStatusButton downloadView = helper.getView(R.id.dsb_download);
        downloadView.setStatusCount(item.getTotalDownloadCount());
        downloadView.setTag(item);
        downloadView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v != null && v.getTag() != null && v.getTag() instanceof HelloBean) {
            HelloBean item = (HelloBean) v.getTag();
            switch (v.getId()) {
                case R.id.dsb_share:
                    progressImage(item, true);
                    break;
                case R.id.dsb_download:
                    progressImage(item, false);
                    break;
            }
        } else {
            ToastUtil.error("数据异常");
        }
    }

    /**
     * 处理图片
     *
     * @param item    数据
     * @param isShare 是否是分享
     */
    public void progressImage(final HelloBean item, final boolean isShare) {
        CommunityImageMaker maker = new CommunityImageMaker(mContext);
        maker.setMode(CommunityImageMaker.Mode.Greeting);
        maker.setMergeImageUrl(item.getImages_url());

        //设置二维码类型
        int qrType = item.getQrCodeType();
        maker.setEventMode(qrType);

        //设置二维码地址
        String qrUrl = item.getQrCodeUrl();
        maker.setEventUrl(qrUrl);

        // 测试代码
//        maker.setEventMode(1);
//        maker.setEventUrl("http://ldmf.net");

        DLog.d("QR mode=" + qrType + ",url=" + qrUrl);

        maker.setListener(new CommunityImageMaker.CommunityImageMakeListener() {
            @Override
            public void onCommunityImageMakeStart() {
                ToastUtil.showLoading(mContext);
            }

            @Override
            public void onCommunityImageMakeReady(String pathToImage) {
                ClipboardUtil.setPrimaryClip(StringUtil.html2Text(item.getContent()));
                if (isShare) {
                    // 分享
                    DDShareWXDialog dialog = new DDShareWXDialog((Activity) mContext);
                    dialog.setMode(DDShareWXDialog.Mode.SingleImage);
                    dialog.setImgPath("" + pathToImage);
                    dialog.setShareListener(new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA share_media) {
                            DLog.i("onStart");
                            insertHelloCount(item, ICommunityService.TYPE_HELLO_SHARE);
                        }

                        @Override
                        public void onResult(SHARE_MEDIA share_media) {
                            //分享成功回调
                            ToastUtil.success("分享成功");
                        }

                        @Override
                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                        }

                        @Override
                        public void onCancel(SHARE_MEDIA share_media) {

                        }
                    });
                    dialog.show();
                    ToastUtil.success("文案已复制，可直接粘贴");
                } else {
                    // 下载
                    Bitmap bitmap = ImageTools.getSmallBitmap(pathToImage, ImageTools.greetingImageWidth, ImageTools.greetingImageHeight);
                    ImageTools.saveBitmapToAlbum(mContext, bitmap, "ddm_greeting_" + item.getId());
                    showResultDialog("文案已复制", "图片已保存至相册");
                    insertHelloCount(item, ICommunityService.TYPE_HELLO_DOWNLOAD);
                }
                ToastUtil.hideLoading();
            }

            @Override
            public void onCommunityImageMakeError(String error) {
                ToastUtil.hideLoading();
                ToastUtil.error(error);
            }
        });
        maker.make();
    }

    private void showResultDialog(String title, String content) {
        DDResDownloadDialog dialog = new DDResDownloadDialog(mContext);
        dialog.setMode(DDResDownloadDialog.DownloadMode.Image, true);
        dialog.show();
    }

    private void insertHelloCount(final HelloBean helloBean, final int type) {
        APIManager.startRequest(mCommunityService.increaseHelloCount(helloBean.getId(), type), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                if (type == ICommunityService.TYPE_HELLO_DOWNLOAD) {
                    // 下载
                    helloBean.setDown_num_real(helloBean.getDown_num_real() + 1);
                } else {
                    // 分享
                    helloBean.setShare_num_real(helloBean.getShare_num_real() + 1);
                }
                notifyItemChanged(helloBean);
            }

            @Override
            public void onError(Throwable e) {
            }
        });
    }

    private void notifyItemChanged(HelloBean helloBean) {
        int itemIndex = -1;
        for (int i = 0; i < getData().size(); i++) {
            if (helloBean == getData().get(i)) {
                itemIndex = i;
            }
        }
        DLog.i("notifyItemChanged index " + itemIndex);
        if (itemIndex > -1) {
            notifyItemChanged(itemIndex);
        } else {
            notifyDataSetChanged();
        }
    }


}
