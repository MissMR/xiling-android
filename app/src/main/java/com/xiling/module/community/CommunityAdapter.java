package com.xiling.module.community;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.squareup.picasso.Picasso;
import com.xiling.R;
import com.xiling.shared.component.ExpandableTextView;
import com.xiling.shared.util.TextViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Stone
 * @time 2018/1/11  10:32
 * @desc ${TODD}
 */

public class CommunityAdapter extends BaseMultiItemQuickAdapter<CommunityMultiItem, BaseViewHolder> {
    public final static String TAG = "RecyclerView2List";
    private final int mTextWidth;
    private boolean mDetailModel;

    public CommunityAdapter(List<CommunityMultiItem> data, boolean detailModel) {
        super(data);
        addItemType(CommunityMultiItem.ITEM_TYPE_TEXT, R.layout.item_type_text_review);
        addItemType(CommunityMultiItem.ITEM_TYPE_VIDEO, R.layout.item_type_video_review);
        addItemType(CommunityMultiItem.ITEM_TYPE_LINK, R.layout.item_type_link_review);
        addItemType(CommunityMultiItem.ITEM_TYPE_COMMENT, R.layout.item_type_comment);
        mTextWidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(80);
        mDetailModel = detailModel;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CommunityMultiItem communityMultiItem) {
        int itemViewType = baseViewHolder.getItemViewType();
        switch (itemViewType) {
            case CommunityMultiItem.ITEM_TYPE_TEXT:
                setInfoData(baseViewHolder, communityMultiItem.getContent());
                setImageViewData(baseViewHolder, communityMultiItem.getContent());
                baseViewHolder.addOnClickListener(R.id.save_tv)
                        .addOnClickListener(R.id.forward_tv)
                        .addOnClickListener(R.id.lease_msg_tv)
                        .addOnClickListener(R.id.like_layout);
                onlyGroupHasLayout(baseViewHolder, communityMultiItem);
                break;
            case CommunityMultiItem.ITEM_TYPE_LINK:
                setInfoData(baseViewHolder, communityMultiItem.getContent());
                baseViewHolder.addOnClickListener(R.id.save_tv)
                        .addOnClickListener(R.id.forward_tv)
                        .addOnClickListener(R.id.lease_msg_tv)
                        .addOnClickListener(R.id.like_layout)
                        .addOnClickListener(R.id.item_link_layout);
                onlyGroupHasLayout(baseViewHolder, communityMultiItem);
                baseViewHolder.setText(R.id.item_link_title, communityMultiItem.getContent().getLinkTitle());
                Picasso.with(mContext).load(communityMultiItem.getContent()
                        .getLinkImage())
                        .placeholder(R.drawable.img_default)
                        .error(R.drawable.img_default)
                        .into((ImageView) baseViewHolder.getView(R.id.item_link_iv));
                break;
            case CommunityMultiItem.ITEM_TYPE_VIDEO:
                setInfoData(baseViewHolder, communityMultiItem.getContent());
                baseViewHolder.addOnClickListener(R.id.save_tv)
                        .addOnClickListener(R.id.forward_tv);
                setVideoItemData(communityMultiItem.getContent(), baseViewHolder);
                //                    onlyGroupHasLayout(baseViewHolder, communityMultiItem);
                break;
            case CommunityMultiItem.ITEM_TYPE_COMMENT:
                TextView contentTv = baseViewHolder.getView(R.id.item_assent_content_tv);
                MaterialVideoModule.CommentModule commentModule = communityMultiItem.getCommentModule();
                TextViewUtil.setTagColorTitle(contentTv, commentModule.getContent(), commentModule.getNickName());
                baseViewHolder.setText(R.id.item_send_data_tv, DateUtils.getDateString(commentModule.getUpdateDate()));
                if (baseViewHolder.getLayoutPosition() == getData().size() - 1) {
                    baseViewHolder.setVisible(R.id.bottom_line, communityMultiItem.needCommondLine());
                }
                break;
            default:
                break;
        }
    }

    private void onlyGroupHasLayout(BaseViewHolder baseViewHolder, CommunityMultiItem communityMultiItem) {
        if (communityMultiItem.getCommunityType() != GroupFragment.CommunityType.TYPE_GROUP) {
            baseViewHolder.setVisible(R.id.lease_msg_tv, false)
                    .setVisible(R.id.like_layout, false);
        } else {
            setCommentData(baseViewHolder, communityMultiItem.getContent());
            setLikeAndMsgCount(baseViewHolder, communityMultiItem.getContent());
            baseViewHolder.setVisible(R.id.item_bottom_line, communityMultiItem.isNeedBottomLine());
        }
    }

