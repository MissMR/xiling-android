package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.CategoryBean;
import com.xiling.ddui.fragment.DDSubCategoryFragment;
import com.xiling.ddui.tools.DLog;
import com.xiling.ddui.tools.UITools;
import com.xiling.module.search.SearchActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;

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

public class DDCategoryActivity extends BaseActivity {

    private static final String kCategoryName = "DDCategoryActivity.kCategoryName";
    private static final String kCategoryId = "DDCategoryActivity.kCategoryId";
    private static final String kPosition = "DDCategoryActivity.kPosition";

    /**
     * @param context
     * @param categoryId   分类ID
     * @param categoryName 分类名称
     */
    public static void jumpTo(Context context, String categoryId, String categoryName) {
        Intent intent = new Intent(context, DDCategoryActivity.class);
        intent.putExtra(kCategoryName, categoryName);
        intent.putExtra(kCategoryId, categoryId);
        context.startActivity(intent);
    }

    /**
     * @param context
     * @param categoryId   分类ID
     * @param categoryName 分类名称
     * @param position     默认选中位置
     */
    public static void jumpTo(Context context, String categoryId, String categoryName, int position) {
        Intent intent = new Intent(context, DDCategoryActivity.class);
        intent.putExtra(kCategoryName, categoryName);
        intent.putExtra(kCategoryId, categoryId);
        intent.putExtra(kPosition, position);
        context.startActivity(intent);
    }

    @OnClick(R.id.ivTitleBarLeftButtonView)
    void onTitleBackPressed() {
        onBackPressed();
    }

    @OnClick(R.id.ivTitleBarRightButtonView)
    void onSearchPressed() {
        Intent intent = new Intent(context, SearchActivity.class);
        startActivity(intent);
    }

    @BindView(R.id.tvTitleBarTextView)
    TextView tvTitleBarTextView;

    @BindView(R.id.magicIndicator)
    protected MagicIndicator mMagicIndicator;

    @BindView(R.id.viewPager)
    protected ViewPager mViewPager;

    private List<BaseFragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTabTitles = new ArrayList<>();

    String categoryId = "";
    String categoryName = "";
    int position = 0;

    DDHomeService homeService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeStatusBarTranslucent();
        darkStatusBar();

        setContentView(R.layout.activity_ddcategory);
        ButterKnife.bind(this);

        homeService = ServiceManager.getInstance().createService(DDHomeService.class);

        categoryId = getIntent().getStringExtra(kCategoryId);
        if (TextUtils.isEmpty(categoryId)) {
            ToastUtil.error("数据错误");
            return;
        }

        categoryName = getIntent().getStringExtra(kCategoryName);
        if (TextUtils.isEmpty(categoryName)) {
            categoryName = "";
        }
        tvTitleBarTextView.setText("" + categoryName);

        position = getIntent().getIntExtra(kPosition, 0);

        //加载数据
        loadNetData();
    }

    //加载二级分类数据
    private void loadNetData() {
        DLog.i("load second category:" + categoryId);
        //获取二级分类数据
        APIManager.startRequest(homeService.getSubCategory(categoryId + ""), new RequestListener<ArrayList<CategoryBean>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ArrayList<CategoryBean> result) {
                super.onSuccess(result);
                DLog.i("加载二级分类完成");
                if (result.isEmpty()) {
                    ToastUtil.error("暂无此分类数据");
                    finish();
                } else {
                    for (CategoryBean item : result) {
                        DDSubCategoryFragment fragment = new DDSubCategoryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(DDSubCategoryFragment.kCategoryId, item.getCategoryId());
                        bundle.putString(DDSubCategoryFragment.kCategoryParentId, categoryId);
                        fragment.setArguments(bundle);
                        mFragments.add(fragment);
                        mTabTitles.add("" + item.getCategoryName());
                    }
                    initViewPager();
                    initIndicator();

                    if (mFragments.size() > position) {
                        mViewPager.setCurrentItem(position);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });
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
        int size = mFragments.size();
        if (size > 0) {
            mViewPager.setOffscreenPageLimit(size);
        }
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
        //自动适配屏幕的逻辑
        commonNavigator.setAdjustMode(canAdjust());
        commonNavigator.setFollowTouch(true);
        commonNavigator.setSkimOver(false);
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
                titleView.setNormalColor(getResources().getColor(R.color.text_black));
                titleView.setSelectedColor(getResources().getColor(R.color.ddm_red));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(index);
                    }
                });
                titleView.setPadding(30, 0, 30, 0);
                titleView.setTextSize(14);
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(getResources().getColor(R.color.ddm_red));
                indicator.setMode(MODE_WRAP_CONTENT);
                indicator.setLineHeight(ConvertUtil.dip2px(2));
                return indicator;
            }
        });
        mMagicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
    }

    public boolean canAdjust() {
        int width = ConvertUtil.dip2px(100);
        int maxWidth = mTabTitles.size() * width;
        int screenWidth = UITools.getScreenWidth(context);
        DLog.i("screenWidth:" + screenWidth + ",itemCount:" + mTabTitles + ",maxWidth:" + maxWidth);
        return screenWidth >= maxWidth;
    }
}
