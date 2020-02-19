package com.xiling.ddui.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.xiling.R;
import com.xiling.ddui.adapter.MackAdapter;
import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.ddui.custom.popupwindow.DirectRechargeDialog;
import com.xiling.ddui.fragment.WeekCardConfigFragment;
import com.xiling.ddui.service.IMemberService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.community.DateUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * pt
 * 会员中心
 */
public class XLMemberCenterActivity extends BaseActivity {
    IMemberService iMemberService;
    @BindView(R.id.recycler_make)
    RecyclerView recyclerMake;
    MackAdapter mackAdapter;
    List<MackAdapter.MackBean> mackBeanList = new ArrayList<>();
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_my_week_card)
    TextView tvMyWeekCard;
    @BindView(R.id.tv_role_discount)
    TextView tvRoleDiscount;
    @BindView(R.id.iv_roleLevel)
    ImageView ivRoleLevel;
    @BindView(R.id.tv_roleLevel)
    TextView tvRoleLevel;
    @BindView(R.id.rel_backgroud_head)
    RelativeLayout relBackgroudHead;
    @BindView(R.id.tv_coupon_size)
    TextView tvCouponSize;
    @BindView(R.id.tv_couponDate)
    TextView tvCouponDate;
    @BindView(R.id.growth_value_current)
    TextView growthValueCurrent;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.tv_growth_message)
    TextView tvGrowthMessage;
    @BindView(R.id.tv_week_card_name)
    TextView tvWeekCardName;
    @BindView(R.id.tv_week_card_expired_time)
    TextView tvWeekCardExpiredTime;
    @BindView(R.id.rel_week_card)
    RelativeLayout relWeekCard;
    @BindView(R.id.ll_week_card_package)
    LinearLayout llWeekCardPackage;
    @BindView(R.id.sliding_tab)
    SlidingTabLayout slidingTab;
    @BindView(R.id.viewpager_order)
    ViewPager viewpagerOrder;
    @BindView(R.id.ll_black_recharge)
    LinearLayout llBlackRecharge;
    @BindView(R.id.rel_week_record)
    RelativeLayout relWeekRecord;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private List<String> childNames = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xlmember_center);
        setLeftBlack();
        ButterKnife.bind(this);
        makeStatusBarTranslucent();
        iMemberService = ServiceManager.getInstance().createService(IMemberService.class);
        initView();
        getMemberCenterInfo();
    }


    private void initView() {
        mackAdapter = new MackAdapter();
        recyclerMake.setLayoutManager(new LinearLayoutManager(context));
        recyclerMake.setAdapter(mackAdapter);

        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_invitation, "邀请周卡会员赚成长值翻倍", "", "去邀请"));
        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_invitation, "邀请黑卡会员赚成长值翻倍", "", "去邀请"));
        mackBeanList.add(new MackAdapter.MackBean(R.drawable.icon_place_order, "下单订货赚成长值翻倍", "350元=1成长值", "去订货"));
        mackAdapter.setNewData(mackBeanList);
    }


    /**
     * 获取周卡配置
     */
    private void getWeekCardConfigList() {
        APIManager.startRequest(iMemberService.getWeekCardConfigList(), new BaseRequestListener<List<WeekCardConfigBean>>() {
            @Override
            public void onSuccess(List<WeekCardConfigBean> result) {
                super.onSuccess(result);
                if (result != null && result.size() > 0) {
                    viewpagerOrder.setOffscreenPageLimit(result.size());
                    llWeekCardPackage.setVisibility(View.VISIBLE);
                    fragments.clear();
                    childNames.clear();
                    for (WeekCardConfigBean weekCardConfigBean : result) {
                        childNames.add(weekCardConfigBean.getWeekName());
                        fragments.add(WeekCardConfigFragment.newInstance(weekCardConfigBean));
                    }

                    slidingTab.setViewPager(viewpagerOrder, childNames.toArray(new String[childNames.size()]), XLMemberCenterActivity.this, fragments);
                    viewpagerOrder.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            viewpagerOrder.requestLayout();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                } else {
                    llWeekCardPackage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }


        });
    }


    /**
     * 会员中心首页接口
     */
    private void getCenterInfo(final NewUserBean newUserBean) {
        APIManager.startRequest(iMemberService.getCenterInfo(), new BaseRequestListener<MemberCenterInfo>() {
            @Override
            public void onSuccess(MemberCenterInfo result) {
                super.onSuccess(result);

                GlideUtils.loadHead(context, ivHead, result.getHeadImage());
                tvMemberName.setText(result.getNickName());
                tvRoleDiscount.setText(Double.valueOf(result.getMemberRole().getRoleDiscount()) / 10 + "");
                tvRoleLevel.setText(result.getMemberRole().getRoleName());
                tvCouponSize.setText(result.getCouponCount() + "");
                if (!TextUtils.isEmpty(result.getCouponDate())) {
                    tvCouponDate.setText(result.getCouponDate().split(" ")[0] + "到期");
                }
                growthValueCurrent.setText(NumberHandler.reservedDecimalFor2(Double.valueOf(result.getGrowValueTotle())));
                if (newUserBean.getRole().getRoleLevel() == 30) {
                    progressBar.setProgress(100);
                    progressBar.setMax(100);
                    tvGrowthMessage.setText("您当前已是黑卡会员");
                    llBlackRecharge.setVisibility(View.GONE);
                    relWeekRecord.setVisibility(View.VISIBLE);
                } else {
                    llBlackRecharge.setVisibility(View.VISIBLE);
                    relWeekRecord.setVisibility(View.GONE);
                    double max = Double.valueOf(result.getNextMemberRole().getGrowValue());
                    progressBar.setMax((int) max);
                    double progress = Double.valueOf(result.getGrowValueTotle());
                    progressBar.setProgress((int) progress);
                    tvGrowthMessage.setText("差" + NumberHandler.reservedDecimalFor2(Double.valueOf(result.getNextMemberRole().getGrowValue()) - Double.valueOf(result.getGrowValueTotle()))
                            + "成长值升级成为" + result.getNextMemberRole().getRoleName() + "(享受" + Double.valueOf(result.getNextMemberRole().getRoleDiscount()) / 10 + "折优惠)");
                }

                switch (Integer.valueOf(result.getMemberRole().getRoleLevel())) {
                    case 10:
                        //普通会员
                        relBackgroudHead.setBackgroundResource(R.drawable.bg_member_head_ordinary);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_ordinary);
                        break;
                    case 20:
                        //vip会员
                        relBackgroudHead.setBackgroundResource(R.drawable.bg_member_head_vip);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_vip);
                        break;
                    case 30:
                        //黑卡会员
                        relBackgroudHead.setBackgroundResource(R.drawable.bg_member_head_black);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_black);
                        break;
                }

                if (newUserBean.getRole().getRoleLevel() == 30) {
                    // 已经是黑卡，不需要请求周卡信息
                    ToastUtil.hideLoading();
                    llWeekCardPackage.setVisibility(View.GONE);
                    setWeekCardStatus(null);
                } else {
                    getWeekCardConfigList();
                    getWeekCardInfo();
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
                ToastUtil.hideLoading();
            }
        });
    }

    /**
     * 启动倒计时
     */
    private void startCountDown(long waitPayTimeMilli) {
        CountDownTimer countDownTimer = new CountDownTimer(waitPayTimeMilli, 1000) {
            @Override
            public void onTick(long l) {
                tvWeekCardExpiredTime.setText(DateUtils.getDayForTime(l));
            }

            @Override
            public void onFinish() {
                finish();
            }
        };
        countDownTimer.start();
    }

    /**
     * 设置周卡
     *
     * @param weekCardInfo
     */
    private void setWeekCardStatus(WeekCardInfo weekCardInfo) {
        if (weekCardInfo == null) {
            // 没有周卡
            tvMyWeekCard.setText("当前暂无周卡体验");
            relWeekCard.setVisibility(View.GONE);
        } else {
            //有周卡
            if (weekCardInfo.getStatus() == 1) {
                // 有效

                switch (weekCardInfo.getWeekType()){
                    case 1:
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_vip);
                        break;
                    case 2:
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_black);
                        break;
                }

                tvMyWeekCard.setText("当前已开通周卡体验");
                relWeekCard.setVisibility(View.VISIBLE);
                tvWeekCardName.setText("喜领" + weekCardInfo.getWeekName());
                long waitPayTimeMilli = DateUtils.date2TimeStampLong(weekCardInfo.getExpiredTime(), "") - System.currentTimeMillis();
                startCountDown(waitPayTimeMilli);
            } else {
                //过期
                tvMyWeekCard.setText("当前周卡已到期");
                relWeekCard.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取周卡状态
     */
    private void getWeekCardInfo() {
        APIManager.startRequest(iMemberService.getWeekCardInfo(), new BaseRequestListener<WeekCardInfo>() {
            @Override
            public void onSuccess(WeekCardInfo result) {
                super.onSuccess(result);
                ToastUtil.hideLoading();
                if (result != null){
                    NewUserBean newUserBean = UserManager.getInstance().getUser();
                    //有效
                    if (result.getWeekType() == 1) {
                        // 如果当前有效周卡为vip卡，只有普通用户有效
                        if (newUserBean.getRole().getRoleLevel() == 10) {
                            setWeekCardStatus(result);
                        } else {
                            setWeekCardStatus(null);
                        }
                    } else if (result.getWeekType() == 2) {
                        // 如果当前有效周卡为黑卡 普通和vip用户都有效
                        setWeekCardStatus(result);
                    }
                }else{
                    setWeekCardStatus(null);
                }


            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
            }
        });
    }


    /**
     * 获取会员中心信息
     */
    private void getMemberCenterInfo() {
        ToastUtil.showLoading(context);
        UserManager.getInstance().checkUserInfo(new UserManager.OnCheckUserInfoLisense() {
            @Override
            public void onCheckUserInfoSucess(NewUserBean newUserBean) {
                getCenterInfo(newUserBean);
            }

            @Override
            public void onCheckUserInfoFail() {
                ToastUtil.hideLoading();
            }
        });
    }


    @OnClick({R.id.btn_close, R.id.btn_notes, R.id.rel_week_record,R.id.btn_recharge})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_notes:
                //会员说明
                break;
            case R.id.rel_week_record:
                //我的周卡记录
                break;
            case R.id.btn_recharge:
                //立即充值
                DirectRechargeDialog directRechargeDialog = new DirectRechargeDialog(context);
                directRechargeDialog.show();
                break;
        }
    }

    /**
     * 去开通周卡，fragment调用
     */
    public void purchaseWeekCard(WeekCardConfigBean weekCardConfigBean){
        String myWeekCard = tvMyWeekCard.getText().toString();
        BuyWeekCardActivity.jumpBuyWeekCardActivity(this,weekCardConfigBean,myWeekCard);
    }

}
