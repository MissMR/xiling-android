package com.xiling.ddui.tools;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.activity.DDVideoViewActivity;
import com.xiling.ddui.adapter.DDCommunityDataAdapter;
import com.xiling.ddui.adapter.MeasurementImageAdapter;
import com.xiling.ddui.adapter.ProductSuggestAdapter;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.ProductEvaluateBean;
import com.xiling.ddui.custom.DDAvatarContainerView;
import com.xiling.ddui.custom.DDExpandTextView;
import com.xiling.ddui.custom.DDIndicator;
import com.xiling.ddui.custom.DDProductActionContainer;
import com.xiling.ddui.custom.DDProductPriceView;
import com.xiling.ddui.custom.DDSmartTab;
import com.xiling.ddui.custom.DDSquareBanner;
import com.xiling.ddui.custom.DDStarView;
import com.xiling.ddui.custom.DDTagView;
import com.xiling.dduis.base.AvatarDemoMaker;
import com.xiling.shared.bean.Product;
import com.xiling.shared.bean.SkuPvIds;
import com.xiling.shared.bean.Tag;
import com.xiling.shared.decoration.GridSpacingItemDecoration;
import com.xiling.shared.util.CarouselUtil;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.ImageUtil;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.WebViewUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * created by Jigsaw at 2018/10/31
 * 商品详情页UI帮助类
 * 点击事件通通放出去 OnActionListener
 */
public class ProductDetailUIHelper {

    // 正常页面  普通用户
    public static final int UI_CATEGORY_USER_NORMAL = 0;
    // 店主页面  店主身份
    public static final int UI_CATEGORY_USER_MASTER = 1;

    public static final String STRING_BECOME_GUIDE = "升级店主 价格再优惠 购物可返%s元";
    // 顶部导航栏
    @BindView(R.id.dd_smart_tab)
    DDSmartTab mDDSmartTab;
    // 整个NestedScrollView
    @BindView(R.id.nsv_container)
    NestedScrollView mNestedScrollView;
    // 产品锚点
    @BindView(R.id.anchor_product)
    View mAnchorProduct;
    // 素材锚点
    @BindView(R.id.anchor_material)
    View mAnchorMaterial;
    // 产品详情锚点
    @BindView(R.id.anchor_detail)
    View mAnchorDetail;

    // 整个toolbar区域
    @BindView(R.id.fl_toolbar_container)
    FrameLayout mFlToolbarContainer;

    // 默认toolbar区域
    @BindView(R.id.rl_toolbar_first)
    RelativeLayout mRlToolbarFirst;
    @BindView(R.id.iv_back_first)
    ImageView mIvBackFirst;
    @BindView(R.id.iv_share_first)
    ImageView mIvShareFirst;
    @BindView(R.id.iv_collect_first)
    ImageView mIvCollectFirst;

    // 上滑后的toolbar区域
    @BindView(R.id.rl_toolbar_second)
    RelativeLayout mRlToolbarSecond;
    @BindView(R.id.iv_back_second)
    ImageView mIvBackSecond;
    @BindView(R.id.iv_share_second)
    ImageView mIvShareSecond;
    @BindView(R.id.iv_collect_second)
    ImageView mIvCollectSecond;

    // banner
    @BindView(R.id.dd_square_banner)
    DDSquareBanner mDDSquareBanner;

    // banner 已抢光
    @BindView(R.id.tv_sold_out)
    TextView mTvSoldOut;

    // 引导成为店主
    @BindView(R.id.rl_become_master_guide)
    RelativeLayout mRlBecomeMasterGuide;

    // 立即升级为店主
    @BindView(R.id.tv_become_master)
    TextView mTvBecomeMaster;
    // 成为店主后的佣金
    @BindView(R.id.tv_reward_guide)
    TextView mTvRewardGuide;

    // 销量
    @BindView(R.id.tv_sale_count)
    TextView mTvSaleCount;
    // 商品标题
    @BindView(R.id.tv_product_title)
    TextView mTvProductTitle;
    // 商品sku 具体商品类别
    @BindView(R.id.tvSkuInfo)
    TextView mTvSkuInfo;

    @BindView(R.id.hsv_product_tag_container)
    HorizontalScrollView mHsvProductTagContainer;

