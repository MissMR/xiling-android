package com.xiling.ddui.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.tools.ProductDetailUIHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by Jigsaw at 2019/3/20
 * 商品详情页 底部按钮
 * 客服 加入购物车 立即购买 分享
 */
public class DDProductActionContainer extends LinearLayout {

    private static final String STRING_BUY_REWARD = "立返￥%s";
    private static final String STRING_SHARE_REWARD = "立赚￥%s";

    // 不可购买文字提示
    @BindView(R.id.tv_un_buyable_hint)
    TextView mTvUnBuyableHint;

    @BindView(R.id.ll_bottom_navigation_normal)
    LinearLayout mLlBottomNavigationNormal;
    @BindView(R.id.rl_bottom_navigation_single)
    RelativeLayout mRlBottomNavigationSingle;

    // 底部购物车
    @BindView(R.id.fl_cart)
    FrameLayout mFlCart;

    @BindView(R.id.tv_cart)
    TextView mTvCart;
    // 购物车
    @BindView(R.id.tv_cart_badge)
    TextView mTvCartBadge;
    // 底部客服
    @BindView(R.id.tv_service)
    TextView mTvService;

    // 普通会员底部导航按钮
    @BindView(R.id.ll_button_container_normal)
    LinearLayout mLlBtnContainerNormal;
    // 加入购物车
    @BindView(R.id.tv_btn_add_cart)
    TextView mTvBtnAddCart;
    // 立即购买
    @BindView(R.id.tv_btn_buy_normal)
    TextView mTvBtnBuyNormal;

    // 店主底部导航按钮
    @BindView(R.id.ll_button_container_master)
    LinearLayout mLlBtnContainerMaster;
    @BindView(R.id.ll_btn_share_left)
    LinearLayout mLlBtnShareLeft;

    // 立省购
    @BindView(R.id.ll_btn_buy_master)
    LinearLayout mLlBtnBuyMaster;
    // 分享赚
    @BindView(R.id.ll_btn_share)
    LinearLayout mLlBtnShare;

    // 店主购买佣金
    @BindView(R.id.tv_buy_reward)
    TextView mTvBuyReward;
    // 店主分享佣金
    @BindView(R.id.tv_share_reward)
    TextView mTvShareReward;
    @BindView(R.id.tv_share_reward_left)
    TextView mTvShareRewardLeft;

    // 店主礼包立即购买
    @BindView(R.id.tv_activity_buy)
    TextView mTvSingleBuy;

    // 单独导航栏的购买
    @BindView(R.id.tv_single_buy)
    TextView mTvSingleNavigationBuy;

    // 开抢提醒
    @BindView(R.id.tv_btn_normal_notify)
    TextView mTvBtnNormalNotify;
    @BindView(R.id.tv_btn_master_notify)
    TextView mTvBtnMasterNotify;

    private Unbinder mUnBinder;

    private ProductDetailUIHelper.OnClickListener mOnClickListener;

