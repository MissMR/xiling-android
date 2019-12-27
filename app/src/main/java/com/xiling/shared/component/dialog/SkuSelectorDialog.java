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

    private void initViews() {
        skusBeanList = mSpuInfo.getSkus();

        skuBean = skusBeanList.get(0);
        setSkuName(skuBean.getPropertyValues());

        //优惠价，需要根据用户等级展示不同价格
        NewUserBean userBean = UserManager.getInstance().getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    NumberHandler.setPriceText(mSpuInfo.getLevel10Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
                case 20:
                    NumberHandler.setPriceText(mSpuInfo.getLevel20Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
                case 30:
                    NumberHandler.setPriceText(mSpuInfo.getLevel30Price(), tvDiscountPrice, tvDiscountPriceDecimal);
                    break;
            }
        } else {
            NumberHandler.setPriceText(mSpuInfo.getLevel10Price(), tvDiscountPrice, tvDiscountPriceDecimal);
        }
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
                if (mNumberField.getmValue() > skuBean.getStock()) {
                    mNumberField.setValue(skuBean.getStock());
                }

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

    @OnClick({R.id.addToCartBtn, R.id.buyNowBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addToCartBtn:
                if (mSelectListener != null) {
                    mSelectListener.joinShopCart(skuBean.getPropertyIds(), skuBean.getPropertyValues(), selectCount);
                }
                dismiss();
                break;
            case R.id.buyNowBtn:
                if (mSelectListener != null) {
                    mSelectListener.buyItNow(skuBean.getPropertyIds(), skuBean.getPropertyValues(), selectCount);
                }
                dismiss();
                break;
        }
    }


    public interface OnSelectListener {
        void onClose(String propertyValue);

        //加入购物车
        void joinShopCart(String propertyIds, String propertyValue, int selectCount);

        //立即购买
        void buyItNow(String propertyIds, String propertyValue, int selectCount);
    }

}
