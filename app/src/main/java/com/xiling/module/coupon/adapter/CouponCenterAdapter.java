package com.xiling.module.coupon.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.module.coupon.CouponDetailActivity;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.service.CouponService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.coupon.adapter
 * @since 2017-06-20
 */
public class CouponCenterAdapter extends BaseAdapter<Coupon, CouponCenterAdapter.ViewHolder> {


    public CouponCenterAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_coupon_center, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Coupon coupon = items.get(position);
        holder.mItemTitleTv.setText(coupon.title);
        holder.mItemPriceTv.setText(ConvertUtil.centToCurrencyNoZero(context, coupon.amount));
        FrescoUtil.setImageSmall(holder.mItemThumbIv, coupon.thumb);
        holder.mItemDescTv.setText(String.format("满%s减%s", ConvertUtil.cent2yuanNoZero(coupon.minOrderMoney), ConvertUtil.cent2yuanNoZero(coupon.amount)));
        holder.mItemDescTv.setVisibility(StringUtils.isEmpty(coupon.productId) ? View.VISIBLE : View.GONE);
//        holder.mItemStoreNameTv.setText(coupon.storeName);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewCouponDetail(coupon);
            }
        });
        setBtnStatus(holder.mGetCouponBtn, coupon);
        holder.mGetCouponBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (coupon.isReceived) {
                    viewCouponDetail(coupon);
                } else {
                    CouponService.getCoupon(context, coupon.couponId, new BaseCallback<Object>() {
                        @Override
                        public void callback(Object data) {
                            coupon.isReceived = true;
                            setBtnStatus(holder.mGetCouponBtn, coupon);
                        }
                    });
                }
            }
        });
    }

    private void setBtnStatus(TextView btn, Coupon coupon) {
        if (coupon.isReceived) {
            btn.setEnabled(false);
            btn.setTextColor(btn.getContext().getResources().getColor(R.color.red));
            btn.setBackgroundResource(R.drawable.bg_coupon_right_select);
            btn.setText("已领取");
        } else {
            btn.setEnabled(true);
            btn.setText("立刻领取");
            btn.setTextColor(btn.getContext().getResources().getColor(R.color.text_black));
            btn.setBackgroundResource(R.mipmap.bg_coupon_right);
        }
    }

    private void viewCouponDetail(Coupon coupon) {
        Intent intent = new Intent(context, CouponDetailActivity.class);
        intent.putExtra("couponId", coupon.couponId);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mItemPriceTv;
        @BindView(R.id.itemDescTv)
        protected TextView mItemDescTv;
//        @BindView(R.id.itemStoreNameTv)
//        protected TextView mItemStoreNameTv;
        @BindView(R.id.getCouponBtn)
        protected TextView mGetCouponBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
