package com.dodomall.ddmall.ddui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.bean.DDCartRowBean;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.dduis.bean.DDProductBean;
import com.dodomall.ddmall.dduis.viewholder.SpuProductViewHolder;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.bean.CartItem;
import com.dodomall.ddmall.shared.bean.CartStore;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.NumberField;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.contracts.OnValueChangeLister;
import com.dodomall.ddmall.shared.manager.CartManager;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hanks.htextview.animatetext.HText;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context = null;

    public DDCartAdapter(Context context) {
        this.context = context;
    }

    //购物车数据源
    ArrayList<CartItem> pData = new ArrayList<>();
    //猜你喜欢数据源
    ArrayList<DDProductBean> sData = new ArrayList<>();

    //实际用来显示的数据
    ArrayList<DDCartRowBean> cData = new ArrayList<>();

    private boolean isEditMode = false;

    public void setEditMode(boolean isEditMode) {
        if (isEditMode != this.isEditMode) {
            checkAll(false);
        }
        this.isEditMode = isEditMode;
    }

    /**
     * 设置购物车数据
     *
     * @param data 店铺分组的购物数据
     */
    public void setCartData(List<CartStore> data) {
        DLog.i("setCartData:" + data.size());
        pData.clear();
        for (CartStore store : data) {
            //合法性校验
            if (store.products != null && store.products.size() > 0) {
                pData.addAll(store.products);
                DLog.i("商品总数:" + pData.size());
            }
        }
    }

    /**
     * 设置猜你喜欢的数据
     *
     * @param data 猜你喜欢数据源
     */
    public void setSuggestData(List<DDProductBean> data) {
        DLog.i("setSuggestData:" + data.size());
        sData.clear();
        sData.addAll(data);
        DLog.i("猜你喜欢总数:" + sData.size());
    }

    /**
     * 追加猜你喜欢数据
     */
    public void appendSuggestData(ArrayList<DDProductBean> data) {
        DLog.i("appendSuggestData:" + data.size());
        sData.addAll(data);
        DLog.i("追加猜你喜欢:" + sData.size());
    }

    public int getSuggestCount() {
        if (sData == null) {
            return 0;
        } else {
            return sData.size();
        }
    }

    /**
     * 将不同分类的数据整合成可以显示的模式
     */
    public void showData() {

        //清除历史数据
        cData.clear();

        //购物车 数据
        if (pData.size() == 0) {
            DDCartRowBean rowBean = new DDCartRowBean();
            rowBean.setType(DDCartRowBean.CartType.Empty);
            cData.add(rowBean);
        } else {
            for (CartItem cartItem : pData) {
                DDCartRowBean rowBean = new DDCartRowBean();
                rowBean.setType(DDCartRowBean.CartType.Product);
                rowBean.setProduct(cartItem);
                cData.add(rowBean);
            }
        }

        if (sData.size() > 0) {
            //猜你喜欢 头部
            DDCartRowBean suggestHeader = new DDCartRowBean();
            suggestHeader.setType(DDCartRowBean.CartType.SuggestHeader);
            cData.add(suggestHeader);

            //猜你喜欢 数据
            for (DDProductBean sBean : sData) {
                DDCartRowBean rowBean = new DDCartRowBean();
                rowBean.setType(DDCartRowBean.CartType.SuggestData);
                rowBean.setSuggest(sBean);
                cData.add(rowBean);
            }
        }

        DLog.i("合并数据后商品：" + pData.size());
//        for (CartItem item : pData) {
//            DLog.d("\t" + item.name);
//        }

        DLog.i("合并数据猜你喜欢：" + sData.size());
//        for (ProductBean item : sData) {
//            DLog.d("\t" + item.getSkuName());
//        }

        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == DDCartRowBean.CartType.Empty.ordinal()) {
            //空数据
            View emptyView = LayoutInflater.from(context).inflate(R.layout.layout_no_data_cart, parent, false);
            EmptyItemHolder emptyHolder = new EmptyItemHolder(emptyView);
            return emptyHolder;
        } else if (viewType == DDCartRowBean.CartType.Product.ordinal()) {
            //购物车产品
            View productView = LayoutInflater.from(context).inflate(R.layout.adapter_cart_item_product, parent, false);
            ProductItemHolder productHolder = new ProductItemHolder(productView);
            return productHolder;
        } else if (viewType == DDCartRowBean.CartType.SuggestData.ordinal()) {
            //猜你喜欢数据
            View suggestView = LayoutInflater.from(context).inflate(R.layout.layout_home_category_data, parent, false);
            SpuProductViewHolder suggestHolder = new SpuProductViewHolder(suggestView);
            return suggestHolder;
        } else if (viewType == DDCartRowBean.CartType.SuggestHeader.ordinal()) {
            //猜你喜欢头部
            View headerView = LayoutInflater.from(context).inflate(R.layout.adapter_cart_item_suggest_header, parent, false);
            SuggestHeaderHolder headerHolder = new SuggestHeaderHolder(headerView);
            return headerHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        DLog.i("onBindViewHolder:" + position);

        DDCartRowBean rowBean = cData.get(position);
        DDCartRowBean.CartType type = rowBean.getType();
        if (type == DDCartRowBean.CartType.Product) {
            ProductItemHolder pHolder = (ProductItemHolder) holder;
            pHolder.setData(rowBean.getProduct());
            pHolder.render();
        } else if (type == DDCartRowBean.CartType.SuggestData) {
            SpuProductViewHolder sHolder = (SpuProductViewHolder) holder;
            sHolder.setData(rowBean.getSuggest());
            sHolder.render();
        }
    }

    @Override
    public int getItemCount() {
        return cData.size();
    }

    @Override
    public int getItemViewType(int position) {
        DDCartRowBean row = cData.get(position);
        return row.getType().ordinal();
    }

    /**
     * 是否选中全部
     *
     * @param flag
     */
    public void checkAll(boolean flag) {
        for (CartItem item : pData) {
            if (!isEditMode) {
                if (flag) {
                    if (item.isPurchasable()) {
                        item.isSelected = flag;
                    }
                } else {
                    item.isSelected = flag;
                }
            } else {
                item.isSelected = flag;
            }
        }
        showData();
    }

    /**
     * 获取选中的购物车数据
     */
    public List<CartItem> getSelectCartItem() {
        ArrayList<CartItem> data = new ArrayList<>();
        for (CartItem item : pData) {
            if (!isEditMode) {
                if (item.isPurchasable() && item.isSelected) {
                    data.add(item);
                }
            } else {
                if (item.isSelected) {
                    data.add(item);
                }
            }
        }
        return data;
    }

    public boolean isSelectAll() {
        int selectSize = getSelectCartItem().size();
        return selectSize == pData.size();
    }

    public void clearCart() {
        pData.clear();
        showData();
    }

    //空数据
    class EmptyItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvGoMain)
        TextView tvGoMain;

        @OnClick(R.id.tvGoMain)
        void onGoMainPressed() {
            if (context instanceof MainActivity) {
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_EDIT_PHONE));
            } else {
                context.startActivity(new Intent(context, MainActivity.class));
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_EDIT_PHONE));
            }
        }

        public EmptyItemHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //购物车数据
    class ProductItemHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemSelectorIv)
        ImageView mItemSelectorIv;

        @BindView(R.id.skuStatusView)
        TextView skuStatusView;

        @BindView(R.id.itemThumbIv)
        SimpleDraweeView mItemThumbIv;

        @BindView(R.id.tv_rush_tag)
        TextView rushTagTextView;

        @BindView(R.id.itemTitleTv)
        TextView mItemTitleTv;

        @BindView(R.id.itemPropertyTv)
        TextView mItemPropertyTv;

        @BindView(R.id.itemPriceTv)
        TextView mItemPriceTv;

        @BindView(R.id.itemNumberField)
        NumberField mItemNumberField;

        CartItem data = null;

        public ProductItemHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            //设置按钮大小
            mItemNumberField.setViewSize(24);
        }

        public void setData(CartItem data) {
            this.data = data;
        }

        private boolean isCanSelect() {
//            return (data.stock > 0 && data.status != 0);
            //TIPS 2.0支持抢购商品，换成内部判断
            return data.isPurchasable();
        }

        public void render() {

            if (data == null) {
                DLog.w("ProductItemHolder render data is null.");
                return;
            }

            DLog.i("render product:" + data.name);

            FrescoUtil.setImageSmall(mItemThumbIv, data.thumb);

            //是否显示抢购标签
            if (data.isRush()) {
                rushTagTextView.setVisibility(View.VISIBLE);
            } else {
                rushTagTextView.setVisibility(View.INVISIBLE);
            }

            mItemTitleTv.setText(data.name);
            mItemPropertyTv.setText(data.properties);

            mItemPriceTv.setText(ConvertUtil.centToCurrency(context, data));

            DLog.d("" + data.name + "(" + data.amount + "/" + data.stock + ")");

            //废弃以前的直接触发数据的逻辑改为新的单独分开设置有效值 at 2018/12/11 by hanQ
//            mItemNumberField.setValues(data.amount, 1, data.stock);
            //使用新的分开设置数据的逻辑，将为0的数据进行过滤操作
//            mItemNumberField.setDefaultValue(0);
//            mItemNumberField.setLimit(1, 0);
            if (isCanSelect()) {
                mItemNumberField.setDefaultValue(data.amount);
                mItemNumberField.setLimit(1, data.stock);
                mItemSelectorIv.setSelected(data.isSelected);
                mItemSelectorIv.setEnabled(true);
                mItemSelectorIv.setVisibility(View.VISIBLE);
                skuStatusView.setVisibility(View.INVISIBLE);
                //已下架或者已售罄的时候不能点击编辑框
                if (data.status == 0 || data.stock == 0) {
                    mItemNumberField.setEditable(false);
                } else {
                    mItemNumberField.setEditable(true);
                }
            } else {

                if (isEditMode) {
                    mItemSelectorIv.setVisibility(View.VISIBLE);
                    mItemSelectorIv.setEnabled(true);
                    mItemSelectorIv.setSelected(data.isSelected);
                } else {
                    mItemSelectorIv.setSelected(false);
                    mItemSelectorIv.setEnabled(false);
                    mItemSelectorIv.setVisibility(View.INVISIBLE);
                }

                skuStatusView.setVisibility(View.INVISIBLE);

                mItemNumberField.setLimit(0, 0);
                mItemNumberField.setDefaultValue(0);
                mItemNumberField.setEditable(false);

                //是否 是抢购 ，更换库存字段
                if (data.isRush()) {
                    long flashStock = data.flashSaleSkuDTO.getInventory();
                    if (flashStock > 0) {
                        //预售商品 - 不控制操作，控制选中结算状态，不控制编辑
                        mItemNumberField.setDefaultValue(data.amount);
                        mItemNumberField.setLimit(1, (int) flashStock);
                        mItemNumberField.setEditable(true);
                        skuStatusView.setVisibility(View.INVISIBLE);
                    } else {
                        data.amount = 0;
                        skuStatusView.setText("已抢光");
                        skuStatusView.setVisibility(View.VISIBLE);
                    }
                } else {
                    data.amount = 0;
                    if (data.status == 0) {
                        skuStatusView.setText("已下架");
                        skuStatusView.setVisibility(View.VISIBLE);
                    } else {
                        if (data.stock <= 0) {
                            skuStatusView.setText("已抢光");
                            skuStatusView.setVisibility(View.VISIBLE);
                        } else {
                            skuStatusView.setVisibility(View.INVISIBLE);
                        }
                    }
                }

            }

            //查看详情
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventUtil.viewProductDetail(context, data.productId);
                }
            });

            //数量
            mItemNumberField.setOnChangeListener(new OnValueChangeLister() {
                @Override
                public void changed(int value) {
                    if (value != data.amount) {
                        data.amount = value;
                        CartManager.updateCartItem(context, data.skuId, value);

                        DLog.e("发送一个  selectCartItem");
                        EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
                    }
                }
            });

            //选中改变
            mItemSelectorIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    data.isSelected = !data.isSelected;
                    mItemSelectorIv.setSelected(data.isSelected);

                    DLog.e("发送一个  selectCartItem");
                    EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
                }
            });
        }
    }

    //猜你喜欢头部
    class SuggestHeaderHolder extends RecyclerView.ViewHolder {
        public SuggestHeaderHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
