package com.xiling.ddmall.shared.component.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.basic.BaseCallback;
import com.xiling.ddmall.shared.bean.Coupon;
import com.xiling.ddmall.shared.service.CouponService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/10.
 */
public class CouponBottomDialog extends BottomSheetDialog {

    @BindView(R.id.itemThumbIv)
    SimpleDraweeView mItemThumbIv;
    @BindView(R.id.itemTitleTv)
    TextView mItemTitleTv;
    @BindView(R.id.itemPriceTv)
    TextView mItemPriceTv;
    @BindView(R.id.itemDescTv)
    TextView mItemDescTv;
    @BindView(R.id.couponLeftLayout)
    RelativeLayout mCouponLeftLayout;
    @BindView(R.id.getCouponBtn)
    TextView mGetCouponBtn;
    private Coupon mCoupon;

    public CouponBottomDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_coupon_bottom);
        ButterKnife.bind(this);
    }

    public void setData(Coupon coupon) {
        mCoupon = coupon;
        FrescoUtil.setImage(mItemThumbIv, coupon.thumb);
        mItemTitleTv.setText(coupon.title);
        mItemDescTv.setVisibility(View.GONE);
        mItemPriceTv.setText(ConvertUtil.centToCurrency(getContext(), coupon.amount));
        mGetCouponBtn.setText(coupon.isReceived?"已领取":"立即领取");
        LogUtils.e(coupon.isReceived?"已领取":"立即领取");

        if (coupon.isReceived) {
            mGetCouponBtn.setTextColor(getContext().getResources().getColor(R.color.red));
            mGetCouponBtn.setBackgroundResource(R.drawable.bg_coupon_right_select);
            mGetCouponBtn.setText("已领取");
        } else {
            mGetCouponBtn.setText("立刻领取");
            mGetCouponBtn.setTextColor(getContext().getResources().getColor(R.color.text_black));
            mGetCouponBtn.setBackgroundResource(R.mipmap.bg_coupon_right);
        }
    }

    @OnClick(R.id.getCouponBtn)
    public void onMGetCouponBtnClicked() {
        if (mCoupon == null || mCoupon.isReceived) {
            return;
        }
        CouponService.getCoupon(getContext(), mCoupon.couponId, new BaseCallback<Object>() {
            @Override
            public void callback(Object data) {
                mCoupon.isReceived = true;
                dismiss();
            }
        });
    }

    @OnClick(R.id.tvSubmit)
    public void onMTvSubmitClicked() {
        dismiss();
    }
}
