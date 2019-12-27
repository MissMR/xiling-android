package com.xiling.dduis.adapter;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.R;
import com.xiling.ddui.activity.DDProductDetailActivity;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.bean.HomeDataBean;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.constant.Key;

import java.util.List;

public class ShopListAdapter extends BaseQuickAdapter<HomeRecommendDataBean.DatasBean, BaseViewHolder> {

    ShopListTagsAdapter tagsAdapter;


    public ShopListAdapter(int layoutResId, @Nullable List<HomeRecommendDataBean.DatasBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HomeRecommendDataBean.DatasBean item) {
        if (!TextUtils.isEmpty(item.getBadgeImg())) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_bgdge), item.getBadgeImg());
        }

        if (!TextUtils.isEmpty(item.getThumbUrl())) {
            GlideUtils.loadImage(mContext, (ImageView) helper.getView(R.id.iv_thumb), item.getThumbUrl());
        }

        if (!TextUtils.isEmpty(item.getProductName())) {
            helper.setText(R.id.tv_title, item.getProductName());
        }

        if (item.getProductTags() != null && item.getProductTags().size() > 0) {
            RecyclerView recyclerView = helper.getView(R.id.recycler_tags);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            tagsAdapter = new ShopListTagsAdapter(R.layout.item_shop_list_tag, item.getProductTags());
            recyclerView.setAdapter(tagsAdapter);
        }
        //优惠价，需要根据用户等级展示不同价格
        NewUserBean userBean = UserManager.getInstance().getUser();
        if (userBean != null) {
            switch (userBean.getRole().getRoleLevel()) {
                case 10:
                    NumberHandler.setPriceText(item.getLevel10Price(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
                    break;
                case 20:
                    NumberHandler.setPriceText(item.getLevel20Price(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
                    break;
                case 30:
                    NumberHandler.setPriceText(item.getLevel30Price(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
                    break;
            }
        } else {
            NumberHandler.setPriceText(item.getLevel10Price(), (TextView) helper.getView(R.id.tv_discount_price), (TextView) helper.getView(R.id.tv_discount_price_decimal));
        }

        if (!TextUtils.isEmpty(item.getStatus()) && item.getStatus().equals("1")) {
            helper.setVisible(R.id.tv_status, true);
        } else {
            helper.setVisible(R.id.tv_status, false);
        }


        //售价
        helper.setText(R.id.tv_minPrice, "¥" + NumberHandler.reservedDecimalFor2(item.getMinPrice()));
        //划线价
        TextView minMarketPriceView = helper.getView(R.id.tv_minMarketPrice);
        minMarketPriceView.setText("¥" + NumberHandler.reservedDecimalFor2(item.getMinMarketPrice()));
        minMarketPriceView.getPaint().setAntiAlias(true);//抗锯齿
        minMarketPriceView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DDProductDetailActivity.class);
                intent.putExtra(Key.SPU_ID, item.getProductId());
                mContext.startActivity(intent);
            }
        });

    }
}
