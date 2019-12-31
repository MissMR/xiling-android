package com.xiling.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.adapter.TagFlowAdapter;
import com.xiling.ddui.bean.ProductNewBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.component.NumberField;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.util.CommonUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SkuSelectorDialog extends Dialog {

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.thumbIv)
    protected SimpleDraweeView mThumbIv;
    @BindView(R.id.buyNowBtn)
    protected TextView mBuyNowBtn;
    @BindView(R.id.addToCartBtn)
    protected TextView mAddToCartBtn;
    @BindView(R.id.confirmBtn)
    protected TextView mConfirmBtn;
    @BindView(R.id.numberField)
    protected NumberField mNumberField;


    @BindView(R.id.tv_discount_price)
    TextView tvDiscountPrice;
    @BindView(R.id.tv_discount_price_decimal)
    TextView tvDiscountPriceDecimal;
    @BindView(R.id.tv_minPrice)
    TextView tvMinPrice;
    @BindView(R.id.tv_minMarketPrice)
    TextView tvMinMarketPrice;
    @BindView(R.id.tvSkuName)
    TextView tvSkuName;

    protected ProductNewBean mSpuInfo;
    private OnSelectListener mSelectListener;
    List<ProductNewBean.SkusBean> skusBeanList;
    private ProductNewBean.SkusBean skuBean;
    private int selectCount = 0;

    private TagFlowAdapter mTagFlowAdapter;

    public void setmAction(int mAction) {
        this.mAction = mAction;
        selectAction();
    }

    /**
     * 用来判断是 加入购物车还是立即购买，如果是0，则进入选择规格流程
     * 0 - 选择规格
     * 1 - 加入购物车
     * 2 - 立即购买
     */
    private int mAction;
    public static final int ACTION_SELECT = 0;
    public static final int ACTION_CARD = 1;
    public static final int ACTION_SHOPPING = 2;

    public SkuSelectorDialog(Context context, ProductNewBean product) {
        this(context, 0);
        this.mSpuInfo = product;
    }

    private SkuSelectorDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    public SkuSelectorDialog(Context context, ProductNewBean mProduct, int action) {
        this(context, mProduct);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_product_sku_selector);
        ButterKnife.bind(this);
        CommonUtil.initDialogWindow(getWindow(), Gravity.BOTTOM);
        setCanceledOnTouchOutside(true);
        initViews();
    }


    private void setSkuName(final String skuName) {
        tvSkuName.post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(skuName)) {
                    tvSkuName.setText("请选择规格");
                } else {
                    tvSkuName.setText("已选择 " + skuName);
                }
            }
        });


    }

    /**
     * 判断状态，如果是选择规格，隐藏确认按钮，否则显示确认按钮
     */
    private void selectAction() {
        if (mAction == ACTION_SELECT) {
            mConfirmBtn.setVisibility(View.GONE);
            mAddToCartBtn.setVisibility(View.VISIBLE);
            mBuyNowBtn.setVisibility(View.VISIBLE);
        } else {
            mConfirmBtn.setVisibility(View.VISIBLE);
            mAddToCartBtn.setVisibility(View.GONE);
            mBuyNowBtn.setVisibility(View.GONE);
        }
    }

    private void initViews() {
        selectAction();
        skusBeanList = mSpuInfo.getSkus();

        skuBean = skusBeanList.get(0);
        setSkuName(skuBean.getPropertyValues());

        GlideUtils.loadImage(getContext(), mThumbIv, skuBean.getThumbUrlForShopNow());

        //优惠价，需要根据用户等级展示不同价格
        NumberHandler.setPriceText(UserManager.getInstance().getPriceForUser(mSpuInfo), tvDiscountPrice, tvDiscountPriceDecimal);

        //售价
        tvMinPrice.setText("¥" + NumberHandler.reservedDecimalFor2(mSpuInfo.getMinPrice()));
        //划线价
        tvMinMarketPrice.setText("¥" + NumberHandler.reservedDecimalFor2(mSpuInfo.getMinMarketPrice()));
        tvMinMarketPrice.getPaint().setAntiAlias(true);//抗锯齿
        tvMinMarketPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mTagFlowAdapter = new TagFlowAdapter(R.layout.item_product_sku_tag_group_layout, skusBeanList);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getContext(), 10), ScreenUtils.dip2px(getContext(), 10)));
        mTagFlowAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mTagFlowAdapter.setSelectPosition(position);
                setSkuName(skusBeanList.get(position).getPropertyValues());
                skuBean = skusBeanList.get(position);
                mNumberField.setLimit(1, skuBean.getStock());
                if (mNumberField.getmValue() >= skuBean.getStock()) {
                    mNumberField.setValue(skuBean.getStock());
                }
                GlideUtils.loadImage(getContext(), mThumbIv, skuBean.getThumbUrlForShopNow());
            }
        });

        mRecyclerView.setAdapter(mTagFlowAdapter);
        mNumberField.setLimit(1, skuBean.getStock());
        mNumberField.setOnChangeListener(new OnValueChangeLister() {
            @Override
            public void changed(int value) {
                selectCount = value;
            }
        });

        getWindow().setWindowAnimations(R.style.ActionSheetDialogAnimation);
    }

    @OnClick(R.id.tv_close)
    protected void onClose() {
        if (mSelectListener != null) {
            mSelectListener.onClose(skuBean.getPropertyValues());
        }
        dismiss();
    }

    public void setSelectListener(OnSelectListener selectListener) {
        mSelectListener = selectListener;
    }

    @OnClick({R.id.addToCartBtn, R.id.buyNowBtn,R.id.confirmBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addToCartBtn:
                if (mSelectListener != null) {
                    mSelectListener.joinShopCart(skuBean.getSkuId(), skuBean.getPropertyValues(), selectCount);
                }
                dismiss();
                break;
            case R.id.buyNowBtn:
                if (mSelectListener != null) {
                    mSelectListener.buyItNow(skuBean.getPropertyIds(), skuBean.getPropertyValues(), selectCount);
                }
                dismiss();
                break;
            case R.id.confirmBtn:
                if (mAction == ACTION_CARD) {
                    mSelectListener.joinShopCart(skuBean.getPropertyIds(), skuBean.getPropertyValues(), selectCount);
                } else if (mAction == ACTION_SHOPPING) {
                    mSelectListener.buyItNow(skuBean.getPropertyIds(), skuBean.getPropertyValues(), selectCount);
                }
                dismiss();
                break;
        }
    }


    public interface OnSelectListener {
        void onClose(String propertyValue);

        //加入购物车
        void joinShopCart(String skuId, String propertyValue, int selectCount);

        //立即购买
        void buyItNow(String propertyIds, String propertyValue, int selectCount);
    }

}