    // 商家承诺区域
    @BindView(R.id.productAuthLayout)
    LinearLayout mLlProductAuth;
    @BindViews({R.id.tvProductAuth1, R.id.tvProductAuth2, R.id.tvProductAuth3})
    List<TextView> mTvAuthList;
    @BindViews({R.id.ivProductAuth1, R.id.ivProductAuth2, R.id.ivProductAuth3})
    List<SimpleDraweeView> mIvAuthList;

    @BindView(R.id.tv_material_all)
    TextView mTvMaterialAll;

    // 产品详情
    @BindView(R.id.web_view)
    WebView mProductDetailWebView;
    // 产品标签区域
    @BindView(R.id.ll_product_tag_container)
    LinearLayout mLlProductTagContainer;

    // 发圈素材总数
    @BindView(R.id.tv_material_count)
    TextView mTvMaterialCount;
    // 发圈素材
    @BindView(R.id.rv_material)
    RecyclerView mRvMaterial;


    // banner 指示器
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;


    @BindView(R.id.rl_sale_info)
    RelativeLayout mRlSaleInfo;
    @BindView(R.id.tv_sale_info_sale_count)
    TextView mTvSaleInfoSaleCount;
    @BindView(R.id.tv_sale_user_count)
    TextView mTvSaleUserCount;
    @BindView(R.id.dd_avatar_container)
    DDAvatarContainerView mDDAvatarContainerView;


    @BindView(R.id.dd_product_action_container)
    DDProductActionContainer mDDProductActionContainer;

    @BindView(R.id.dd_product_price_view)
    DDProductPriceView mDDProductPriceView;

    @BindView(R.id.vs_measurement)
    ViewStub mVsMeasurement;

    @BindView(R.id.vs_suggest)
    ViewStub mVsSuggest;

    private OnActionListener mOnActionListener;

    private DDProductDetailActivity mContext;
    private View baseContentView;

    private MeasurementViewHolder mMeasurementViewHolder;
    private SuggestProductViewHolder mSuggestProductViewHolder;

    private OnClickListener mOnClickListener;

    // 是否是从上往上滑动 往上滑动时，tab 的 onCheckChanged 不做处理
    private boolean isScrollingToTop = false;

    // 滑动阀值
    private int mProductThreshold;
    private int mMaterialThreshold;

    // toolbar 动画阀值
    private int mToolbarThreshold;

    // 解决快速上滑到顶 toolbar不隐藏
    private int mCurrentScrollY = 0;

    private int mUIUserCategory;

    // 店主且非0元购 true
    private boolean mIsShowProductMaterial;

    private DDCommunityDataAdapter mProductDetailMaterialAdapter;


    public ProductDetailUIHelper(DDProductDetailActivity productDetailActivity) {
        mContext = productDetailActivity;
        mUIUserCategory = SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isStoreMaster()
                ? UI_CATEGORY_USER_MASTER : UI_CATEGORY_USER_NORMAL;
        initView();
        QMUIStatusBarHelper.setStatusBarLightMode(mContext);
    }

    private void initView() {
        baseContentView = mContext.findViewById(R.id.baseContentLayout);

        ButterKnife.bind(this, baseContentView);

        QMUIStatusBarHelper.translucent(mContext);
        QMUIStatusBarHelper.setStatusBarDarkMode(mContext);

        initBaseViewByUIUserCategory();

        initScrollThreshold();

        initAnimateListener();

        initMaterialRecyclerView();

        baseContentView.findViewById(R.id.tv_service).setActivated(true);
    }


    public void updateSkuViews(SkuPvIds skuInfo) {

        if (skuInfo == null) {
            mTvSkuInfo.setText("请选择规格");
            return;
        }

        mTvSkuInfo.setText(skuInfo.propertyValues);

    }

