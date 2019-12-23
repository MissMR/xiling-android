package com.xiling.ddui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.bean.ProductBean;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.util.ConvertUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * created by Jigsaw at 2018/8/31
 */
public class HomeProductAdapter extends RecyclerView.Adapter {

    private static final int TYPE_1 = 1;
    private static final int TYPE_2 = 2;
    private List<ProductBean> list;
    private Activity mContext;

    public HomeProductAdapter(List<ProductBean> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = (Activity) parent.getContext();
        RecyclerView.ViewHolder holder;
        if (viewType == TYPE_1) {
            holder = new ViewHolder1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product1, parent, false));
        } else {
            holder = new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_product2, parent, false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (position < 0 || position >= getItemCount()) {
            return;
        }

        final ProductBean productBean = list.get(position);

        if (getItemViewType(position) == TYPE_1) {
            holder = (ViewHolder1) holder;
            ((ViewHolder1) holder).sdvProduct.setImageURI(productBean.getImage());
            ((ViewHolder1) holder).rlItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra(Constants.Extras.WEB_URL, productBean.getTarget()));
                }
            });
        } else {
            holder = ((ViewHolder2) holder);
            ((ViewHolder2) holder).sdvProduct.setImageURI(productBean.getThumbUrl());
            ((ViewHolder2) holder).sdvProduct.setHierarchy(getSDVDierarchy());
            ((ViewHolder2) holder).tvTitle.setText(productBean.getSkuName());
            ((ViewHolder2) holder).tvSubTitle.setText(productBean.getIntro());

            ((ViewHolder2) holder).tvMoneyNow.setText(ConvertUtil.centToCurrency(mContext, productBean.getRetailPrice()) + "/");
            ((ViewHolder2) holder).tvMoneyOld.setText(ConvertUtil.centToCurrency(mContext, productBean.getMarketPrice()) + "");
            // 设置横线
            ((ViewHolder2) holder).tvMoneyOld.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            // sku的销量 曹永说的 用saleCount这个字段 2018/9/3  Jigsaw
            ((ViewHolder2) holder).tvSaleCount.setText("销量：" + productBean.getSaleCount());
            ((ViewHolder2) holder).rlItemRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DDProductDetailActivity.start(mContext, productBean.getProductId());
                }
            });
        }
    }

    private GenericDraweeHierarchy getSDVDierarchy() {
        float radius = SizeUtils.dp2px(8);
        return
                GenericDraweeHierarchyBuilder.newInstance(mContext.getResources())
                        .setRoundingParams(RoundingParams.fromCornersRadii(radius, radius, 0, 0))
                        .build();
    }

    @Override
    public int getItemViewType(int position) {
        return TextUtils.isEmpty(list.get(position).getEvent()) ? TYPE_2 : TYPE_1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_product)
        SimpleDraweeView sdvProduct;
        @BindView(R.id.rl_item_root)
        RelativeLayout rlItemRoot;

        public ViewHolder1(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.sdv_product)
        SimpleDraweeView sdvProduct;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_sub_title)
        TextView tvSubTitle;
        @BindView(R.id.tv_money_now)
        TextView tvMoneyNow;
        @BindView(R.id.tv_money_old)
        TextView tvMoneyOld;
        @BindView(R.id.tv_sale_count)
        TextView tvSaleCount;
        @BindView(R.id.rl_item_root)
        RelativeLayout rlItemRoot;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

}
