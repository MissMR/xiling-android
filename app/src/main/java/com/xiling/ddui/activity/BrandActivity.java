package com.xiling.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.xiling.R;
import com.xiling.ddui.bean.BrandBean;
import com.xiling.ddui.bean.TopCategoryBean;
import com.xiling.ddui.custom.ScreeningView;
import com.xiling.ddui.custom.popupwindow.ScreeningPopupWindow;
import com.xiling.ddui.fragment.ShopFragment;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IProductService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 逄涛
 * 品牌馆
 */
public class BrandActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.screenView)
    ScreeningView screenView;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;
    @BindView(R.id.parentView)
    LinearLayout parentView;
    @BindView(R.id.headerLayout)
    HeaderLayout mHeaderLayout;

    private IProductService mProductService;

    private String categoryId, brandId, brandName;
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

    private ShopFragment shopFragment;
    ScreeningPopupWindow screeningPopupWindow;

    public static void jumpBrandActivity(Context context, String categoryId, String brandId) {
        Intent intent = new Intent(context, BrandActivity.class);
        intent.putExtra("categoryId", categoryId);
        intent.putExtra("brandId", brandId);
        context.startActivity(intent);
    }

    private void getIntentData() {
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
            brandId = getIntent().getStringExtra("brandId");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand);
        ButterKnife.bind(this);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        getIntentData();
        initView();
        requestBrandData();
    }

    private void initView() {
        mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
        mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        shopFragment = ShopFragment.newInstance(categoryId, "", brandId, minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, shopFragment, "shopFragment").commit();


        screenView.setOnItemClickLisener(new ScreeningView.OnItemClickLisener() {
            @Override
            public void onSort(int orderBy, int orderType) {
                BrandActivity.this.orderBy = orderBy;
                BrandActivity.this.orderType = orderType;
                shopFragment.requestShopFill(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
            }

            @Override
            public void onFilter(int isShippingFree, String minPrice, String maxPrice) {
                BrandActivity.this.isShippingFree = isShippingFree;
                BrandActivity.this.minPrice = minPrice;
                BrandActivity.this.maxPrice = maxPrice;
                shopFragment.requestShopFill(minPrice, maxPrice, isShippingFree, orderBy, orderType, keyWord);
            }
        });

    }

    private void requestBrandData() {
        APIManager.startRequest(mProductService.getBrandDetail(brandId), new BaseRequestListener<BrandBean>(this) {
            @Override
            public void onSuccess(BrandBean result) {
                super.onSuccess(result);
                if (result != null) {
                    brandName = result.getBrandName();
                    mHeaderLayout.setTitle(brandName);
                    if (!TextUtils.isEmpty(result.getIconUrl())) {
                        ivHead.setVisibility(View.VISIBLE);
                        GlideUtils.loadImage(context, ivHead, result.getIconUrl());
                    } else {
                        ivHead.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

}
