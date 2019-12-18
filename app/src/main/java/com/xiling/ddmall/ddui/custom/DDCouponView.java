package com.xiling.ddmall.ddui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.DDCouponBean;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.dduis.custom.DDNumberTextView;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Jigsaw at 2019/6/13
 */
public class DDCouponView extends RelativeLayout {

    @BindView(R.id.iv_arrow)
    ImageView mIvArrow;
    @BindView(R.id.tv_money)
    DDNumberTextView tvMoney;
    @BindView(R.id.tv_coupon_range)
    TextView tvCouponRange;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_coupon_tag)
    TextView tvCouponTag;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_btn_status)
    TextView tvBtnStatus;
    @BindView(R.id.iv_select)
    ImageView mIvSelect;

    private DDCouponBean mDDCouponBean;
    private boolean mSelectable = false;
    private boolean mEnableBtnStatus = true;


    private OnCouponExpandListener mOnCouponExpandListener;

    public DDCouponView(Context context) {
        super(context);
        initView();
    }

    public DDCouponView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DDCouponView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_coupon, this);
        ButterKnife.bind(this);
        setBackgroundResource(R.drawable.bg_dd_coupon);
    }

    public void setSelectable(boolean selectable) {
        this.mSelectable = selectable;
        setActivated(true);
    }

    public void setDDCoupon(DDCouponBean coupon) {
        if (coupon == null) {
            return;
        }
        mDDCouponBean = coupon;
        renderView();
    }

    private void renderView() {
        if (mDDCouponBean == null) {
            return;
        }

        tvMoney.setText(String.valueOf(ConvertUtil.cent2yuan(mDDCouponBean.getReducedPrice())));

        tvCouponRange.setText(mDDCouponBean.getConditionText());

        tvName.setText(mDDCouponBean.getTitle());
        tvCouponTag.setText(mDDCouponBean.getLabel());
        tvTime.setText("有效期至" + mDDCouponBean.getEndDate());

        tvBtnStatus.setVisibility((!mEnableBtnStatus || mDDCouponBean.getStatus() == DDCouponBean.STATUS_UNUSE) ? GONE : VISIBLE);
        tvBtnStatus.setText(mDDCouponBean.getStatus() == DDCouponBean.STATUS_TIME_OUT ? "已过期" : "已使用");

        setCouponExpand(mDDCouponBean.isExpand());

        mIvSelect.setVisibility(mSelectable ? VISIBLE : GONE);

        setSelected(mDDCouponBean.isSelect());

    }

    public DDNumberTextView getTvMoney() {
        return tvMoney;
    }

    public void setTvMoney(DDNumberTextView tvMoney) {
        this.tvMoney = tvMoney;
    }

    public void showBtnStatus(boolean isShow) {
        mEnableBtnStatus = isShow;
        tvBtnStatus.setVisibility(isShow ? VISIBLE : GONE);
    }

    @OnClick(R.id.iv_arrow)
    public void onArrowClicked(final View view) {
        DLog.i("onArrowClicked");
        final boolean activated = view.isActivated();
        view.setActivated(!activated);
        if (mOnCouponExpandListener != null) {
            mOnCouponExpandListener.onCouponExpand(view.isActivated());
        }
    }

    public void setCouponExpandable(boolean isEnable) {
        mIvArrow.setVisibility(isEnable ? VISIBLE : GONE);
    }

    public void setCouponExpand(boolean isExpand) {
        mIvArrow.setActivated(isExpand);
    }

    public void setOnCouponExpandListener(OnCouponExpandListener onCouponExpandListener) {
        mOnCouponExpandListener = onCouponExpandListener;
    }

    public interface OnCouponExpandListener {
        void onCouponExpand(boolean isExpand);
    }


}
