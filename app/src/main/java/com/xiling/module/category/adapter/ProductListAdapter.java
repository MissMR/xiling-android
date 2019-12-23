package com.xiling.module.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.module.push.ProductPushDetailActivity;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.SkuInfo;
import com.xiling.shared.component.TagTextView;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.TextViewUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.category
 * @since 2017-06-18
 */
public class ProductListAdapter extends BaseAdapter<SkuInfo, ProductListAdapter.ViewHolder> {

    private int mColumns = 1;
    private boolean mShopkeeper;
    private Context mContext;

    public ProductListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        User loginUser = SessionUtil.getInstance().getLoginUser();
//        if (loginUser != null) {
//            mShopkeeper = loginUser.isShopkeeper();
//        }
        mShopkeeper = true;
        if (viewType == 2) {
            return new ViewHolder(layoutInflater.inflate(R.layout.el_product_column2_item, parent, false));
        } else if (viewType == AppTypes.PUSH.PRODUCT_TYPE) {
            return new ViewHolder(layoutInflater.inflate(R.layout.item_product_push, parent, false));
        }
        return new ViewHolder(layoutInflater.inflate(R.layout.el_product_column_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SkuInfo skuInfo = items.get(position);
        holder.setProduct(skuInfo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemViewType(position) == AppTypes.PUSH.PRODUCT_TYPE) {
                    Intent intent = new Intent(mContext, ProductPushDetailActivity.class);
                    intent.putExtra(Config.INTENT_KEY_ID, skuInfo.skuId);
                    mContext.startActivity(intent);
                } else {
                    EventUtil.viewProductDetail(context, skuInfo.productId);
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return mColumns;
    }

    public void setColumns(int columns) {
        this.mColumns = columns;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemSalesTv)
        protected TextView itemSalesTv;
        @BindView(R.id.itemPriceTv)
        protected TextView itemPriceTv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView itemThumbIv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setProduct(final SkuInfo product) {
            FrescoUtil.setImageSmall(itemThumbIv, product.thumb);
            TagTextView itemTitleTv = (TagTextView) itemView.findViewById(R.id.itemTitleTv);
            itemTitleTv.setText(product.name);
            itemTitleTv.setTags(product.tags);
            itemPriceTv.setText(ConvertUtil.centToCurrency(itemTitleTv.getContext(), product.retailPrice));
            itemSalesTv.setText("销量：" + product.totalSaleCount);

            if (getItemViewType() == AppTypes.PUSH.PRODUCT_TYPE) {
                itemSalesTv.setText("销量：" + product.sales);
                TextView tvGuige = (TextView) itemView.findViewById(R.id.tvGuige);
                tvGuige.setText(product.spec);
                TextView tvSharePrice = (TextView) itemView.findViewById(R.id.tvSharePrice);
                tvSharePrice.setText("分享赚 " + (product.minPrice / 100) + "~" + (product.maxPrice / 100));
            } else {
                TextView tvMarkPrice = (TextView) itemView.findViewById(R.id.itemMarkPriceTv);
                if (tvMarkPrice!= null) {
                    TextViewUtil.addThroughLine(tvMarkPrice);
                    tvMarkPrice.setText(ConvertUtil.centToCurrency(itemTitleTv.getContext(), product.marketPrice));
                }
            }
        }
    }
}
