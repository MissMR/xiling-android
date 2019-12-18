package com.xiling.ddmall.module.community;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.ExpandableTextView;
import com.xiling.ddmall.shared.component.dialog.CommentBottomSheetDialog;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICommunityService;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.ImgDownLoadUtils;
import com.xiling.ddmall.shared.util.RvUtils;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author Stone
 * @time 2017/12/26  10:42
 * @desc 对无忧圈进行重构, 这个fragment代表三个页面，
 * 分别是素材库，无忧圈和视频三个。
 */

public class GroupFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.pullRefresh)
    BetterPtrClassicFrameLayout mPullRefresh;
    /**
     * 入口类型
     */
    private CommunityType mCommunityType;
    /**
     * 分类id
     */
    private String mCategoryId = "";
    private CommunityAdapter mAdapter;
    /**
     * 页数
     */
    private int pageSize = 1;
    private String key = "";
    private int clickPosition = 0;
    private LinearLayoutManager manager;
    private boolean mFull;
    private CommunityMultiItem clickItem;
    private int clickDetailPosition;
    private ICommunityService mPageService;

    public enum CommunityType {
        /**
         * type of enter
         */
        TYPE_GROUP, TYPE_MATERIAL, TYPE_VIDEO
    }

    private boolean isShareCicle;

    public static Fragment instance(CommunityType enterType, String categoryId, boolean b) {
        GroupFragment groupFragment = new GroupFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("type", enterType);
        bundle.putString("categoryId", categoryId);
        groupFragment.setArguments(bundle);
        return groupFragment;
    }

    @Override
    protected int getFragmentResId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        mAdapter = new CommunityAdapter(null,false);
        RvUtils.configRecycleView(mActivity, mRecyclerView, mAdapter);
        //防止刷新闪屏
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        manager = RvUtils.getLayoutManager();
        mAdapter.setHeaderFooterEmpty(true, false);
    }

