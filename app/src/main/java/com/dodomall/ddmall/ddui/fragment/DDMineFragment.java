package com.dodomall.ddmall.ddui.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDCouponActivity;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.activity.MessageGroupActivity;
import com.dodomall.ddmall.ddui.activity.UserSettingsActivity;
import com.dodomall.ddmall.ddui.bean.UnReadMessageCountBean;
import com.dodomall.ddmall.ddui.custom.NestScrollView;
import com.dodomall.ddmall.ddui.service.HtmlService;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.UITools;
import com.dodomall.ddmall.module.collect.CollectListActivity;
import com.dodomall.ddmall.module.community.BetterPtrClassicFrameLayout;
import com.dodomall.ddmall.module.foot.FootListActivity;
import com.dodomall.ddmall.module.notice.NoticeListActivity;
import com.dodomall.ddmall.module.order.NewRefundsOrderListActivity;
import com.dodomall.ddmall.module.order.OrderListActivity;
import com.dodomall.ddmall.module.page.WebViewActivity;
import com.dodomall.ddmall.module.user.FamilyActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.MyStatus;
import com.dodomall.ddmall.shared.bean.Order;
import com.dodomall.ddmall.shared.bean.OrderCount;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.component.ItemWithIcon;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMessageService;
import com.dodomall.ddmall.shared.service.contract.IOrderService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * A simple {@link Fragment} subclass.
 */
public class DDMineFragment extends BaseFragment implements NestScrollView.OnScrollChangedCallback {

    @BindView(R.id.refreshLayout)
    BetterPtrClassicFrameLayout mPullRefresh;

    @BindView(R.id.mainScrollView)
    NestScrollView mainScrollView = null;

    @BindView(R.id.avatarIv)
    SimpleDraweeView mAvatarIv;

    @BindView(R.id.username)
    TextView mNicknameTv;

    @BindView(R.id.userRoleImageView)
    ImageView userRoleImageView;

    @BindView(R.id.joinImageView)
    ImageView joinImageView;

    IUserService mUserService = null;
    User mUser = null;

    IOrderService mOrderService = null;

    @BindView(R.id.waitPayItem)
    ItemWithIcon waitPayItem;

    @BindView(R.id.waitSendItem)
    ItemWithIcon waitSendItem;

    @BindView(R.id.waitReceiveItem)
    ItemWithIcon waitReceiveItem;

//    @BindView(R.id.waitCommitItem)
//    ItemWithIcon waitCommitItem;

    @BindView(R.id.waitServiceItem)
    ItemWithIcon waitServiceItem;

    @BindView(R.id.inviteItem)
    ItemWithIcon inviteItem;

    @BindView(R.id.followItem)
    ItemWithIcon followItem;

    @BindView(R.id.couponItem)
    ItemWithIcon couponItem;

    @BindView(R.id.messageItem)
    ItemWithIcon messageItem;

    @BindView(R.id.topBarMaskView)
    View topBarMaskView;//顶部导航栏遮罩

    @BindView(R.id.titleBarAvatarIv)
    SimpleDraweeView titleBarAvatarIv;

    @BindView(R.id.titleBarUserName)
    TextView titleBarUserName;//顶部导航栏的用户昵称

    @BindView(R.id.layout_free_buy)
    LinearLayout freeBuyLayout;

    @OnClick(R.id.sd_free_buy)
    void onFreeBuyPressed() {
        if (mUser != null && !TextUtils.isEmpty(mUser.activitySpuId)) {
            DDProductDetailActivity.start(getContext(), mUser.activitySpuId);
        } else {
            DLog.i("数据异常，点击无效");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ddmine, container, false);
        ButterKnife.bind(this, view);

        if (SessionUtil.getInstance().isLogin()) {
            mUser = SessionUtil.getInstance().getLoginUser();
            setUserData();
        } else {
            EventBus.getDefault().post(new EventMessage(Event.viewHome));
        }

        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);

        //设置拉动事件监听
        mainScrollView.setOnScrollChangedCallback(this);

