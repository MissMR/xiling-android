package com.dodomall.ddmall.ddui.custom;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.AuthActivity;
import com.dodomall.ddmall.ddui.activity.BankCardActivity;
import com.dodomall.ddmall.ddui.activity.CashWithdrawActivity;
import com.dodomall.ddmall.ddui.activity.DDCommunityActivity;
import com.dodomall.ddmall.ddui.activity.EconomicClubActivity;
import com.dodomall.ddmall.ddui.activity.EconomicCourseActivity;
import com.dodomall.ddmall.ddui.activity.EconomicMasterStoryActivity;
import com.dodomall.ddmall.ddui.activity.EconomicTopNewsActivity;
import com.dodomall.ddmall.ddui.activity.IncomeRecordActivity;
import com.dodomall.ddmall.ddui.activity.MyFollowersActivity;
import com.dodomall.ddmall.ddui.activity.SecurityQuestionActivity;
import com.dodomall.ddmall.ddui.activity.SharePosterActivity;
import com.dodomall.ddmall.ddui.activity.TeamOrderActivity;
import com.dodomall.ddmall.ddui.bean.ActivityBannerItemBean;
import com.dodomall.ddmall.ddui.bean.DDHomeBanner;
import com.dodomall.ddmall.ddui.bean.UserAuthBean;
import com.dodomall.ddmall.ddui.service.HtmlService;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.module.page.WebViewActivity;
import com.dodomall.ddmall.module.user.UserInfoActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.AchievementBean;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.CashWithdrawManager;
import com.dodomall.ddmall.shared.util.ClipboardUtil;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.zhy.magicviewpager.transformer.AlphaPageTransformer;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * created by Jigsaw at 2019/1/17
 */
