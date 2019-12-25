package com.xiling.image;

import android.view.View;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.List;

public class BannerManager {

    public static void startBanner(Banner banner, List<String> bannerList){
        if (bannerList.size() > 0){
            banner.setVisibility(View.VISIBLE);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setIndicatorGravity(BannerConfig.CENTER);
            banner.setImageLoader(new BannerGlideLoad());
            banner.setDelayTime(2000);
            banner.setImages(bannerList);
            banner.setBannerAnimation(Transformer.Default);
            banner.start();
        }else{
            banner.setVisibility(View.GONE);
        }
    }

}
