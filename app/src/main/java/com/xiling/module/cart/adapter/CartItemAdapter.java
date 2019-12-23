package com.xiling.module.cart.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.CartItem;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.NumberField;
import com.xiling.shared.constant.Event;
import com.xiling.shared.contracts.OnValueChangeLister;
import com.xiling.shared.manager.CartManager;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.cart.adapter
 * @since 2017-06-19
 */
class CartItemAdapter extends BaseAdapter<CartItem, CartItemAdapter.ViewHolder> {

    public CartItemAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_cart_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setCartItem(items.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemSelectorIv)
        protected ImageView mItemSelectorIv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemPropertyTv)
        protected TextView mItemPropertyTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mItemPriceTv;
        @BindView(R.id.itemNumberField)
        protected NumberField mItemNumberField;
        @BindView(R.id.itemPresentLayout)
        protected LinearLayout mItemPresentLayout;
        @BindView(R.id.recyclerView)
        protected RecyclerView mRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setCartItem(final CartItem cartItem) {
//            if (cartItem.equals(itemView.getTag())) {
//                mItemSelectorIv.setSelected(cartItem.isSelected);
//                return;
//            }
//            itemView.setTag(cartItem);
            FrescoUtil.setImageSmall(mItemThumbIv, cartItem.thumb);
            mItemTitleTv.setText(cartItem.name);
            mItemPropertyTv.setText(cartItem.properties);
            mItemPriceTv.setText(ConvertUtil.centToCurrency(context, cartItem));
            mItemNumberField.setValues(cartItem.amount, 1, cartItem.stock);
            mItemSelectorIv.setSelected(cartItem.isSelected);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventUtil.viewProductDetail(context, cartItem.productId);
                }
            });
            mItemNumberField.setOnChangeListener(new OnValueChangeLister() {
                @Override
                public void changed(int value) {
                    if (value != cartItem.amount) {
                        //FIXME 这里可能有 bug
                        cartItem.amount = value;
                        CartManager.updateCartItem(context, cartItem.skuId, value);
                        EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
                    }
                }
            });
            mItemSelectorIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.e("发送一个  selectCartItem");

                    cartItem.isSelected = !cartItem.isSelected;
                    mItemSelectorIv.setSelected(cartItem.isSelected);
                    EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
                }
            });

            if (cartItem.presents != null && cartItem.presents.size() > 0) {
                mItemPresentLayout.setVisibility(View.VISIBLE);
//                setPresentsList(mRecyclerView, cartItem.presents);

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                linearLayoutManager.setAutoMeasureEnabled(true);
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setHasFixedSize(true);
                CartItemPresentAdapter cartItemPresentAdapter = new CartItemPresentAdapter(context);
                cartItemPresentAdapter.setHasStableIds(true);
                cartItemPresentAdapter.setItems(cartItem.presents);
                mRecyclerView.setAdapter(cartItemPresentAdapter);

            } else {
                mItemPresentLayout.setVisibility(View.GONE);
            }
        }

        private void setPresentsList(RecyclerView rvPresents, List<SkuInfo> presents) {
            CartPresentsAdapter cartPresentsAdapter = new CartPresentsAdapter(presents);
            rvPresents.setLayoutManager(new LinearLayoutManager(rvPresents.getContext()));
            rvPresents.setAdapter(cartPresentsAdapter);
        }
    }
}
