package com.dodomall.ddmall.module.community;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.squareup.picasso.Picasso;
import com.dodomall.ddmall.R;

/**
 * @author Stone
 * @time 2018/4/19  10:34
 * @desc ${TODD}
 */

public class VideoDetailActivity extends CourseDetailActivity {

    private StandardGSYVideoPlayer mVideo_player;
    private TextView mVideoTitle;
    private TextView browerNumTv;
    private TextView leaveMsgTv;
    private TextView tvContent;
    private View commentLl;
    private TextView mCommentTv;

    @Override
    protected void updateData(Course detail) {
        mVideoTitle.setText(detail.getTitle());
        browerNumTv.setText(detail.getBrowseCount());
        leaveMsgTv.setText(detail.getCommentCount());
        tvContent.setText(detail.getIntro());
        mCommentTv.setText("评论（"+detail.getCommentCount()+"）");
        setVideo(detail);
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        setTitle("课程详情");
    }

    @Override
    public void changeCommentNub(Course course) {
        mCommentTv.setText("评论（"+course.getCommentCount()+"）");
    }

    private void setVideo(Course detail) {
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Picasso.with(this).load(detail.getThumbUrl()).placeholder(R.drawable.img_default).
                error(R.drawable.img_default).into(imageView);
        if (imageView.getParent() != null) {
            ViewGroup viewGroup = (ViewGroup) imageView.getParent();
            viewGroup.removeView(imageView);
        }
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setThumbPlay(true)
                .setUrl(detail.getAudioUrl())
                .setVideoTitle(detail.getTitle())
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
                .build(mVideo_player);
        //增加title
        mVideo_player.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        mVideo_player.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        mVideo_player.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideo_player.startWindowFullscreen(VideoDetailActivity.this, true, true);
            }
        });
    }

    @Override
    protected View createHeaderView() {
        View headView = getLayoutInflater().inflate(R.layout.layout_video_course, null);
        mVideo_player = (StandardGSYVideoPlayer) headView.findViewById(R.id.video_player);
        mVideoTitle = (TextView) headView.findViewById(R.id.video_title_tv);
        browerNumTv = (TextView) headView.findViewById(R.id.brower_num_tv);
        leaveMsgTv = (TextView) headView.findViewById(R.id.leave_msg_tv);
        tvContent = (TextView) headView.findViewById(R.id.tvContent);
        commentLl = headView.findViewById(R.id.comment_ll);
        mCommentTv = (TextView) headView.findViewById(R.id.tv_commont_num);

        return headView;
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoPlayer.releaseAllVideos();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        GSYVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