    public void updateSpuViews(Product spuInfo) {
        if (spuInfo == null) {
            return;
        }

        if (!spuInfo.images.isEmpty()) {
            updateSkuBanner(spuInfo.images);
        }

        updateSpuLikeState(spuInfo.isProductLiked());

        mDDProductPriceView.setData(spuInfo);

        // 店主佣金

        String rewardPrice = String.valueOf(ConvertUtil.cent2yuanNoZero(spuInfo.getRewardPrice()));
        mDDProductActionContainer.setRewardPrice(rewardPrice);
        mTvRewardGuide.setText(String.format(STRING_BECOME_GUIDE, ConvertUtil.cent2yuanNoZero(spuInfo.rewardPrice)));

        mTvProductTitle.setText(spuInfo.name);

        clearProductTagView();
        if (!spuInfo.tags.isEmpty()) {
            for (Tag t : spuInfo.tags) {
                addItemProductTagView(t.name);
            }
        } else {
            mHsvProductTagContainer.setVisibility(View.GONE);
        }

        mTvSkuInfo.setText("请选择规格");

        if (spuInfo.skus.size() == 1) {
            mTvSkuInfo.setText(spuInfo.skus.get(0).propertyValues);
        }

        mTvSaleCount.setText("销量" + spuInfo.getFormatSaleCount() + "件");

        updateAuthViews(spuInfo);

        // 购买人信息相关
        mRlSaleInfo.setVisibility(spuInfo.saleIncCount > 2 ? View.VISIBLE : View.GONE);
        mTvSaleUserCount.setText(String.format("%s人已购买", spuInfo.saleIncCount));
        mTvSaleInfoSaleCount.setText(String.format("共售出%s件", spuInfo.getFormatSaleCount()));
        mDDAvatarContainerView.setData(AvatarDemoMaker.randomAvatarWithFirstMoreImage(spuInfo.saleIncCount, 8));

        // 测评相关
        if (spuInfo.hasProductEvaluate()) {
            getMeasurementViewHolder().render(spuInfo.productEvaluate);
            getMeasurementViewHolder().setOnClickListener(mOnClickListener);
        }

        if (spuInfo.hasSuggestProduct()) {
            String tag = spuInfo.hasSetRelationProduct() ? "相关推荐"
                    : (SessionUtil.getInstance().isMaster() ? "大家都在推" : "大家都在抢");
            getSuggestViewHolder().setSuggestTag(tag);
            getSuggestViewHolder().render(spuInfo.relationProducts);
        }

        mDDProductActionContainer.setBuyButtonEnable(spuInfo.checkProductEnable());

        loadDetailWebView(spuInfo.content);

        if (mIsShowProductMaterial) {
            mTvMaterialCount.setText("· " + spuInfo.roundMaterialCount);
            updateMaterialViews(spuInfo.roundMaterials);
        }

        renderViewByProductType(spuInfo);

        renderUnBuyableHintView(spuInfo.getUnBuyableHintText());

    }

    private void renderViewByProductType(final Product spuInfo) {
        if (spuInfo.isStoreGift()) {
            // 店主礼包
            setStoreGiftView();
        } else if (spuInfo.isProductFree()) {
            // 0元购
            setProductFreeView();
        } else if (spuInfo.isTomorrowGrounding()) {
            // 明日上新
            showShareButton(false);
            mDDProductActionContainer.setBuyButtonEnable(false);
        } else if (spuInfo.isFlashSale() && spuInfo.flashSaleDetail.isBeforeFlashSale24()) {
            // 秒杀预售
            mDDProductActionContainer.showPreFlashSale(true);
            mDDProductActionContainer.setNotifyActive(!spuInfo.flashSaleDetail.hasNotified());
            mDDProductPriceView.setOnFlashSaleListener(new DDProductPriceView.OnFlashSaleListener() {
                @Override
                public void onFlashSaleStart() {
                    mDDProductActionContainer.showPreFlashSale(false);
                }
            });
        }
    }

    private void renderUnBuyableHintView(String hint) {
        if (TextUtils.isEmpty(hint)) {
            return;
        }
        mTvSoldOut.setVisibility(View.VISIBLE);
        mDDProductActionContainer.setProductUnBuyableHint(hint);
    }

