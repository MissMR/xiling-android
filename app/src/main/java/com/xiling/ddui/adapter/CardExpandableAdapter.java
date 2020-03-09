package com.xiling.ddui.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.bean.CardExpandableBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.ddui.tools.ShopUtils;
import com.xiling.image.GlideUtils;
import com.xiling.shared.component.NumberField;
import com.xiling.shared.constant.Key;
import com.xiling.shared.contracts.OnValueChangeLister;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车adapter
 */
public class CardExpandableAdapter extends BaseMultiItemQuickAdapter<CardExpandableBean<XLCardListBean.SkuProductListBean>, BaseViewHolder> {

    boolean isEdit = false; //编辑状态
    OnSelectChangeListener onSelectChangeListener;
    List<CardExpandableBean<XLCardListBean.SkuProductListBean>> parentList = new ArrayList<>();

    public CardExpandableAdapter(@Nullable List<CardExpandableBean<XLCardListBean.SkuProductListBean>> data) {
        super(data);
        addItemType(CardExpandableBean.PARENT, R.layout.item_card_parent);
        addItemType(CardExpandableBean.CHILD, R.layout.item_card_child);
    }

    @Override
    public void setNewData(@Nullable List<CardExpandableBean<XLCardListBean.SkuProductListBean>> data) {
        super.setNewData(data);
        parentList.clear();
        for (CardExpandableBean<XLCardListBean.SkuProductListBean> mData : data) {
            if (mData.isParent()) {
                parentList.add(mData);
            }
        }

    }

    /**
     * 判断选中
     */
    private boolean judgeSelect(CardExpandableBean<XLCardListBean.SkuProductListBean> item) {
        if (isEdit) {
            return item.isEditSelect();
        }
        return item.isSelect();
    }

