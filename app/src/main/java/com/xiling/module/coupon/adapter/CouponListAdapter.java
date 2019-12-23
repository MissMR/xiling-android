package com.xiling.module.coupon.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.R;
import com.xiling.shared.basic.BaseAdapter;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.coupon.adapter
 * @since 2017-06-20
 */
public class CouponListAdapter extends BaseAdapter<Coupon, CouponListAdapter.ViewHolder> {

    private Activity mActivity;
    private final boolean mIsSelectCoupon;
    private int mSelectPosition = -1;

    public CouponListAdapter(Activity context, boolean isSelectCoupon) {
        super(context);
        mActivity = context;
        mIsSelectCoupon = isSelectCoupon;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_coupon, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Coupon coupon = items.get(position);
        coupon.isSelected = position == mSelectPosition;
        holder.mItemTitleTv.setText(coupon.title);
        holder.mItemPriceTv.setText(ConvertUtil.centToCurrencyNoZero(context, coupon.amount));
        FrescoUtil.setImageSmall(holder.mItemThumbIv, coupon.thumb);

        holder.mItemDescTv.setText(String.format("满%s减%s", ConvertUtil.cent2yuanNoZero(coupon.minOrderMoney), ConvertUtil.cent2yuanNoZero(coupon.amount)));
        holder.mItemDescTv.setVisibility(StringUtils.isEmpty(coupon.productId) ? View.VISIBLE : View.GONE);

        holder.mItemStoreNameTv.setText(coupon.storeName);
        holder.mItemTimeTv.setText(coupon.getStatusText() + "有效期：" + coupon.getDateRange());
        holder.mItemSelectFlag.setVisibility(coupon.isSelected ? View.VISIBLE : View.GONE);

        if (mIsSelectCoupon) {
            holder.mToUseBtn.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!coupon.userCanUse()) {
                        ToastUtils.showShortToast("该优惠券还未到使用时间");
                        return;
                    }
                    if (position == mSelectPosition) {
                        mSelectPosition = -1;
                        coupon.isSelected = false;
                    } else {
                        mSelectPosition = position;
                        coupon.isSelected = true;
                    }
                    notifyDataSetChanged();
                }
            });

            holder.mItemBackgroundIv.setEnabled(coupon.userCanUse());
            if (coupon.userCanUse()) {
//                holder.mItemDescTv.setTextColor(context.getResources().getColor(R.color.red));
                holder.mItemPriceTv.setTextColor(context.getResources().getColor(R.color.redPrice));
                holder.mItemTitleTv.setTextColor(context.getResources().getColor(R.color.text_black));
            } else {
                holder.mItemDescTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mItemPriceTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mItemTitleTv.setTextColor(context.getResources().getColor(R.color.text_gray));
            }
        } else {
            holder.mItemBackgroundIv.setEnabled(coupon.couponCanUse());
            if (coupon.couponCanUse()) {
//                holder.mItemDescTv.setTextColor(context.getResources().getColor(R.color.red));
                holder.mItemPriceTv.setTextColor(context.getResources().getColor(R.color.redPrice));
                holder.mItemTitleTv.setTextColor(context.getResources().getColor(R.color.text_black));
                holder.mToUseBtn.setVisibility(View.VISIBLE);
            } else {
                holder.mItemDescTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mItemPriceTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mItemTitleTv.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mToUseBtn.setVisibility(View.GONE);
            }

            holder.mToUseBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventUtil.viewCouponDetail(mActivity, coupon);
                }
            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.itemBackgroundIv)
        protected ImageView mItemBackgroundIv;
        @BindView(R.id.itemThumbIv)
        protected SimpleDraweeView mItemThumbIv;
        @BindView(R.id.itemTitleTv)
        protected TextView mItemTitleTv;
        @BindView(R.id.itemPriceTv)
        protected TextView mItemPriceTv;
        @BindView(R.id.itemDescTv)
        protected TextView mItemDescTv;
        @BindView(R.id.itemStoreNameTv)
        protected TextView mItemStoreNameTv;
        @BindView(R.id.itemTimeTv)
        protected TextView mItemTimeTv;
        @BindView(R.id.toUseBtn)
        protected TextView mToUseBtn;
        @BindView(R.id.itemSelectFlag)
        protected ImageView mItemSelectFlag;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