    public void showBecomeMasterGuide(boolean isShow) {
        mRlBecomeMasterGuide.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void updateSkuBanner(List<String> URLList) {
        CarouselUtil.initCarousel(mDDSquareBanner, URLList, mTvIndicator);
    }

    public void updateCartBadge(String count) {
        mDDProductActionContainer.updateCartBadge(count);
    }

    public void updateSpuLikeState(boolean isLike) {
        mIvCollectFirst.setSelected(isLike);
        mIvCollectSecond.setSelected(isLike);
    }

    public void setOnActionListener(OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
        mDDProductActionContainer.setOnActionListener(onActionListener);
        mOnClickListener = new OnClickListener(mOnActionListener);

        // toolbar返回 分享按钮
        mIvBackFirst.setOnClickListener(mOnClickListener);
        mIvBackSecond.setOnClickListener(mOnClickListener);
        mIvShareFirst.setOnClickListener(mOnClickListener);
        mIvShareSecond.setOnClickListener(mOnClickListener);
        mIvCollectFirst.setOnClickListener(mOnClickListener);
        mIvCollectSecond.setOnClickListener(mOnClickListener);

        // 商品相关点击事件
        mTvSkuInfo.setOnClickListener(mOnClickListener);
        mLlProductAuth.setOnClickListener(mOnClickListener);

        // 全部素材
        mTvMaterialAll.setOnClickListener(mOnClickListener);
        // 引导成为店主
        mRlBecomeMasterGuide.setOnClickListener(mOnClickListener);

        mDDProductPriceView.setOnClickListener(mOnClickListener);

    }

    public void recyclerWebView() {
        WebViewUtil.clearWebViewResource(mProductDetailWebView);
    }

    private void initBaseViewByUIUserCategory() {

        boolean isShowMasterUI = mUIUserCategory == UI_CATEGORY_USER_MASTER;
        mIsShowProductMaterial = isShowMasterUI;

        // 顶部导航栏
        List<String> tabList = isShowMasterUI ?
                Arrays.asList("商品", "发圈素材", "详情") : Arrays.asList("商品", "详情");
        mDDSmartTab.setTabTexts(tabList);

        // 分享
        showShareButton(true);

        // 发圈素材
        showProductMaterial(isShowMasterUI);

        // 底部按钮
        mDDProductActionContainer.renderViewByUserCategory(isShowMasterUI);

    }

    private void showProductMaterial(boolean isShow) {
        setViewVisibility(mAnchorMaterial, isShow);
    }

    private void showShareButton(boolean isShow) {
        mIvShareFirst.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        mIvShareSecond.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    private void setStoreGiftView() {
        mDDProductActionContainer.showSingleBuyContainer();
        showBecomeMasterGuide(false);
    }

    private void setViewVisibility(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    private void setProductFreeView() {

        mIsShowProductMaterial = false;
        // 隐藏分享
        showShareButton(false);

        mDDProductActionContainer.showSingleBuyButton("0元免费领");

        mDDSmartTab.setTabTexts(Arrays.asList("商品", "详情"));

        showProductMaterial(false);
    }

    private void loadDetailWebView(String htmlString) {
        mProductDetailWebView.setFocusable(false);
        WebViewUtil.loadDataToWebView(mProductDetailWebView, htmlString);
    }

    private void updateMaterialViews(List<CommunityDataBean> materials) {
        if (materials == null || materials.isEmpty()) {
            return;
        }
        mProductDetailMaterialAdapter.getItems().clear();
        mProductDetailMaterialAdapter.addItems(materials);

    }

    // 正品保证  7天无理由退货 等
    private void updateAuthViews(Product spuInfo) {
        if (spuInfo.auths == null || spuInfo.auths.size() < 1) {
            mLlProductAuth.setVisibility(View.GONE);
        } else {
            mLlProductAuth.setVisibility(View.VISIBLE);
            for (int i = 0; i < spuInfo.auths.size(); i++) {
                if (i > 2) {
                    break;
                }
                mIvAuthList.get(i).setVisibility(View.VISIBLE);
                FrescoUtil.setImageSmall(mIvAuthList.get(i), spuInfo.auths.get(i).icon);
                mTvAuthList.get(i).setVisibility(View.VISIBLE);
                mTvAuthList.get(i).setText(spuInfo.auths.get(i).title);
            }
        }
    }

    private void initAnimateListener() {

        mDDSmartTab.setOnDispatchTouchEventListener(new DDSmartTab.OnDispatchTouchEventListener() {
            @Override
            public void onDispatchingTouchEvent() {
                isScrollingToTop = false;
            }
        });

        mDDSmartTab.setOnTabChangedListener(new DDSmartTab.OnTabChangedListener() {
            @Override
            public void onTabChanged(int tabIndex) {
                DLog.i("onTabChanged:index=" + tabIndex + ",isScrollingToTop=" + isScrollingToTop);
                if (isScrollingToTop) {
                    // 阻止因为上滑tab改变导致scroll到指定锚点的
                    return;
                }
                int y = 0;
                switch (tabIndex) {
                    case 0:
                        y = 0;
                        break;
                    case 1:
                        y = mProductThreshold;
                        break;
                    case 2:
                        y = mMaterialThreshold;
                        break;
                }
                mNestedScrollView.scrollTo(0, y);
            }
        });

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                DLog.i("scrollY " + scrollY + ",oldScrollY " + oldScrollY);
                isScrollingToTop = scrollY < oldScrollY && Math.abs(scrollY - oldScrollY) < 100;
                if (mIsShowProductMaterial) {
                    // 三个tab
                    if (scrollY >= 0 && scrollY < mProductThreshold) {
                        mDDSmartTab.checkTab(0);
                    } else if (scrollY < mMaterialThreshold) {
                        mDDSmartTab.checkTab(1);
                    } else {
                        mDDSmartTab.checkTab(2);
                    }
                } else {
                    // 两个tab
                    mDDSmartTab.checkTab(scrollY < mProductThreshold ? 0 : 1);
                }

                setToolbarViewAlphaByScrollY(scrollY);

            }
        });
    }

