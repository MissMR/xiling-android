package com.dodomall.ddmall.module.publish;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.community.BasicActivity;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICommunityService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author Stone
 * @time 2018/4/16  10:56
 * @desc ${TODD}
 */

public class PublishHisActivity extends BasicActivity {
    @BindView(R.id.rvList)
    RecyclerView mRvList;
    private int pageOffset = 1;
    private boolean isFirstLoad = true;
    private HisQuickAdapter mAdapter;
    private ICommunityService mPageService;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        EventBus.getDefault().register(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        mRvList.setLayoutManager(linearLayoutManager);
        mAdapter = new HisQuickAdapter();
        mRvList.setAdapter(mAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void needRefresh(EventMessage eventMessage) {
        pageOffset = 1;
        requestPageData();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageOffset++;
                requestPageData();
            }
        }, mRvList);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PublishHisModule publishHisModule = mAdapter.getData().get(position);
                if (publishHisModule.getStatus() == 2) {
                    toPublishActivity(publishHisModule);
                }
            }
        });
    }

    private void toPublishActivity(PublishHisModule publishHisModule) {
        Intent intent = new Intent(mActivity, PublishPicActivity.class);
        if (publishHisModule.getType() == 1) {
            //素材库
            intent.putExtra(Constants.KEY_IS_EDIT, true);
        } else {
            //视频
            intent.putExtra(Constants.KEY_IS_EDIT, true);
            intent.putExtra(Constants.KEY_IS_VIDEO, true);
        }
        intent.putExtra(Constants.KEY_LIBRARY_ID, publishHisModule.getLibraryId());
        startActivity(intent);
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        requestPageData();
    }

    private void requestPageData() {
        APIManager.startRequest(mPageService.getPublishHistory(pageOffset, 15), new RequestListener<PaginationEntity<PublishHisModule, HistoryExtra>>() {
                    @Override
                    public void onSuccess(PaginationEntity<PublishHisModule, HistoryExtra> result) {
                        super.onSuccess(result);
                        if(isFirstLoad) {
                            addHeadView(result.ex);
                            isFirstLoad=!isFirstLoad;
                        }
                        if(result.page>=result.totalPage) {
                            mAdapter.loadMoreEnd();
                        }else {
                            mAdapter.loadMoreComplete();
                        }
                        if(pageOffset==1) {
                            mAdapter.setNewData(result.list);
                        }else {
                            mAdapter.addData(result.list);
                        }
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
                }
        );
    }

    private void addHeadView(HistoryExtra ex) {
        View headView = getLayoutInflater().inflate(R.layout.publish_head_layout, mRvList, false);
        CircleImageView headerUserIv = (CircleImageView) headView.findViewById(R.id.header_user_iv);
        TextView headerUserName = (TextView) headView.findViewById(R.id.header_user_name);
        TextView headMaterialNum = (TextView) headView.findViewById(R.id.head_material_num);
        TextView headVideoNum = (TextView) headView.findViewById(R.id.header_video_num);
        Glide.with(mActivity).load(ex.getHeadImage()).into(headerUserIv);
        headerUserName.setText(ex.getName());
        headMaterialNum.setText("素材 " + ex.getImageCount());
        headVideoNum.setText("视频 " + ex.getMediaCount());
        mAdapter.addHeaderView(headView);
    }
}
