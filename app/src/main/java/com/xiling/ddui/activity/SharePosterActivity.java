package com.xiling.ddui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.DPosterBean;
import com.xiling.ddui.bean.PosterBean;
import com.xiling.ddui.config.UIConfig;
import com.xiling.ddui.custom.DDShareWXDialog;
import com.xiling.ddui.manager.PosterMaker;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IPageService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.joker.pager.BannerPager;
import com.joker.pager.PagerOptions;
import com.joker.pager.holder.ViewHolder;
import com.joker.pager.holder.ViewHolderCreator;
import com.joker.pager.transformer.ScaleTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SharePosterActivity extends BaseActivity {

    /**
     * 海报类型
     */
    public static class PosterType {
        /*创业礼包*/
        public static final int BAG = 0x01;
        /*专属粉丝*/
        public static final int FANS = 0x02;
        /*专属海报*/
        public static final int POSTER = 0x03;
    }

    private static String kType = "TYPE";

    /**
     * 跳转到指定类型的海报分享
     *
     * @param type 海报类型
     */
    public static void jumpSharePoster(Context context, int type) {
        Intent intent = new Intent(context, SharePosterActivity.class);
        intent.putExtra(kType, type);
        context.startActivity(intent);
    }

    @BindView(R.id.mainView)
    RelativeLayout mainView;

    @BindView(R.id.tipTextView)
    TextView tipTextView;

    @BindView(R.id.bannerPager)
    BannerPager bannerPager = null;

    ArrayList<PosterBean> data = new ArrayList<>();

    int type = PosterType.POSTER;
    String typeName = "专属海报";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取传入的海报类型
        type = getIntent().getIntExtra(kType, PosterType.POSTER);

        if (SessionUtil.getInstance().isLogin()) {
            setContentView(R.layout.activity_share_poster);
            ButterKnife.bind(this);
            initView();
            initData();
        } else {
            DLog.w("未登录不启动海报界面");
            finish();
        }
    }

    void initView() {

        setLeftBlack();

        int space = 0;
        int unitWidth = 0;

        space = ConvertUtil.dip2px(30);
        double scale = 1080.0 / 1920.0;

        int screenHeight = UIConfig.getScreenHeight(context);
        int height = UIConfig.getActivityShowViewHeight(context, 45) - 2 * space;

        int screenWidth = UIConfig.getScreenWidth(context);
        double width = height * scale;
        unitWidth = (int) ((screenWidth - width) / 2.0) + 30 * 2;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(screenWidth, height);
        layoutParams.setMargins(0, space, 0, space);
        bannerPager.setLayoutParams(layoutParams);

        DLog.d("space:" + space);
        DLog.d("scale:" + scale);

        DLog.d("screenWidth:" + screenWidth);
        DLog.d("screenHeight:" + screenHeight);

        DLog.d("width:" + width);
        DLog.d("height:" + height);

        DLog.d("unitWidth:" + unitWidth);

        PagerOptions pagerOptions = new PagerOptions.Builder(this)
                .setPagePadding(30)
                .setPrePagerWidth(unitWidth)
                .setIndicatorVisibility(View.GONE)
                .setLoopEnable(false)
                .setPageTransformer(new ScaleTransformer())
                .build();
        bannerPager.setPagerOptions(pagerOptions);
    }

    /**
     *
     * */
    void initData() {
        int c0 = Color.parseColor("#f6f6f6");
        int c1 = Color.parseColor("#333333");

        switch (type) {
            case PosterType.BAG://创业礼包
                mainView.setBackgroundColor(c0);
                tipTextView.setTextColor(c1);
                typeName = "创业礼包";
                loadBagData();
                break;
            case PosterType.FANS://专属粉丝
                mainView.setBackgroundColor(c0);
                tipTextView.setTextColor(c1);
                typeName = "专属粉丝";
                loadFansData();
                break;
            case PosterType.POSTER://海报的数据从网络下载
            default:
                mainView.setBackgroundColor(c1);
                tipTextView.setTextColor(c0);
                typeName = "专属海报";
                loadNetData();
                break;
        }
        setTitle(typeName);
    }

    /**
     * 加载创业礼包数据
     */
    public void loadBagData() {
        data.clear();
        PosterBean poster = new PosterBean();
        poster.setImgUrl("" + PosterMaker.BG_SHARE_BAG);
        poster.setStyle("b");
        data.add(poster);
        makePoster();
    }

    /**
     * 加载专属粉丝数据
     */
    public void loadFansData() {
        data.clear();
        PosterBean poster = new PosterBean();
        poster.setImgUrl("" + PosterMaker.BG_SHARE_FANS);
        poster.setStyle("b");
        data.add(poster);
        makePoster();
    }

    /**
     * 获取网络数据
     */
    void loadNetData() {
        IPageService pageService = ServiceManager.getInstance().createService(IPageService.class);
        APIManager.startRequest(pageService.getPosterData(), new RequestListener<DPosterBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DPosterBean result) {
                super.onSuccess(result);
                data = result.getList();
                makePoster();
            }

            @Override
            public void onError(Throwable e) {
                tipTextView.setText("" + e.getMessage());
                tipTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    void makePoster() {
        PosterMaker maker = new PosterMaker(context);
        maker.setMode(type);
        maker.setData(data);
        maker.setListener(new PosterMaker.PosterMakeListener() {
            @Override
            public void onPosterMakeStart() {
                DLog.d("onPosterMakeStart");
                tipTextView.setText("正在生成" + typeName + ",请等待...");
                tipTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUserAvatarCreate(String pathToFile) {
                DLog.d("onUserAvatarCreate:" + pathToFile);
                tipTextView.setText("生成" + typeName + "用户头像中...");
            }

            @Override
            public void onUserQRCreate(String pathToFile) {
                DLog.d("onUserQRCreate:" + pathToFile);
                tipTextView.setText("生成" + typeName + "二维码中...");
            }

            @Override
            public void onPosterCreate(String pathToFile) {
                DLog.d("onPosterCreate:" + pathToFile);
                tipTextView.setText("正在生成" + typeName + ",请等待...");
            }

            @Override
            public void onPosterMakeError(String error) {
                DLog.e("onPosterMakeError:" + error);
                tipTextView.setText("生成海报失败!");
                tipTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPosterMakeEnd() {
                DLog.d("onPosterMakeEnd");
                tipTextView.setVisibility(View.GONE);
                bannerPager.setPages(data, new ViewHolderCreator<BannerPagerHolder>() {
                    @Override
                    public BannerPagerHolder createViewHolder() {
                        View view = LayoutInflater.from(context).inflate(R.layout.adapter_poster, null);
                        BannerPagerHolder pagerHolder = new BannerPagerHolder(view);
                        return pagerHolder;
                    }
                });

                if (bannerPager.getViewPager().getAdapter() != null) {
                    bannerPager.getViewPager().getAdapter().notifyDataSetChanged();
                }
            }
        });
        maker.generate();
    }

    //当前轮播的ViewHolder
    public class BannerPagerHolder extends ViewHolder<PosterBean> {

        int position;
        PosterBean data = null;

        @BindView(R.id.posterImageView)
        SimpleDraweeView posterImageView = null;

        @OnClick(R.id.sharePosterButton)
        void onSharePosterPressed() {
            if (data != null) {
                DLog.i("onSharePosterPressed:" + position);

                String url = data.getImgUrl();
                String poster = PosterMaker.getPosterFile(type, url);

                DDShareWXDialog dialog = new DDShareWXDialog(SharePosterActivity.this);
                dialog.setImgPath(poster);

                dialog.show();
            }
        }

        public BannerPagerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public View getItemView() {
            return super.getItemView();
        }

        @Override
        public void onBindView(View view, PosterBean data, int position) {
            this.data = data;
            this.position = position;
            String url = data.getImgUrl();
            String poster = PosterMaker.getPosterFile(type, url);

            DLog.d("onBindView " + position + ":" + poster);
            Uri uri = Uri.parse("file://" + poster);

            // 清除Fresco缓存的历史记录
            ImagePipeline imagePipeline = Fresco.getImagePipeline();
            imagePipeline.evictFromMemoryCache(uri);
            imagePipeline.evictFromDiskCache(uri);
            imagePipeline.evictFromCache(uri);

            posterImageView.setImageURI(uri);
        }
    }


}
