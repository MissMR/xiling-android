package com.xiling.ddmall.module.community;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.user.LoginActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.dialog.CommentBottomSheetDialog;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICommunityService;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.ImgDownLoadUtils;
import com.xiling.ddmall.shared.util.SessionUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/1/5  14:10
 * @desc ${TODD}
 */

public class CommunityDetailActivity extends BasicActivity {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    private CommunityAdapter mAdapter;
    private CommunityMultiItem mHeadData;
    private int pageOffset = 1;
    ArrayList<CommunityMultiItem> dates = new ArrayList<>();
    private int clickPosition;
    private boolean mIsShareCircle;
    private ICommunityService mPageService;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_community_detail;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        ((TextView) findViewById(R.id.titleTv)).setText(getString(R.string.s_detail_title));
        mHeadData = (CommunityMultiItem) getIntent().getSerializableExtra(Constants.Extras.KEY_EXTRAS);
        mIsShareCircle = getIntent().getBooleanExtra(Constants.KEY_TYPE, false);
        mHeadData.getContent().setComments(null);
        mHeadData.needBottomLine(false);
        dates.add(mHeadData);
        mAdapter = new CommunityAdapter(dates,true);
        mAdapter.setDetailModel(true);
        mAdapter.disableLoadMoreIfNotFullPage(mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.getItemAnimator().setChangeDuration(0);
        mRecyclerView.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        requestCommentData();
    }

