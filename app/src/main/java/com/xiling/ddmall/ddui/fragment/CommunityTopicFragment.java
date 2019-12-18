package com.xiling.ddmall.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.CommunityTopicDetailActivity;
import com.xiling.ddmall.ddui.adapter.CommunityTopicAdapter;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.TopicBean;
import com.xiling.ddmall.ddui.bean.TopicFollowChangeEvent;
import com.xiling.ddmall.ddui.service.ICommunityService;
import com.xiling.ddmall.ddui.tools.DLog;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observable;

/**
 * created by Jigsaw at 2018/10/11
 */
public class CommunityTopicFragment extends DDListFragment<TopicBean> {

    private static final String ARG_ONLY_SHOW_FOLLOW_TOPIC = "show";


    private ICommunityService mCommunityService;
    // 是否显示 关注按钮
    private boolean isOnlyShowFollowTopic;
    private boolean isShowFollowView;

    public static CommunityTopicFragment newInstance(boolean isOnlyShowMyFollow) {
        CommunityTopicFragment fragment = new CommunityTopicFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_ONLY_SHOW_FOLLOW_TOPIC, isOnlyShowMyFollow);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isOnlyShowFollowTopic = getArguments().getBoolean(ARG_ONLY_SHOW_FOLLOW_TOPIC);
            isShowFollowView = !isOnlyShowFollowTopic;
        }
        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void init() {
        if (SessionUtil.getInstance().isLogin()) {
            getList();
        }
        if (isShowFollowView) {
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    if (!view.isSelected()) {
                        // 未关注 可点击关注
                        ToastUtil.showLoading(view.getContext());
                        followTopic(mAdapter.getItem(position).getTopicId(), position);
                    }
                }
            });
        }

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(view.getContext(), CommunityTopicDetailActivity.class)
                        .putExtra(Constants.Extras.TOPIC_ID, mAdapter.getItem(position).getTopicId()));
            }
        });

        setAdapterEmptyView();
    }

    private void setAdapterEmptyView() {
        if (isOnlyShowFollowTopic) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.cmp_no_data, null);
            TextView tvNoData = view.findViewById(R.id.noDataLabel);
            tvNoData.setText("亲，您还没有关注的话题哦");
            mAdapter.setEmptyView(view);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFollowStateChange(TopicFollowChangeEvent event) {
        if (event == null) {
            return;
        }

        if (checkRefreshAllTopicList()) {
            // 是关注列表的时候
            DLog.i("关注列表 刷新第一页数据");
            mPage = 1;
            mAdapter.getData().clear();
            mAdapter.notifyDataSetChanged();
            getList();
            return;
        } else if (checkRefreshTopicItem()) {
            // 话题列表
            int index = getTopicIndexOfList(event.topicId);
            DLog.i("话题列表 刷新topic item : " + index);
            if (index > -1) {
                updateFollowItemView(index, event.isFollowTopic);
            }
        }

    }

    private int getTopicIndexOfList(String topicId) {
        if (TextUtils.isEmpty(topicId)) {
            return -1;
        }
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            if (topicId.equals(mAdapter.getData().get(i).getTopicId())) {
                DLog.i("topic index : " + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * 收到关注change事件时，是否需要刷新列表
     *
     * @return
     */
    private boolean checkRefreshAllTopicList() {
        return isOnlyShowFollowTopic && !isResumed();
    }

    /**
     * 热门话题列表
     *
     * @return
     */
    private boolean checkRefreshTopicItem() {
        return !isOnlyShowFollowTopic && !isResumed();
    }

    @Override
    protected Observable<RequestResult<ListResultBean<TopicBean>>> getApiObservable() {
        return isOnlyShowFollowTopic ? mCommunityService.getTopicFollowedList(mPage, mSize, ICommunityService.TYPE_TOPIC_LIST_FOLLOW) :
                mCommunityService.getTopicList(mPage, mSize);
    }

    @Override
    protected BaseQuickAdapter<TopicBean, BaseViewHolder> getBaseQuickAdapter() {
        return new CommunityTopicAdapter(isShowFollowView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void followTopic(String topicId, final int position) {
        APIManager.startRequest(mCommunityService.updateTopicFollowState(topicId, ICommunityService.TYPE_TOPIC_FOLLOW),
                new BaseRequestListener<Object>(getActivity()) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        ToastUtil.success("您已成功关注该话题");
                        updateFollowItemView(position, true);
                        EventBus.getDefault().post(new TopicFollowChangeEvent(true));
                    }
                });
    }

    private void updateFollowItemView(int position, boolean isFollow) {
        mAdapter.getItem(position).setIsMyLike(isFollow ? 1 : 0);
        int offset = isFollow ? +1 : -1;
        mAdapter.getItem(position).setHotNum(mAdapter.getItem(position).getHotNum() + offset);
        mAdapter.notifyItemChanged(position);
    }


}
