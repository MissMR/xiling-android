package com.xiling.ddui.tools;

import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.adapter.DDCommunityDataAdapter;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.custom.DDSmartTab;
import com.xiling.ddui.custom.DDSquareBanner;
import com.xiling.ddui.manager.ShopDetailManager;
import com.xiling.dduis.adapter.ShopListTagsAdapter;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.SkuPvIds;
import com.xiling.shared.component.dialog.SkuSelectorDialog;
import com.xiling.shared.util.CarouselUtil;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.SessionUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
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


    // 商品标题
    @BindView(R.id.tv_product_title)
    TextView mTvProductTitle;
    // 商品sku 具体商品类别
    @BindView(R.id.tvSkuInfo)
    TextView mTvSkuInfo;
    @BindView(R.id.relSkuInfo)
    RelativeLayout relSkuInfo;


    @BindView(R.id.tv_material_all)
    TextView mTvMaterialAll;


    // 发圈素材总数
    @BindView(R.id.tv_material_count)
    TextView mTvMaterialCount;
    // 发圈素材
    @BindView(R.id.rv_material)
    RecyclerView mRvMaterial;


    // banner 指示器
    @BindView(R.id.tv_indicator)
    TextView mTvIndicator;


    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;

    @BindView(R.id.tv_discount_price_decimal)
    TextView tvDiscountPriceDecimal;

    @BindView(R.id.tv_minPrice)
    TextView tvMinPrice;

    @BindView(R.id.tv_minMarketPrice)
    TextView tvMinMarketPrice;

    @BindView(R.id.tv_sale_size)
    TextView tvSaleSize;

    @BindView(R.id.recycler_tag)
    RecyclerView recyclerTag;
    ShopListTagsAdapter shopListTagsAdapter;


    private OnActionListener mOnActionListener;

    private DDProductDetailActivity mContext;
    private View baseContentView;
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


    ShopDetailManager shopDetailManager;
    SkuSelectorDialog mSkuSelectorDialog;
    ProductNewBean mSpuInfo;

    public ProductDetailUIHelper(DDProductDetailActivity productDetailActivity) {
        mContext = productDetailActivity;
        mUIUserCategory = SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isStoreMaster()
                ? UI_CATEGORY_USER_MASTER : UI_CATEGORY_USER_NORMAL;

        shopDetailManager = new ShopDetailManager();

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

    }


    public void updateSkuViews(String name) {

        if (TextUtils.isEmpty(name)) {
            mTvSkuInfo.setText("请选择规格");
        } else {
            mTvSkuInfo.setText(name);
        }

    }

    public void updateSpuViews(ProductNewBean spuInfo) {
        if (spuInfo == null) {
            return;
        }
        mSpuInfo = spuInfo;

        if (spuInfo.getImages() != null && spuInfo.getImages().size() > 0) {
            updateSkuBanner(spuInfo.getImages());
        }

        mTvProductTitle.setText(spuInfo.getProductName());

        if (spuInfo.getStatus() == 1) {
            mTvSoldOut.setVisibility(View.VISIBLE);
        } else {
            mTvSoldOut.setVisibility(View.GONE);
        }

        //优惠价，需要根据用户等级展示不同价格
        NewUserBean userBean = UserManager.getInstance().getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    NumberHandler.setPriceText(spuInfo.getLevel10Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
                case 20:
                    NumberHandler.setPriceText(spuInfo.getLevel20Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
                case 30:
                    NumberHandler.setPriceText(spuInfo.getLevel30Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
            }
        } else {
            NumberHandler.setPriceText(spuInfo.getLevel10Price(), tvDiscountPrice, tvDiscountPriceDecimal);
        }

        //售价
        tvMinPrice.setText("¥" + NumberHandler.reservedDecimalFor2(spuInfo.getMinPrice()));
        //划线价
        tvMinMarketPrice.setText("¥" + NumberHandler.reservedDecimalFor2(spuInfo.getMinMarketPrice()));
        tvMinMarketPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvMinMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tvSaleSize.setText("已售" + spuInfo.getSaleCount());

        LinearLayoutManager tagManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        tagManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerTag.setLayoutManager(tagManager);
        shopListTagsAdapter = new ShopListTagsAdapter(R.layout.item_shop_detail_tag, spuInfo.getProductTags());
        recyclerTag.setAdapter(shopListTagsAdapter);
        if (spuInfo.getSkus() != null && spuInfo.getSkus().size() > 0) {
            updateSkuViews(spuInfo.getSkus().get(0).getPropertyValues());
        } else {
            updateSkuViews("");
        }
    }


    public void updateSkuBanner(List<String> URLList) {
        CarouselUtil.initCarousel(mDDSquareBanner, URLList, mTvIndicator);
    }


    public void setOnActionListener(OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
        mOnClickListener = new OnClickListener(mOnActionListener);

        // toolbar返回 分享按钮
        mIvBackFirst.setOnClickListener(mOnClickListener);
        mIvBackSecond.setOnClickListener(mOnClickListener);
        mIvShareFirst.setOnClickListener(mOnClickListener);
        mIvShareSecond.setOnClickListener(mOnClickListener);
        mIvCollectSecond.setOnClickListener(mOnClickListener);

        // 商品相关点击事件
        mTvSkuInfo.setOnClickListener(mOnClickListener);
        relSkuInfo.setOnClickListener(mOnClickListener);

        // 全部素材
        mTvMaterialAll.setOnClickListener(mOnClickListener);


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

    }

    private void showProductMaterial(boolean isShow) {
        setViewVisibility(mAnchorMaterial, isShow);
    }

    private void showShareButton(boolean isShow) {
        mIvShareFirst.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        mIvShareSecond.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    private void setViewVisibility(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
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

    public class OnClickListener implements View.OnClickListener {
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
                case R.id.iv_collect_second:
                    mActionListener.onClickProductLike(!v.isSelected());
                    break;
                case R.id.relSkuInfo:
                case R.id.tvSkuInfo:
                    //选择规格
                    if (mSpuInfo != null && mSpuInfo.getSkus() != null && mSpuInfo.getSkus().size() > 0) {
                        if (mSkuSelectorDialog == null) {
                            mSkuSelectorDialog = new SkuSelectorDialog(mContext, mSpuInfo, 0);
                        }
                        mSkuSelectorDialog.setSelectListener(new SkuSelectorDialog.OnSelectListener() {

                            @Override
                            public void onClose(String propertyValue) {
                                updateSkuViews(propertyValue);
                            }

                            @Override
                            public void joinShopCart(String propertyIds, String propertyValue, int selectCount) {
                                updateSkuViews(propertyValue);
                            }

                            @Override
                            public void buyItNow(String propertyIds, String propertyValue, int selectCount) {
                                updateSkuViews(propertyValue);
                            }
                        });
                        mSkuSelectorDialog.show();
                    }

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
