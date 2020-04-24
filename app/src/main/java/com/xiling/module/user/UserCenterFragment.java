package com.xiling.module.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.R;
import com.xiling.ddui.activity.UserSettingsActivity;
import com.xiling.ddui.service.HtmlService;
import com.xiling.module.auth.AuthCardSuccssdActivity;
import com.xiling.module.auth.AuthIdentitySuccssdActivity;
import com.xiling.module.auth.AuthPhoneActivity;
import com.xiling.module.auth.Config;
import com.xiling.module.auth.SubmitStatusActivity;
import com.xiling.module.auth.event.MsgStatus;
import com.xiling.module.auth.model.AuthModel;
import com.xiling.module.auth.model.CardDetailModel;
import com.xiling.module.balance.BalanceListActivity;
import com.xiling.module.collect.CollectListActivity;
import com.xiling.module.coupon.CouponListActivity;
import com.xiling.module.foot.FootListActivity;
import com.xiling.module.message.MessageListActivity;
import com.xiling.module.notice.NoticeListActivity;
import com.xiling.module.order.NewRefundsOrderListActivity;
import com.xiling.module.order.OrderListActivity;
import com.xiling.module.page.WebViewActivity;
import com.xiling.module.point.PointListActivity;
import com.xiling.module.profit.ProfitListActivity;
import com.xiling.module.push.ProductPushActivity;
import com.xiling.module.qrcode.QrCodeShowActivity;
import com.xiling.module.store.MsgStore;
import com.xiling.module.store.StoreInfoActivity;
import com.xiling.module.store.StoreManageActivity;
import com.xiling.module.store.StoreSettingActivity;
import com.xiling.module.upgrade.UpgradeProgressActivity;
import com.xiling.module.user.model.SignModel;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.MemberStore;
import com.xiling.shared.bean.MyStatus;
import com.xiling.shared.bean.OrderCount;
import com.xiling.shared.bean.ProfitData;
import com.xiling.shared.bean.ScoreStat;
import com.xiling.shared.bean.User;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.ItemWithIcon;
import com.xiling.shared.component.dialog.WJDialog;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.UserService;
import com.xiling.shared.service.contract.IBalanceService;
import com.xiling.shared.service.contract.IOrderService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.FrescoUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserCenterFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.avatarIv)
    protected SimpleDraweeView mAvatarIv;

    @BindView(R.id.nicknameTv)
    protected TextView mNicknameTv;

    @BindView(R.id.pointTv)
    protected TextView mPointTv;

    @BindView(R.id.balanceTv)
    protected TextView mBalanceTv;

    @BindView(R.id.orderUnpayCmp)
    protected ItemWithIcon mOrderUnpayCmp;
    @BindView(R.id.orderPaidCmp)
    protected ItemWithIcon mOrderPaidCmp;
    @BindView(R.id.orderDispatchedCmp)
    protected ItemWithIcon mOrderDispatchedCmp;
    @BindView(R.id.orderCommentCmp)
    protected ItemWithIcon mOrderCommentCmp;
    @BindView(R.id.orderServiceCmp)
    protected ItemWithIcon mOrderServiceCmp;
    @BindView(R.id.linkFamilyCmp)
    protected ItemWithIcon mLinkFamilyCmp;
    @BindView(R.id.linkFamilyOrderCmp)
    protected ItemWithIcon mLinkFamilyOrderCmp;
    @BindView(R.id.linkQrCodeCmp)
    protected ItemWithIcon mLinkQrCodeCmp;
    @BindView(R.id.linkUpgradeProgressCmp)
    protected ItemWithIcon mLinkUpgradeProgressCmp;

    @BindView(R.id.levelTv)
    protected TextView mLevelTv;
    @BindView(R.id.ivSign)
    ImageView mIvSign;
    @BindView(R.id.linkVerifyCmp)
    ItemWithIcon mLinkVerifyCmp;
    @BindView(R.id.linkBindCardCmp)
    ItemWithIcon mLinkBindCardCmp;
    @BindView(R.id.layoutMoney)
    LinearLayout mLayoutMoney;
    @BindView(R.id.userHeaderLayout)
    RelativeLayout mUserHeaderLayout;
    @BindView(R.id.pointLayout)
    LinearLayout mPointLayout;
    @BindView(R.id.balanceLayout)
    LinearLayout mBalanceLayout;
    @BindView(R.id.viewMoreOrderLayout)
    LinearLayout mViewMoreOrderLayout;
    @BindView(R.id.linkCouponCmp)
    ItemWithIcon mLinkCouponCmp;
    @BindView(R.id.linkMessageCmp)
    ItemWithIcon mLinkMessageCmp;
    @BindView(R.id.linkFavCmp)
    ItemWithIcon mLinkFavCmp;
    @BindView(R.id.linkHistoryCmp)
    ItemWithIcon mLinkHistoryCmp;
    @BindView(R.id.linkPointCmp)
    ItemWithIcon mLinkPointCmp;
    @BindView(R.id.linkBindWePayCmp)
    ItemWithIcon mLinkBindWePayCmp;
    @BindView(R.id.linkSubscribeCmp)
    ItemWithIcon mLinkSubscribeCmp;
    @BindView(R.id.linkBindPhoneCmp)
    ItemWithIcon mLinkBindPhoneCmp;
    @BindView(R.id.linkLockQrCodeCmp)
    ItemWithIcon mLinkLockQrCodeCmp;
    @BindView(R.id.tvScore)
    TextView mTvScore;
    @BindView(R.id.layoutScore)
    LinearLayout mLayoutScore;
    @BindView(R.id.linkShipCmp)
    ItemWithIcon mLinkShipCmp;
    @BindView(R.id.linkStoreSettingCmp)
    ItemWithIcon mLinkStoreSettingCmp;
    @BindView(R.id.linkNotice)
    ItemWithIcon mLinkNotice;
    @BindView(R.id.linkPush)
    ItemWithIcon mLinkPush;

    private MyStatus mMyStatus;
    private IUserService mUserService;
    private IBalanceService mBalanceService;
    private IOrderService mOrderService;
    private User mUser;
    private MemberStore mMemberStore;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);

        if (SessionUtil.getInstance().isLogin()) {
            mUser = SessionUtil.getInstance().getLoginUser();
            setUserInfoView();
        } else {
            EventBus.getDefault().post(new EventMessage(Event.viewHome));
        }

        mRefreshLayout.setOnRefreshListener(this);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mBalanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isAdded() && SessionUtil.getInstance().isLogin()) {
                onRefresh();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(EventMessage message) {
        switch (message.getEvent()) {
            case updateAvatar:
                FrescoUtil.setImage(mAvatarIv, String.valueOf(message.getData()));
                break;
            case updateNickname:
                mNicknameTv.setText(String.valueOf(message.getData()));
                break;
            case loginSuccess:
                onRefresh();
                break;
            default:
                break;
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onRefresh() {
        reloadUserInfo();
        reloadBalance();
        reloadOrderStats();
        reloadMyStatus();
        reloadScore();
    }

    private void reloadStore() {
        mMemberStore = null;
        APIManager.startRequest(mUserService.getMemberStore(), new BaseRequestListener<MemberStore>(mRefreshLayout) {
            @Override
            public void onSuccess(MemberStore memberStore) {
                mMemberStore = memberStore;
                showEditStoreDialog();
            }
        });
    }

    private void showEditStoreDialog() {
//        if (mMemberStore.status != AppTypes.STORE.NO_SUBMIT) {
//            return;
//        }
//
//        final WJDialog wjDialog = new WJDialog(getContext());
//        wjDialog.show();
//        wjDialog.setContentText("您是店多多店主,请完善您的店铺信息");
//        wjDialog.setCancelText("取消");
//        wjDialog.setConfirmText("确定");
//        wjDialog.setOnConfirmListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wjDialog.dismiss();
//                goStoreSetting();
//            }
//        });
    }

    private void reloadScore() {
        APIManager.startRequest(mUserService.getScoreStat(), new BaseRequestListener<ScoreStat>(mRefreshLayout) {
            @Override
            public void onSuccess(ScoreStat result) {
                mTvScore.setText(result.memberScore.totalScore + "");
            }
        });
    }

    private void reloadMyStatus() {
        APIManager.startRequest(mUserService.getMyStatus(), new BaseRequestListener<MyStatus>(mRefreshLayout) {
            @Override
            public void onSuccess(MyStatus myStatus) {
                mMyStatus = myStatus;
                if (myStatus.bindBankStatus != AppTypes.CARD_STATUS.SUCESS) {
                    mLinkBindCardCmp.setBadge(" 未绑定 ");
                } else {
                    mLinkBindCardCmp.setBadge("");
                }
                mLinkCouponCmp.setBadge(myStatus.couponCount);
                mLinkMessageCmp.setBadge(myStatus.messageCount);
            }
        });
    }

    private void reloadUserInfo() {
        APIManager.startRequest(mUserService.getUserInfo(), new BaseRequestListener<User>(mRefreshLayout) {
            @Override
            public void onSuccess(User user) {
                SessionUtil.getInstance().setLoginUser(user);
                mUser = user;
                setUserInfoView();
//                if (user.isShowStoreView()) {
                reloadStore();
//                }
            }
        });
    }

    private void setUserInfoView() {
        mIvSign.setSelected(mUser.isSignIn == 1);
//        if (mUser.isStore != 0) {
//            mLevelTv.setText(mUser.storeTypeStr);
//        } else {
        mLevelTv.setText(mUser.vipTypeStr);
//        }
        mLevelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HtmlService.startBecomeStoreMasterActivity(getActivity());
            }
        });
        FrescoUtil.setImage(mAvatarIv, mUser.avatar);
        mNicknameTv.setText(mUser.nickname);


        mLinkPush.setVisibility(View.GONE);
        mLinkStoreSettingCmp.setVisibility(View.GONE);
        mLinkShipCmp.setVisibility(View.GONE);
        if (mUser.vipType >= AppTypes.FAMILY.MEMBER_TIYAN) {
            mLinkPush.setVisibility(View.VISIBLE);
        }
        if (mUser.isShowStoreView()) {
            mLinkPush.setVisibility(View.VISIBLE);
//            if (mUser.inviteMemberType == 0) {
//                mLinkShipCmp.setVisibility(View.VISIBLE);
//            }
        }
        if (mUser.vipType >= 3) {
            mLinkStoreSettingCmp.setVisibility(View.VISIBLE);
        }
        if (mUser.authStatus != AppTypes.AUTH_STATUS.SUCESS) {
            mLinkVerifyCmp.setBadge(" 未认证 ");
        } else {
            mLinkVerifyCmp.setBadge("");
        }

        mLinkPush.setVisibility(View.GONE);//美集推手
        mPointLayout.setVisibility(View.GONE);//累计货款
        mLinkFamilyOrderCmp.setVisibility(View.GONE);//积分订单
//        mBalanceLayout.setVisibility(View.VISIBLE);//余额
//        mLinkBindCardCmp.setVisibility(View.VISIBLE);//绑定银行卡
//        mLinkVerifyCmp.setVisibility(View.VISIBLE);//账户认证
    }

    private void reloadBalance() {
        APIManager.startRequest(mBalanceService.get(), new BaseRequestListener<ProfitData>(mRefreshLayout) {
            @Override
            public void onSuccess(ProfitData profitData) {
                mPointTv.setText(String.format("%s", ConvertUtil.cent2yuan(profitData.profitSumMoney)));
                mBalanceTv.setText(String.format("%s", ConvertUtil.cent2yuan(profitData.availableMoney)));
            }
        });
    }

    private void reloadOrderStats() {
       /* APIManager.startRequest(mOrderService.getOrderCount(), new BaseRequestListener<OrderCount>(mRefreshLayout) {

            @Override
            public void onSuccess(OrderCount orderCount) {
                mOrderCommentCmp.setBadge(orderCount.waitComment);
                mOrderDispatchedCmp.setBadge(orderCount.hasShip);
                mOrderPaidCmp.setBadge(orderCount.waitShip);
                mOrderUnpayCmp.setBadge(orderCount.waitPay);
                mOrderServiceCmp.setBadge(orderCount.afterSales);
            }
        });*/
    }

    /**
     * 账户认证
     */
    private void goAuth() {
        if (mUser == null) {
            reloadUserInfo();
            ToastUtil.error("等待数据");
            return;
        }
        UserService.checkHasPassword(getActivity(), new UserService.HasPasswordListener() {
            @Override
            public void onHasPassword() {
                switch (mUser.authStatus) {
                    case AppTypes.AUTH_STATUS.WAIT:
                        startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                        EventBus.getDefault().postSticky(new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_WAIT));
                        break;
                    case AppTypes.AUTH_STATUS.SUCESS:
                        startActivity(new Intent(getContext(), AuthIdentitySuccssdActivity.class));
                        break;
                    case AppTypes.AUTH_STATUS.FAIL:
                        APIManager.startRequest(mUserService.getAuth(), new BaseRequestListener<AuthModel>(mRefreshLayout) {
                            @Override
                            public void onSuccess(AuthModel model) {
                                startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                                MsgStatus msgStatus = new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_FAIL);
                                msgStatus.setTips(model.checkRemark);
                                EventBus.getDefault().postSticky(msgStatus);
                            }
                        });
                        break;
                    case AppTypes.AUTH_STATUS.NO_SUBMIT:
                        Intent intent2 = new Intent(getContext(), AuthPhoneActivity.class);
                        intent2.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_PHONE);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick(R.id.viewMoreOrderLayout)
    protected void viewOrderAllList() {
        Intent intent = new Intent(getContext(), OrderListActivity.class);
        intent.putExtra("type", "all");
        startActivity(intent);
    }

    @OnClick(R.id.orderUnpayCmp)
    protected void viewOrderUnpayList() {
        Intent intent = new Intent(getContext(), OrderListActivity.class);
        intent.putExtra("type", "wait-pay");
        startActivity(intent);
    }

    @OnClick(R.id.orderPaidCmp)
    protected void viewOrderPaidList() {
        Intent intent = new Intent(getContext(), OrderListActivity.class);
        intent.putExtra("type", "paid");
        startActivity(intent);
    }

    @OnClick(R.id.orderDispatchedCmp)
    protected void viewOrderDispatchedList() {
        Intent intent = new Intent(getContext(), OrderListActivity.class);
        intent.putExtra("type", "dispatched");
        startActivity(intent);
    }

    @OnClick(R.id.orderCommentCmp)
    protected void viewOrderWaitCommentList() {
        Intent intent = new Intent(getContext(), OrderListActivity.class);
        intent.putExtra("type", "wait-comment");
        startActivity(intent);
    }

    @OnClick(R.id.orderServiceCmp)
    protected void viewOrderServiceList() {
        startActivity(new Intent(getContext(), NewRefundsOrderListActivity.class));
    }

    @OnClick(R.id.userHeaderLayout)
    protected void viewProfile() {
//        startActivity(new Intent(getContext(), ProfileActivity.class));
        startActivity(new Intent(getContext(), UserSettingsActivity.class));
    }

    @OnClick(R.id.linkCouponCmp)
    protected void viewCouponList() {
        startActivity(new Intent(getContext(), CouponListActivity.class));
    }

    @OnClick(R.id.linkFavCmp)
    protected void viewCollect() {
        startActivity(new Intent(getContext(), CollectListActivity.class));
    }

    @OnClick(R.id.linkHistoryCmp)
    protected void viewFoot() {
        startActivity(new Intent(getContext(), FootListActivity.class));
    }

    @OnClick(R.id.pointLayout)
    protected void viewProfit() {
        startActivity(new Intent(getContext(), ProfitListActivity.class));
    }

    @OnClick(R.id.balanceLayout)
    protected void viewBalance() {
        startActivity(new Intent(getContext(), BalanceListActivity.class));
    }

    @OnClick(R.id.linkMessageCmp)
    protected void viewMessageList() {
        startActivity(new Intent(getContext(), MessageListActivity.class));
    }

    @OnClick(R.id.linkVerifyCmp)
    protected void viewVerifyCmp() {
        goAuth();
    }

    @OnClick(R.id.linkBindPhoneCmp)
    protected void viewBindPhone() {
        Intent intent = new Intent(getContext(), BindPhoneActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linkFamilyOrderCmp)
    protected void viewFamilyOrder() {
        Intent intent = new Intent(getContext(), FamilyOrderActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linkFamilyCmp)
    protected void viewFamily() {
        Intent intent = new Intent(getContext(), FamilyActivity.class);
        startActivity(intent);
    }


    @OnClick(R.id.linkQrCodeCmp)
    protected void viewQrCode() {
        HtmlService.startInviteActivity(getContext());
    }

    @OnClick(R.id.linkSubscribeCmp)
    protected void viewSubscribe() {
        startActivity(new Intent(getActivity(), WebViewActivity.class)
                .putExtra(Constants.Extras.WEB_URL, HtmlService.FOLLOW_US)
        );
    }

    @OnClick(R.id.layoutScore)
    protected void viewPointList() {
        startActivity(new Intent(getContext(), PointListActivity.class));
    }

    @OnClick(R.id.linkUpgradeProgressCmp)
    protected void viewUpgradeProgress() {
        startActivity(new Intent(getContext(), UpgradeProgressActivity.class));
    }

    @OnClick(R.id.linkBindWePayCmp)
    protected void bindWechat() {
        Intent intent = new Intent(getContext(), QrCodeShowActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, AppTypes.QRCODE.BIND_WECHAT);
        startActivity(intent);
    }

    @OnClick(R.id.linkNotice)
    protected void goNotice() {
        Intent intent = new Intent(getContext(), NoticeListActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linkShipCmp)
    protected void goShip() {
        Intent intent = new Intent(getContext(), StoreManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.linkPush)
    protected void goPush() {
        startActivity(new Intent(getContext(), ProductPushActivity.class));
    }

    @OnClick(R.id.linkStoreSettingCmp)
    protected void goStoreSetting() {
        if (mMemberStore == null) {
            ToastUtil.error("等待数据");
            return;
        }
        switch (mMemberStore.status) {
            case AppTypes.STORE.COMMON:
                getContext().startActivity(new Intent(getContext(), StoreInfoActivity.class));
                MsgStore msgStore = new MsgStore(MsgStore.ACTION_SEND_STORE_OBJ);
                msgStore.setMemberStore(mMemberStore);
                EventBus.getDefault().postSticky(msgStore);
                break;
            case AppTypes.STORE.CLOSE:
                ToastUtil.error("您不是店主");
                break;
            case AppTypes.STORE.WAIT:
                startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                EventBus.getDefault().postSticky(new MsgStatus(AppTypes.STATUS.WAIT_STORE));
                break;
            case AppTypes.STORE.FAIL:
                startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                EventBus.getDefault().postSticky(new MsgStatus(AppTypes.STATUS.FAIL_STORE));
                break;
            case AppTypes.STORE.NO_SUBMIT:
                getContext().startActivity(new Intent(getContext(), StoreSettingActivity.class));
                break;
            default:
                break;
        }
    }


    /**
     * 绑定银行卡
     */
    @OnClick(R.id.linkBindCardCmp)
    protected void bindCard() {
        if (mUser == null || mUser.authStatus != AppTypes.AUTH_STATUS.SUCESS) {
            ToastUtils.showShortToast("请先账户认证");
            return;
        }
        if (mMyStatus == null) {
            reloadMyStatus();
            return;
        }
        UserService.checkHasPassword(getActivity(), new UserService.HasPasswordListener() {
            @Override
            public void onHasPassword() {
                switch (mMyStatus.bindBankStatus) {
                    case AppTypes.CARD_STATUS.NO_SUBMIT:
                        Intent intent2 = new Intent(getContext(), AuthPhoneActivity.class);
                        intent2.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_CARD);
                        getContext().startActivity(intent2);
                        break;
                    case AppTypes.CARD_STATUS.WAIT:
                        startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                        EventBus.getDefault().postSticky(new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_CARD_WAIT));
                        break;
                    case AppTypes.CARD_STATUS.SUCESS:
                        startActivity(new Intent(getContext(), AuthCardSuccssdActivity.class));
                        break;
                    case AppTypes.CARD_STATUS.FAIL:
                        APIManager.startRequest(mUserService.getCard(), new BaseRequestListener<CardDetailModel>(mRefreshLayout) {
                            @Override
                            public void onSuccess(CardDetailModel model) {
                                startActivity(new Intent(getContext(), SubmitStatusActivity.class));
                                MsgStatus msgStatus = new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_CARD_FAIL);
                                msgStatus.setTips(model.checkResult);
                                EventBus.getDefault().postSticky(msgStatus);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @OnClick(R.id.linkeToBeShopkeeper)
    protected void linkeToBeShopkeeper() {
        HtmlService.startBecomeStoreMasterActivity(getActivity());
    }

    @OnClick(R.id.ivSign)
    public void onViewClicked() {
        if (mIvSign.isSelected()) {
            final WJDialog dialog = new WJDialog(getContext());
            dialog.show();
            dialog.hideCancelBtn();
            dialog.setContentText("今天已签到，明天再来吧");
            return;
        }
        APIManager.startRequest(mUserService.sign(), new BaseRequestListener<SignModel>(mRefreshLayout) {
            @Override
            public void onSuccess(SignModel data) {
                final WJDialog dialog = new WJDialog(getContext());
                dialog.show();
                dialog.hideCancelBtn();
                dialog.setContentText("签到成功，获得积分" + data.totalScore);
                dialog.setOnConfirmListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIvSign.setSelected(true);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStatusMsg(MsgStatus message) {
        switch (message.getAction()) {
            case MsgStatus.ACTION_CARD_CHANGE:
                reloadMyStatus();
                break;
            case MsgStatus.ACTION_USER_CHANGE:
                reloadUserInfo();
                break;
            case MsgStatus.ACTION_CANCEL_REFUNDS:
                reloadOrderStats();
                break;
            case MsgStatus.ACTION_STORE_CHANGE:
                reloadStore();
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void transferHandler(EventMessage message) {
        if (
                message.getEvent().equals(Event.cancelOrder)
                        || message.getEvent().equals(Event.refundOrder)
                        || message.getEvent().equals(Event.paySuccess)
                        || message.getEvent().equals(Event.refundOrder)
                        || message.getEvent().equals(Event.finishOrder)
                ) {
            reloadOrderStats();
        }

        if (message.getEvent().equals(Event.transferSuccess)) {
            reloadScore();
        } else if (message.getEvent().equals(Event.paySuccess)) {
            reloadUserInfo();
        }
    }
}