        mPullRefresh.setPtrHandler(new PtrHandler() {
            //需要加载数据时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                onRefresh();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        initTitleBarValue();
        return view;
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

    /**
     * 用户设置被点击
     * <p>
     * 跳转到新的用户设置界面
     */
    @OnClick(R.id.title_bar_settings)
    void onSettingPressed() {
        startActivity(new Intent(getContext(), UserSettingsActivity.class));
    }

    /**
     * 成为店主被点击
     */
    @OnClick(R.id.joinImageView)
    void onJoinPressed() {
        HtmlService.startBecomeStoreMasterActivity(getActivity());
    }

    @OnClick({R.id.orderHeaderPanel, R.id.waitPayItem, R.id.waitSendItem, R.id.waitReceiveItem, R.id.waitServiceItem})
    void onOrderPressed(View view) {
        if (view.getId() == R.id.waitServiceItem) {
            startActivity(new Intent(getContext(), NewRefundsOrderListActivity.class));
        } else {
            Intent intent = new Intent(getContext(), OrderListActivity.class);
            switch (view.getId()) {
                case R.id.orderHeaderPanel:
                default:
                    intent.putExtra("type", Order.ORDER_ALL);
                    break;
                case R.id.waitPayItem:
                    intent.putExtra("type", Order.ORDER_WAIT_PAY);
                    break;
                case R.id.waitSendItem:
                    intent.putExtra("type", Order.ORDER_PAID);
                    break;
                case R.id.waitReceiveItem:
                    intent.putExtra("type", Order.ORDER_DISPATCHED);
                    break;
//                case R.id.waitCommitItem:
//                    intent.putExtra("type", Order.ORDER_WAIT_COMMENT);
//                    break;
            }
            startActivity(intent);
        }
    }

    @OnClick({R.id.inviteItem, R.id.followItem, R.id.couponItem, R.id.messageItem, R.id.collectPanel,
            R.id.historyPanel, R.id.vipPanel, R.id.noticePanel, R.id.protocolPanel, R.id.aboutUsPanel,
            R.id.guidePanel, R.id.questionPanel})
    void onMineMenuPressed(View view) {
        switch (view.getId()) {
            case R.id.inviteItem:
                HtmlService.startInviteActivity(getActivity());
                break;
            case R.id.followItem:
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(Constants.Extras.WEB_URL, HtmlService.FOLLOW_US)
                );
                break;
            case R.id.couponItem:
                startActivity(new Intent(getContext(), DDCouponActivity.class));
                break;
            case R.id.messageItem:
                startActivity(new Intent(getContext(), MessageGroupActivity.class));
                break;
            case R.id.collectPanel:
                startActivity(new Intent(getContext(), CollectListActivity.class));
                break;
            case R.id.historyPanel:
                startActivity(new Intent(getContext(), FootListActivity.class));
                break;
            case R.id.vipPanel:
                startActivity(new Intent(getContext(), FamilyActivity.class));
                break;
            case R.id.noticePanel:
                startActivity(new Intent(getContext(), NoticeListActivity.class));
                break;
            case R.id.protocolPanel:
                startActivity(new Intent(getActivity(), WebViewActivity.class)
                        .putExtra(Constants.Extras.WEB_URL, HtmlService.PRIVACY_PROTOCOL));
                break;
            case R.id.aboutUsPanel:
                // 关于店多多
                WebViewActivity.jumpUrl(getActivity(), HtmlService.ABOUT_DODOMALL);
                break;
            case R.id.guidePanel:
                // 新手指引
                WebViewActivity.jumpUrl(getActivity(), HtmlService.GUIDE);
                break;
            case R.id.questionPanel:
                // 常见问题
                WebViewActivity.jumpUrl(getActivity(), HtmlService.QUESTION);
                break;

        }
    }

    public void onRefresh() {
        loadNetData();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUser != null && SessionUtil.getInstance().isLogin()) {
            loadOrderStats();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUserInfo(EventMessage message) {
        switch (message.getEvent()) {
            case updateAvatar:
                String avatarUrl = String.valueOf(message.getData());
                FrescoUtil.setImage(mAvatarIv, avatarUrl);
                FrescoUtil.setImage(titleBarAvatarIv, avatarUrl);
                break;
            case updateNickname:
                mUser = SessionUtil.getInstance().getLoginUser();
                String nickName = mUser.getNickNameLimit();
                mNicknameTv.setText(nickName);
                titleBarUserName.setText(nickName);
                break;
            case loginSuccess:
                onRefresh();
                break;
            case paySuccess:
                loadOrderStats();
                break;
            default:
                break;
        }
    }

    /**
     * 加载网络数据
     */
    void loadNetData() {
        APIManager.startRequest(mUserService.getUserInfo(), new BaseRequestListener<User>() {
            @Override
            public void onSuccess(User user) {
                SessionUtil.getInstance().setLoginUser(user);
                mUser = user;
                setUserData();
                loadUnReadMsgCount();
                loadOrderStats();
            }
        });
    }

    /**
     * 获取未读消息数量
     */
    void loadUnReadMsgCount() {
        IMessageService messageService = ServiceManager.getInstance().createService(IMessageService.class);
        APIManager.startRequest(messageService.getUnReadCount(), new BaseRequestListener<UnReadMessageCountBean>() {
            @Override
            public void onSuccess(UnReadMessageCountBean result) {
                super.onSuccess(result);
                if (result != null) {

                    DLog.i("DDMineFragment.getNum:" + result.getNum());

                    MyStatus status = new MyStatus();
                    status.messageCount = result.getNum();
                    EventBus.getDefault().post(status);
                }
            }
        });
    }

    void loadOrderStats() {
        APIManager.startRequest(mOrderService.getOrderCount(), new BaseRequestListener<OrderCount>() {

            @Override
            public void onSuccess(OrderCount orderCount) {
                waitPayItem.setBadge(orderCount.waitPay);
                waitSendItem.setBadge(orderCount.waitShip);
                waitReceiveItem.setBadge(orderCount.hasShip);
//                waitCommitItem.setBadge(orderCount.waitComment);
                waitServiceItem.setBadge(orderCount.afterSales);
//                waitPayItem.setBadge(1);
//                waitSendItem.setBadge(0);
//                waitReceiveItem.setBadge(10);
//                waitCommitItem.setBadge(999);
//                waitServiceItem.setBadge(3000);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mPullRefresh.refreshComplete();
            }
        });
    }

    /**
     * 设置用户数据到界面
     */
    void setUserData() {
        //设置头像
        SessionUtil.getInstance().setLoginUser(mUser);

        FrescoUtil.setImage(mAvatarIv, mUser.avatar);
        FrescoUtil.setImage(titleBarAvatarIv, mUser.avatar);

        //设置昵称
        mNicknameTv.setText(mUser.getNickNameLimit());
        titleBarUserName.setText(mUser.getNickNameLimit());

        //检查用户是否是店主
        int vipType = mUser.vipType;
        updateUserMaster(vipType > 0);

        //更新是否显示零元购入口
        freeBuyLayout.setVisibility(mUser.isShowFreeBuy() ? View.VISIBLE : View.GONE);
    }

    private void updateUserMaster(boolean isMaster) {
        userRoleImageView.setBackgroundResource(isMaster ? R.mipmap.icon_user_role_vip : R.mipmap.icon_user_role_normal);
        //店主隐藏加入按钮
        joinImageView.setVisibility(isMaster ? View.GONE : View.VISIBLE);
    }

    public void initTitleBarValue() {
        int statusBarHeight = UITools.getStatusBarHeight(getContext());
        float topHeight = getContext().getResources().getDimension(R.dimen.dd_mine_top_height);
        titleBarHeight = topHeight + statusBarHeight;
        //用户昵称的中心线
//        nameViewCenter = mNicknameTv.getY() + mNicknameTv.getBaseline();
        nameViewCenter = ConvertUtil.dip2px(82);
    }

    float titleBarHeight = 0;
    float nameViewCenter = 0;

    @Override
    public void onScroll(int dx, int dy) {
        //解决滑动冲突
        mPullRefresh.setEnabled(dy == 0);

        float alpha = 0;
        if (dy > titleBarHeight) {
            //标题栏完全显示
            alpha = 1;
        } else {
            if (dy == 0) {
                //隐藏标题栏颜色
                alpha = 0;
            } else {
                //修改状态栏颜色透明度
                alpha = (float) ((dy * 1.0) / titleBarHeight);
            }
        }

        topBarMaskView.setAlpha(alpha);

        DLog.i("check:" + dy + ">>" + nameViewCenter);
        if (dy < nameViewCenter) {
            DLog.i("显示内容的昵称");
            mNicknameTv.setVisibility(View.VISIBLE);
            titleBarUserName.setVisibility(View.INVISIBLE);

            mAvatarIv.setVisibility(View.VISIBLE);
            titleBarAvatarIv.setVisibility(View.INVISIBLE);

        } else {
            DLog.i("显示标题的昵称");
            titleBarUserName.setVisibility(View.VISIBLE);
            mNicknameTv.setVisibility(View.INVISIBLE);

            titleBarAvatarIv.setVisibility(View.VISIBLE);
            mAvatarIv.setVisibility(View.INVISIBLE);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MyStatus status) {
        DLog.i("onEvent+MyStatus");
        if (status != null) {
            messageItem.setBadge(status.messageCount);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBecomeMasterEvent(EventMessage eventMessage) {
        DLog.i("onBecomeMasterEvent");
        if (eventMessage.getEvent().equals(Event.becomeStoreMaster)) {
            updateUserMaster(true);
        }
    }


}