    public DDProductActionContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public DDProductActionContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DDProductActionContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.include_product_detail_footer, this);
        setOrientation(VERTICAL);
        mUnBinder = ButterKnife.bind(this);

        mTvService.setActivated(true);
        mTvCart.setActivated(true);

    }


    public void setRewardPrice(String rewardPrice) {
        mTvBuyReward.setText(String.format(STRING_BUY_REWARD, rewardPrice));
        mTvShareReward.setText(String.format(STRING_SHARE_REWARD, rewardPrice));
        mTvShareRewardLeft.setText(String.format(STRING_SHARE_REWARD, rewardPrice));
    }

    public void updateCartBadge(String count) {
        if (TextUtils.isEmpty(count) || "0".equals(count)) {
            mTvCartBadge.setVisibility(View.GONE);
            return;
        }
        mTvCartBadge.setText(count);
        mTvCartBadge.setVisibility(View.VISIBLE);
    }

    public void renderViewByUserCategory(boolean isMaster) {
        mLlBtnContainerMaster.setVisibility(isMaster ? VISIBLE : GONE);
        mLlBtnContainerNormal.setVisibility(isMaster ? GONE : VISIBLE);
    }

    /**
     * 预售样式
     *
     * @param isInPreFlashSale 是否是预售
     */
    public void showPreFlashSale(boolean isInPreFlashSale) {

        boolean isShowNotify = isInPreFlashSale;
        mTvBtnMasterNotify.setVisibility(isShowNotify ? VISIBLE : GONE);
        mTvBtnNormalNotify.setVisibility(isShowNotify ? VISIBLE : GONE);

        boolean isShowRedBtn = !isInPreFlashSale;
        mTvBtnBuyNormal.setVisibility(isShowRedBtn ? VISIBLE : GONE);
        mLlBtnShare.setVisibility(isShowRedBtn ? VISIBLE : GONE);

        // 若是预售 店主左侧为分享赚
        boolean isShowShareLeft = isInPreFlashSale;
        setViewVisibility(mLlBtnShareLeft, isShowShareLeft);
        setViewVisibility(mLlBtnBuyMaster, !isShowShareLeft);

        mTvCart.setActivated(!isInPreFlashSale);

    }

    public void toggleNotify() {
        boolean active = mTvBtnNormalNotify.isActivated();
        setNotifyActive(!active);
    }

    public void setNotifyActive(boolean active) {

        mTvBtnNormalNotify.setActivated(active);
        mTvBtnMasterNotify.setActivated(active);

        String btnText = active ? "开抢提醒" : "已提醒";
        mTvBtnMasterNotify.setText(btnText);
        mTvBtnNormalNotify.setText(btnText);
    }

    public void showSingleBuyContainer() {
        mRlBottomNavigationSingle.setVisibility(View.VISIBLE);
        mLlBottomNavigationNormal.setVisibility(View.GONE);
    }

    public void showSingleBuyButton(String buttonText) {
        // 底部导航按钮
        setViewVisibility(mLlBtnContainerMaster, false);
        setViewVisibility(mLlBtnContainerNormal, false);
        setViewVisibility(mTvSingleBuy, true);
        mTvSingleBuy.setText(buttonText);
    }

    public void setProductUnBuyableHint(String text) {
        if (TextUtils.isEmpty(text)) {
            mTvUnBuyableHint.setVisibility(View.GONE);
        } else {
            mTvUnBuyableHint.setText(text);
            mTvUnBuyableHint.setVisibility(View.VISIBLE);
            setViewVisibility(mLlBtnContainerMaster, false);
            setViewVisibility(mLlBtnContainerNormal, false);
            mTvService.setActivated(false);
        }
    }

    public void setBuyButtonEnable(boolean enable) {
        mLlBtnBuyMaster.setEnabled(enable);
        mLlBtnShare.setEnabled(enable);
        mTvBtnBuyNormal.setEnabled(enable);
        mTvBtnAddCart.setEnabled(enable);
    }

    public void setOnActionListener(ProductDetailUIHelper.OnActionListener listener) {
        mOnClickListener = new ProductDetailUIHelper.OnClickListener(listener);
        // 底部导航按钮
        mFlCart.setOnClickListener(mOnClickListener);
        mTvCart.setOnClickListener(mOnClickListener);
        mTvService.setOnClickListener(mOnClickListener);
        // 普通会员
        mTvBtnAddCart.setOnClickListener(mOnClickListener);
        mTvBtnBuyNormal.setOnClickListener(mOnClickListener);
        // 店主
        mLlBtnBuyMaster.setOnClickListener(mOnClickListener);
        mLlBtnShare.setOnClickListener(mOnClickListener);
        mLlBtnShareLeft.setOnClickListener(mOnClickListener);
        // 店主礼包购买
        mTvSingleBuy.setOnClickListener(mOnClickListener);

        mTvSingleNavigationBuy.setOnClickListener(mOnClickListener);

        // 开抢提醒
        mTvBtnMasterNotify.setOnClickListener(mOnClickListener);
        mTvBtnNormalNotify.setOnClickListener(mOnClickListener);

    }

    private void setViewVisibility(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mUnBinder.unbind();
    }
}
