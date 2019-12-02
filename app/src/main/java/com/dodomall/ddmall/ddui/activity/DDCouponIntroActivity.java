package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DDCouponIntroActivity extends BaseActivity {

    @BindView(R.id.tv_intro)
    TextView mTvIntro;
    private String[] mIntros = {"优惠券不能兑换现金，优惠金额不能开具发票\n",
            "优惠券应在券面载明的有效期内使用，过期作废\n",
            "如使用满减类优惠券的订单发生售后退货，优惠券不予退还；如使用直接抵扣类现金券的订单发生售后退货，现金券按照退货商品的金额比例退还。此外，如有其他明确不予退还优惠券的情形时则不予退还\n",
            "如店多多已发放的优惠券存在有失公平等不符合运营目的的情形（包括但不限于发券类别失误，价格配置失误），店多多有权将您的优惠券或使用优惠券的订单冻结或关闭。您已领取未使用的优惠券将进行冻结，已使用优惠券并支付的订单，将按" +
                    "交易失败处理，您已实际支付的款项将进行退款，优惠券不予退还。\n",
            "优惠券券面对同一用户领取优惠券有规定时，如您违反该规定，店多多有权将您已使用优惠券的订单关闭并按交易处理，您已实际支付的款项将进行退款，优惠券不予退还。同一用户是指：根据用户登录或适用的店多多账号，店多多根据其关联信息判" +
                    "断实际为同一用户的。关联信息包括但不限于：同一手机号或同一账号或同一身份证号码或同一设备号。\n",
            "优惠券的具体使用除应满足每张优惠券全面载明的使用规则外，还应满足优惠券全部说明规则。"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ddcoupon_intro);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        showHeader("优惠券说明");
        SpannableStringUtils.Builder builder = SpannableStringUtils.getBuilder("");
        for (int i = 0; i < mIntros.length; i++) {
            builder.append(mIntros[i])
                    .setBullet(ConvertUtil.dip2px(14), mTvIntro.getCurrentTextColor());
        }
        mTvIntro.setText(builder.create());

    }


}
