package com.xiling.ddui.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.CommunityTopicDetailActivity;
import com.xiling.ddui.activity.DDVideoViewActivity;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.HProductPostBean;
import com.xiling.ddui.custom.DDResDownloadDialog;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.custom.DDStatusButton;
import com.xiling.ddui.custom.DDUnLikeDialog;
import com.xiling.ddui.custom.TextViewNoScrollMethod;
import com.xiling.ddui.custom.TopicTagHandler;
import com.xiling.ddui.manager.CommunityImageMaker;
import com.xiling.ddui.manager.CommunityShareManager;
import com.xiling.ddui.manager.DDCommunityShareManager;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.ImageTools;
import com.xiling.ddui.tools.TextTools;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.dialog.DDMProductQrCodeDialog;
import com.xiling.shared.constant.Event;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICommunityService;
import com.xiling.shared.util.ClipboardUtil;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ImageUtil;
import com.xiling.shared.util.StringUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import rx.Observer;

public class DDCommunityDataAdapter extends BaseAdapter<CommunityDataBean, RecyclerView.ViewHolder> {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_NORMAL = 1;

    ICommunityService iCommunity = null;
    private View mHeader;

    // 是否是圆角
    private boolean mHasCorner = true;

    public enum Mode {
        MaterialProduct,/*社区首页*/
        MaterialMarketing,/*营销素材 单图*/
        Topic,/*话题*/
        Like,
        Product/*商品详情页 素材列表 隐藏 建议时间发、话题*/
    }

    /**
     * 计数类型
     */
    public class CountType {
        //下载
        public static final int Download = 1;
        //喜欢
        public static final int LikeCommunity = 2;
        //分享
        public static final int Share = 3;
        //取消喜欢
        public static final int UnLikeCommunity = 4;
    }

    private Mode mode = Mode.MaterialProduct;

    public DDCommunityDataAdapter(Context context) {
        this(context, Mode.MaterialProduct);
    }

    public DDCommunityDataAdapter(Context context, Mode mode) {
        super(context);
        iCommunity = ServiceManager.getInstance().createService(ICommunityService.class);
        setMode(mode);
    }

    public void setHeader(View view) {
        mHeader = view;
        super.hasHeader = true;
        notifyItemInserted(0);
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setItemStyleCorner(boolean hasCorner) {
        this.mHasCorner = hasCorner;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder;
        if (mHeader != null && viewType == TYPE_HEADER) {
            holder = new HeaderHolder(mHeader);
        } else {
            holder = new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_item_community, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_HEADER) {
            return;
        }

        if (holder instanceof ViewHolder) {
            ViewHolder viewHolder = ((ViewHolder) holder);
            CommunityDataBean bean = items.get(getPosition(position));
            //设置数据
            viewHolder.setData(bean);
            //检查位置，最后一个元素开启下间隔
            if (mHasCorner) {
                viewHolder.setPosition(getPosition(position));
            }
            //渲染数据
            viewHolder.render();
        }

    }

    public int getPosition(int position) {
        return mHeader == null ? position : position - 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeader != null) {
            return TYPE_HEADER;
        }
        return TYPE_NORMAL;
    }

    public void setItemUnLike(String roundId) {
        if (TextUtils.isEmpty(roundId)) {
            return;
        }
        int index = getItemIndexByRoundId(roundId);

        if (index > -1) {
            CommunityDataBean bean = items.get(index);
            bean.setIsMyLike(0);
            bean.setLikeNum(Integer.valueOf(bean.getLikeNum()) - 1 + "");
            notifyItemChanged(index);
        }

    }

    private int getItemIndexByRoundId(String roundId) {
        if (TextUtils.isEmpty(roundId)) {
            return -1;
        }
        int itemIndex = -1;
        for (int i = 0; i < items.size(); i++) {
            if (roundId.equals(items.get(i).getRoundId())) {
                itemIndex = i;
                break;
            }
        }
        return mHeader == null ? itemIndex : ++itemIndex;
    }

