package com.dodomall.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.SkuHelper;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Product;
import com.dodomall.ddmall.shared.bean.Property;
import com.dodomall.ddmall.shared.bean.PropertyValue;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.SkuPvIds;
import com.dodomall.ddmall.shared.component.NumberField;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.contracts.OnValueChangeLister;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.CartManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IProductService;
import com.dodomall.ddmall.shared.util.CommonUtil;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.flexbox.FlexboxLayout;
import com.google.common.base.Joiner;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class SkuSelectorDialog extends Dialog {

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.priceTv)
    protected TextView mPriceTv;
    @BindView(R.id.thumbIv)
    protected SimpleDraweeView mThumbIv;
    @BindView(R.id.stockTv)
    protected TextView mStockTv;
    @BindView(R.id.buyNowBtn)
    protected TextView mBuyNowBtn;
    @BindView(R.id.addToCartBtn)
    protected TextView mAddToCartBtn;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;
    @BindView(R.id.numberField)
    protected NumberField mNumberField;

    @BindView(R.id.layoutBotttom)
    LinearLayout mLayoutBottom;
    @BindView(R.id.tvBottomGreyText)
    TextView mTvBottomGreyText;

    @BindView(R.id.tv_market_price)
    TextView mTvMarketPrice;
    @BindView(R.id.tv_activity_tag)
    TextView mTvActivityTag;

    @BindView(R.id.tv_property_selected)
    TextView mTvPropertySelected;

    protected Product mSpuInfo;
    private SkuInfo mSkuResult;
    private SkuPvIds mSkuPvIdsResult;
    private IProductService mProductService;
    private int mAction = -1;
    @IntRange(from = 1)
    private int mAmount = 1;
    private OnSelectListener mSelectListener;
    private String mGroupCode;
    private TagFlowAdapter mTagFlowAdapter;

    public SkuSelectorDialog(Context context, Product product) {
        this(context, 0);
        this.mSpuInfo = product;
    }

    private SkuSelectorDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    public SkuSelectorDialog(Context context, Product mProduct, int action) {
        this(context, mProduct);
        mAction = action;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_sku_selector);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        mProductService = ServiceManager.getInstance().createService(IProductService.class);
        initViews();
    }

    private void initViews() {

        if (mSpuInfo.isFlashSale() && mSpuInfo.flashSaleDetail.isInFlashSale()) {
            mTvActivityTag.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTagFlowAdapter = new TagFlowAdapter(mSpuInfo, new SkuHelper.OnSkuPropertySelectListener() {
            @Override
            public void onSkuSelected(List<String> propertyValueIds, SkuPvIds skuPvIds) {
                DLog.i("onSkuSelected:" + skuPvIds.toString());
//                loadSkuInfoByPropertyValueIds(propertyValueIds);
                setSkuInfo(skuPvIds);
                mTvPropertySelected.setText("已选：" + skuPvIds.propertyValues);
            }

            @Override
            public void onPropertyChanged(Map<Property, PropertyValue> propertyPropertyValueMap, boolean isSelectedEnd) {
                if (propertyPropertyValueMap.isEmpty()) {
                    mTvPropertySelected.setText("请选择规格属性");
                    mSkuPvIdsResult = null;
                    return;
                }
                if (!isSelectedEnd && mSkuPvIdsResult != null) {
                    mSkuPvIdsResult = null;
                }
                StringBuilder stringBuilder = new StringBuilder("已选：");
                for (PropertyValue p : propertyPropertyValueMap.values()) {
                    stringBuilder.append(p.value + " ");
                }
                mTvPropertySelected.setText(stringBuilder);
            }
        });
        mRecyclerView.setAdapter(mTagFlowAdapter);
        mNumberField.setOnChangeListener(new OnValueChangeLister() {
            @Override
            public void changed(int value) {
                mAmount = value;
                if (checkShowReward()) {
                    long reward = mAmount * (mSkuPvIdsResult == null ? mSpuInfo.getRewardPrice() : mSkuPvIdsResult.rewardPrice);
                    setRewardBuyButton(reward);
                }

            }
        });
        getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
        setBottomViews();
        initSpuInfo();
    }

    private void initSpuInfo() {
        mThumbIv.setImageURI(mSpuInfo.thumb);
        mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mSpuInfo.getRetailPrice()));
        if (checkShowReward()) {
            // 底部按钮
            setRewardBuyButton(mSpuInfo.getRewardPrice());
            // 标价价格
            mPriceTv.setTextColor(getContext().getResources().getColor(R.color.ddm_gray_dark));
//            mTvMarketPrice.setText("返" + ConvertUtil.cent2yuanNoZero(mSpuInfo.getRewardPrice()));
            mTvMarketPrice.setTextColor(getContext().getResources().getColor(R.color.ddm_red));
            mTvMarketPrice.setTextSize(COMPLEX_UNIT_SP, 20);

            mTvMarketPrice.setText(SpannableStringUtils.getBuilder("返")
                    .setProportion(0.8F)
                    .append(ConvertUtil.cent2yuanNoZero(mSpuInfo.getRewardPrice()))
                    .create()
            );

            TextViewUtil.removeThroughLine(mTvMarketPrice);
        } else {
            mPriceTv.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_red));
            mTvMarketPrice.setTextColor(ContextCompat.getColor(getContext(), R.color.ddm_gray_dark));
            mTvMarketPrice.setText(ConvertUtil.cent2yuanNoZero(mSpuInfo.getMarketPrice()));
            TextViewUtil.addThroughLine(mTvMarketPrice);
        }

        if (mSpuInfo.getSelectedSkuPvIds() != null) {
            setSkuInfo(mSpuInfo.getSelectedSkuPvIds());
        }

    }

    private void setBottomViews() {
        mTvBottomGreyText.setVisibility(View.GONE);
        mConfirmBtn.setVisibility(View.GONE);
        mAddToCartBtn.setVisibility(View.GONE);
        mBuyNowBtn.setVisibility(View.GONE);
        if (!StringUtils.isEmpty(getBottomGreyText())) {
            mTvBottomGreyText.setVisibility(View.VISIBLE);
            mTvBottomGreyText.setText(getBottomGreyText());
        } else if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_ACTIVITY_FREE) {
            mBuyNowBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setText("0元免费领");
        } else if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_SINGLE) {
            mConfirmBtn.setVisibility(View.VISIBLE);
        } else if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_CART) {
            mAddToCartBtn.setVisibility(View.VISIBLE);
            mAddToCartBtn.setText("确定");
            mAddToCartBtn.setTextAppearance(mAddToCartBtn.getContext(), R.style.MyButton_DDMRed);
            mAddToCartBtn.setBackgroundResource(R.drawable.btn_ddmred_selector);
        } else if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_BUY) {
            mBuyNowBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setText("确定");
        } else if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_DEFAULT) {
            mAddToCartBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setVisibility(View.VISIBLE);
        } else {
            mAddToCartBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setVisibility(View.VISIBLE);
        }

        boolean enable = mSpuInfo.checkProductEnable();
        mConfirmBtn.setEnabled(enable);
        mAddToCartBtn.setEnabled(enable);
        mAddToCartBtn.setEnabled(enable);
        mBuyNowBtn.setEnabled(enable);

    }

    private void setViewByState() {
        if (checkShowReward()) {
            // 底部按钮
            setRewardBuyButton(mSkuPvIdsResult.rewardPrice * mAmount);
            // 标价价格
            mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mSkuPvIdsResult.retailPrice));
            mPriceTv.setTextColor(getContext().getResources().getColor(R.color.ddm_gray_dark));
            mTvMarketPrice.setTextColor(getContext().getResources().getColor(R.color.ddm_red));
            mTvMarketPrice.setTextSize(COMPLEX_UNIT_SP, 20);
            mTvMarketPrice.setText(SpannableStringUtils.getBuilder("返")
                    .setProportion(0.8F)
                    .append(ConvertUtil.cent2yuanNoZero(mSkuPvIdsResult.rewardPrice))
                    .create()
            );

            TextViewUtil.removeThroughLine(mTvMarketPrice);
        }
    }

    private void setRewardBuyButton(long reward) {
        mBuyNowBtn.setText(SpannableStringUtils.getBuilder("立即购买\n")
                .append("立返" + ConvertUtil.centToCurrencyNoZero(getContext(), reward)).setProportion(0.8f).create());
    }

    private boolean checkShowReward() {
        // 商品非店主礼包 & 用户是店主 & 非零元购
        return SessionUtil.getInstance().isLogin() && SessionUtil.getInstance().getLoginUser().isStoreMaster()
                && !mSpuInfo.isStoreGift() && mAction != AppTypes.SKU_SELECTOR_DIALOG.ACTION_ACTIVITY_FREE;
    }

    private void setSkuInfo(SkuInfo sku) {
        this.mSkuResult = sku;
        mThumbIv.setImageURI(mSkuResult.thumbURL);
        mStockTv.setText(String.format("库存 %s 件", mSkuResult.stock));
        if (mSpuInfo.extType == 2 && (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_CREATE_GROUP || mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_JOIN_GROUP)) {
            Product.GroupExtEntity.GroupSkuListEntity groupEntity = mSpuInfo.getGroupEntity(mSkuResult.skuId);
            mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(getContext(), groupEntity.groupPrice));
        } else {
            mPriceTv.setText(ConvertUtil.centToCurrency(getContext(), mSkuResult) + "/");
        }
        mTvMarketPrice.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mSkuResult.marketPrice));
        TextViewUtil.addThroughLine(mTvMarketPrice);
        if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_ACTIVITY_FREE || mSkuResult.isStoreGift()) {
            // 0元购  店主礼包  只能购买一个
            mNumberField.setValues(mAmount, 1, 1);
        } else {
            mNumberField.setValues(mAmount, 1, mSkuResult.stock);
        }
        mNumberField.setEditable(mSkuResult.checkBuyable());
    }

    private void setSkuInfo(SkuPvIds sku) {
        this.mSkuPvIdsResult = sku;
        if (sku == null) {
            DLog.e("sku == null");
            return;
        }

        mThumbIv.setImageURI(mSkuPvIdsResult.thumbUrlForShopNow);
        mStockTv.setText(String.format("库存 %s 件", mSkuPvIdsResult.stock));
        if (mSpuInfo.extType == 2 && (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_CREATE_GROUP || mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_JOIN_GROUP)) {
            Product.GroupExtEntity.GroupSkuListEntity groupEntity = mSpuInfo.getGroupEntity(mSkuPvIdsResult.skuId);
            mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(getContext(), groupEntity.groupPrice));
        } else {
            mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mSkuPvIdsResult.retailPrice) + "/");
        }
        mTvMarketPrice.setText(ConvertUtil.centToCurrencyNoZero(getContext(), mSkuPvIdsResult.marketPrice));
        TextViewUtil.addThroughLine(mTvMarketPrice);
        if (mAction == AppTypes.SKU_SELECTOR_DIALOG.ACTION_ACTIVITY_FREE || mSpuInfo.isStoreGift()) {
            // 0元购  店主礼包  只能购买一个
            mNumberField.setValues(mAmount, 1, 1);
        } else {
            mNumberField.setValues(mAmount, 1, mSkuPvIdsResult.stock);
        }

        mNumberField.setEditable(mSpuInfo.checkBuyable() && mSkuPvIdsResult.stock > 0);
        setViewByState();
    }


    private boolean isStockEmpty() {
        if (mSkuResult.isStockEmpty()) {
            ToastUtil.error("您当前选择的规格库存不足");
        }
        return mSkuResult.isStockEmpty();
    }

    private boolean checkActionActive() {
        if (mSkuPvIdsResult == null) {
            ToastUtil.error("请先选择规格");
            return false;
        }

        return true;
    }

    private String getFlashSaleId() {

        if (mSpuInfo.isFlashSale() && mSpuInfo.flashSaleDetail.isInFlashSale()) {
            return mSpuInfo.flashSaleDetail.getFlashSaleId();
        }

        return "";
    }

    @OnClick(R.id.buyNowBtn)
    protected void buyNow() {
        if (checkActionActive()) {
            if (mSpuInfo.isFlashSale() && mSpuInfo.flashSaleDetail.isBeforeFlashSale24()) {
                ToastUtil.error("好货即将开售，先加入购物车吧");
                return;
            }
            CartManager.buyNow(getContext(), mSkuPvIdsResult, mAmount, getFlashSaleId());
        }
    }

    @OnClick(R.id.addToCartBtn)
    protected void addToCart() {
        if (checkActionActive()) {
            CartManager.addToCart(getContext(), mSkuPvIdsResult, mAmount);
        }
    }

    @OnClick(R.id.confirmBtn)
    protected void onConfirm() {
        switch (mAction) {
            case AppTypes.SKU_SELECTOR_DIALOG.ACTION_CART:
                addToCart();
                break;
            case AppTypes.SKU_SELECTOR_DIALOG.ACTION_SINGLE:
            case AppTypes.SKU_SELECTOR_DIALOG.ACTION_BUY:
                buyNow();
                break;
        }
    }


    @OnClick(R.id.tv_close)
    protected void onClose() {
        dismiss();
    }


    private void loadSkuInfoByPropertyValueIds(List<String> ids) {
        APIManager.startRequest(mProductService.getSkuByPropertyValueIds(mSpuInfo.productId, Joiner.on(",").join(ids)), new BaseRequestListener<SkuInfo>() {
            @Override
            public void onSuccess(SkuInfo result) {
//                if (mSelectListener != null) {
//                    mSelectListener.onSelectSku(result);
//                }
                setSkuInfo(result);
            }
        });
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    public void setGroupCode(String groupCode) {
        mGroupCode = groupCode;
    }

    public String getBottomGreyText() {
        if (mSpuInfo == null) {
            return null;
        }
        return mSpuInfo.getUnBuyableHintText();
    }

    public static class TagFlowAdapter extends RecyclerView.Adapter<TagFlowAdapter.ViewHolder> {
        private SkuHelper mSkuHelper;

        public TagFlowAdapter(Product product, SkuHelper.OnSkuPropertySelectListener listener) {
            mSkuHelper = new SkuHelper(product, listener);
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_sku_tag_group_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Property property = mSkuHelper.getProduct().properties.get(position);
            holder.titleTv.setText(String.format("选择%s", property.name));
            holder.tagContainer.removeAllViews();
            for (final PropertyValue value : property.values) {
                View view = LayoutInflater.from(holder.titleTv.getContext()).inflate(R.layout.sku_tag_item_layout, null);
                final TextView tagView = (TextView) view.findViewById(R.id.tagTv);
                tagView.setText(value.value);
                tagView.setEnabled(value.enable);
                tagView.setSelected(value.isSelected);
                holder.tagContainer.addView(view);
                tagView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isEnabled() || mSkuHelper.getProduct().skus.size() == 1) {
                            // 2340 AndroidAPP测试环境，当商品只有一个规格时，该规格应该默认选中并且点击不能取消选中
                            return;
                        }
                        mSkuHelper.toggle(property, value);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            if (null == mSkuHelper || null == mSkuHelper.getProduct()) {
                return 0;
            }
            return mSkuHelper.getProduct().properties == null ? 0 : mSkuHelper.getProduct().properties.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.titleTv)
            protected TextView titleTv;
            @BindView(R.id.tagContainer)
            protected FlexboxLayout tagContainer;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }


    }

    @Override
    public void dismiss() {
        if (mSelectListener != null) {
            // 未选择的话是null
            if (mSkuPvIdsResult == null) {
                mSelectListener.onSelectCancel();
            } else {
                mSelectListener.onSelectSku(mSkuPvIdsResult);
            }
        }
        super.dismiss();
    }

    public interface OnSelectListener {
        void onSelectSku(SkuPvIds skuInfo);

        void onSelectCancel();
    }

}
