package com.xiling.ddui.tools;

import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.adapter.DDCommunityDataAdapter;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.bean.RealAuthBean;
import com.xiling.ddui.bean.SkuListBean;
import com.xiling.ddui.custom.DDSmartTab;
import com.xiling.ddui.custom.DDSquareBanner;
import com.xiling.ddui.manager.ShopDetailManager;
import com.xiling.dduis.adapter.ShopListTagsAdapter;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.SkuPvIds;
import com.xiling.shared.component.dialog.SkuSelectorDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.util.CarouselUtil;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.SessionUtil;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WebViewUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.xiling.shared.component.dialog.SkuSelectorDialog.ACTION_CARD;
import static com.xiling.shared.component.dialog.SkuSelectorDialog.ACTION_SELECT;
import static com.xiling.shared.component.dialog.SkuSelectorDialog.ACTION_SHOPPING;

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

    @BindView(R.id.fl_cart)
    View flCard;
    @BindView(R.id.tv_btn_add_cart)
    TextView tvBtnAddCart;
    @BindView(R.id.tv_btn_buy_normal)
    TextView tvBtnBuyNormal;
    // 产品详情
    @BindView(R.id.web_view)
    WebView mProductDetailWebView;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.rl_become_master_guide)
    RelativeLayout rlBecomeMasterGuide;
    @BindView(R.id.tv_reward_guide)
    TextView tvRewardGuide;

    private OnActionListener mOnActionListener;

    private DDProductDetailActivity mContext;
    private View baseContentView;
    private OnClickListener mOnClickListener;

    // 是否是从上往上滑动 往上滑动时，tab 的 onCheckChanged 不做处理
    private boolean isScrollingToTop = false;

    // 滑动阀值
    private int mProductThreshold;

    // toolbar 动画阀值
    private int mToolbarThreshold;

    // 解决快速上滑到顶 toolbar不隐藏
    private int mCurrentScrollY = 0;

    SkuSelectorDialog mSkuSelectorDialog;
    ProductNewBean mSpuInfo;
    NewUserBean newUserBean;

    public ProductDetailUIHelper(DDProductDetailActivity productDetailActivity) {
        mContext = productDetailActivity;
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
        initDetailThreshold();

        initAnimateListener();

    }


    public void updateSkuViews(String name) {

        if (TextUtils.isEmpty(name)) {
            mTvSkuInfo.setText("请选择规格");
        } else {
            mTvSkuInfo.setText(name);
        }

    }


    public void updateSpuViews(ProductNewBean spuInfo, NewUserBean newUserBean) {
        if (spuInfo == null) {
            return;
        }
        mSpuInfo = spuInfo;
        this.newUserBean = newUserBean;

        if (newUserBean != null) {
            if (newUserBean.getRole().getRoleLevel() == 30) {
                //黑卡会员
                rlBecomeMasterGuide.setVisibility(View.GONE);
            } else {
                rlBecomeMasterGuide.setVisibility(View.VISIBLE);
                tvRewardGuide.setText("开通黑卡会员，享受最低价¥" + mSpuInfo.getLevel30Price());
            }
        }


        if (spuInfo.getImages() != null && spuInfo.getImages().size() > 0) {
            updateSkuBanner(spuInfo.getImages());
        }

        mTvProductTitle.setText(spuInfo.getProductName());

        String mStatus = ShopUtils.checkShopStatus(spuInfo.getStatus(), spuInfo.getStock());
        if (!TextUtils.isEmpty(mStatus)) {
            mTvSoldOut.setText(mStatus);
            mTvSoldOut.setVisibility(View.VISIBLE);
            tvBtnAddCart.setTextColor(Color.parseColor("#AAAAAA"));
            tvBtnBuyNormal.setBackgroundColor(Color.parseColor("#AAAAAA"));
            mTvSkuInfo.setTextColor(Color.parseColor("#AAAAAA"));
            tvBtnAddCart.setClickable(false);
            tvBtnBuyNormal.setClickable(false);
            mTvSkuInfo.setClickable(false);
            relSkuInfo.setClickable(false);
        } else {
            mTvSoldOut.setVisibility(View.GONE);
            tvBtnAddCart.setTextColor(Color.parseColor("#202020"));
            tvBtnBuyNormal.setBackgroundColor(Color.parseColor("#DCB982"));
            tvBtnAddCart.setClickable(true);
            tvBtnBuyNormal.setClickable(true);
            mTvSkuInfo.setTextColor(Color.parseColor("#202020"));
            mTvSkuInfo.setClickable(true);
            relSkuInfo.setClickable(true);
        }

        //优惠价，需要根据用户等级展示不同价格
        NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(spuInfo), tvDiscountPrice, tvDiscountPriceDecimal);

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
        updateSkuViews("");


        loadDetailWebView(spuInfo.getContent());

        GlideUtils.loadImage(mContext, ivBottom, "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1922673126,3860003593&fm=26&gp=0.jpg");


    }

    public void recyclerWebView() {
        WebViewUtil.clearWebViewResource(mProductDetailWebView);
    }

    private void loadDetailWebView(String htmlString) {
        mProductDetailWebView.setFocusable(false);
        WebViewUtil.loadDataToWebView(mProductDetailWebView, htmlString);
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

        // 商品相关点击事件
        mTvSkuInfo.setOnClickListener(mOnClickListener);
        relSkuInfo.setOnClickListener(mOnClickListener);

        flCard.setOnClickListener(mOnClickListener);
        tvBtnAddCart.setOnClickListener(mOnClickListener);
        tvBtnBuyNormal.setOnClickListener(mOnClickListener);

        rlBecomeMasterGuide.setOnClickListener(mOnClickListener);

    }


    private void initBaseViewByUIUserCategory() {

        // 顶部导航栏
        List<String> tabList = Arrays.asList("商品", "详情");
        mDDSmartTab.setTabTexts(tabList);

        // 分享
        showShareButton(true);


    }

    private void showShareButton(boolean isShow) {
        mIvShareFirst.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        mIvShareSecond.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
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
                }
                mNestedScrollView.scrollTo(0, y);
            }
        });

        mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                DLog.i("scrollY " + scrollY + ",oldScrollY " + oldScrollY);
                isScrollingToTop = scrollY < oldScrollY && Math.abs(scrollY - oldScrollY) < 100;
                mDDSmartTab.checkTab(scrollY < mProductThreshold ? 0 : 1);
                setToolbarViewAlphaByScrollY(scrollY);
            }
        });
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
    }

    private void initDetailThreshold() {
        mAnchorDetail.post(new Runnable() {
            @Override
            public void run() {
                mProductThreshold = mAnchorDetail.getTop() - mAnchorDetail.getHeight();
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
                case R.id.relSkuInfo:
                case R.id.tvSkuInfo:
                    //选择规格
                    showSkuDialog(ACTION_SELECT);
                    break;
                case R.id.tv_btn_add_cart:
                    //加入购物车
                    showSkuDialog(ACTION_CARD);
                    break;
                case R.id.tv_btn_buy_normal:
                    //立即购买
                    showSkuDialog(ACTION_SHOPPING);
                    break;
                case R.id.fl_cart:
                    //进入购物车
                    mActionListener.onClickCart();
                    break;
                case R.id.tv_become_master:
                case R.id.rl_become_master_guide:
                    //立即升级
                    mActionListener.onClickBecomeMaster();
                    break;
                default:
            }
        }
    }


    private void showSkuDialog(int action) {
        if (mSpuInfo != null && mSpuInfo.getSkus() != null && mSpuInfo.getSkus().size() > 0) {
            if (mSkuSelectorDialog == null) {
                mSkuSelectorDialog = new SkuSelectorDialog(mContext, mSpuInfo, action);
            } else {
                mSkuSelectorDialog.setmAction(action);
            }

            mSkuSelectorDialog.setSelectListener(new SkuSelectorDialog.OnSelectListener() {

                @Override
                public void onClose(String propertyValue) {
                    updateSkuViews(propertyValue);
                }

                @Override
                public void joinShopCart(String skuId, String propertyValue, int selectCount) {
                    updateSkuViews(propertyValue);
                    mOnActionListener.onAddCart(skuId, selectCount);
                }

                @Override
                public void buyItNow(String skuId, String propertyValue, int selectCount) {
                    updateSkuViews(propertyValue);
                    SkuListBean skuListBean = new SkuListBean(skuId, selectCount);
                    mOnActionListener.onClickBuy(skuListBean);
                }
            });
            mSkuSelectorDialog.show();
        }
    }


    public interface OnActionListener {

        // 返回按钮
        void onClickFinish();

        // 分享
        void onClickShare();

        // 点击去购物车
        void onClickCart();

        // 加入购物车
        void onAddCart(String skuId, int selectCount);

        // 立即购买
        void onClickBuy(SkuListBean skuListBean);

        // 立即升级
        void onClickBecomeMaster();

    }

}
