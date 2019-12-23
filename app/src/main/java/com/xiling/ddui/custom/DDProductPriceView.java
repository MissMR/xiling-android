package com.xiling.ddui.custom;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.dduis.custom.SaleProgressView;
import com.xiling.shared.bean.Product;
import com.xiling.shared.component.CountDown;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.TextViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by Jigsaw at 2019/4/8
 * 商品详情页 价格/抢购信息
 */
public class DDProductPriceView extends RelativeLayout implements CountDown.OnFinishListener {

    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_price_market)
    TextView mTvPriceMarket;
    @BindView(R.id.tv_price_reward)
    TextView mTvPriceReward;
    @BindView(R.id.tv_share_count)
    TextView mTvShareCount;
    @BindView(R.id.tv_sale_count)
    TextView mTvSaleCount;
    @BindView(R.id.tv_flash_hint_pre)
    TextView mTvFlashHintPre;
    @BindView(R.id.sale_progress_view)
    SaleProgressView mSaleProgressView;
    @BindView(R.id.ll_flash_sale)
    LinearLayout mLlFlashSale;
    @BindView(R.id.tv_flash_sale_time)
    TextView mTvFlashSaleTime;
    @BindView(R.id.count_down)
    CountDown mCountDown;

    private Unbinder mUnBinder;

    private Product mProduct;
    private boolean isMaster;

    private OnFlashSaleListener mOnFlashSaleListener;

    public DDProductPriceView(Context context) {
        super(context);
        initView();
    }

    public DDProductPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DDProductPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setData(Product product) {
        this.mProduct = product;
        render();
    }

    public void setOnFlashSaleListener(OnFlashSaleListener listener) {
        this.mOnFlashSaleListener = listener;
    }

    private void initView() {
        inflate(getContext(), R.layout.include_product_price, this);
        mUnBinder = ButterKnife.bind(this);
        isMaster = SessionUtil.getInstance().isMaster();
    }

    private void render() {
        if (mProduct.isFlashSaleActive()) {
            renderFlashSaleView();
        } else {
            renderNormalView();
        }
    }

    private void renderNormalView() {
        renderNormalStyle();

        mTvPrice.setText(mProduct.getRetailPriceStr());
        mTvPriceReward.setText("赚" + ConvertUtil.cent2yuanNoZero(mProduct.rewardPrice));
        mTvPriceMarket.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mProduct.marketPrice));

        mTvSaleCount.setText(String.format("已售%s件", mProduct.saleCount));

    }

    // 普通商品样式
    private void renderNormalStyle() {
        setBackground(new ColorDrawable());
        mTvPriceMarket.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_gray_dark));
        mTvShareCount.setVisibility(GONE);
        mLlFlashSale.setVisibility(GONE);
        mTvSaleCount.setVisibility(VISIBLE);
        if (isMaster && !skipMasterStyle()) {
            mTvPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_black_dark));
            mTvPriceReward.setVisibility(VISIBLE);
            mTvPriceReward.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_red));
            mTvPriceMarket.setVisibility(GONE);
        } else {
            mTvPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_red));
            mTvPriceReward.setVisibility(GONE);
            mTvPriceMarket.setVisibility(VISIBLE);
            TextViewUtil.addThroughLine(mTvPriceMarket);
        }
    }

    private void renderFlashSaleView() {
        if (!mProduct.isFlashSale()) {
            renderNormalStyle();
            return;
        }
        renderFlashSaleStyle();

        Product.FlashSaleDetail flashSaleDetail = mProduct.flashSaleDetail;

        mTvPrice.setText(mProduct.getRetailPriceStr());
        mTvPriceMarket.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mProduct.flashSaleDetail.getMaxSalePrice()));
        mTvPriceReward.setText("赚" + ConvertUtil.cent2yuanNoZero(mProduct.flashSaleDetail.getMaxBrokeragePrice()));

        String desc = isMaster ? String.format("已推%s次", flashSaleDetail.getExtendTime())
                : String.format("%s人已关注", flashSaleDetail.getFocusTime());
        mTvShareCount.setText(desc);
        mTvFlashSaleTime.setText(flashSaleDetail.getFormatStartTime() + "开抢");

        if (flashSaleDetail.isBeforeFlashSale24()) {
            mCountDown.setTimeLeft(flashSaleDetail.getTimeBeforeFlashSale(), this);
        } else if (flashSaleDetail.isInFlashSale()) {
            if (flashSaleDetail.hasSoldOut()) {
                renderSoldOutStyle();
            } else {
                mCountDown.setTimeLeft(flashSaleDetail.getRemainingTime(), this);
            }
        }

    }

    // 抢购商品样式
    private void renderFlashSaleStyle() {
        Product.FlashSaleDetail flashSaleDetail = mProduct.flashSaleDetail;

        mTvPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        mTvPriceReward.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_yellow_product_detail));
        mTvPriceMarket.setTextColor(ContextCompat.getColor(getContext(), R.color.white_a50));
        mTvShareCount.setVisibility(GONE);
        mTvSaleCount.setVisibility(GONE);

        mTvPriceReward.setVisibility(isMaster ? VISIBLE : INVISIBLE);
        mTvPriceMarket.setVisibility(isMaster ? INVISIBLE : VISIBLE);
        TextViewUtil.addThroughLine(mTvPriceMarket);

        if (flashSaleDetail.isBeforeFlashSale24()) {
            // 预售 抢购前24小时
            setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.bg_flash_sale_green));
            mLlFlashSale.setVisibility(VISIBLE);
            mTvShareCount.setVisibility(VISIBLE);
            mTvFlashSaleTime.setVisibility(VISIBLE);
            mSaleProgressView.setVisibility(GONE);
            mTvFlashSaleTime.setVisibility(VISIBLE);
            mTvFlashHintPre.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            mTvFlashHintPre.setText("距开始");
            mCountDown.setColorStyle(Color.WHITE);
        } else if (flashSaleDetail.isInFlashSale()) {
            // 抢购中
            setBackground(ContextCompat.getDrawable(getContext(), R.mipmap.bg_flash_sale_red));
            mLlFlashSale.setVisibility(VISIBLE);
            mTvShareCount.setVisibility(GONE);
            mTvFlashSaleTime.setVisibility(GONE);
            mSaleProgressView.setVisibility(VISIBLE);
            mTvFlashSaleTime.setVisibility(GONE);
            mTvFlashHintPre.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_red));
            mTvFlashHintPre.setText("距结束");
            mCountDown.setColorStyle(Color.RED);
            mSaleProgressView.setProgress(flashSaleDetail.getInventory() + flashSaleDetail.getSaleCount(),
                    flashSaleDetail.getSaleCount(), flashSaleDetail.getFlashInventoryRate());
        } else {
            // 抢购结束 或 抢购前超过24小时
            renderNormalStyle();
        }

    }

    private void renderSoldOutStyle() {
        renderNormalStyle();
        mTvPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_gray_ccc));
        mTvPriceMarket.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_gray_ccc));
        mTvSaleCount.setVisibility(GONE);

        setBackground(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.ddm_gray_dark)));
    }

    /**
     * 显示普通商品价格样式
     * 店主礼包/0元购/抢购抢光了
     *
     * @return
     */
    private boolean skipMasterStyle() {
        return mProduct.isStoreGift() || mProduct.isProductFree()
                || (mProduct.isFlashSale() && mProduct.flashSaleDetail.hasSoldOut());
    }

    @Override
    protected void onDetachedFromWindow() {
        mUnBinder.unbind();
        super.onDetachedFromWindow();
    }

    @Override
    public void onFinish() {
        if (mOnFlashSaleListener != null && mProduct.flashSaleDetail.isBeforeFlashSale24()) {
            mProduct.flashSaleDetail.setFlashSaleStart();
            mOnFlashSaleListener.onFlashSaleStart();
        }
        if (mProduct.flashSaleDetail.isInFlashSale()) {
            mProduct.flashSaleDetail.setFlashSaleEnd();
        }
        render();
    }

    // 抢购开始监听
    public interface OnFlashSaleListener {
        void onFlashSaleStart();
    }

}
