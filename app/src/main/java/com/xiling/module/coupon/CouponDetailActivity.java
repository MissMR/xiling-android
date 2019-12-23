package com.xiling.module.coupon;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.xiling.BuildConfig;
import com.xiling.R;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseCallback;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.bean.User;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.util.CommonUtil;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.EventUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ShareUtils;
import com.xiling.shared.util.ToastUtil;
import com.xiling.shared.util.WechatUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.coupon
 * @since 2017-07-05
 */
public class CouponDetailActivity extends BaseActivity implements IWXAPIEventHandler {

    @BindView(R.id.titleTv)
    protected TextView mTitleTv;
    @BindView(R.id.priceTv)
    protected TextView mPriceTv;
    @BindView(R.id.tipsTv)
    protected TextView mTipsTv;
    @BindView(R.id.storeNameTv)
    protected TextView mStoreNameTv;
    @BindView(R.id.descTv)
    protected TextView mDescTv;
    private String mCouponId;
    private Coupon mCoupon;
    private IWXAPI mWxApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        ButterKnife.bind(this);
        showHeader();
        setTitle("优惠券");
        Intent intent = getIntent();
        if (!(intent == null || intent.getExtras() == null)) {
            mCouponId = intent.getExtras().getString("couponId");
        }
        if (mCouponId == null || mCouponId.isEmpty()) {
            ToastUtil.error("参数错误");
            finish();
            return;
        }
        mWxApi = WechatUtil.newWxApi(this);
        mWxApi.handleIntent(intent, this);
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getHeaderLayout().setRightDrawable(R.mipmap.icon_share_rec);
        getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareCoupon();
            }
        });

        ICouponService couponService = ServiceManager.getInstance().createService(ICouponService.class);
        APIManager.startRequest(couponService.getCouponDetailById(mCouponId), new BaseRequestListener<Coupon>(this) {
            @Override
            public void onSuccess(Coupon result) {
                if (result == null || result.limitStartDate == null || result.limitEndDate == null) {
                    ToastUtils.showShortToast("后台数据异常");
                    finish();
                }
                mCoupon = result;
                mTitleTv.setText(mCoupon.title);
                mPriceTv.setText(ConvertUtil.centToCurrencyNoZero(CouponDetailActivity.this, mCoupon.amount));
                mTipsTv.setText(String.format("满%s减%s", ConvertUtil.cent2yuanNoZero(mCoupon.minOrderMoney), ConvertUtil.cent2yuanNoZero(mCoupon.amount)));
                mTipsTv.setVisibility(StringUtils.isEmpty(result.productId) ? View.VISIBLE : View.GONE);
                mStoreNameTv.setText(mCoupon.storeName);
                mDescTv.setText("有效期：" + mCoupon.getDateRange());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtil.hideLoading();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mWxApi.handleIntent(intent, this);
    }

    private void shareCoupon() {
        if (mCoupon == null) {
            return;
        }
        String url = BuildConfig.BASE_URL + "c/" + mCoupon.couponId;
        if (SessionUtil.getInstance().isLogin()) {
            User user = SessionUtil.getInstance().getLoginUser();
            url += "/" + user.id;
        }
        ShareUtils.showShareDialog(
                this,
                CommonUtil.getAppName(this) + "有钱任性，送您一张优惠券",
                "领券后下单，购满即减，省钱就是这么简单",
                mCoupon.thumb,
                url);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            WechatUtil.compileResponse(baseResp, new BaseCallback<BaseResp>() {
                @Override
                public void callback(BaseResp data) {
                    ToastUtil.success("分享成功");
                }
            });
        }
    }

    @OnClick(R.id.toUseBtn)
    protected void toUseCoupon() {
        if (mCoupon == null) {
            return;
        }
        EventUtil.viewCouponDetail(this, mCoupon);
    }
}
