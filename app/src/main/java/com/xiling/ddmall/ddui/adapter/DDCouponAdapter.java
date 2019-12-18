package com.xiling.ddmall.ddui.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.SpannableStringBuilder;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.DDCouponBean;
import com.xiling.ddmall.ddui.custom.DDCouponView;
import com.xiling.ddmall.ddui.tools.DLog;

/**
 * created by Jigsaw at 2019/6/12
 */
public class DDCouponAdapter extends BaseQuickAdapter<DDCouponBean, BaseViewHolder> {

    private int mCouponStatus = -1;
    private SparseArray<MoneyTextViewCache> mMoneyWidthCache = new SparseArray<>();

    public DDCouponAdapter(int couponStatus) {
        super(R.layout.item_dd_coupon);
        this.mCouponStatus = couponStatus;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DDCouponBean item) {

        DDCouponView couponView = helper.getView(R.id.dd_coupon_view);
        couponView.setCouponExpandable(true);
        couponView.setEnabled(isCouponEnable());
        couponView.setDDCoupon(item);
        fixMoneyWidth(couponView.getTvMoney(), item);
        helper.getView(R.id.rl_coupon_desc).setVisibility(item.isExpand() ? View.VISIBLE : View.GONE);
        couponView.setOnCouponExpandListener(new DDCouponView.OnCouponExpandListener() {
            @Override
            public void onCouponExpand(boolean isExpand) {
                item.setExpand(isExpand);
                helper.getView(R.id.rl_coupon_desc).setVisibility(isExpand ? View.VISIBLE : View.GONE);
            }
        });

        helper.setText(R.id.tv_coupon_desc, getCouponDescText(item.getDescription()));
    }

    private void fixMoneyWidth(final TextView textView, final DDCouponBean coupon) {
        int key = (int) coupon.getReducedPrice();
        if (mMoneyWidthCache.indexOfKey(key) < 0) {
            textView.post(new Runnable() {
                @Override
                public void run() {
                    DLog.i("coupon price : " + coupon.getReducedPrice() + " , width : " + textView.getWidth());
                    mMoneyWidthCache.put((int) coupon.getReducedPrice(),
                            new MoneyTextViewCache((int) textView.getTextSize(), (int) textView.getWidth()));
                }
            });
        } else {
            if (TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE != TextViewCompat.getAutoSizeTextType(textView)) {
                TextViewCompat.setAutoSizeTextTypeWithDefaults(textView, TextViewCompat.AUTO_SIZE_TEXT_TYPE_NONE);
            }
            textView.setWidth(mMoneyWidthCache.get(key).getTextWidth());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mMoneyWidthCache.get(key).getTextSize());
            DLog.i("key : " + key + " , width : " + mMoneyWidthCache.get(key).toString());
        }
    }

    private SpannableStringBuilder getCouponDescText(String text) {
        return SpannableStringUtils.getBuilder("优惠券使用说明：")
                .setForegroundColor(isCouponEnable() ? ContextCompat.getColor(mContext, R.color.ddm_black_dark) : ContextCompat.getColor(mContext, R.color.ddm_gray_dark))
                .append(text == null ? "" : text)
                .setForegroundColor(ContextCompat.getColor(mContext, R.color.ddm_gray_dark))
                .create();
    }

    private boolean isCouponEnable() {
        return mCouponStatus == DDCouponBean.STATUS_UNUSE;
    }

    private static class MoneyTextViewCache {
        private int textSize;
        private int textWidth;

        public MoneyTextViewCache(int textSize, int textWidth) {
            this.textSize = textSize;
            this.textWidth = textWidth;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public int getTextWidth() {
            return textWidth;
        }

        public void setTextWidth(int textWidth) {
            this.textWidth = textWidth;
        }

        @Override
        public String toString() {
            return "MoneyTextViewCache{" +
                    "textSize=" + textSize +
                    ", textWidth=" + textWidth +
                    '}';
        }
    }
}
