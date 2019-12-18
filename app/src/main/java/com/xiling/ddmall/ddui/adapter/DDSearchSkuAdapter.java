package com.xiling.ddmall.ddui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.ProductBean;
import com.xiling.ddmall.ddui.custom.DDTagView;
import com.xiling.ddmall.ddui.custom.SquareDraweeView;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.basic.BaseAdapter;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.google.gson.internal.LinkedTreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DDSearchSkuAdapter extends BaseAdapter<ProductBean, DDSearchSkuAdapter.ViewHolder> {

    public DDSearchSkuAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProductBean data = items.get(position);
        holder.setData(data);
        holder.render();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_product_gird_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llSuggest)
        LinearLayout llSuggest;

        @BindView(R.id.skuStatusView)
        TextView skuStatusView;

        @BindView(R.id.itemThumbIv)
        SquareDraweeView mItemThumbIv;

        //商品名称
        @BindView(R.id.tvProductName)
        TextView tvName;

        //商品描述
        @BindView(R.id.tvProductDesc)
        TextView tvDesc;

        //商品标价
        @BindView(R.id.tvRetailPrice)
        TextView tvRetailPrice;

        //价格分隔符
        @BindView(R.id.tvPriceLine)
        TextView tvPriceLine;

        //市场价
        @BindView(R.id.tvMarkerPrice)
        TextView tvMarkerPrice;

        //分享赚预期收益
        @BindView(R.id.tvRewardPrice)
        TextView tvRewardPrice;
        @BindView(R.id.ll_reward)
        LinearLayout mLlReward;

        @BindView(R.id.tagLayout)
        LinearLayout tagLayout;

        @BindView(R.id.tagView)
        DDTagView tagView;

        ProductBean data = null;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            float radius = ConvertUtil.dip2px(8);
            GenericDraweeHierarchy hierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                    .setRoundingParams(RoundingParams.fromCornersRadii(radius, radius, 0, 0))
//                    .setFadeDuration(2000)
                    .build();
            mItemThumbIv.setHierarchy(hierarchy);

            //添加删除线
            tvMarkerPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        public void setData(ProductBean data) {
            this.data = data;
        }


        public void render() {

            if (data == null) {
                DLog.w("SuggestHolder render data is null.");
                return;
            }

            mItemThumbIv.setImageURI(data.getThumbUrlForShopNow());
            skuStatusView.setVisibility(data.getAllStockFlag() == 0 ? View.GONE : View.VISIBLE);

            //商品名称
            tvName.setText("" + data.getSkuName());
            //商品描述
            tvDesc.setText("" + data.getIntro());
            //商品标价
            tvRetailPrice.setText("" + ConvertUtil.centToCurrency(context, data.getRetailPrice()));
            //市场价
            tvMarkerPrice.setText("" + ConvertUtil.centToCurrency(context, data.getMarketPrice()));
            //分享赚收益
            tvRewardPrice.setText(ConvertUtil.cent2yuan2(data.getRewardPrice()));
            //设置标签
            if (data.getTags() != null && !data.getTags().isEmpty()) {
                LinkedTreeMap tag = (LinkedTreeMap) data.getTags().get(0);
                String tagText = (String) tag.get("tagName");
                if (!TextUtils.isEmpty(tagText)) {
                    setTag(tagText);
                    tagLayout.setVisibility(View.VISIBLE);
                } else {
                    tagLayout.setVisibility(View.GONE);
                }
            } else {
                tagLayout.setVisibility(View.GONE);
            }

            //用户登录
            if (SessionUtil.getInstance().isLogin()) {
                User user = SessionUtil.getInstance().getLoginUser();
                //根据用户角色显示对应的数据
                if (user.vipType > 0) {
                    tvMarkerPrice.setVisibility(View.GONE);
                    mLlReward.setVisibility(View.VISIBLE);
//                    tvPriceLine.setTextColor(Color.parseColor("#FF4647"));
                    tvRetailPrice.setTextSize(14);
                    tvRetailPrice.setTextColor(Color.BLACK);
                } else {
                    tvMarkerPrice.setVisibility(View.VISIBLE);
                    mLlReward.setVisibility(View.GONE);
//                    tvPriceLine.setTextColor(Color.BLACK);
                    tvRetailPrice.setTextSize(16);
                    tvRetailPrice.setTextColor(Color.parseColor("#FF4647"));
                }
            }
        }

        void setTag(String tagValue) {
            DDTagView ddTagView = (DDTagView) LayoutInflater.from(context)
                    .inflate(R.layout.item_product_category_tag, tagLayout, false);
            if (tagValue.contains("#")) {
                ddTagView.setText(tagValue.split("#")[0]);
                ddTagView.setTagBackground("#" + tagValue.split("#")[1]);
            } else {
                ddTagView.setText(tagValue);
            }
            tagLayout.removeAllViews();
            tagLayout.addView(ddTagView);
        }

        @OnClick(R.id.llSuggest)
        void onSuggestItemPressed() {
            EventUtil.viewProductDetail(context, data.getProductId());
        }
    }

}
