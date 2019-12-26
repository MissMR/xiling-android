package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.bean.SecondCategoryBean;
import com.xiling.ddui.custom.ScreeningView;
import com.xiling.ddui.custom.popupwindow.ScreeningPopupWindow;
import com.xiling.ddui.fragment.ShopFragment;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.component.HeaderLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author pt
 * 二级分类页面
 */
public class CategorySecondActivity extends BaseActivity {

    @BindView(R.id.headerLayout)
    HeaderLayout mHeaderLayout;
    Unbinder unbinder;

    public static final String SECOND_CATEGORY_LIST = "secondCategoryList";
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_shop)
    ViewPager viewpagerShop;
    @BindView(R.id.screenView)
    ScreeningView screenView;
    @BindView(R.id.parentView)
    LinearLayout parentView;


    private ArrayList<Fragment> fragments = new ArrayList<>();


    private String parentName, categoryId;

    // 当前在第几个分类
    private int mPosition = 0;
    private ArrayList<SecondCategoryBean.SecondCategoryListBean> secondCategoryList;
    private List<String> childNames = new ArrayList<>();

    private String minPrice, maxPrice;
    /**
     * 是否包邮 0-非,1-是
     */
    private int isShippingFree = 0;

    /**
     * 排序属性 0-价格,1-上新,2-销量
     * 默认 上新
     */
    private int orderBy = 1;

    /**
     * 排序方式 0-降序(Desc), 1-升序(Asc)
     */
    private int orderType = 0;
    private String keyWord = "";


    public static void jumpCategorySecondActivity(Context mContext, String parentName, String categoryId, ArrayList<SecondCategoryBean.SecondCategoryListBean> secondCategoryList, int childPosition) {
        Intent intent = new Intent(mContext, CategorySecondActivity.class);
        intent.putParcelableArrayListExtra(SECOND_CATEGORY_LIST, secondCategoryList);
        intent.putExtra("parentName", parentName);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("mPosition", childPosition);
        mContext.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_second);
        unbinder = ButterKnife.bind(this);

        if (getIntent() != null) {
            parentName = getIntent().getStringExtra("parentName");
            categoryId = getIntent().getStringExtra("categoryId");
            mPosition = getIntent().getIntExtra("mPosition", 0);
            secondCategoryList = getIntent().getParcelableArrayListExtra(SECOND_CATEGORY_LIST);

            if (secondCategoryList != null) {
                fragments.clear();
                childNames.clear();
                for (SecondCategoryBean.SecondCategoryListBean secondCategoryListBean : secondCategoryList) {
                    childNames.add(secondCategoryListBean.getCategoryName());
                    fragments.add(ShopFragment.newInstance(categoryId, secondCategoryListBean.getCategoryId(), "", minPrice, maxPrice, isShippingFree, orderBy, orderType, ""));
                }
            }

        }

        initView();
    }

    private void initView() {
        mHeaderLayout.setTitle(parentName);
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
        mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        slidingTab.setViewPager(viewpagerShop, childNames.toArray(new String[childNames.size()]), this, fragments);

        if (mPosition > 0) {
            viewpagerShop.setCurrentItem(mPosition);
        }

        screenView.setOnItemClickLisener(new ScreeningView.OnItemClickLisener() {
            @Override
            public void onSort(int orderBy, int orderType) {
                CategorySecondActivity.this.orderBy = orderBy;
                CategorySecondActivity.this.orderType = orderType;
                for (Fragment fragment : fragments) {
                    ((ShopFragment) fragment).requestShopFill(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
                }
            }

            @Override
            public void onFilter(int isShippingFree, String minPrice, String maxPrice) {
                CategorySecondActivity.this.isShippingFree = isShippingFree;
                CategorySecondActivity.this.minPrice = minPrice;
                CategorySecondActivity.this.maxPrice = maxPrice;
                for (Fragment fragment : fragments) {
                    ((ShopFragment) fragment).requestShopFill(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
                }
            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
