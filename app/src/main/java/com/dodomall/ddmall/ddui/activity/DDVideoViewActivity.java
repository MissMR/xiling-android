package com.dodomall.ddmall.ddui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.community.VideoListener;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDVideoViewActivity extends BaseActivity {

    private static final String kVideo = "video";
    private static final String kImage = "image";

    public static void startWithTransition(Activity context, View shareView, String videoURL, String videoImageURL) {
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                shareView, "transition");
        context.startActivity(DDVideoViewActivity
                .newIntent(context, videoURL, videoImageURL), optionsCompat.toBundle());
    }

    public static Intent newIntent(Context context, String videoUrl, String imageUrl) {
        Intent intent = new Intent(context, DDVideoViewActivity.class);
        intent.putExtra(kVideo, videoUrl);
        intent.putExtra(kImage, imageUrl);
        return intent;
    }

    public static void start(Context context, String videoUrl, String imageUrl) {
        Intent intent = new Intent(context, DDVideoViewActivity.class);
        intent.putExtra(kVideo, videoUrl);
        intent.putExtra(kImage, imageUrl);
        context.startActivity(intent);
    }

    String videoUrl = "";
    String imageUrl = "";

    @OnClick(R.id.titleBackButton)
    void onTitleBackPressed() {
        onBackPressed();
    }

    ImageView thumbImageView = null;

    @BindView(R.id.videoPlayer)
    StandardGSYVideoPlayer gsyVideoPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video_preview);
        ButterKnife.bind(this);

        //设置全屏样式
        makeStatusBarTranslucent();

        videoUrl = getIntent().getStringExtra(kVideo);
        imageUrl = getIntent().getStringExtra(kImage);

        initView();
    }

    public void initView() {

        thumbImageView = new ImageView(context);
        thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        //增加title
        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setVisibility(View.GONE);

        if (TextUtils.isEmpty(videoUrl)) {
            ToastUtil.error("视频链接异常");
            finish();
        }

        if (!TextUtils.isEmpty(imageUrl)) {
            Glide.with(context).load(imageUrl).into(thumbImageView);
        }

        GSYVideoOptionBuilder gsyVideoOptionBuilder = new GSYVideoOptionBuilder();
        gsyVideoOptionBuilder
                .setIsTouchWiget(false)
                .setThumbImageView(thumbImageView)
                .setThumbPlay(true)
                .setUrl(videoUrl)
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
