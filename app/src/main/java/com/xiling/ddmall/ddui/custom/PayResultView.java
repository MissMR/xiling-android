package com.xiling.ddmall.ddui.custom;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.xiling.ddmall.MyApplication;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.bean.OrderPayStatusBean;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * created by Jigsaw at 2019/1/22
 * 订单回调成功 可进行操作
 * 查看订单 返回首页
 * 订单回调失败 可进行操作
 * 刷新页面 联系客服(skuId)
 * 支付失败
 * 查看订单 去支付
 */
public class PayResultView extends FrameLayout {

    // 支付失败
    public static final int PAY_FAIL = 0x00;
    // 支付成功 订单处理中
    public static final int PAY_SUCCESS_WITH_ORDER_DEALING = 0x01;
    // 支付成功 订单处理失败
    public static final int PAY_SUCCESS_WITH_ORDER_FAIL = 0x02;
    // 支付成功 订单处理成功
    public static final int PAY_SUCCESS_WITH_ORDER_SUCCESS = 0x03;

    @BindView(R.id.sdv_pay_img)
    SimpleDraweeView mSdvPayImg;
    @BindView(R.id.tv_pay_status)
    TextView mTvPayStatus;
    @BindView(R.id.tv_pay_hint)
    TextView mTvPayHint;
    @BindView(R.id.tv_pay_way)
    TextView mTvPayWay;
    @BindView(R.id.tv_pay_money)
    TextView mTvPayMoney;
    @BindView(R.id.ll_btn_container)
    LinearLayout mLlBtnContainer;
    @BindView(R.id.tv_btn_left)
    TextView mTvBtnLeft;
    @BindView(R.id.tv_btn_right)
    TextView mTvBtnRight;

    private String mOrderId;
    private int mPayResultStatus = PAY_SUCCESS_WITH_ORDER_DEALING;
    private OnPayResultActionClickListener mOnPayResultActionClickListener;


    public PayResultView(@NonNull Context context) {
        super(context);
        initView();
    }

    public PayResultView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PayResultView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_pay_result, this, true);
        ButterKnife.bind(this);
        render();
    }


    public void setPayResultStatus(int status) {
        this.mPayResultStatus = status;
        render();
    }

    public void setOrderPaySuccess(OrderPayStatusBean orderPayStatus) {
        setPayResultStatus(PAY_SUCCESS_WITH_ORDER_SUCCESS);
        showPayDetail(true);
        mTvPayWay.setText("支付方式：" + orderPayStatus.getPayWayStr());
        mTvPayMoney.setText(SpannableStringUtils.getBuilder("支付金额：")
                .append(ConvertUtil.centToCurrency(MyApplication.getInstance(), orderPayStatus.getPayMoney()))
                .setForegroundColor(ContextCompat.getColor(getContext(), R.color.ddm_red)).create());
    }

    private void render() {
        setDefaultView();
        switch (mPayResultStatus) {
            case PAY_FAIL:
                setPayStatusImageRes(R.mipmap.ic_pay_fail);
                showOrderPayStatus("支付失败");
                showOrderPayHint(null);
                setBottomButton("查看订单", "去支付");
                break;
            case PAY_SUCCESS_WITH_ORDER_SUCCESS:
                setPayStatusImageRes(R.mipmap.ic_pay_success);
                showOrderPayStatus("订单处理成功");
                showOrderPayHint(null);
                showPayDetail(true);
                setBottomButton("查看订单", "返回首页");
                break;
            case PAY_SUCCESS_WITH_ORDER_FAIL:
                setPayStatusImageRes(R.mipmap.ic_pay_fail);
                showOrderPayStatus("订单处理失败");
                showOrderPayHint("请再次刷新页面或联系客服");
                setBottomButton("刷新页面", "联系客服");
                break;
            default:
        }


    }

    private void setPayStatusImageRes(@DrawableRes int res) {
        mSdvPayImg.setImageResource(res);
    }

    private void setDefaultView() {

        showOrderPayStatus("支付成功");
        showOrderPayHint("订单处理中...");
        showPayDetail(false);
        showBottomButton(false);

        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_RESOURCE_SCHEME)
                .path(String.valueOf(R.drawable.ic_pay_dealing))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        mSdvPayImg.setController(controller);
    }


    private void showOrderPayHint(String hint) {
        mTvPayHint.setVisibility(TextUtils.isEmpty(hint) ? GONE : VISIBLE);
        mTvPayHint.setText(hint);
    }

    private void showOrderPayStatus(String status) {
        mTvPayStatus.setText(status);
    }

    private void showPayDetail(boolean isShow) {
        mTvPayWay.setVisibility(isShow ? VISIBLE : GONE);
        mTvPayMoney.setVisibility(isShow ? VISIBLE : GONE);
    }

    private void setBottomButton(String leftBtnText, String rightBtnText) {
        showBottomButton(true);
        mTvBtnLeft.setText(leftBtnText);
        mTvBtnRight.setText(rightBtnText);
    }

    private void showBottomButton(boolean isShow) {
        mLlBtnContainer.setVisibility(isShow ? VISIBLE : GONE);
    }

    @OnClick(R.id.tv_btn_left)
    public void onLeftButtonClick() {
        if (getOnPayResultActionClickListener() == null) {
            return;
        }
        if (mPayResultStatus == PAY_SUCCESS_WITH_ORDER_FAIL) {
            // 刷新页面
            getOnPayResultActionClickListener().onRefreshClick();
        } else {
            // 查看订单
            getOnPayResultActionClickListener().onOrderClick();
        }
    }

    @OnClick(R.id.tv_btn_right)
    public void onRightButtonClick() {
        if (getOnPayResultActionClickListener() == null) {
            return;
        }
        if (mPayResultStatus == PAY_FAIL) {
            // 去支付
            getOnPayResultActionClickListener().onPayClick();
        } else if (mPayResultStatus == PAY_SUCCESS_WITH_ORDER_FAIL) {
            // 联系客服
            getOnPayResultActionClickListener().onServiceClick();
        } else {
            // 返回首页
            getOnPayResultActionClickListener().onHomeClick();
        }
    }

    public void setOnPayResultActionClickListener(OnPayResultActionClickListener onPayResultActionClickListener) {
        mOnPayResultActionClickListener = onPayResultActionClickListener;
    }

    public OnPayResultActionClickListener getOnPayResultActionClickListener() {
        return mOnPayResultActionClickListener;
    }

    public interface OnPayResultActionClickListener {
        // 查看订单
        void onOrderClick();

        // 去首页
        void onHomeClick();

        // 刷新页面
        void onRefreshClick();

        // 去支付
        void onPayClick();

        // 联系客服
        void onServiceClick();
    }

}