    @Override
    protected void convert(BaseViewHolder helper, final CardExpandableBean<XLCardListBean.SkuProductListBean> item) {
        switch (item.getItemType()) {
            case CardExpandableBean.PARENT:
                helper.setText(R.id.tv_store_name, item.getParentName());
                break;
            case CardExpandableBean.CHILD:
                helper.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, DDProductDetailActivity.class);
                        intent.putExtra(Key.SPU_ID, item.getBean().getProductId());
                        mContext.startActivity(intent);
                    }
                });
                XLCardListBean.SkuProductListBean skuProductListBean = item.getBean();
                GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_icon), skuProductListBean.getThumbUrl());
                NumberField mNumberField = helper.getView(R.id.numberField);
                String mStatus = ShopUtils.checkShopStatus(skuProductListBean.getStatus(), skuProductListBean.getStock());
                if (!TextUtils.isEmpty(mStatus)) {
                    //下架
                    helper.setText(R.id.tv_status, mStatus);
                    helper.setVisible(R.id.tv_status, true);
                    helper.setVisible(R.id.itemSelectorIv, isEdit);
                    helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#A6251A"));
                    helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#A6251A"));
                    helper.setTextColor(R.id.tv_rmb, Color.parseColor("#999999"));
                    helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#999999"));
                    helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#999999"));
                    mNumberField.setLimit(0, 0);

                } else {
                    helper.setVisible(R.id.tv_status, false);
                    helper.setVisible(R.id.itemSelectorIv, true);
                    helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#a6251a"));
                    helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#a6251a"));
                    helper.setTextColor(R.id.tv_rmb, Color.parseColor("#a6251a"));
                    helper.setTextColor(R.id.tv_discount_price, Color.parseColor("#a6251a"));
                    helper.setTextColor(R.id.tv_discount_price_decimal, Color.parseColor("#a6251a"));
                    mNumberField.setLimit(1, skuProductListBean.getStock());
                    mNumberField.setValue(item.getBean().getQuantity(), false);
                    mNumberField.setOnChangeListener(new OnValueChangeLister() {
                        @Override
                        public void changed(int value) {
                            if (value != item.getBean().getQuantity() && onSelectChangeListener != null) {
                                item.getBean().setQuantity(value);
                                onSelectChangeListener.onShopChange(item, value);
                                noticeSelectSize();
                            }
                            getSelectPrice();
                        }
                    });
                }
                if (!TextUtils.isEmpty(skuProductListBean.getProductName())) {
                    helper.setText(R.id.tv_product_name, skuProductListBean.getProductName());
                }

                if (!TextUtils.isEmpty(skuProductListBean.getSkuName())) {
                    helper.setText(R.id.tv_product_des, skuProductListBean.getProperties());
                }

                helper.setText(R.id.tv_price, "¥" + NumberHandler.reservedDecimalFor2(skuProductListBean.getRetailPrice()));
                NumberHandler.setPriceText(skuProductListBean.getPrice(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
                break;
            default:
                break;
        }
        helper.setOnClickListener(R.id.btn_select, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (judgeSelect(item)) {
                    removeSelect(item);
                } else {
                    addSelect(item);
                }
            }
        });
        View selectView = helper.getView(R.id.itemSelectorIv);
        selectView.setSelected(judgeSelect(item));
    }

    private void addSelect(CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean) {
        if (cardExpandableBean.isParent()) {
            cardExpandableBean.setSelect(true);
            cardExpandableBean.setEditSelect(true);
            if (cardExpandableBean.getChildPositions() != null) {
                for (int position : cardExpandableBean.getChildPositions()) {
                    if (inspectStatus(mData.get(position))) {
                        mData.get(position).setSelect(true);
                    }
                    mData.get(position).setEditSelect(true);
                }
            }
        } else {
            if (inspectStatus(cardExpandableBean)) {
                cardExpandableBean.setSelect(true);
            }
            cardExpandableBean.setEditSelect(true);
            inspectParentSelect(cardExpandableBean.getParentPosition());
        }
        noticeSelectSize();
        getSelectPrice();
        notifyDataSetChanged();
    }

    private void noticeSelectSize() {
        if (onSelectChangeListener != null) {
            int selectSize = 0;
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean : getSelectList()) {
                selectSize += cardExpandableBean.getBean().getQuantity();
            }
            onSelectChangeListener.onSelectChange(selectSize);
        }
    }


    private boolean inspectParentSelect(int parentPosition) {
        CardExpandableBean parentCard = mData.get(parentPosition);
        if (parentCard.getChildPositions() != null) {
            List<Integer> childPosList = parentCard.getChildPositions();
            for (int childPos : childPosList) {
                if (isEdit) {
                    if (!mData.get(childPos).isEditSelect()) {
                        parentCard.setSelect(false);
                        parentCard.setEditSelect(false);
                        return false;
                    }
                } else {
                    if (inspectStatus(mData.get(childPos))) {
                        if (!mData.get(childPos).isSelect()) {
                            parentCard.setSelect(false);
                            parentCard.setEditSelect(false);
                            return false;
                        }
                    }
                }

            }
            parentCard.setSelect(true);
            parentCard.setEditSelect(true);
            return true;
        }
        return false;
    }

    private void removeSelect(CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean) {
        if (cardExpandableBean.isParent()) {
            cardExpandableBean.setSelect(false);
            cardExpandableBean.setEditSelect(false);
            if (cardExpandableBean.getChildPositions() != null) {
                for (int position : cardExpandableBean.getChildPositions()) {
                    mData.get(position).setSelect(false);
                    mData.get(position).setEditSelect(false);
                }
            }
        } else {
            if (inspectStatus(cardExpandableBean)) {
                cardExpandableBean.setSelect(false);
            }
            cardExpandableBean.setEditSelect(false);
            inspectParentSelect(cardExpandableBean.getParentPosition());
        }
        noticeSelectSize();
        getSelectPrice();
        notifyDataSetChanged();
    }


    public boolean selectAll() {
        if (mData.size() > 0) {
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean : mData) {
                if (inspectStatus(cardExpandableBean)) {
                    cardExpandableBean.setSelect(true);
                }
                cardExpandableBean.setEditSelect(true);
            }
            noticeSelectSize();
            getSelectPrice();
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    public boolean cancleSelectAll() {
        if (mData.size() > 0) {
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean : mData) {
                if (inspectStatus(cardExpandableBean)) {
                    cardExpandableBean.setSelect(false);
                }
                cardExpandableBean.setEditSelect(false);
            }

            noticeSelectSize();
            getSelectPrice();
            notifyDataSetChanged();
            return true;
        }

        return false;
    }


    private boolean inspectStatus(CardExpandableBean<XLCardListBean.SkuProductListBean> cardBean) {
        if (!cardBean.isParent()) {
            return TextUtils.isEmpty(ShopUtils.checkShopStatus(cardBean.getBean().getStatus(), cardBean.getBean().getStock()));
        }
        return true;
    }

    public String getSelectPrice() {
        double price = 0;
        for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardBean : mData) {
            if (!cardBean.isParent()) {
                if (cardBean.getBean() != null) {
                    if (cardBean.isSelect() && inspectStatus(cardBean)) {
                        // 确保是选中且没下架的
                        price += cardBean.getBean().getPrice() * cardBean.getBean().getQuantity();
                    }
                }
            }
        }

        if (onSelectChangeListener != null) {
            onSelectChangeListener.onPriceChange(price);
        }

        return NumberHandler.reservedDecimalFor2(price);
    }

    public List<CardExpandableBean<XLCardListBean.SkuProductListBean>> getSelectList() {
        List<CardExpandableBean<XLCardListBean.SkuProductListBean>> selectList = new ArrayList<>();
        for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardBean : mData) {
            if (!cardBean.isParent()) {
                if (cardBean.getBean() != null) {
                    if (isEdit) {
                        if (cardBean.isEditSelect()) {
                            selectList.add(cardBean);
                        }
                    } else {
                        if (cardBean.isSelect()) {
                            if (inspectStatus(cardBean)) {
                                selectList.add(cardBean);
                            }
                        }
                    }
                }
            }
        }
        return selectList;
    }

    public boolean isAllSelect() {
        boolean isAllSelect = false;
        List allData = new ArrayList();
        if (isEdit) {
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardBean : mData) {
                if (!cardBean.isParent()) {
                    allData.add(cardBean);
                }
            }
            if (getSelectList().size() > 0) {
                isAllSelect = getSelectList().size() == allData.size();
            }
        } else {
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> cardBean : mData) {
                if (!cardBean.isParent()) {
                    if (inspectStatus(cardBean)) {
                        allData.add(cardBean);
                    }
                }
            }
            int slectSize = getSelectList().size();
            if (slectSize > 0) {
                isAllSelect = slectSize == allData.size();
            }

        }


        return isAllSelect;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;

        for (CardExpandableBean<XLCardListBean.SkuProductListBean> parentData : parentList) {
            inspectParentSelect(parentData.getPosition());
        }
        noticeSelectSize();
        notifyDataSetChanged();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setOnSelectChangeListener(OnSelectChangeListener onSelectChangeListener) {
        this.onSelectChangeListener = onSelectChangeListener;
    }


    /**
     * 价格与选中数量改变监听
     */
    public interface OnSelectChangeListener {
        void onPriceChange(double price);

        void onSelectChange(int selectSize);

        void onShopChange(CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean, int quantity);
    }

}