    /**
     * 设置视频的数据
     *
     * @param videoModel     videoModel
     * @param baseViewHolder baseViewHolder
     */
    private void setVideoItemData(MaterialVideoModule videoModel, BaseViewHolder baseViewHolder) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        if (!TextUtils.isEmpty(videoModel.getMediaImage())) {
            Picasso.with(mContext).load(videoModel.getMediaImage()).placeholder(R.drawable.img_default).
                    error(R.drawable.img_default).into(imageView);
        }
        if (imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) imageView.getParent();
            viewGroup.removeView(imageView);
        }
        int adapterPosition = baseViewHolder.getAdapterPosition();
        final StandardGSYVideoPlayer gsyVideoPlayer = baseViewHolder.getView(R.id.video_item_player);

        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setThumbPlay(true)
                .setUrl(videoModel.getMediaUrl())
                .setVideoTitle(videoModel.getMediaTitle())
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setPlayTag(TAG)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .setPlayPosition(adapterPosition)
                .setStandardVideoAllCallBack(new VideoListener() {
                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                        if (!gsyVideoPlayer.isIfCurrentIsFullscreen()) {
                            //静音
                            GSYVideoManager.instance().setNeedMute(false);
                        }
                    }

                    @Override
                    public void onQuitFullscreen(String url, Object... objects) {
                        super.onQuitFullscreen(url, objects);
                        //全屏不静音
                        GSYVideoManager.instance().setNeedMute(false);
                    }

                    @Override
                    public void onEnterFullscreen(String url, Object... objects) {
                        super.onEnterFullscreen(url, objects);
                        GSYVideoManager.instance().setNeedMute(false);
                    }
                }).build(gsyVideoPlayer);
        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gsyVideoPlayer.startWindowFullscreen(mContext, true, true);
            }
        });

    }


    /**
     * 设置和imageview类型的数据
     *
     * @param baseViewHolder baseViewHolder
     * @param content        content
     */
    private void setImageViewData(BaseViewHolder baseViewHolder, final MaterialVideoModule content) {
        final GridView gridView = baseViewHolder.getView(R.id.user_pic_gv);
        ArrayList<String> images = content.getImages();
        int columns = 3;
        if (images.size() == 1) {
            columns = 1;
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            layoutParams.width = ScreenUtils.getScreenWidth() / 2;
            gridView.setLayoutParams(layoutParams);
        } else {
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            layoutParams.width = ScreenUtils.getScreenWidth();
            gridView.setLayoutParams(layoutParams);
        }
        gridView.setNumColumns(columns);
        gridView.setAdapter(new GalleryAdapter(mContext, content.getImages()));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                toPreview(content.getImages(), position);
            }
        });
    }

    /**
     * 跳转到预览界面
     *
     * @param images   all Views
     * @param position position
     */
    private void toPreview(ArrayList<String> images, int position) {
        Intent intent = new Intent(mContext, ImagePreviewActivity.class);
        intent.putExtra(ImagePreviewActivity.MULTI_IMAGES, images);
        intent.putExtra(ImagePreviewActivity.POSITION, position);
        mContext.startActivity(intent);
    }

    /**
     * 设置评论的数据和布局
     *
     * @param baseViewHolder baseViewHolder
     * @param content        content
     */
    private void setCommentData(BaseViewHolder baseViewHolder, MaterialVideoModule content) {
        ArrayList<MaterialVideoModule.CommentModule> comments = content.getComments();
        baseViewHolder.setVisible(R.id.assient_layout, false);
        if (comments != null && comments.size() > 0) {
            baseViewHolder.setVisible(R.id.assient_layout, true);
            addCommentLayout(comments, (LinearLayout) baseViewHolder.getView(R.id.assient_layout));
        }
    }

    /**
     * 设置头部的信息
     *
     * @param baseViewHolder baseViewHolder
     * @param module         module
     */
    private void setInfoData(BaseViewHolder baseViewHolder, final MaterialVideoModule module) {
        baseViewHolder.addOnClickListener(R.id.id_expand_textview);
        baseViewHolder.setText(R.id.user_name_tv, module.getAuthorName() + "");
        baseViewHolder.setText(R.id.send_data_tv, module.getCreateDate() + "");

        String content = module.getContent() + "";
        baseViewHolder.setVisible(R.id.user_send_content_tv, !TextUtils.isEmpty(content));
        final com.xiling.shared.component.ExpandableTextView tvContent = baseViewHolder.getView(R.id.user_send_content_tv);
        tvContent.setText(module.getContent(), module.isCollapsed);
//        tvContent.setText(module.getContent(), content.length() % 2 == 0);

        tvContent.setListener(new ExpandableTextView.OnExpandStateChangeListener() {
            @Override
            public void onExpandStateChanged(boolean isExpanded) {
                module.isCollapsed = isExpanded;
            }
        });
        tvContent.setMaxExpandLines(mDetailModel ? 100 : 5);


        if (!StringUtils.isEmpty(module.getAuthorHeadImage())) {
            Picasso.with(mContext).load(module.getAuthorHeadImage()).placeholder(R.drawable.img_default).
                    error(R.drawable.img_default).into((ImageView) baseViewHolder.getView(R.id.user_pic_iv));
        } else {
            ((ImageView) baseViewHolder.getView(R.id.user_pic_iv)).setImageResource(R.drawable.img_default);
        }
    }


    /**
     * 添加评论的布局
     *
     * @param comments comments
     * @param layout   layout
     */
    private void addCommentLayout(ArrayList<MaterialVideoModule.CommentModule> comments, LinearLayout layout) {
        layout.removeAllViews();
        for (MaterialVideoModule.CommentModule commentModule : comments) {
            View view = View.inflate(mContext, R.layout.layout_item_assient, null);
            TextView commentContentTv = (TextView) view.findViewById(R.id.item_assent_content_tv);
            TextView commentDataTv = (TextView) view.findViewById(R.id.item_send_data_tv);
            TextViewUtil.setTagColorTitle(commentContentTv, commentModule.getContent(), commentModule.getNickName());
            commentDataTv.setText(DateUtils.getDateString(commentModule.getUpdateDate()));
            layout.addView(view);
        }
    }


    private void setLikeAndMsgCount(BaseViewHolder baseViewHolder, MaterialVideoModule content) {
        int likeStatus = content.getLikeStatus();
        ((ImageView) baseViewHolder.getView(R.id.live_iv))
                .setImageResource(likeStatus == 1 ? R.drawable.ic_like_click : R.drawable.ic_like_default);
        baseViewHolder.setText(R.id.live_tv, String.valueOf(content.getLikeCount()));
        baseViewHolder.setText(R.id.lease_msg_tv, content.getCommentCount());
    }

    public void setDetailModel(boolean detailModel) {
        mDetailModel = detailModel;
    }
}
