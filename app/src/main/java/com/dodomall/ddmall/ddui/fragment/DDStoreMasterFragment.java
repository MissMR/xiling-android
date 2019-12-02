package com.dodomall.ddmall.ddui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.adapter.DDCommunityDataAdapter;
import com.dodomall.ddmall.ddui.bean.CommunityDataBean;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.UIEvent;
import com.dodomall.ddmall.ddui.custom.DDMasterCenterView;
import com.dodomall.ddmall.ddui.service.ICommunityService;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.AchievementBean;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.MainAdView;
import com.dodomall.ddmall.shared.component.dialog.WJDialog;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMasterCenterService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.RvUtils;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 店主中心
 */
public class DDStoreMasterFragment extends BaseFragment {

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.iv_master_invitation_region)
    ImageView mIvMasterInvitationRegion;
    @BindView(R.id.fl_toolbar)
    FrameLayout mFlToolbar;
    @BindView(R.id.tv_toolbar_nickname)
    TextView mTvToolbarNickname;

    private DDMasterCenterView mDDMasterCenterView;

    private IUserService mUserService = null;
    private IMasterCenterService mIMasterCenterService;
    private ICommunityService mCommunityService;

    private Unbinder mUnbinder;

    private DDCommunityDataAdapter mCommunityDataAdapter;

    private int mMaterialPage = 1;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ddstore_master, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initData();
        initView();

        return view;
    }

    private void initData() {
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mIMasterCenterService = ServiceManager.getInstance().createService(IMasterCenterService.class);
        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
    }

    private void initView() {

        mDDMasterCenterView = new DDMasterCenterView(getActivity());
        //设置拉动事件监听

        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mMaterialPage = 1;
                mSmartRefreshLayout.setNoMoreData(false);
                mCommunityDataAdapter.getItems().clear();
                loadNetData();
            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mMaterialPage++;
                getMaterialList();
            }
        });
        mSmartRefreshLayout.setEnableLoadMore(true);

        mRecyclerView.setFocusable(false);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mCommunityDataAdapter = new DDCommunityDataAdapter(getActivity());
        mCommunityDataAdapter.setHeader(mDDMasterCenterView);
        mRecyclerView.setAdapter(mCommunityDataAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int verticalOffset = mRecyclerView.computeVerticalScrollOffset();
                onScroll(verticalOffset);
//                DLog.i("onScrolled verticalOffset:" + verticalOffset);
            }
        });

        RvUtils.clearItemAnimation(mRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isAdded() && SessionUtil.getInstance().isLogin()) {
                loadNetData();
            }
        }
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

    void loadNetData() {
        if (!SessionUtil.getInstance().isLogin()) {
            return;
        }
        getAchievement();
        getMaterialList();
    }

    private void getMaterialList() {
        APIManager.startRequest(mCommunityService.getInvitationMaterialList(mMaterialPage, 10),
                new BaseRequestListener<ListResultBean<CommunityDataBean>>(getActivity()) {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(ListResultBean<CommunityDataBean> result) {
                        super.onSuccess(result);
                        finishRefresh();
                        if (result.getDatas().size() > 0) {
                            mCommunityDataAdapter.addItems(result.getDatas());
                        } else {
                            if (mMaterialPage > 1) {
                                mSmartRefreshLayout.setNoMoreData(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        finishRefresh();
                        if (mMaterialPage > 1) {
                            mMaterialPage--;
                        }
                    }
                });
    }

    private void getAchievement() {
        User user = SessionUtil.getInstance().getLoginUser();
        APIManager.startRequest(mIMasterCenterService.getMemberAchievementDetail(user.id), new BaseRequestListener<AchievementBean>() {
            @Override
            public void onSuccess(AchievementBean result) {
                super.onSuccess(result);
                finishRefresh();
                mDDMasterCenterView.renderAchievementView(result);
                setToolbarNickname(result);
                if (checkShowActivityDialog(result)) {
                    initActivityDialog(result);
                }
            }
        });
    }

    private void initActivityDialog(AchievementBean activityBean) {
        final WJDialog activityDialog = new WJDialog(getActivity());
        MainAdView activityDialogView = MainAdView.create(getActivity(), activityBean.getActivityDialogImgURL(),
                activityBean.getActivityDialogActionURL());
        activityDialogView.setCloseClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityDialog.dismiss();
            }
        });
        activityDialog.show();
        activityDialog.setContentView(activityDialogView);
    }

    private boolean checkShowActivityDialog(AchievementBean activity) {
        return activity.isShowActivityDialog();
    }

    private void setToolbarNickname(AchievementBean bean) {
        if (bean == null || bean.getApiMyStatByRoleBean() == null) {
            return;
        }
        mTvToolbarNickname.setText(bean.getApiMyStatByRoleBean().getNickNameLimit());
    }

    private void finishRefresh() {
        if (mMaterialPage > 1) {
            mSmartRefreshLayout.finishLoadMore();
        } else {
            mSmartRefreshLayout.finishRefresh();
        }
    }

    public void onScroll(int dy) {
        mFlToolbar.setVisibility(dy >= 240 ? View.VISIBLE : View.GONE);
        mIvMasterInvitationRegion.setVisibility(mLinearLayoutManager.findLastVisibleItemPosition() > 0 ? View.GONE : View.VISIBLE);
//        DLog.i("mLinearLayoutManager.findFirstVisibleItemPosition()" + mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @OnClick(R.id.iv_master_invitation_region)
    void onInvitationRegionClicked() {
        // 邀新专区 offset =  toolbarHeight + invitationRegionHeight
        mLinearLayoutManager.scrollToPositionWithOffset(1, ConvertUtil.dip2px(75 + 40));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UIEvent event) {
        UIEvent.Type type = event.getType();
        //需要重新刷新数据
        if (type == UIEvent.Type.CloseQuestionActivity ||
                type == UIEvent.Type.ClosePasswordActivity ||
                type == UIEvent.Type.CloseBindCardActivity) {
        } else if (type == UIEvent.Type.RefreshWithdraw) {
            getAchievement();
        }
    }


    private void showInvitationRegion(boolean isShow) {
        mIvMasterInvitationRegion.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

}