public class DDMasterCenterView extends FrameLayout {

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.tv_income_accept)
    TextView tvIncomeAccept;
    @BindView(R.id.tv_income_today)
    TextView tvIncomeToday;
    @BindView(R.id.tv_income_total)
    TextView tvIncomeTotal;
    @BindView(R.id.tv_income_sale)
    TextView tvIncomeSale;
    @BindView(R.id.tv_income_train)
    TextView tvIncomeTrain;
    @BindView(R.id.ll_income_container)
    LinearLayout mLlIncomeContainer;
    @BindView(R.id.ll_anchor)
    LinearLayout mLlAnchor;

    @BindView(R.id.activityBanner)
    ConvenientBanner mActivityBanner;

    private CashWithdrawManager mCashWithdrawManager;
    private Activity mContext;
    private UserAuthBean mUserAuthBean;
    private IUserService mUserService = ServiceManager.getInstance().createService(IUserService.class);

    public DDMasterCenterView(Context context) {
        super(context);
        initView();
    }

    public DDMasterCenterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public DDMasterCenterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    ArrayList<ActivityBannerItemBean> bannerItemBeans = new ArrayList<>();

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_master_center, this, true);
        ButterKnife.bind(this);

        //        mActivityBanner.setOnPageChangeListener(bannerChangeListener);
        mActivityBanner.setPageTransformer(new AlphaPageTransformer());
        mActivityBanner.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (bannerItemBeans == null) return;
                if (position < bannerItemBeans.size()) {
                    ActivityBannerItemBean banner = bannerItemBeans.get(position);
                    String event = banner.getActivityEventType();
                    String target = banner.getActivityActionUrl();
                    DDHomeBanner.process(getContext(), event, target);
                }
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getContext() instanceof Activity) {
            mContext = (Activity) getContext();
        }
    }

    public void renderAchievementView(AchievementBean result) {
        AchievementBean.ApiMyStatByRoleBeanBean bean = result.getApiMyStatByRoleBean();

        FrescoUtil.setImage(sdvAvatar, bean.getHeadImage());

        String nickName = bean.getNickNameLimit();
        String inviteCode = "邀请码 " + bean.getInviteCode();

        tvNickname.setText(nickName);
        tvInviteCode.setText(inviteCode);

        tvIncomeAccept.setText(ConvertUtil.cent2yuan2(bean.getBalance()));
        tvIncomeToday.setText(ConvertUtil.cent2yuan2(bean.getExpectSalePrizeTotal()));
        tvIncomeTotal.setText(ConvertUtil.cent2yuan2(bean.getIncomeTotal()));
        tvIncomeSale.setText(ConvertUtil.cent2yuan2(bean.getSalePrizeTotal()));
        tvIncomeTrain.setText(ConvertUtil.cent2yuan2(bean.getTrainingTotal()));

        setActivityBannerData(result.getActivityList());
    }

    public void setActivityBannerData(ArrayList<ActivityBannerItemBean> activitys) {
        bannerItemBeans = activitys;

        ArrayList<String> images = new ArrayList<>();
        for (ActivityBannerItemBean item : activitys) {
            images.add(item.getActivityPicUrl());
        }

        if (activitys != null && activitys.size() > 0) {
            //存在活动显示活动区域
            mActivityBanner.setVisibility(View.VISIBLE);
            mActivityBanner.setPages(new CBViewHolderCreator() {
                @Override
                public DDMasterCenterView.ImageViewHolder createHolder() {
                    return new DDMasterCenterView.ImageViewHolder();
                }
            }, images);
            mActivityBanner.stopTurning();
            mActivityBanner.setcurrentitem(0);
            if (images.size() > 1) {
                mActivityBanner.startTurning(5000)
                        .setPageIndicator(new int[]{
                                R.mipmap.ic_activity_indicator,
                                R.mipmap.ic_activity_indicator_focused})
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                        .setPointViewVisible(true)
                        .setCanLoop(true);
            } else {
                mActivityBanner.setPointViewVisible(false).setCanLoop(false);
            }
        } else {
            //不存在活动则隐藏
            mActivityBanner.setVisibility(View.GONE);
        }
    }

    public class ImageViewHolder implements Holder<String> {

        SimpleDraweeView bannerImageView = null;

        public ImageViewHolder() {

        }

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_home_banner, null);
            bannerImageView = view.findViewById(R.id.bannerImageView);
            ViewGroup.LayoutParams layoutParams = bannerImageView.getLayoutParams();
            if (layoutParams instanceof RelativeLayout.LayoutParams) {
                ((RelativeLayout.LayoutParams) layoutParams).leftMargin = ConvertUtil.dip2px(12);
                ((RelativeLayout.LayoutParams) layoutParams).rightMargin = ConvertUtil.dip2px(12);
                bannerImageView.setLayoutParams(layoutParams);
            }
            return view;
        }

        @Override
        public void UpdateUI(Context context, int position, String data) {
            if (!TextUtils.isEmpty(data)) {
                bannerImageView.setImageURI(data);
            }
        }
    }

    // 立即提现
    @OnClick(R.id.tv_btn_cash_withdraw)
    public void onCashWithdrawClicked() {
        getCashWithdrawManager().checkCanWithdraw(new CashWithdrawManager.OnCheckListener() {
            @Override
            public void onCheckSuccess(UserAuthBean userAuthBean) {
                mUserAuthBean = userAuthBean;
                mContext.startActivity(new Intent(mContext, CashWithdrawActivity.class));
            }

            @Override
            public void onCheckFailed(UserAuthBean userAuthBean) {
                mUserAuthBean = userAuthBean;
            }
        });
    }

    @OnClick({R.id.ll_master_follower, R.id.ll_master_team_order, R.id.ll_master_income_record, R.id.ll_master_referrer})
    public void onDataStatisticsClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_master_follower:
                // 我的粉丝
                intent.setClass(getContext(), MyFollowersActivity.class);
                break;
            case R.id.ll_master_team_order:
                // 团队订单
                intent.setClass(getContext(), TeamOrderActivity.class);
                break;
            case R.id.ll_master_income_record:
                // 收益记录
                intent.setClass(getContext(), IncomeRecordActivity.class);
                break;
            case R.id.ll_master_referrer:
                // 我的分享人
                intent.setClass(getContext(), UserInfoActivity.class);
                break;
        }
        startActivity(intent);
    }

    @OnClick({R.id.tv_btn_copy,})
    public void onCopyClicked(View view) {
        ClipboardUtil.setPrimaryClip(SessionUtil.getInstance().getLoginUser().invitationCode);
        ToastUtil.success("复制成功");
    }

    @OnClick({R.id.iv_share_gift, R.id.iv_invitation, R.id.iv_page})
    void onThreeBlackTabClick(View v) {
        final int type;
        switch (v.getId()) {
            case R.id.iv_share_gift:
                // 分享升级礼包
                WebViewActivity.jumpUrl(getContext(), HtmlService.MASTER_SHARE_PACKAGE);
                return;
            case R.id.iv_invitation:
                // 邀请下载app
                WebViewActivity.jumpUrl(getContext(), HtmlService.MASTER_INVITATION);
                return;
            case R.id.iv_page:
            default:
                // 定制专属海报
                type = SharePosterActivity.PosterType.POSTER;
        }

        //判断是否有读取用户信息权限
        RxPermissions rxPermissions = new RxPermissions(mContext);
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onCompleted() {
                        DLog.i("STORAGE Permission.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.e("STORAGE Permission.onError:" + e.getMessage());
                        ToastUtil.error("申请存储失败，请前往APP应用设置中打开此权限");
                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            jumpSharePoster(type);
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtil.error("缺少存储权限，请同意存储权限的申请");
                        } else {
                            ToastUtil.error("缺少存储权限，请前往APP应用设置中打开此权限");
                        }
                    }
                });
    }


    @OnClick(R.id.tv_material_enter)
    void onMaterialClick() {
        // 发圈素材
        startActivity(new Intent(mContext, DDCommunityActivity.class));
    }

    void jumpSharePoster(int type) {
        SharePosterActivity.jumpSharePoster(getContext(), type);
    }

    @OnClick({R.id.tv_economic_all, R.id.ll_master_course_newer, R.id.ll_master_course_master, R.id.ll_master_story, R.id.ll_master_news})
    public void onEconomicClubItemClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_economic_all:
                // 商学院 查看全部
                intent.setClass(mContext, EconomicClubActivity.class);
                break;
            case R.id.ll_master_course_newer:
                // 新手学堂
                intent.setClass(mContext, EconomicCourseActivity.class);
                break;
            case R.id.ll_master_course_master:
                // 店主进阶
                intent.setClass(mContext, EconomicCourseActivity.class);
                intent.putExtra(Constants.Extras.TAB_INDEX, 1);
                break;
            case R.id.ll_master_story:
                // 店主故事
                intent.setClass(mContext, EconomicMasterStoryActivity.class);
                break;
            case R.id.ll_master_news:
                // 店多多头条
                intent.setClass(mContext, EconomicTopNewsActivity.class);
                break;
        }
        startActivity(intent);
    }

    @OnClick({R.id.ll_master_auth, R.id.ll_master_bank, R.id.ll_master_password, R.id.ll_master_security})
    public void onAuthAndSecurityItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_master_auth:
                // 实名认证
                Intent intent = new Intent();
                intent.setClass(mContext, AuthActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_master_bank:
                // 银行卡信息
                //进入绑定银行卡之前检查是否实名认证
                checkAuth(new CheckAuthListener() {
                    @Override
                    public void onPass(UserAuthBean result) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(mContext, BankCardActivity.class));
                        intent.putExtra(Constants.Extras.HAS_SET_QUESTION, result.isSetSecurityQuestion());
                        startActivity(intent);
                    }

                    @Override
                    public void onNoPass() {
                        ToastUtil.error("绑卡前请先进行实名认证哦~");
                    }
                });
                break;
            case R.id.ll_master_password:
                // 交易密码
                //进入交易密码之前检查是否实名认证
                checkAuth(new CheckAuthListener() {
                    @Override
                    public void onPass(UserAuthBean result) {
                        SecurityQuestionActivity.jumpPassword(mContext);
                    }

                    @Override
                    public void onNoPass() {
                        ToastUtil.error("设置交易密码前请先进行实名认证哦~");
                    }
                });
                return;
            case R.id.ll_master_security:
                // 安全问题
                //进入安全问题之前检查是否实名认证
                checkAuth(new CheckAuthListener() {
                    @Override
                    public void onPass(UserAuthBean result) {
                        SecurityQuestionActivity.jumpQuestion(mContext);
                    }

                    @Override
                    public void onNoPass() {
                        ToastUtil.error("设置安全问题前请先进行实名认证哦~");
                    }
                });

                return;
        }
    }

    /**
     * 检查是否已通过实名认证
     */
    public void checkAuth(final CheckAuthListener listener) {
        APIManager.startRequest(mUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>() {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                // 0:未认证;1:认证申请;2:认证通过;3:驳回重申;4:认证拒绝
                int status = result.getAuthStatus();
                if (status == 2) {
                    listener.onPass(result);
                } else {
                    listener.onNoPass();
//                    D3ialogTools.showAuthDialog(getContext(), status);
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
            }
        });
    }

    private void startActivity(Intent intent) {
        mContext.startActivity(intent);
    }

    private CashWithdrawManager getCashWithdrawManager() {
        if (null == mCashWithdrawManager) {
            mCashWithdrawManager = new CashWithdrawManager(mContext);
        }
        return mCashWithdrawManager;
    }

    public interface CheckAuthListener {

        void onPass(UserAuthBean result);

        void onNoPass();

    }

}
