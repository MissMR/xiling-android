package com.xiling.ddmall.ddui.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.FollowerAdapter;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.custom.DDUserAchievementDialog;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.bean.FansBean;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IMasterCenterService;
import com.xiling.ddmall.shared.util.EmptyViewUtils;
import com.google.gson.internal.LinkedTreeMap;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/20
 * 粉丝列表
 */
public class MyFollowersFragment extends DDListFragment<FansBean> {

    private static final String ARG_TYPE = "type";

    // type 类型 1:全部;2:直属粉丝;3:团队粉丝;
    private int mType = 1;
    private int mRoleType = IMasterCenterService.ROLE_ALL;
    private String mReferrerName = "";
    private String mFansCount = "";
    private FollowerExtendReceiver mFollowerExtendReceiver;

    public void setFollowerExtendReceiver(FollowerExtendReceiver followerExtendReceiver) {
        mFollowerExtendReceiver = followerExtendReceiver;
    }

    public MyFollowersFragment() {
    }

    public static MyFollowersFragment newInstance(int type) {
        MyFollowersFragment fragment = new MyFollowersFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
        setEnableLazyLoad(true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                new DDUserAchievementDialog(getActivity()).show(mAdapter.getItem(position).getId());
            }
        });

        View emptyView = EmptyViewUtils.getAdapterEmptyView(getContext());
        emptyView.findViewById(R.id.tv_desc).setVisibility(View.GONE);
        emptyView.findViewById(R.id.iv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jumpUrl(getContext(), HtmlService.MASTER_SHARE_PACKAGE);
            }
        });

        mAdapter.setEmptyView(emptyView);
        mAdapter.getEmptyView().setVisibility(View.GONE);
    }

    @Override
    protected void onRequestSuccess(ListResultBean<FansBean> resultBean) {
        super.onRequestSuccess(resultBean);
        try {
            LinkedTreeMap<String, Object> map = (LinkedTreeMap<String, Object>) resultBean.getEx();
            if (map.containsKey("referrerName")) {
                mReferrerName = (String) map.get("referrerName");
            }
            if (map.containsKey("fansCount")) {
                mFansCount = String.valueOf(map.get("fansCount"));
                mFansCount = mFansCount.contains(".0") ? mFansCount.replace(".0", "") : mFansCount;
            }

            if (mFollowerExtendReceiver != null) {
                mFollowerExtendReceiver.onFollowerExtendReceive(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFilterRole(int roleType) {
        mRoleType = roleType;
    }

    public String getReferrerName() {
        return mReferrerName;
    }

    public String getFansCount() {
        return mFansCount;
    }

    @Override
    protected Observable<RequestResult<ListResultBean<FansBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IMasterCenterService.class).getFans(mPage, mSize, mType, mRoleType);
    }

    @Override
    protected BaseQuickAdapter<FansBean, BaseViewHolder> getBaseQuickAdapter() {
        return new FollowerAdapter();
    }

    public interface FollowerExtendReceiver {
        void onFollowerExtendReceive(MyFollowersFragment followersFragment);
    }
}