    void notifyItemChange(CommunityDataBean communityDataBean) {
        if (communityDataBean == null) {
            return;
        }
        for (int i = 0; i < getItemCount(); i++) {
            if (communityDataBean == getItems().get(i)) {
                notifyItemChanged(mHeader == null ? i : ++i);
                return;
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void addItems(List<CommunityDataBean> items) {
        for (CommunityDataBean item : items) {
            this.items.add(item);
        }
        if (mHeader != null) {
            notifyItemRangeChanged(1, getItems().size());
        } else {
            notifyDataSetChanged();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        //用户头像
        @BindView(R.id.sdUserHeaderView)
        SimpleDraweeView sdUserHeaderView;

        //几点发的提示性文本
        @BindView(R.id.sendHintTextView)
        TextView sendHintTextView;

        //用户名称
        @BindView(R.id.sendUserNameTextView)
        TextView sendUserNameTextView;

        //发送时间
        @BindView(R.id.sendDateTimeTextView)
        TextView sendDateTimeTextView;

        //内容文本
        @BindView(R.id.contentTextView)
        TextView contentTextView;

        //全文 按钮
        @BindView(R.id.fullTextView)
        TextView fullButton;

        //视频区域
        @BindView(R.id.videoPanel)
        FrameLayout videoPanel;

        //视频预览图片
        @BindView(R.id.sdVideoMaskView)
        SimpleDraweeView sdVideoMaskView;

        //图片区域
        @BindView(R.id.photoPanel)
        RelativeLayout photoPanel;

        //显示九宫格图片的控件
        @BindView(R.id.plPhoto)
        BGANinePhotoLayout plPhoto;

        //商品区域
        @BindView(R.id.productPanel)
        RelativeLayout productPanel;

        //商品缩略图
        @BindView(R.id.sdProductView)
        SimpleDraweeView sdProductView;

        //商品名称
        @BindView(R.id.productNameTextView)
        TextView productNameTextView;

        //商品描述
        @BindView(R.id.productDescTextView)
        TextView productDescTextView;

        //商品价格区域
        @BindView(R.id.productPricePanel)
        LinearLayout productPricePanel;

        //商品价格
        @BindView(R.id.productPriceTextView)
        TextView productPriceTextView;

        //商品赚 - 提示
        @BindView(R.id.productRewardHintTextView)
        TextView productRewardHintTextView;

        //商品赚 - 价格
        @BindView(R.id.productRewardTextView)
        TextView productRewardTextView;

        //链接区域
        @BindView(R.id.linkPanel)
        RelativeLayout linkPanel;

        //链接区域的标题
        @BindView(R.id.linkTitleView)
        TextView linkTitleView;

        // 喜欢
        @BindView(R.id.dsb_like)
        DDStatusButton dsbLike;
        // 分享
        @BindView(R.id.dsb_share)
        DDStatusButton dsbShare;

        //下载
        @BindView(R.id.dsb_download)
        DDStatusButton dsbDownload;

        //更多按钮区域
        @BindView(R.id.morePanel)
        RelativeLayout morePanel;

        //更多按钮
        @BindView(R.id.moreImageView)
        ImageView moreButton;

        @BindView(R.id.blankView)
        View blankView;

        @BindView(R.id.materialProductPanel)
        View materialProductPanel;

        @BindView(R.id.materialMarketingPanel)
        View materialMarketingPanel;
        @BindView(R.id.sdv_img)
        SimpleDraweeView sdvMarketingImage;
        @BindView(R.id.videoMaskView2)
        ImageView videoMaskView2;
        @BindView(R.id.playVideoImageView2)
        ImageView playVideoImageView2;

        @BindView(R.id.ll_item_container)
        LinearLayout mLlItemContainer;
        @BindView(R.id.v_divider)
        View mDivider;

        boolean isFullText = false;
        private CommunityDataBean data = null;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            //根据模式来控制布局
            if (mode == Mode.MaterialProduct) {
                sendHintTextView.setVisibility(View.VISIBLE);
                dsbLike.setVisibility(View.VISIBLE);
                moreButton.setVisibility(View.GONE);
            } else if (mode == Mode.Topic) {
                sendHintTextView.setVisibility(View.GONE);
                dsbLike.setVisibility(View.VISIBLE);
                moreButton.setVisibility(View.GONE);
            } else if (mode == Mode.Product) {
                sendHintTextView.setVisibility(View.GONE);
                productPanel.setVisibility(View.GONE);
                moreButton.setVisibility(View.GONE);
            } else if (mode == Mode.MaterialMarketing) {
                sendHintTextView.setVisibility(View.VISIBLE);
                dsbLike.setVisibility(View.GONE);
                moreButton.setVisibility(View.GONE);
                productPanel.setVisibility(View.GONE);
                materialMarketingPanel.setVisibility(View.VISIBLE);
                materialProductPanel.setVisibility(View.GONE);
                clearShareAlignParentRight();
            } else {
                // 喜欢
                sendHintTextView.setVisibility(View.GONE);
                dsbLike.setVisibility(View.GONE);
                moreButton.setVisibility(View.VISIBLE);

                clearShareAlignParentRight();
            }
            setItemStyle();
            //设置发圈内容响应HTML标记 <a></a>
            contentTextView.setMovementMethod(TextViewNoScrollMethod.getInstance());
            contentTextView.setClickable(true);

            //查看大图的事件绑定
            plPhoto.setDelegate(new BGANinePhotoLayout.Delegate() {
                @Override
                public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
                    HProductPostBean bean = data.getPostImages().get(position);
                    if (bean.getType() == HProductPostBean.PostType.Product) {
                        SkuInfo skuInfo = bean.getProduct();
                        showProductQRDialog(skuInfo);
                    } else {
                        ArrayList<String> urls = data.getPostUrls();
                        ImageUtil.previewImageWithTransition(context, plPhoto, urls, position);
                    }
                }
            });
        }

        private void clearShareAlignParentRight() {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) dsbShare.getLayoutParams();
            layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            dsbShare.setLayoutParams(layoutParams);
        }

        private void setItemStyle() {
            if (!mHasCorner) {
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mLlItemContainer.getLayoutParams();
                layoutParams.rightMargin = 0;
                layoutParams.leftMargin = 0;
                mLlItemContainer.setLayoutParams(layoutParams);

                mDivider.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams dividerLayoutParams = (LinearLayout.LayoutParams) mDivider.getLayoutParams();
                dividerLayoutParams.topMargin = 0;
                mDivider.setLayoutParams(dividerLayoutParams);
            }
        }

        public void setPosition(int position) {
            int count = getItemCount();
            if (count > 0) {
                if (position == count - 1) {
                    blankView.setVisibility(View.VISIBLE);
                } else {
                    blankView.setVisibility(View.GONE);
                }
            } else {
                blankView.setVisibility(View.VISIBLE);
            }
        }

        public void setData(CommunityDataBean data) {
            data.mergerImage();
            this.data = data;
//            this.data.setVideoPlayUrl("https://oss.dodomall.com/ueditor/1539157139028.mp4");
//            this.data.setVideoImageUrl("https://oss.dodomall.com/ueditor/1539157086148.jpg");
        }

        public void render() {

            //建议发圈时间
            String sendHint = data.getSuggestShareDateTime();
            sendHintTextView.setText("建议" + sendHint + "发");

            //设置发圈用户头像
            FrescoUtil.setImageSmall(sdUserHeaderView, data.getHeadImage());
            //设置发圈用户姓名
            sendUserNameTextView.setText("" + data.getNickName());
            //设置 - 发圈 优化显示 时间
            sendDateTimeTextView.setText("" + data.getDifftime());

            //设置视频和照片数据
            String videoUrl = data.getVideoPlayUrl();
            if (!TextUtils.isEmpty(videoUrl)) {
                //有视频数据
                showVideoPanel();
            } else {
                //无视频数据
                ArrayList<String> photoArray = data.getPostUrls();
//                photoArray.add("https://oss.dodomall.com/communityAccount/2018-10/20181010101522.jpg");
                if (photoArray != null && photoArray.size() > 0) {
                    //有照片数据
                    showPhotoPanel(photoArray);
                } else {
                    //无照片数据
                    hidePhotoAndVideoPanel();
                }
            }

            String spuId = data.getProductId();
            if (TextUtils.isEmpty(spuId)) {
                //隐藏商品数据
                productPanel.setVisibility(View.GONE);
            } else {
                //设置商品缩略图数据
                FrescoUtil.setImageSmall(sdProductView, data.getThumbUrlForShopNow());
                //设置商品名称
                productNameTextView.setText("" + data.getSkuName());
                //设置商品描述
                String desc = data.getIntro();
                if (TextUtils.isEmpty(desc)) {
                    desc = "暂无描述";
                }
                productDescTextView.setText("" + desc);
                //设置商品标价
                long price = data.getRetailPrice();
                productPriceTextView.setText("" + ConvertUtil.cent2yuan2(price));
                //设置商品分享赚
                String rewardPrice = ConvertUtil.cent2yuan2(data.getRewardPrice());
                productRewardTextView.setText("" + rewardPrice);
                //显示商品数据
                if (mode == Mode.Product) {
                    productPanel.setVisibility(View.GONE);
                } else {
                    productPanel.setVisibility(View.VISIBLE);
                }
            }

            //检测是否有链接数据 新增链接需求。
            progressLinkData();

            dsbLike.setSelected(data.getIsMyLike() == 1);
            //喜欢数据量
            String likeCount = ConvertUtil.toShowCount(data.getLikeNum());
            dsbLike.setStatusCount(String.valueOf(likeCount));

            //分享数据量
            String shareCount = ConvertUtil.toShowCount(data.getShareNum());
            dsbShare.setStatusCount(String.valueOf(shareCount));

            //下载数据量
            String downloadCount = ConvertUtil.toShowCount(data.getDownNum());
            dsbDownload.setStatusCount(String.valueOf(downloadCount));

            //处理发圈内容数据
            String topicId = data.getTopicId();//话题ID
            String title = data.getTitle();//话题文本
            String content = data.getContent();//详情内容

            handlerContent(topicId, title, content);
        }

        void showProductQRDialog(SkuInfo skuInfo) {
            if (skuInfo == null) {
                ToastUtil.error("商品信息错误!");
                return;
            }

            DLog.i("jump product qr dialog:" + skuInfo.productId);

            String url = skuInfo.getProductUrl();
            UMWeb mWeb = new UMWeb(url);
            mWeb.setTitle(skuInfo.name);
            mWeb.setDescription(skuInfo.desc);
            mWeb.setThumb(new UMImage(context, skuInfo.thumbURL));

            DDMProductQrCodeDialog qrCodeDialog = new DDMProductQrCodeDialog((Activity) context, skuInfo, mWeb, new UMShareListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onResult(SHARE_MEDIA share_media) {

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

        void showPhotoPanel(ArrayList<String> photoArray) {

            StringBuffer buffer = new StringBuffer();
            for (String url : photoArray) {
                buffer.append("\t" + url + "\n");
            }
            DLog.d("image urls:\n" + buffer);

            if (mode == Mode.MaterialMarketing) {
                videoMaskView2.setVisibility(View.GONE);
                playVideoImageView2.setVisibility(View.GONE);
                sdvMarketingImage.setImageURI(photoArray.get(0));
            } else {
                videoPanel.setVisibility(View.GONE);
                photoPanel.setVisibility(View.VISIBLE);
                plPhoto.setData(photoArray);
            }

        }

        void showVideoPanel() {
            if (mode == Mode.MaterialMarketing) {
                videoMaskView2.setVisibility(View.VISIBLE);
                playVideoImageView2.setVisibility(View.VISIBLE);
                FrescoUtil.setImageSmall(sdvMarketingImage, data.getVideoImageUrl());
            } else {
                videoPanel.setVisibility(View.VISIBLE);
                photoPanel.setVisibility(View.GONE);
                plPhoto.setData(new ArrayList<String>());
                //设置视频缩略图
                FrescoUtil.setImageSmall(sdVideoMaskView, data.getVideoImageUrl());
            }
        }

        /**
         * 处理话题内容
         *
         * @param topicId 话题Id
         * @param title   话题名字
         * @param content 话题内容
         */
        void handlerContent(String topicId, String title, String content) {

            DLog.i("content:" + content);

            Spanned topicTitle = null;

            if (TextUtils.isEmpty(topicId) || mode == Mode.Product) {
                topicTitle = TextTools.fromHtml("" + content + "");
            } else {
                if (TextUtils.isEmpty(title)) {
                    title = " ";
                }
                topicTitle = TextTools.fromHtml("<font color=\"#FFA304\"><topic>#" + title + "#</topic></font>" + content + "",
                        new TopicTagHandler(topicId, new TopicTagHandler.TopicTagListener() {
                            @Override
                            public void onTopicTagPressed(String topicId) {
                                DLog.i("onTopicTagPressed:" + topicId);
                                //跳转话题详情界面
                                Intent intent = new Intent(context, CommunityTopicDetailActivity.class);
                                intent.putExtra(Constants.Extras.TOPIC_ID, topicId);
                                context.startActivity(intent);
                            }
                        }));
            }
            contentTextView.setText(topicTitle);
            fullButton.setVisibility(data.isShowedFullButton() ? View.VISIBLE : View.GONE);
            contentTextView.post(new Runnable() {
                @Override
                public void run() {
                    int line = contentTextView.getLineCount();
                    DLog.i("line:" + line);
                    if (line > 3) {
                        fullButton.setVisibility(View.VISIBLE);
                        data.setShowedFullButton(true);
                    } else {
                        fullButton.setVisibility(View.GONE);
                    }
                }
            });
        }

        void hidePhotoAndVideoPanel() {
            videoPanel.setVisibility(View.GONE);
            photoPanel.setVisibility(View.GONE);
            plPhoto.setData(new ArrayList<String>());

            materialMarketingPanel.setVisibility(View.GONE);

        }

        void progressLinkData() {
            String link = data.getRelationLink();
            if (TextUtils.isEmpty(link)) {
                hideLink();
            } else {
                showLink();
            }
        }

        /**
         * 显示链接区域
         */
        void showLink() {
            String linkTitle = data.getRelationLinkWord();
            linkTitleView.setText("" + linkTitle);
            linkPanel.setVisibility(View.VISIBLE);
        }

        /**
         * 隐藏链接区域
         */
        void hideLink() {
            linkPanel.setVisibility(View.GONE);
        }

        //链接区域被点击
        @OnClick(R.id.linkPanel)
        void onLinkPanelPressed() {
            String link = data.getRelationLink();
            if (!TextUtils.isEmpty(link)) {
                WebViewActivity.jumpUrl(context, link);
            } else {
                DLog.d("onLinkPanelPressed:" + link);
            }
        }

        @OnClick(R.id.sdv_img)
        void onMarketingImageClicked() {
            // 营销素材图片点击
            ImageUtil.previewImageWithTransition(context, sdvMarketingImage, data.getImagesUrlList().get(0));
        }

        @OnClick(R.id.moreImageView)
        void onMorePressed() {
            //取消喜欢确认对话框
            DDUnLikeDialog unLikeDialog = new DDUnLikeDialog(context);
            unLikeDialog.setCancelLikeEvent(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DLog.d("My Community Data Cancel Like");
                    unlikeRowData();
                }
            });
            unLikeDialog.show();
        }

        @OnClick(R.id.dsb_like)
        void onLikePressed() {
            if (data.getIsMyLike() == 1) {
                //取消喜欢确认对话框
                DDUnLikeDialog unLikeDialog = new DDUnLikeDialog(context);
                unLikeDialog.setCancelLikeEvent(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DLog.d("Community Cancel Like");
                        unlikeRowData();
                    }
                });
                unLikeDialog.show();
            } else {
                likeRowData();
            }
        }

        /**
         * 喜欢这条素材
         */
        private void likeRowData() {
            addCount(CountType.LikeCommunity, new RequestListener<Object>() {
                @Override
                public void onStart() {
                    ToastUtil.showLoading(context);
                }

                @Override
                public void onSuccess(Object result, String msg) {
                    super.onSuccess(result);
                    data.setIsMyLike(1);
                    long count = ConvertUtil.add1Count(data.getLikeNum());
                    if (count > -1) {
                        data.setLikeNum("" + count);
                    }

                    notifyItemChange(data);
                    ToastUtil.hideLoading();
                    ToastUtil.success(msg);
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.hideLoading();
                    ToastUtil.error("" + e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        }

        /**
         * 增加分享次数
         */
        void addShareCount() {
            DLog.i("增加分享次数");

            addCount(CountType.Share, new RequestListener<Object>() {
                @Override
                public void onStart() {
                    ToastUtil.showLoading(context);
                }

                @Override
                public void onSuccess(Object result) {
                    super.onSuccess(result);
                    DLog.i("已增加一次分享");
                    long count = ConvertUtil.add1Count(data.getShareNum());
                    if (count > -1) {
                        data.setShareNum("" + count);
                        notifyItemChange(data);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.hideLoading();
                    DLog.w(e.getMessage());
                }

                @Override
                public void onComplete() {
                    ToastUtil.hideLoading();
                }
            });
        }

        /**
         * 取消喜欢这条素材
         */
        private void unlikeRowData() {
            addCount(CountType.UnLikeCommunity, new RequestListener<Object>() {
                @Override
                public void onStart() {
                    ToastUtil.showLoading(context);
                }

                @Override
                public void onSuccess(Object result, String msg) {
                    super.onSuccess(result);
                    if (mode == Mode.Like) {
                        //我的喜欢素材的时候要移除该条数据
                        items.remove(data);
                        EventMessage eventMessage = new EventMessage(Event.materialUnLike, data.getRoundId());
                        EventBus.getDefault().post(eventMessage);
                    } else {
                        //非我的喜欢的时候改变喜欢的状态
                        data.setIsMyLike(0);
                        long count = ConvertUtil.reduce1Count(data.getLikeNum());
                        if (count > -1) {
                            data.setLikeNum("" + count);
                        }
                    }

                    notifyItemChange(data);
                    ToastUtil.hideLoading();
                    ToastUtil.success(msg);
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.hideLoading();
                    ToastUtil.error("" + e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });
        }

        /**
         * 增加下载次数
         */
        void addDownloadCount() {
            DLog.i("增加下载次数");

            addCount(CountType.Download, new RequestListener<Object>() {
                @Override
                public void onStart() {
                    ToastUtil.showLoading(context);
                }

                @Override
                public void onSuccess(Object result) {
                    super.onSuccess(result);
                    DLog.i("已增加一次下载");
                    long count = ConvertUtil.add1Count(data.getDownNum());
                    if (count > -1) {
                        data.setDownNum("" + count);
                        notifyItemChange(data);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    ToastUtil.hideLoading();
                    DLog.w(e.getMessage());
                }

                @Override
                public void onComplete() {
                    ToastUtil.hideLoading();
                }
            });
        }

        //增加数据次数
        void addCount(int type, RequestListener<Object> listener) {
            String roundId = data.getRoundId();
            APIManager.startRequest(iCommunity.submitCommunityCount(roundId, type), listener);
        }

        CommunityShareManager.CommunityShareListener shareListener = new CommunityShareManager.CommunityShareListener() {
            @Override
            public void onShareStart() {
                DLog.i("share -> onShareStart");
                addShareCount();
            }

            @Override
            public void onShareSuccess() {
                DLog.i("share -> onShareSuccess");
            }

            @Override
            public void onDownloadSuccess() {
                DLog.i("download -> onDownloadSuccess");
                addDownloadCount();
            }
        };

        @OnClick(R.id.dsb_download)
        void onDownloadPressed() {
            DLog.d("onDownloadPressed");
            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Permission>() {
                        @Override
                        public void onCompleted() {
                            DLog.i("STORAGE.onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            DLog.e("STORAGE.onError:" + e.getMessage());
                            ToastUtil.error("申请存储权限失败，请前往APP应用设置中打开此权限");
                        }

                        @Override
                        public void onNext(Permission permission) {
                            if (permission.granted) {
                                if (checkMaterialProductAction()) {
                                    downloadMaterialProduct();
                                } else {
                                    handleMaterialMarketingImage(false);
                                }
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                ToastUtil.error("缺少存储权限，请同意位置权限的申请");
                            } else {
                                ToastUtil.error("缺少存储权限，请前往APP应用设置中打开此权限");
                            }
                        }
                    });
        }

        /**
         * 视频 和 九宫格图片的操作
         *
         * @return
         */
        private boolean checkMaterialProductAction() {
            return mode != Mode.MaterialMarketing || !TextUtils.isEmpty(data.getVideoPlayUrl());
        }

        // 商品素材 下载
        private void downloadMaterialProduct() {
            DDCommunityShareManager manager = new DDCommunityShareManager((Activity) context, data);
            manager.setListener(shareListener);
            manager.download();
        }

        @OnLongClick(R.id.contentTextView)
        boolean onContentLongClicked() {
            ClipboardUtil.setPrimaryClip(StringUtil.html2Text(data.getContent()));
            ToastUtil.success("文案已复制");
            return true;
        }

        @OnClick({R.id.dsb_share})
        void onSharePressed() {
            DLog.d("onSharePressed");
            RxPermissions rxPermissions = new RxPermissions((Activity) context);
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Observer<Permission>() {
                        @Override
                        public void onCompleted() {
                            DLog.i("LOCATION.onCompleted");
                        }

                        @Override
                        public void onError(Throwable e) {
                            DLog.e("LOCATION.onError:" + e.getMessage());
                            ToastUtil.error("申请存储权限失败，请前往APP应用设置中打开此权限");
                        }

                        @Override
                        public void onNext(Permission permission) {
                            if (permission.granted) {
                                if (checkMaterialProductAction()) {
                                    shareMaterialProduct();
                                } else {
                                    handleMaterialMarketingImage(true);
                                }
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                ToastUtil.error("缺少存储权限，请同意存储权限的申请");
                            } else {
                                ToastUtil.error("缺少存储权限，请前往APP应用设置中打开此权限");
                            }
                        }
                    });
        }

        /**
         * 营销素材下载 / 一键分享
         *
         * @param isShare 是否是分享操作
         */
        public void handleMaterialMarketingImage(final boolean isShare) {
            CommunityImageMaker maker = new CommunityImageMaker(context);
            maker.setMode(CommunityImageMaker.Mode.Greeting);

            //设置二维码类型
            int qrType = data.getQrCodeType();
            maker.setEventMode(qrType);

            //设置二维码地址
            String qrUrl = data.getQrCodeUrl();
            maker.setEventUrl(qrUrl);

//            // 测试代码
//            maker.setEventMode(1);
//            maker.setEventUrl("http://ldmf.net");
            DLog.d("QR mode=" + qrType + ",url=" + qrUrl);

            maker.setMergeImageUrl(data.getImagesUrlList().get(0));
            maker.setListener(new CommunityImageMaker.CommunityImageMakeListener() {
                @Override
                public void onCommunityImageMakeStart() {
                    ToastUtil.showLoading(context);
                }

                @Override
                public void onCommunityImageMakeReady(String pathToImage) {
                    ClipboardUtil.setPrimaryClip(StringUtil.html2Text(data.getContent()));
                    if (isShare) {
                        // 分享
                        DDShareWXDialog dialog = new DDShareWXDialog((Activity) context);
                        dialog.setMode(DDShareWXDialog.Mode.SingleImage);
                        dialog.setImgPath("" + pathToImage);
                        dialog.setShareListener(new UMShareListener() {
                            @Override
                            public void onStart(SHARE_MEDIA share_media) {
                                DLog.i("onStart");
                                addShareCount();
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
                        ImageTools.saveBitmapToAlbum(context, bitmap, "ddm_greeting_" + data.getRoundId());
                        showResultDialog("文案已复制", "图片已保存至相册");
                        addDownloadCount();
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
            DDResDownloadDialog dialog = new DDResDownloadDialog(context);
            dialog.setMode(DDResDownloadDialog.DownloadMode.Image, true);
            dialog.show();
        }

        /**
         * 分享商品素材
         */
        private void shareMaterialProduct() {
            //移除单商品关联的分享逻辑
            ToastUtil.success("文案已复制，可直接粘贴");
            //调用新的分享控制器
            DDCommunityShareManager manager = new DDCommunityShareManager((Activity) context, data);
            manager.setListener(shareListener);
            manager.show();
        }

        @OnClick(R.id.fullTextView)
        void onFullTextPressed() {
            DLog.d("onFullTextPressed");
            if (isFullText) {
                contentTextView.setMaxLines(3);
                fullButton.setText("全文");
            } else {
                int line = contentTextView.getLineCount();
                contentTextView.setMaxLines(line);
                fullButton.setText("收起");
            }
            isFullText = !isFullText;
        }

        @OnClick(R.id.productPanel)
        void onProductPressed() {
            DLog.d("onProductPressed:" + data.getProductId() + "," + data.getSkuName());
            //跳转商品详情
            EventUtil.viewProductDetail(context, data.getProductId());
        }

        @OnClick({R.id.videoPanel, R.id.videoMaskView, R.id.sdVideoMaskView, R.id.playVideoImageView,
                R.id.playVideoImageView2, R.id.videoMaskView2})
        void onVideoPressed() {
            String videoUrl = data.getVideoPlayUrl();
            String imageUrl = data.getVideoImageUrl();
            DLog.d("onVideoPressed:" + videoUrl);
            View view = mode == Mode.MaterialMarketing ? sdvMarketingImage : videoPanel;
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    view, "transition");
            context.startActivity(DDVideoViewActivity.newIntent(context, videoUrl, imageUrl), optionsCompat.toBundle());
        }

    }

    public static class HeaderHolder extends RecyclerView.ViewHolder {
        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

}