    private void initMaterialRecyclerView() {
        mRvMaterial.setLayoutManager(new LinearLayoutManager(mContext));
        mProductDetailMaterialAdapter = new DDCommunityDataAdapter(mContext, DDCommunityDataAdapter.Mode.Product);
        mProductDetailMaterialAdapter.setItemStyleCorner(false);
        mRvMaterial.setAdapter(mProductDetailMaterialAdapter);
        mRvMaterial.setFocusable(false);

        RvUtils.clearItemAnimation(mRvMaterial);
    }


    private void setToolbarViewAlphaByScrollY(int y) {
        DLog.i("setToolbarViewAlphaByScrollY:y = " + y);
        if (y > mCurrentScrollY && Math.abs(y - mCurrentScrollY) > 1000) {
            // 解决快速滑动 toolbar 不隐藏的bug
            mCurrentScrollY = y;
            return;
        }
        mCurrentScrollY = y;
        if (y > mToolbarThreshold) {
            mRlToolbarSecond.setAlpha(1);
            mRlToolbarFirst.setAlpha(0);
        } else {
            mRlToolbarFirst.setAlpha((mToolbarThreshold - y) * 1.0F / mToolbarThreshold);
            mRlToolbarSecond.setAlpha((y * 1.0F) / mToolbarThreshold);
        }

    }


