package com.xiling.ddmall.module.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.module.MainActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.bean.Order;
import com.xiling.ddmall.shared.bean.Page;
import com.xiling.ddmall.shared.util.ConvertUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;

/**
 * Created by Chan on 2017/6/16.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.module.order
 * @since 2017/6/16 下午6:37
 * 订单列表
 */

public class OrderListActivity extends BaseActivity {

    @BindView(R.id.magicIndicator)
    protected MagicIndicator mMagicIndicator;
    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    protected List<BaseFragment> mFragments = new ArrayList<>();
    protected List<Page> mPages = new ArrayList<>();
    private String mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_tab_page);
        ButterKnife.bind(this);
        getIntentData();
        initData();
        initView();
        selectTabItem(mType);
    }


    private void getIntentData() {
        Intent intent = getIntent();
        mType = intent.getExtras().getString("type", "all");
    }

    private void initView() {
        setTitle("我的订单");
        setLeftBlack();
        initViewPager();
        initIndicator();
    }

    private void initViewPager() {
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mPages.get(position).name;
            }
        });
        mViewPager.setOffscreenPageLimit(mFragments.size());
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mPages.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setText(mPages.get(index).name);
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
                    titleView.setTextSize(16);
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
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    private void initData() {
        mPages.add(new Page(Order.ORDER_ALL, "全部"));
        mPages.add(new Page(Order.ORDER_WAIT_PAY, "待付款"));
        mPages.add(new Page(Order.ORDER_PAID, "待发货"));
        mPages.add(new Page(Order.ORDER_DISPATCHED, "已发货"));
        for (Page page : mPages) {
            OrderListFragment orderListFragment = new OrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("page", page);
            orderListFragment.setArguments(bundle);
            mFragments.add(orderListFragment);
        }
    }

    public void selectTabItem(String type) {
        int index = 0;
        for (Page page : mPages) {
            if (page.id.equalsIgnoreCase(type)) {
                index = mPages.indexOf(page);
                break;
            }
        }
        mViewPager.setCurrentItem(index);
    }

    @Override
    public void finish() {
        MainActivity.goBack(this, 3);
        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        DLog.i("OrderListActivity onNewIntent");
        if (intent != null && intent.hasExtra("type")) {
            String type = intent.getStringExtra("type");
            selectTabItem(type);
        }
    }
}
