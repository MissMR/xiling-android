package com.dodomall.ddmall.shared.page.element;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseCallback;
import com.dodomall.ddmall.shared.bean.SkuInfo;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.TagTextView;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.decoration.SpacesItemDecoration;
import com.dodomall.ddmall.shared.manager.CartManager;
import com.dodomall.ddmall.shared.page.bean.Element;
import com.dodomall.ddmall.shared.service.ProductService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.EventUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.TextViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductElement extends LinearLayout {

    private final RecyclerView mListRv;
    private ProductAdapter mProductAdapter;

    public ProductElement(Context context, Element element) {
        super(context);
        View view = inflate(getContext(), R.layout.el_product_layout, this);
        element.setBackgroundInto(view);
        mListRv = (RecyclerView) view.findViewById(R.id.eleListRv);
        mListRv.setScrollContainer(false);
        mListRv.requestDisallowInterceptTouchEvent(true);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mListRv.getLayoutParams();
        if (element.columns == 1) {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            linearLayoutManager.setAutoMeasureEnabled(true);
            linearLayoutManager.setSmoothScrollbarEnabled(false);
            mListRv.setLayoutManager(linearLayoutManager);
            mListRv.addItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(10)));
            layoutParams.setMargins(0, 0, 0, 0);
            mListRv.setLayoutParams(layoutParams);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(context, element.columns);
            gridLayoutManager.setAutoMeasureEnabled(true);
            gridLayoutManager.setSmoothScrollbarEnabled(false);
            mListRv.setLayoutManager(gridLayoutManager);
            int margin = ConvertUtil.dip2px(2.5f);
            layoutParams.setMargins(margin, 0, margin, 0);
            mListRv.setLayoutParams(layoutParams);
        }
        mListRv.setNestedScrollingEnabled(false);
        mListRv.setScrollContainer(false);
        ArrayList<String> skuIds = ConvertUtil.json2StringList(element.data);
        setSkuIds(skuIds, element.columns);
    }

    private void setSkuIds(ArrayList<String> skuIds, final int columns) {
        ProductService.getListBySkuIds(skuIds, new BaseCallback<ArrayList<SkuInfo>>() {

            @Override
            public void callback(ArrayList<SkuInfo> data) {
                mProductAdapter = new ProductAdapter(data, columns);
                mListRv.setAdapter(mProductAdapter);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updatePrice(EventMessage message) {
        if (message.getEvent().equals(Event.loginSuccess) || message.getEvent().equals(Event.logout)) {
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final ArrayList<SkuInfo> items;
        private final int columns;

        ProductAdapter(ArrayList<SkuInfo> items, int columns) {
            this.items = items;
            this.columns = columns;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 2) {
                return new ProductColumn2ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_product_column2_item, parent, false));
            } else if (viewType == 3) {
                return new ProductColumn3ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_product_column3_item, parent, false));
            } else {
                return new ProductViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.el_product_column_item, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            holder.setIsRecyclable(false);
            final SkuInfo skuInfo = items.get(position);
            if (columns == 2) {
                ProductColumn2ViewHolder column2ViewHolder = (ProductColumn2ViewHolder) holder;
                column2ViewHolder.setProduct(skuInfo);
                column2ViewHolder.marginTopView.setVisibility(position < 2 ? View.GONE : View.VISIBLE);
            } else if (columns == 3) {
                ProductColumn3ViewHolder column3ViewHolder = (ProductColumn3ViewHolder) holder;
                column3ViewHolder.setProduct(skuInfo);
                column3ViewHolder.marginTopView.setVisibility(position < 3 ? View.GONE : View.VISIBLE);
            } else {
                ((ProductViewHolder) holder).setProduct(skuInfo);
            }
            holder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventUtil.viewProductDetail(getContext(), skuInfo.productId);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return columns;
        }
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TagTextView itemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView itemPriceTv;
        @BindView(R.id.itemSalesTv)
        protected TextView itemSalesTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;
        @BindView(R.id.itemMarkPriceTv)
        protected TextView itemMarkPriceTv;
        @BindView(R.id.addToCartBtn)
        protected ImageView addToCartBtn;

        ProductViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setProduct(final SkuInfo product) {
            FrescoUtil.setImageSmall(itemThumbIv, product.thumb);
            itemTitleTv.setText(product.name);
            itemTitleTv.setTags(product.tags);
            itemSalesTv.setText(String.format("销量：%s 件", product.totalSaleCount));
            itemPriceTv.setText(ConvertUtil.centToCurrency(getContext(), product));
//            if (3 > 2 || SessionUtil.getInstance().getLoginUser().isShopkeeper()) {
            itemMarkPriceTv.setText(ConvertUtil.centToCurrency(itemMarkPriceTv.getContext(), product.marketPrice));
            TextViewUtil.addThroughLine(itemMarkPriceTv);
//            } else {
//                itemMarkPriceTv.setVisibility(GONE);
//            }
            addToCartBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartManager.addToCart(getContext(), product, 1, false);
                }
            });
        }
    }

    class ProductColumn2ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TagTextView itemTitleTv;
        @BindView(R.id.itemSalesTv)
        protected TextView itemSalesTv;
        @BindView(R.id.itemPriceTv)
        protected TextView itemPriceTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;
        @BindView(R.id.marginTopView)
        protected View marginTopView;

        ProductColumn2ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setProduct(SkuInfo product) {
            FrescoUtil.setImage(itemThumbIv, product.thumb);
            itemTitleTv.setText(product.name);
            itemTitleTv.setTags(product.tags);
            itemSalesTv.setText(String.format("销量：%s件", product.totalSaleCount));
            itemPriceTv.setText(ConvertUtil.centToCurrency(getContext(), product));
        }
    }

    class ProductColumn3ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemTitleTv)
        protected TextView itemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView itemPriceTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;
        @BindView(R.id.marginTopView)
        protected View marginTopView;

        ProductColumn3ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setProduct(SkuInfo product) {
            FrescoUtil.setImage(itemThumbIv, product.thumb);
            TextViewUtil.setTagTitle(itemTitleTv,product.name,product.tags);
            itemPriceTv.setText(ConvertUtil.centToCurrency(getContext(), product));
        }
    }
}