/*
    @Override
    protected void initDataNew() {
        super.initDataNew();
        //获取入口判断
        mCommunityType = (CommunityType) getArguments().getSerializable("type");
        //获取分类的id,二级列表需要
        mCategoryId = getArguments().getString("categoryId");
        //添加头部
        requestAddHeadView(mCommunityType);
        requestPageData();
    }
*/

    @Override
    protected void lazyLoadData() {
        super.lazyLoadData();
        mCommunityType = (CommunityType) getArguments().getSerializable("type");
        isShareCicle = mCommunityType == CommunityType.TYPE_GROUP;
        //获取分类的id,二级列表需要
        mCategoryId = getArguments().getString("categoryId");
        //添加头部
        requestAddHeadView(mCommunityType);
        requestPageData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCommunityType == CommunityType.TYPE_VIDEO) {
            GSYVideoManager.onResume();
        }
        //        pageSize = 1;
        //        requestPageData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                onCommonChildClick(baseQuickAdapter, view, i);
            }
        });
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                if (mCommunityType == CommunityType.TYPE_GROUP) {
                    CommunityMultiItem item = (CommunityMultiItem) baseQuickAdapter.getItem(i);
                    onFriendsItemClick(item, i);
                }
            }
        });

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageSize++;
                requestPageData();
            }
        }, mRecyclerView);

        mPullRefresh.setPtrHandler(new PtrHandler() {
            //需要加载数据时触发
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageSize = 1;
                requestPageData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mCommunityType == CommunityType.TYPE_VIDEO) {
                    scrollerReleaseVideo();
                }
            }
        });
    }


    private void scrollerReleaseVideo() {
        int firstVisibleItem, lastVisibleItem;
        firstVisibleItem = manager.findFirstVisibleItemPosition();
        lastVisibleItem = manager.findLastVisibleItemPosition();
        //大于0说明有播放
        if (GSYVideoManager.instance().getPlayPosition() >= 0) {
            //当前播放的位置
            int position = GSYVideoManager.instance().getPlayPosition();
            //对应的播放列表TAG
            if (GSYVideoManager.instance().getPlayTag().equals(CommunityAdapter.TAG)
                    && (position < firstVisibleItem || position > lastVisibleItem)) {
                //如果滑出去了上面和下面就是否，和今日头条一样
                if (!mFull) {
                    GSYVideoPlayer.releaseAllVideos();
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        mFull = newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser && mCommunityType == CommunityType.TYPE_VIDEO) {
            GSYVideoPlayer.releaseAllVideos();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCommunityType == CommunityType.TYPE_VIDEO) {
            GSYVideoPlayer.releaseAllVideos();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCommunityType == CommunityType.TYPE_VIDEO) {
            GSYVideoPlayer.releaseAllVideos();
        }
    }

    /**
     * item子控件点击时间
     *
     * @param baseQuickAdapter adapter
     * @param view             view
     * @param i                position
     */
    private void onCommonChildClick(BaseQuickAdapter baseQuickAdapter, View view, final int i) {
        final CommunityMultiItem item = (CommunityMultiItem) baseQuickAdapter.getItem(i);
        assert item != null;
        final MaterialVideoModule module = item.getContent();
        int itemViewType = item.getItemType();
        switch (view.getId()) {
            case R.id.save_tv:
                if (itemViewType == CommunityMultiItem.ITEM_TYPE_LINK) {
                    forwardLinkEvent(module);
                } else if (itemViewType == CommunityMultiItem.ITEM_TYPE_VIDEO) {
                    saveVideoEvent(module);
                } else if (itemViewType == CommunityMultiItem.ITEM_TYPE_TEXT) {
                    saveImgEvent(module);
                }
                break;
            case R.id.forward_tv:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                if (itemViewType == CommunityMultiItem.ITEM_TYPE_LINK) {
                    forwardLinkEvent(module);
                } else if (itemViewType == CommunityMultiItem.ITEM_TYPE_VIDEO) {
                    forWardVideoEvent(module);
                } else if (itemViewType == CommunityMultiItem.ITEM_TYPE_TEXT) {
                    forWardImgEvent(module);
                }
                break;
            case R.id.lease_msg_tv:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                final CommentBottomSheetDialog dialog = new CommentBottomSheetDialog(mActivity);
                dialog.setSubmitListener(new CommentBottomSheetDialog.OnSubmitListener() {
                    @Override
                    public void submit(String content) {
                        dialog.dismiss();
                        requestAddComment(content, module.getTopicId(), item, i);
                    }
                });
                dialog.show("发送", "");
                break;
            case R.id.like_layout:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                clickPosition = i;
                int likeStatus = module.getLikeStatus();
                if (likeStatus == 0) {
                    requestAddLike(module.getTopicId(), item);
                } else {
                    requestRemoveLike(module.getTopicId(), item);
                }
                break;
            case R.id.item_link_layout:
                EventUtil.compileEvent(mActivity, "link", item.getContent().getLinkUrl());
                break;
            case R.id.id_expand_textview:
                ExpandableTextView tvContent = (ExpandableTextView) mAdapter.getViewByPosition(i+mAdapter.getHeaderLayoutCount(),R.id.user_send_content_tv);
                if (tvContent != null) {
                    tvContent.onTvExpandClick();
                }
                break;
            default:
        }
    }

    /**
     * 添加评论
     *
     * @param content  内容
     * @param topicId  topId
     * @param item     ITEM
     * @param position position
     */
    private void requestAddComment(String content, String topicId, final CommunityMultiItem item, final int position) {
        APIManager.startRequest(mPageService.addGroupTopicComment(topicId, content), new RequestListener<MaterialVideoModule.CommentModule>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(getActivity());
            }
            @Override
            public void onSuccess(MaterialVideoModule.CommentModule result, String msg) {
                hideLoading();
                ToastUtils.showShortToast(msg);
                item.getContent().getComments().add(result);
                int i = Integer.parseInt(item.getContent().getCommentCount())+1;
                item.getContent().setCommentCount(String.valueOf(i));
                mAdapter.notifyItemChanged(position);
            }
            @Override
            public void onError(Throwable e) {
                hideLoading();
            }
            @Override
            public void onComplete() {
                hideLoading();
            }
        });
    }

    /**
     * 移除点赞
     *
     * @param topicId topicId
     * @param item    item
     */
    private void requestRemoveLike(String topicId, final CommunityMultiItem item) {
        showLoading();

        APIManager.startRequest(mPageService.cancelGroupTopicLike(topicId, item.getContent().getLikeId()), new RequestListener<Object>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(getActivity());
            }
            @Override
            public void onSuccess(Object result, String msg) {
                hideLoading();
                showToast(msg);
                int likeCount = item.getContent().getLikeCount();
                likeCount -= 1;
                item.getContent().setLikeCount(likeCount);
                item.getContent().setLikeStatus(0);
                mAdapter.notifyItemChanged(clickPosition);
            }
            @Override
            public void onError(Throwable e) {
                hideLoading();
                showError(e);
            }
            @Override
            public void onComplete() {
                hideLoading();
            }
        });
    }



    /**
     * 添加点赞
     *
     * @param topicId topicId
     * @param item    item
     */
    private void requestAddLike(String topicId, final CommunityMultiItem item) {

        APIManager.startRequest(mPageService.addGroupTopicLike(topicId), new RequestListener<Like>() {
            @Override
            public void onStart() {
                ToastUtil.showLoading(getActivity());
            }
            @Override
            public void onSuccess(Like result, String msg) {
                hideLoading();
                showToast(msg);
                int likeCount = item.getContent().getLikeCount();
                likeCount += 1;
                item.getContent().setLikeCount(likeCount);
                item.getContent().setLikeStatus(1);
                item.getContent().setLikeId(result.likeId);
                mAdapter.notifyItemChanged(clickPosition);
            }
            @Override
            public void onError(Throwable e) {
                hideLoading();
                showError(e);
            }
            @Override
            public void onComplete() {
                hideLoading();
            }
        });
    }

    /**
     * 转发图图片类型
     *
     * @param module module
     */
    private void forWardImgEvent(MaterialVideoModule module) {
        final ShareDialogNew shareDialog = new ShareDialogNew(isShareCicle, mActivity, module.getImages(), module.getContent());
        shareDialog.setPicType(module);
        requestPermission(new PermissionListener() {
            @Override
            public void onSuccess() {
                shareDialog.show();
            }
        });
    }

    /**
     * 转发视频类型
     *
     * @param module module
     */
    private void forWardVideoEvent(MaterialVideoModule module) {
        ShareDialogNew shareDialog = new ShareDialogNew(isShareCicle, mActivity, null, "");
        shareDialog.setVideoType(module);
        shareDialog.show();
    }

    /**
     * 保存图片类型
     *
     * @param module module
     */
    private void saveImgEvent(final MaterialVideoModule module) {
        requestPermission(new PermissionListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast(getString(R.string.s_saving_text));
                ImgDownLoadUtils.savePic2Local(module.getImages(), mActivity.getApplicationContext());
            }
        });
    }

    /**
     * 保存视频
     *
     * @param module module
     */
    private void saveVideoEvent(final MaterialVideoModule module) {
        requestPermission(new PermissionListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast( "正在保存中");
                ImgDownLoadUtils.saveVideo2Local
                        (module.getMediaUrl(), getActivity());
            }
        });
    }

    /**
     * 保村转发链接事件
     *
     * @param module module
     */
    private void forwardLinkEvent(MaterialVideoModule module) {
        ShareDialogNew shareDialog = new ShareDialogNew(isShareCicle, mActivity, module.getImages(), module.getContent());
        shareDialog.setLinkType(module);
        shareDialog.show();
    }

    /**
     * 朋友圈条目点击事件
     *
     * @param item          item
     * @param clickPosition
     */
    private void onFriendsItemClick(CommunityMultiItem item, int clickPosition) {
        this.clickItem = item;
        this.clickDetailPosition = clickPosition;
        Intent intent = new Intent(mActivity, CommunityDetailActivity.class);
        intent.putExtra(Constants.Extras.KEY_EXTRAS, item);
        intent.putExtra(Constants.KEY_TYPE, isShareCicle);
        startActivity(intent);
    }


    /**
     * 请求不同的数据，圈子，视频，素材
     */
    private void requestPageData() {
        switch (mCommunityType) {
            case TYPE_GROUP:
                requestGroupTopList();
                break;
            case TYPE_MATERIAL:
                requestMaterialVideoList(mCommunityType);
                break;
            case TYPE_VIDEO:
                requestMaterialVideoList(mCommunityType);
                break;
            default:
        }
    }

    private void requestAddHeadView(CommunityType communityType) {
        int type = communityType == CommunityType.TYPE_MATERIAL ? 1 : 2;
        if (TextUtils.isEmpty(mCategoryId) && pageSize == 1 && communityType != CommunityType.TYPE_GROUP) {
            APIManager.startRequest(mPageService.getMaterialLibraryCategoryList(String.valueOf(type)), new RequestListener<List<GroupCategoryModel>>() {
                @Override
                public void onSuccess(List<GroupCategoryModel> result) {
                    super.onSuccess(result);
                    setHeadView(result);
                    mRecyclerView.scrollToPosition(0);
                }

                @Override
                public void onStart() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }

    /**
     * 请求材料库和视频的数据
     *
     * @param communityType type_video,type_material
     */
    private void requestMaterialVideoList(CommunityType communityType) {
        int type = communityType == CommunityType.TYPE_MATERIAL ? 1 : 2;
        APIManager.startRequest(mPageService.getMaterialLibraryList(type, mCategoryId, key, pageSize, 15), new RequestListener<PaginationEntity<MaterialVideoModule, Object>>() {
            @Override
            public void onStart() {

            }
            @Override
            public void onSuccess(PaginationEntity<MaterialVideoModule, Object> result) {
                super.onSuccess(result);
                dealResult(result);
            }

            @Override
            public void onError(Throwable e) {
                showError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    /**
     * 设置头部的搜索的分类的view和View事件
     *
     * @param result result
     */
    private void setHeadView(List<GroupCategoryModel> result) {
        View headView = mInflater.inflate(R.layout.layout_group_head_view, null);
        RecyclerView headRecycleView = (RecyclerView) headView.findViewById(R.id.head_recycleView);
        headRecycleView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        GroupHeadAdapter groupHeadAdapter = new GroupHeadAdapter(result);
        groupHeadAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                Intent intent = new Intent(mActivity, CommunityListActivity.class);
                intent.putExtra(Constants.Extras.KEY_EXTRAS, (GroupCategoryModel) baseQuickAdapter.getItem(i));
                if (mCommunityType == CommunityType.TYPE_VIDEO) {
                    intent.putExtra(Constants.Extras.KET_TYPE, 1);
                }
                startActivity(intent);
            }
        });
        CommonSearchBar searchBar = (CommonSearchBar) headView.findViewById(R.id.search_bar);
        searchBar.setOnQueryTextListener(new CommonSearchBar.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                key = query;
                pageSize = 1;
                requestPageData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public void onResetQuery() {
                key = "";
                pageSize = 1;
                requestPageData();
            }

            @Override
            public void onClose() {

            }
        });
        headRecycleView.setAdapter(groupHeadAdapter);

        mAdapter.addHeaderView(headView);
    }

    /**
     * 请求圈子的列表
     */
    private void requestGroupTopList() {

        APIManager.startRequest(mPageService.getGroupTopicListNew(15, pageSize), new RequestListener<PaginationEntity<MaterialVideoModule, Object>>() {
            @Override
            public void onSuccess(PaginationEntity<MaterialVideoModule, Object> result) {
                super.onSuccess(result);
                dealResult(result);
            }

            @Override
            public void onStart() {
            }
            @Override
            public void onError(Throwable e) {
                showError(e);
            }
            @Override
            public void onComplete() {

            }
        });
    }

    private void dealResult(PaginationEntity<MaterialVideoModule, Object> result) {
        ArrayList<CommunityMultiItem> multiItems = getCommunityMultiItems(result.list);
        if (pageSize >= result.totalPage) {
            mAdapter.loadMoreEnd();
        } else {
            mAdapter.loadMoreComplete();
        }
        if (pageSize == 1) {
            if (mPullRefresh != null) {
                mPullRefresh.refreshComplete();
            }
            mAdapter.setNewData(multiItems);
        } else {
            mAdapter.addData(multiItems);
        }
    }

    /**
     * 根据数据确定类型
     *
     * @param datas datas
     */
    private ArrayList<CommunityMultiItem> getCommunityMultiItems(ArrayList<MaterialVideoModule> datas) {
        ArrayList<CommunityMultiItem> groupMultiItems = new ArrayList<>();
        if (null == datas) {
            return groupMultiItems;
        }
        for (MaterialVideoModule moment : datas) {
            CommunityMultiItem item = null;
            if (moment.getType() == CommunityMultiItem.ITEM_TYPE_TEXT || moment.getTopicType() == CommunityMultiItem.ITEM_TYPE_TEXT) {
                item = new CommunityMultiItem(CommunityMultiItem.ITEM_TYPE_TEXT, moment, mCommunityType);
            } else if (moment.getType() == CommunityMultiItem.ITEM_TYPE_VIDEO) {
                item = new CommunityMultiItem(CommunityMultiItem.ITEM_TYPE_VIDEO, moment, mCommunityType);
            } else if (moment.getType() == CommunityMultiItem.ITEM_TYPE_LINK || moment.getTopicType() == CommunityMultiItem.ITEM_TYPE_LINK) {
                item = new CommunityMultiItem(CommunityMultiItem.ITEM_TYPE_LINK, moment, mCommunityType);
            }
            groupMultiItems.add(item);
        }
        return groupMultiItems;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void addCommond(EventMessage eventMessage) {
        if (eventMessage.getEvent() == Event.addCommond) {
            MaterialVideoModule.CommentModule module = (MaterialVideoModule.CommentModule) eventMessage.getData();
            if (null == clickItem) {
                return;
            }
            clickItem.getContent().getComments().add(module);
            int oldCommentCount = Integer.parseInt(clickItem.getContent().getCommentCount());
            oldCommentCount += 1;
            clickItem.getContent().setCommentCount(String.valueOf(oldCommentCount));
            mAdapter.notifyItemChanged(clickDetailPosition);
        } else if (eventMessage.getEvent() == Event.cancelSupport) {
            if (null == clickItem) {
                return;
            }
            clickItem.getContent().setLikeStatus(0);
            int likeCount = clickItem.getContent().getLikeCount() - 1;
            clickItem.getContent().setLikeCount(likeCount);
            mAdapter.notifyItemChanged(clickDetailPosition);
        } else if (eventMessage.getEvent() == Event.addSupport) {
            if (null == clickItem) {
                return;
            }
            clickItem.getContent().setLikeStatus(1);
            int likeCount = clickItem.getContent().getLikeCount() + 1;
            clickItem.getContent().setLikeCount(likeCount);
            mAdapter.notifyItemChanged(clickDetailPosition);
        }
    }

}
