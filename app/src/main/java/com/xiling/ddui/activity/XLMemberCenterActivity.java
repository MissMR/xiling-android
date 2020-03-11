package com.xiling.ddui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.adapter.WeekPackageAdapter;
import com.xiling.ddui.bean.MemberCenterInfo;
import com.xiling.ddui.bean.WeekCardConfigBean;
import com.xiling.ddui.bean.WeekCardInfo;
import com.xiling.ddui.config.H5UrlConfig;
import com.xiling.ddui.custom.popupwindow.DirectRechargeDialog;
import com.xiling.ddui.custom.popupwindow.WeekBeOverdueDialog;
import com.xiling.ddui.service.IMemberService;
import com.xiling.ddui.tools.NumberHandler;
import com.xiling.dduis.magnager.UserManager;
import com.xiling.image.GlideUtils;
import com.xiling.module.MainActivity;
import com.xiling.module.community.DateUtils;
import com.xiling.module.page.WebViewActivity;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.NewUserBean;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.SharedPreferenceUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @auth 逄涛
 * 会员中心
 */
public class XLMemberCenterActivity extends BaseActivity {
    IMemberService iMemberService;

    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_member_name)
    TextView tvMemberName;
    @BindView(R.id.tv_my_week_card)
    TextView tvMyWeekCard;
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
    @BindView(R.id.rel_title)
    RelativeLayout relTitle;
    @BindView(R.id.btn_growth_detailed)
    View btnGrowthDetailed;
    @BindView(R.id.iv_cart_us)
    ImageView ivCartUs;
    @BindView(R.id.tv_cart_us)
    TextView tvCartUs;
    @BindView(R.id.pager_weekpackage)
    ViewPager pagerWeekPackage;
    @BindView(R.id.iv_upgrading)
    ImageView ivUpgrading;


    WeekPackageAdapter weekPackageAdapter;
    List<View> weekPackageList = new ArrayList<>();


    WeekCardInfo weekCardInfo;
    boolean isOpenWeekCard = false;
    @BindView(R.id.btn_buy_black)
    TextView btnBuyBlack;
    @BindView(R.id.btn_sale)
    TextView btnSale;
    @BindView(R.id.btn_buy_vip)
    TextView btnBuyVip;
    @BindView(R.id.tv_experience)
    TextView tvExperience;

    List<WeekCardConfigBean> weekCardConfigBeanList = new ArrayList<>();
    WeekCardConfigBean weekCardConfigBean = null;


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
        pagerWeekPackage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                weekCardConfigBean = weekCardConfigBeanList.get(position);
                if (weekCardConfigBean.getWeekType() == 1) {
                    ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_vip);
                    btnBuyVip.setVisibility(View.VISIBLE);
                    btnBuyBlack.setVisibility(View.GONE);
                    btnSale.setVisibility(View.GONE);
                } else if (weekCardConfigBean.getWeekType() == 2) {
                    ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_balck);
                    btnBuyVip.setVisibility(View.GONE);
                    btnBuyBlack.setVisibility(View.VISIBLE);
                    btnSale.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

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
                    pagerWeekPackage.setVisibility(View.VISIBLE);
                    weekPackageList.clear();
                    NewUserBean userBean = UserManager.getInstance().getUser();
                    weekCardConfigBeanList = result;
                    weekCardConfigBean = weekCardConfigBeanList.get(0);
                    for (WeekCardConfigBean weekCardConfigBean : result) {
                        if (userBean.getRoleId() == 1) {
                            // 如果本身是普通会员，都可以购买
                            View view = LayoutInflater.from(context).inflate(R.layout.item_week_package, null);
                            ImageView imageView = view.findViewById(R.id.iv_img);
                            if (weekCardConfigBean.getWeekType() == 1) {
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_vip);
                            } else if (weekCardConfigBean.getWeekType() == 2) {
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_balck);
                            }
                            weekPackageList.add(view);
                        } else if (userBean.getRoleId() == 2) {
                            //如果是vip会员，只能购买黑卡
                            if (weekCardConfigBean.getWeekType() == 2) {
                                View view = LayoutInflater.from(context).inflate(R.layout.item_week_package, null);
                                ImageView imageView = view.findViewById(R.id.iv_img);
                                imageView.setBackgroundResource(R.drawable.bg_upgrading_balck);
                                weekPackageList.add(view);
                            }
                        }
                    }

                    if (result.get(0).getWeekType() == 1) {
                        ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_vip);
                        btnBuyBlack.setVisibility(View.GONE);
                        btnSale.setVisibility(View.GONE);
                        btnBuyVip.setVisibility(View.VISIBLE);
                    } else if (result.get(0).getWeekType() == 2) {
                        ivUpgrading.setBackgroundResource(R.drawable.bg_upgrading_mode_balck);
                        btnBuyBlack.setVisibility(View.VISIBLE);
                        btnSale.setVisibility(View.VISIBLE);
                        btnBuyVip.setVisibility(View.GONE);
                    }

                    if (weekPackageAdapter == null) {
                        weekPackageAdapter = new WeekPackageAdapter(context, weekPackageList);
                        pagerWeekPackage.setAdapter(weekPackageAdapter);
                    } else {
                        weekPackageAdapter.notifyDataSetChanged();
                    }

                } else {
                    pagerWeekPackage.setVisibility(View.GONE);
                    ivUpgrading.setVisibility(View.GONE);
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

                tvCouponSize.setText(result.getCouponCount() + "");
                if (!TextUtils.isEmpty(result.getCouponDate())) {
                    tvCouponDate.setText(result.getCouponDate().split(" ")[0] + "到期");
                }
                growthValueCurrent.setText(NumberHandler.reservedDecimalFor2(Double.valueOf(result.getGrowValueTotle())));
                if (newUserBean.getRoleId() == 3) {
                    progressBar.setProgress(100);
                    progressBar.setMax(100);
                    tvGrowthMessage.setText("您当前已是黑卡会员");
                } else {
                    double max = Double.valueOf(result.getNextMemberRole().getGrowValue());
                    progressBar.setMax((int) max);
                    double progress = Double.valueOf(result.getGrowValueTotle());
                    progressBar.setProgress((int) progress);
                    tvGrowthMessage.setText("差" + NumberHandler.reservedDecimalFor2(Double.valueOf(result.getNextMemberRole().getGrowValue()) - Double.valueOf(result.getGrowValueTotle()))
                            + "成长值升级成为终身" + result.getNextMemberRole().getRoleName() + "(享受" + Double.valueOf(result.getNextMemberRole().getRoleDiscount()) / 10 + "折优惠)");
                }

                switch (Integer.valueOf(result.getMemberRole().getRoleLevel())) {
                    case 10:
                        //普通会员
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_ordinary);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_ordinary);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_ordinary);
                        break;
                    case 20:
                        //vip会员
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_vip);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_vip);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_vip);
                        break;
                    case 30:
                        //黑卡会员
                        btnGrowthDetailed.setBackgroundResource(R.drawable.bg_member_black);
                        tvRoleLevel.setBackgroundResource(R.drawable.icon_id_black);
                        ivRoleLevel.setBackgroundResource(R.drawable.icon_member_black);
                        break;
                }

                if (newUserBean.getRoleId() == 3) {
                    // 已经是黑卡，不需要请求周卡信息
                    ToastUtil.hideLoading();
                    pagerWeekPackage.setVisibility(View.GONE);
                    ivUpgrading.setVisibility(View.GONE);
                    setWeekCardStatus(null);
                } else {
                    pagerWeekPackage.setVisibility(View.VISIBLE);
                    ivUpgrading.setVisibility(View.VISIBLE);
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
            if (weekCardInfo.getStatus().equals("1")) {
                // 有效
                switch (weekCardInfo.getWeekType()) {
                    case "1":
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_vip);
                        ivCartUs.setBackgroundResource(R.drawable.icon_us_vip);
                        tvCartUs.setTextColor(Color.parseColor("#6D8891"));
                        tvExperience.setTextColor(Color.parseColor("#6D8891"));
                        break;
                    case "2":
                        relWeekCard.setBackgroundResource(R.drawable.bg_member_week_card_black);
                        ivCartUs.setBackgroundResource(R.drawable.icon_us_black);
                        tvCartUs.setTextColor(Color.parseColor("#A27309"));
                        tvExperience.setTextColor(Color.parseColor("#B68B2A"));
                        break;
                }

                tvMyWeekCard.setText("当前已开通" + weekCardInfo.getWeekName());
                relWeekCard.setVisibility(View.VISIBLE);
                tvWeekCardName.setText("喜领" + weekCardInfo.getWeekName());
                long waitPayTimeMilli = DateUtils.date2TimeStampLong(weekCardInfo.getExpiredTime(), "") - System.currentTimeMillis();
                startCountDown(waitPayTimeMilli);
            } else {
                //过期
                tvMyWeekCard.setText("当前周卡已到期");
                relWeekCard.setVisibility(View.GONE);
                //如果没有弹出过，弹出过期提醒
                if (SharedPreferenceUtil.getInstance().getBoolean("weekBeOverdue")) {
                    SharedPreferenceUtil.getInstance().putBoolean("weekBeOverdue", false);
                    WeekBeOverdueDialog weekBeOverdueDialog = new WeekBeOverdueDialog(context);
                    weekBeOverdueDialog.show();
                }

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
                weekCardInfo = result;
                if (result != null) {
                    NewUserBean newUserBean = UserManager.getInstance().getUser();
                    //有效
                    if (result.getWeekType().equals("1")) {
                        // 如果当前有效周卡为vip卡，只有普通用户有效
                        if (newUserBean.getRoleId() == 1) {
                            setWeekCardStatus(result);
                        } else {
                            setWeekCardStatus(null);
                        }
                    } else if (result.getWeekType().equals("2")) {
                        // 如果当前有效周卡为黑卡 普通和vip用户都有效
                        setWeekCardStatus(result);
                    } else {
                        setWeekCardStatus(null);
                    }
                } else {
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


    @OnClick({R.id.btn_close, R.id.btn_notes, R.id.btn_order_goods, R.id.btn_my_week_card_package, R.id.btn_growth, R.id.ll_coupon,
            R.id.btn_growth_detailed, R.id.btn_buy_black, R.id.btn_buy_vip, R.id.btn_sale})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                finish();
                break;
            case R.id.btn_notes:
                //会员说明
                WebViewActivity.jumpUrl(context, "会员说明", H5UrlConfig.MEMBER_EXPLAIN);
                break;
            case R.id.btn_my_week_card_package:
                //我的周卡包
                Intent intent = new Intent(context, MyWeekCardPackageActivity.class);
                if (weekCardInfo != null) {
                    intent.putExtra("weekCardInfo", weekCardInfo);
                }
                startActivity(intent);
                break;
            case R.id.btn_growth:
                WebViewActivity.jumpUrl(context, "成长值说明", H5UrlConfig.GROWTH_INTRO);
                break;
            case R.id.btn_growth_detailed:
                WebViewActivity.jumpUrl(context, "成长值明细", H5UrlConfig.GROWTH_DETAILED);
                break;
            case R.id.ll_coupon:
                //优惠券
                startActivity(new Intent(context, XLCouponActivity.class));
                break;
            case R.id.btn_order_goods:
                //去订货
                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
                break;
            case R.id.btn_buy_black:
                //购买黑卡
            case R.id.btn_buy_vip:
                //购买vip卡
                purchaseWeekCard(weekCardConfigBean);
                break;
            case R.id.btn_sale:
                //预存货款升级
                DirectRechargeDialog directRechargeDialog = new DirectRechargeDialog(context);
                directRechargeDialog.show();
                break;
        }
    }

    /**
     * 去购买周卡，fragment调用
     */
    public void purchaseWeekCard(WeekCardConfigBean weekCardConfigBean) {
        String myWeekCard = tvMyWeekCard.getText().toString();
        BuyWeekCardActivity.jumpBuyWeekCardActivity(this, weekCardConfigBean, myWeekCard);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBus(EventMessage message) {
        switch (message.getEvent()) {
            case WEEK_CARD_OPEN:
                //开通了周卡,刷新
                isOpenWeekCard = true;
                getMemberCenterInfo();
                break;
        }
    }
}
