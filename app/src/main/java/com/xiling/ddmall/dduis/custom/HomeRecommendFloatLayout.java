package com.xiling.ddmall.dduis.custom;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.config.UIConfig;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.ddui.tools.UITools;
import com.xiling.ddmall.dduis.adapter.HomeRecommendAdapter;
import com.xiling.ddmall.dduis.base.BackgroundMaker;
import com.xiling.ddmall.dduis.bean.DDRushHeaderBean;
import com.xiling.ddmall.shared.util.ConvertUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeRecommendFloatLayout {

    HomeRecommendAdapter.HomeRecommendDataListener listener;
    ArrayList<DDRushHeaderBean> data = new ArrayList<>();

    @BindView(R.id.layout_float)
    LinearLayout mainLayout;//吸顶布局

    @BindView(R.id.rush_buy_magic_indicator)
    MagicIndicator mIndicator;

    Context context = null;

    CommonNavigator commonNavigator = null;
    private ArrayList<String> mTabTitles = new ArrayList<>();

    int currentIndex = -1;
    int defaultIndex = 0;

    public HomeRecommendFloatLayout(View itemView) {
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
    }

    public void setData(ArrayList<DDRushHeaderBean> data) {
        this.data = data;
        mTabTitles.clear();

        for (DDRushHeaderBean bean : data) {
            mTabTitles.add(bean.getFormatTimeText());
        }
    }

    public void setListener(HomeRecommendAdapter.HomeRecommendDataListener listener) {
        this.listener = listener;
    }

    private boolean canAdjust() {
        int width = ConvertUtil.dip2px(100);
        int maxWidth = mTabTitles.size() * width;
        int screenWidth = UITools.getScreenWidth(context);
        DLog.i("screenWidth:" + screenWidth + ",itemCount:" + mTabTitles + ",maxWidth:" + maxWidth);
        return screenWidth >= maxWidth;
    }

    public void select(int position) {
        mIndicator.onPageSelected(position);
        commonNavigator.notifyDataSetChanged();
    }

    public void render() {
        commonNavigator = new CommonNavigator(context);
        //自动适配屏幕的逻辑
        commonNavigator.setAdjustMode(canAdjust());
        commonNavigator.setFollowTouch(true);
        commonNavigator.setSkimOver(false);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return data.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                CommonPagerTitleView titleView = new CommonPagerTitleView(context);
                titleView.setContentView(R.layout.layout_rush_pager);

                final LinearLayout layoutRush = titleView.findViewById(R.id.layout_rush);
                final TextView tvTime = titleView.findViewById(R.id.tv_time);
                final TextView tvStatus = titleView.findViewById(R.id.tv_status);

                DDRushHeaderBean bean = data.get(index);
                //设置抢购时间
                tvTime.setText("" + bean.getFormatTimeText());
                //设置抢购状态
                tvStatus.setText("" + bean.getStatusText(index == defaultIndex));

                final View vDown = titleView.findViewById(R.id.v_down);
                layoutRush.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (index != currentIndex) {

                            mIndicator.onPageSelected(index);
                            commonNavigator.notifyDataSetChanged();
                            currentIndex = index;

                            if (listener != null) {
                                DDRushHeaderBean header = data.get(index);
                                listener.onRushHeaderChanged(index, header);
                            }
                        }
                    }
                });
                titleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {
                    @Override
                    public void onSelected(int index, int totalCount) {
                        vDown.setVisibility(View.VISIBLE);
                        DDRushHeaderBean rushHeaderBean = data.get(index);
                        if (rushHeaderBean.isRushEnable()) {
                            layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_VIP);
                            BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_VIP);
                        } else {
                            layoutRush.setBackgroundColor(UIConfig.COLOR_RUSH_USER);
                            BackgroundMaker.setTriangleDrawableColor(vDown, UIConfig.COLOR_RUSH_USER);
                        }
                        tvTime.setTextColor(Color.WHITE);
                        tvStatus.setTextColor(Color.WHITE);
                    }

                    @Override
                    public void onDeselected(int index, int totalCount) {
                        layoutRush.setBackgroundColor(Color.WHITE);
                        vDown.setVisibility(View.INVISIBLE);
                        tvTime.setTextColor(Color.BLACK);
                        tvStatus.setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {

                    }

                    @Override
                    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {

                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                return null;
            }
        });
        mIndicator.setNavigator(commonNavigator);

        //取出默认的选中
        if (currentIndex < 0) {
            defaultIndex = 0;
            for (DDRushHeaderBean headerBean : data) {
                if (headerBean.isSelected()) {
                    currentIndex = defaultIndex;
                    break;
                }
                defaultIndex++;
            }
        }
        mIndicator.onPageSelected(currentIndex);
        commonNavigator.notifyDataSetChanged();
    }

    public void resetIndex() {
        currentIndex = -1;
    }

    public void show() {
        mainLayout.setVisibility(View.VISIBLE);
    }

    public void hide() {
        mainLayout.setVisibility(View.INVISIBLE);
    }

}