    private void initScrollThreshold() {
        mDDSquareBanner.post(new Runnable() {
            @Override
            public void run() {
                mToolbarThreshold = mDDSquareBanner.getHeight() - mFlToolbarContainer.getHeight();
            }
        });
        mRvMaterial.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mProductThreshold = mAnchorProduct.getHeight() - mFlToolbarContainer.getHeight();

                mMaterialThreshold = mAnchorDetail.getTop() - mFlToolbarContainer.getHeight();
                DLog.i("firstThreshold:" + mProductThreshold + ",secondThreshold:" + mMaterialThreshold);
            }
        });
    }

    private void clearProductTagView() {
        mLlProductTagContainer.removeAllViews();
    }

    private void addItemProductTagView(String category) {
        if (TextUtils.isEmpty(category)) {
            return;
        }
        DDTagView ddTagView = (DDTagView) LayoutInflater.from(mContext)
                .inflate(R.layout.item_product_category_tag, mLlProductTagContainer, false);

        if (category.contains("#")) {
            ddTagView.setText(category.split("#")[0]);
            ddTagView.setTagBackground("#" + category.split("#")[1]);
        } else {
            ddTagView.setText(category);
        }

        mLlProductTagContainer.addView(ddTagView);

    }

    public void toggleNotifyStyle() {
        mDDProductActionContainer.toggleNotify();
    }

    private MeasurementViewHolder getMeasurementViewHolder() {
        if (mMeasurementViewHolder == null) {
            mMeasurementViewHolder = new MeasurementViewHolder(mVsMeasurement.inflate());
        }
        return mMeasurementViewHolder;
    }

    private SuggestProductViewHolder getSuggestViewHolder() {
        if (mSuggestProductViewHolder == null) {
            mSuggestProductViewHolder = new SuggestProductViewHolder(mVsSuggest.inflate());
        }
        return mSuggestProductViewHolder;
    }

    public static class SuggestProductViewHolder implements BaseQuickAdapter.OnItemClickListener {

        // 大家都在买
        @BindView(R.id.ll_product_suggest)
        LinearLayout mLlProductSuggest;
        @BindView(R.id.dd_indicator)
        DDIndicator mDDIndicator;

        @BindView(R.id.tv_suggest_tag)
        TextView mTvSuggestTag;

        @BindView(R.id.vp_suggest)
        ViewPager mVpSuggest;

        private ProductSuggestAdapter mProductSuggestAdapter;

        public SuggestProductViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void setSuggestTag(String tag) {
            mTvSuggestTag.setText(tag);
        }

        private void render(List<Product.RelationProduct> list) {

            mLlProductSuggest.setVisibility(View.VISIBLE);
            int pageCount = list.size() / 3;
            mDDIndicator.setIndicatorCount(pageCount);
            mDDIndicator.setVisibility(pageCount > 1 ? View.VISIBLE : View.GONE);

            initViewPager(list);

        }

        private void initViewPager(final List<Product.RelationProduct> list) {
            mVpSuggest.setOffscreenPageLimit(3);
            ViewGroup.LayoutParams layoutParams = mVpSuggest.getLayoutParams();
            layoutParams.height = ScreenUtils.getScreenWidth() / 3 + ConvertUtil.dip2px(40);
            mVpSuggest.setLayoutParams(layoutParams);
            mVpSuggest.setAdapter(new PagerAdapter() {

                @Override
                public int getCount() {
                    if (list.size() > 6) {
                        return 3;
                    } else if (list.size() > 3) {
                        return 2;
                    }
                    return 1;
                }

                @Override
                public boolean isViewFromObject(View view, Object object) {
                    return view == object;
                }

                @Override
                public Object instantiateItem(ViewGroup container, int position) {
                    RecyclerView recyclerView = new RecyclerView(container.getContext());
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getInstance());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setFocusable(false);
                    ProductSuggestAdapter adapter = new ProductSuggestAdapter();
                    recyclerView.setAdapter(adapter);

                    List<Product.RelationProduct> l = new ArrayList<>();
                    for (int i = 0; i < list.size(); i++) {
                        if (i / 3 == position) {
                            l.add(list.get(i));
                        }
                        if (l.size() >= 3) {
                            break;
                        }
                    }
                    adapter.replaceData(l);
                    container.addView(recyclerView);
                    return recyclerView;
                }
            });


            mVpSuggest.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    mDDIndicator.setIndexActive(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }

        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        }
    }

    public static class MeasurementViewHolder {
        @BindView(R.id.sdv_avatar)
        SimpleDraweeView mSdvAvatar;
        @BindView(R.id.tv_measurement_nickname)
        TextView mTvMeasurementNickname;
        @BindView(R.id.tv_measurement_desc)
        TextView mTvMeasurementDesc;
        @BindView(R.id.dd_star_view)
        DDStarView mDDStarView;
        @BindView(R.id.tv_measurement_content)
        DDExpandTextView mTvMeasurementContent;
        @BindView(R.id.rv_measurement_image)
        RecyclerView mRvMeasurementImage;
        @BindView(R.id.rl_measurement)
        RelativeLayout mRlMeasurement;
        @BindView(R.id.fl_video)
        FrameLayout mFlVideo;
        @BindView(R.id.sdv_video)
        SimpleDraweeView mSdvVideo;

        public MeasurementViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        public void render(ProductEvaluateBean productEvaluateBean) {

            FrescoUtil.setImage(mSdvAvatar, productEvaluateBean.getHeadImage());
            mTvMeasurementNickname.setText(productEvaluateBean.getNikeName());
            mTvMeasurementDesc.setText(String.format("已甄选%s件好货", productEvaluateBean.getSpuNum()));

            mDDStarView.setValue(productEvaluateBean.getScore());
            mTvMeasurementContent.initWidth(ScreenUtils.getScreenWidth());
            mTvMeasurementContent.setCloseText(productEvaluateBean.getContent());

            if (productEvaluateBean.hasVideo()) {
                renderVideoModel(productEvaluateBean);
            } else if (productEvaluateBean.hasImages()) {
                renderImageModel(productEvaluateBean);
            }

        }

        private void renderImageModel(final ProductEvaluateBean productEvaluateBean) {
            mFlVideo.setVisibility(View.GONE);
            mRvMeasurementImage.setFocusable(false);
            mRvMeasurementImage.setVisibility(View.VISIBLE);

            mRvMeasurementImage.setLayoutManager(new GridLayoutManager(MyApplication.getInstance(), 4));
            mRvMeasurementImage.addItemDecoration(new GridSpacingItemDecoration(4, ConvertUtil.dip2px(12), false));
            MeasurementImageAdapter adapter = new MeasurementImageAdapter();
            adapter.addData(productEvaluateBean.getImageList());
            mRvMeasurementImage.setAdapter(adapter);

            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    ImageUtil.previewImage(view.getContext(), (ArrayList<String>) productEvaluateBean.getImageList(), position);
                }
            });

        }

        private void renderVideoModel(final ProductEvaluateBean productEvaluateBean) {
            mRvMeasurementImage.setVisibility(View.GONE);
            mFlVideo.setVisibility(View.VISIBLE);
            mSdvVideo.setImageURI(productEvaluateBean.getVideoImageUrl());
            mFlVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    DDVideoViewActivity.startWithTransition((Activity) context, mSdvVideo,
                            productEvaluateBean.getVideoPlayUrl(), productEvaluateBean.getVideoImageUrl());
                }
            });
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            mRlMeasurement.setOnClickListener(onClickListener);
            mTvMeasurementContent.setOnClickListener(onClickListener);
        }
    }

    public static class OnClickListener implements View.OnClickListener {
        private OnActionListener mActionListener;

        public OnClickListener(OnActionListener actionListener) {
            mActionListener = actionListener;
            if (mActionListener == null) {
                throw new NullPointerException("actionListener 不能为null");
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back_first:
                case R.id.iv_back_second:
                    mActionListener.onClickFinish();
                    break;
                case R.id.iv_share_first:
                case R.id.iv_share_second:
                case R.id.ll_btn_share:
                case R.id.ll_btn_share_left:
                    mActionListener.onClickShare();
                    break;
                case R.id.ll_collect:
                case R.id.iv_collect_first:
                case R.id.iv_collect_second:
                    mActionListener.onClickProductLike(!v.isSelected());
                    break;
                case R.id.tvSkuInfo:
                    mActionListener.onSelectSkuInfo();
                    break;
                case R.id.productAuthLayout:
                    mActionListener.onClickAuthInfo();
                    break;
                case R.id.tv_material_all:
                    mActionListener.onClickMaterialAll();
                    break;
                case R.id.tv_btn_add_cart:
                    mActionListener.onAddCart();
                    break;
                case R.id.tv_btn_buy_normal:
                case R.id.ll_btn_buy_master:
                case R.id.tv_activity_buy:
                case R.id.tv_single_buy:
                    mActionListener.onClickBuy();
                    break;
                case R.id.tv_cart:
                case R.id.fl_cart:
                    mActionListener.onClickCart();
                    break;
                case R.id.tv_service:
                    mActionListener.onClickCustomService();
                    break;
                case R.id.tv_become_master:
                case R.id.rl_become_master_guide:
                    mActionListener.onClickBecomeMaster();
                    break;
                case R.id.rl_measurement:
                    mActionListener.onClickMeasurement();
                    break;
                case R.id.tv_measurement_content:
                    mActionListener.onClickMeasurementDetail();
                    break;
                case R.id.tv_btn_master_notify:
                case R.id.tv_btn_normal_notify:
                    mActionListener.onClickNotify();
                    break;
                case R.id.dd_product_price_view:
                    mActionListener.onClickFlashSale();
                    break;
                default:
            }
        }
    }

    public interface OnActionListener {

        // 返回按钮
        void onClickFinish();

        // 分享
        void onClickShare();

        // 喜欢商品
        void onClickProductLike(boolean isLikeProduct);

        // 选择商品规格
        void onSelectSkuInfo();

        // 选择地址
        void onSelectAddress();

        // 7天包邮 等 商家承诺？
        void onClickAuthInfo();

        // 查看全部素材
        void onClickMaterialAll();

        // 点击去购物车
        void onClickCart();

        // 加入购物车
        void onAddCart();

        // 点击购买
        void onClickBuy();

        // 点击客服
        void onClickCustomService();

        // 成为店主
        void onClickBecomeMaster();

        // 品控师
        void onClickMeasurement();

        // 测评详情
        void onClickMeasurementDetail();

        // 开抢提醒
        void onClickNotify();

        // 抢购列表
        void onClickFlashSale();

    }

}
