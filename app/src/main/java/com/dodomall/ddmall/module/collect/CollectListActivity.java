package com.dodomall.ddmall.module.collect;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.fragment.DDLikeCollectFragment;
import com.dodomall.ddmall.ddui.fragment.DDLikeCourseFragment;
import com.dodomall.ddmall.ddui.fragment.DDLikeDataFragment;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Page;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.PageManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICollectService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

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
import butterknife.OnClick;

import static net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator.MODE_WRAP_CONTENT;


/**
 * Created by Chan on 2017/6/15.
 * Modify by hanQ at 2018/10/10.
 */

public class CollectListActivity extends BaseActivity {

    @BindView(R.id.magicIndicator)
    protected MagicIndicator mMagicIndicator;

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    private List<BaseFragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();

    @OnClick(R.id.btnTitleBack)
    void onTitleBackPressed() {
        onBackPressed();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        buildView();
    }

    void buildView() {
        if (SessionUtil.getInstance().isLogin()) {

            User user = SessionUtil.getInstance().getLoginUser();

            mTabTitles.add("宝贝");
            mFragments.add(new DDLikeCollectFragment());

            if (user.isStoreUser()) {
                mTabTitles.add("素材");
                mFragments.add(new DDLikeDataFragment());

                mTabTitles.add("课程");
                mFragments.add(new DDLikeCourseFragment());

            }

            initViewPager();
            initIndicator();

        } else {
            ToastUtil.success("用户未登录");
            finish();
            return;
        }
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
        });
//        mViewPager.setOffscreenPageLimit(mFragments.size());
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {

                String title = "";
                if (index < mTabTitles.size()) {
                    title = mTabTitles.get(index);
                }

                SimplePagerTitleView titleView = new SimplePagerTitleView(context);

// 粗体
//                TextPaint tp = titleView.getPaint();
//                tp.setFakeBoldText(true);

                titleView.setText("" + title);
                titleView.setNormalColor(getResources().getColor(R.color.likeTopTextColor));
                titleView.setSelectedColor(getResources().getColor(R.color.mainColor));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(0, 0, 0, 0);
                titleView.setTextSize(16);
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.mainColor));
                indicator.setMode(MODE_WRAP_CONTENT);
                indicator.setLineHeight(ConvertUtil.dip2px(2));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

}