    private void requestCommentData() {
        APIManager.startRequest(mPageService.getGroupTopicCommentList(pageOffset, 15,
                mHeadData.getContent().getTopicId()), new RequestListener<PaginationEntity<MaterialVideoModule.CommentModule, Object>>() {
            @Override
            public void onSuccess(PaginationEntity<MaterialVideoModule.CommentModule, Object> result) {
                super.onSuccess(result);
                if (pageOffset == 1 && pageOffset >= result.totalPage) {
                    mAdapter.loadMoreComplete();
                    mAdapter.setEnableLoadMore(false);
                } else if (pageOffset >= result.totalPage) {
                    mAdapter.loadMoreEnd();
                } else {
                    mAdapter.loadMoreComplete();
                }
                getCommonItems(result.list);
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

    private void getCommonItems(ArrayList<MaterialVideoModule.CommentModule> data) {
        ArrayList<CommunityMultiItem> dates = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            CommunityMultiItem item = new CommunityMultiItem(CommunityMultiItem.ITEM_TYPE_COMMENT,
                    null, GroupFragment.CommunityType.TYPE_GROUP);
            item.setCommontData(data.get(i));
            item.setLastCommond(i == data.size() - 1);
            dates.add(item);
        }
        mAdapter.addData(dates);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageOffset++;
                requestCommentData();
            }
        }, mRecyclerView);
        findViewById(R.id.back_iv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                onCommonItemClick(view, i);
            }
        });
    }

    private void onCommonItemClick(View view, int i) {
        switch (view.getId()) {
            case R.id.save_tv:
                if (mHeadData.getItemType() == CommunityMultiItem.ITEM_TYPE_LINK) {
                    forwardLinkEvent(mHeadData.getContent());
                } else {
                    saveImgEvent(mHeadData.getContent());
                }
                break;
            case R.id.forward_tv:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                if (mHeadData.getItemType() == CommunityMultiItem.ITEM_TYPE_LINK) {
                    forwardLinkEvent(mHeadData.getContent());
                } else if (mHeadData.getItemType() == CommunityMultiItem.ITEM_TYPE_TEXT) {
                    forWardImgEvent(mHeadData.getContent());
                }
                break;
            case R.id.like_layout:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                clickPosition = i;
                int likeStatus = mHeadData.getContent().getLikeStatus();
                if (likeStatus == 0) {
                    requestAddLike(mHeadData.getContent().getTopicId(), mHeadData);
                } else {
                    requestRemoveLike(mHeadData.getContent().getTopicId(), mHeadData);
                }
                break;
            case R.id.lease_msg_tv:
                if (!SessionUtil.getInstance().isLogin()) {
                    gotoLogin();
                    break;
                }
                final CommentBottomSheetDialog dialog = new CommentBottomSheetDialog(CommunityDetailActivity.this);
                dialog.setSubmitListener(new CommentBottomSheetDialog.OnSubmitListener() {
                    @Override
                    public void submit(String content) {
                        dialog.dismiss();
                        requestAddComment(content, mHeadData.getContent().getTopicId());
                    }
                });
                dialog.show("发送", "");
                break;
            case R.id.item_link_layout:
                EventUtil.compileEvent(mActivity, "link", mHeadData.getContent().getLinkUrl());
                break;
            default:
                break;
        }
    }

    private void gotoLogin() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        startActivity(intent);
    }

    private void requestAddComment(String content, String topicId) {

        APIManager.startRequest(mPageService.addGroupTopicComment(topicId, content), new RequestListener<MaterialVideoModule.CommentModule>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(MaterialVideoModule.CommentModule result, String msg) {
                hideLoading();
                ToastUtils.showShortToast(msg);
                CommunityMultiItem multiItem = new CommunityMultiItem(CommunityMultiItem.ITEM_TYPE_COMMENT,
                        null, GroupFragment.CommunityType.TYPE_GROUP);
                multiItem.setCommontData(result);
                mAdapter.addData(multiItem);
                EventBus.getDefault().post(new EventMessage(Event.addCommond, result));
                String commentCount = mHeadData.getContent().getCommentCount();
                int newCommentCount = Integer.parseInt(commentCount);
                newCommentCount += 1;
                mHeadData.getContent().setCommentCount(String.valueOf(newCommentCount));
                mAdapter.notifyItemChanged(0);
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
     * 保村转发链接事件
     *
     * @param module
     */
    private void forwardLinkEvent(MaterialVideoModule module) {
        ShareDialogNew shareDialog = new ShareDialogNew(mIsShareCircle, mActivity, module.getImages(), module.getContent());
        shareDialog.setLinkType(module);
        shareDialog.show();
    }

    /**
     * 保存图片类型
     *
     * @param module
     */
    private void saveImgEvent(final MaterialVideoModule module) {
        requestPermission(new PermissionListener() {
            @Override
            public void onSuccess() {
                ToastUtils.showShortToast(getString(R.string.s_saving_text));
                ImgDownLoadUtils.savePic2Local(module.getImages(), getApplicationContext());
            }
        });
    }

    /**
     * 转发图图片类型
     *
     * @param module
     */
    private void forWardImgEvent(MaterialVideoModule module) {
        final ShareDialogNew shareDialog = new ShareDialogNew(mIsShareCircle, mActivity, module.getImages(), module.getContent());
        shareDialog.setPicType(module);
        requestPermission(new PermissionListener() {
            @Override
            public void onSuccess() {
                shareDialog.show();
            }
        });
    }

    private void requestAddLike(String topicId, final CommunityMultiItem item) {
        APIManager.startRequest(mPageService.addGroupTopicLike(topicId), new RequestListener<Like>() {
            @Override
            public void onStart() {
                showLoading();
            }

            @Override
            public void onSuccess(Like result, String msg) {
                EventBus.getDefault().post(new EventMessage(Event.addSupport));
                hideLoading();
                showToast(msg);
                int likeCount = item.getContent().getLikeCount();
                likeCount += 1;
                item.getContent().setLikeId(result.likeId);
                item.getContent().setLikeCount(likeCount);
                item.getContent().setLikeStatus(1);
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

    private void requestRemoveLike(String topicId, final CommunityMultiItem item) {
        APIManager.startRequest(mPageService.cancelGroupTopicLike(topicId, item.getContent().getLikeId()), new RequestListener<Object>() {
            @Override
            public void onStart() {
                showLoading();
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
                EventBus.getDefault().post(new EventMessage(Event.cancelSupport));
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
}
