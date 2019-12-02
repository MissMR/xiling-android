package com.dodomall.ddmall.module.publish;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.community.BasicActivity;
import com.dodomall.ddmall.module.community.TitleView;
import com.dodomall.ddmall.module.community.VideoListener;
import com.dodomall.ddmall.shared.Constants;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/4/17  14:11
 * @desc ${TODD}
 */

public class VideoPreViewActivity extends BasicActivity {

    @BindView(R.id.video_item_player)
    StandardGSYVideoPlayer gsyVideoPlayer;
    @BindView(R.id.titleView)
    TitleView mTitleView;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_preview;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTitleView.setRightCilckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        String data = getIntent().getStringExtra(Constants.KEY_EXTROS);
        String mediaUrl = getIntent().getStringExtra(Constants.KEY_MEDIAURL);
        ImageView imageView = new ImageView(mActivity);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        String realUrl = data;
        if (data.startsWith("content")) {
            GSYVideoManager.instance().setVideoType(this, 1);
            imageView.setImageBitmap(FileUtils.getVideoFirst(this, Uri.parse(data)));
        } else {
            realUrl = mediaUrl;
            Glide.with(this).load(data).into(imageView);
        }
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(imageView)
                .setThumbPlay(true)
                .setUrl(realUrl)
                .setCacheWithPlay(true)
                .setRotateViewAuto(true)
                .setLockLand(true)
                .setShowFullAnimation(true)
                .setNeedLockFull(true)
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
        gsyVideoPlayer.getFullscreenButton().setVisibility(View.GONE);
        //设置全屏按键功能
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
}
