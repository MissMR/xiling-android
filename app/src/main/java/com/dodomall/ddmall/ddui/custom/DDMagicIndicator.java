package com.dodomall.ddmall.ddui.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;

/**
 * created by Jigsaw at 2019/5/28
 */
public class DDMagicIndicator extends MagicIndicator {
    public DDMagicIndicator(Context context) {
        super(context);
    }

    public DDMagicIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setupViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            throw new NullPointerException("viewpager must not be null");
        }
        setNavigator(new DDNavigator(getContext(), viewPager));
        ViewPagerHelper.bind(this, viewPager);
    }

    public static class DDNavigator extends CommonNavigator {
        private ViewPager mViewPager;

        public DDNavigator(Context context) {
            super(context);
            init();
        }

        public DDNavigator(Context context, ViewPager viewPager) {
            super(context);
            mViewPager = viewPager;
            init();
            setupWithViewPager();
        }

        private void init() {
            setAdjustMode(true);
        }

        private void setupWithViewPager() {
            setAdapter(new CommonNavigatorAdapter() {
                @Override
                public int getCount() {
                    return mViewPager.getAdapter().getCount();
                }

                @Override
                public IPagerTitleView getTitleView(Context context, final int index) {
                    SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                    titleView.setText(mViewPager.getAdapter().getPageTitle(index));
                    titleView.setNormalColor(getResources().getColor(R.color.settings_value_text));
                    titleView.setSelectedColor(getResources().getColor(R.color.mainColor));
                    titleView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mViewPager.setCurrentItem(index);
                        }
                    });
                    titleView.setPadding(0, 0, 0, 0);
                    if (mViewPager.getCurrentItem() == index) {
                        titleView.setTextSize(15);
                    } else {
                        titleView.setTextSize(14);
                    }
                    return titleView;
                }

                @Override
                public IPagerIndicator getIndicator(Context context) {
                    LinePagerIndicator indicator = new LinePagerIndicator(context);
                    indicator.setColors(getResources().getColor(R.color.red));
                    indicator.setMode(MODE_WRAP_CONTENT);
                    indicator.setLineHeight(ConvertUtil.dip2px(2));
                    indicator.setRoundRadius(ConvertUtil.dip2px(2));
                    return indicator;
                }
            });

        }

        public ViewPager getViewPager() {
            return mViewPager;
        }

        public void setViewPager(ViewPager viewPager) {
            mViewPager = viewPager;
            if (viewPager == null) {
                return;
            }
            setupWithViewPager();
        }

    }

}
