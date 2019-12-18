package com.xiling.ddmall.shared.util;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.common.CarouselItemViewHolder;

import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.util
 * @since 2017-06-09
 */
public class CarouselUtil {

    public static <T> void initCarousel(ConvenientBanner convenientBanner, List<T> data) {
        convenientBanner.setPages(new CBViewHolderCreator<CarouselItemViewHolder<T>>() {
            @Override
            public CarouselItemViewHolder<T> createHolder() {
                return new CarouselItemViewHolder<>();
            }
        }, data);
        convenientBanner.setPageIndicator(new int[]{R.drawable.icon_page_indicator, R.drawable.icon_page_indicator_focused})
                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        convenientBanner.startTurning(5000);
    }

    public static <T> void initCarousel(ConvenientBanner convenientBanner, List<T> data, final TextView textView) {
        if (data == null || data.isEmpty() || convenientBanner == null || textView == null) {
            return;
        }
        convenientBanner.setPages(new CBViewHolderCreator<CarouselItemViewHolder<T>>() {
            @Override
            public CarouselItemViewHolder<T> createHolder() {
                return new CarouselItemViewHolder<>();
            }
        }, data);
        textView.setVisibility(View.VISIBLE);
        final String indicatorText = "%s/" + data.size();
        textView.setText(String.format(indicatorText, "1"));
        convenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                textView.setText(String.format(indicatorText, ++position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        convenientBanner.startTurning(5000);
    }
}
