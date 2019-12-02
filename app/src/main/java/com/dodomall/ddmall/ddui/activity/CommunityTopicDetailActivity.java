package com.dodomall.ddmall.ddui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.adapter.DDCommunityDataAdapter;
import com.dodomall.ddmall.ddui.bean.CommunityDataBean;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.TopicBean;
import com.dodomall.ddmall.ddui.bean.TopicFollowChangeEvent;
import com.dodomall.ddmall.ddui.service.ICommunityService;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.component.dialog.DDMDialog;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/10/11
 * 热门话题详情页
 */
public class CommunityTopicDetailActivity extends BaseActivity implements OnLoadMoreListener, RadioGroup.OnCheckedChangeListener {

    public static final int TYPE_HOT = 0;
    public static final int TYPE_NEW = 1;


    @BindView(R.id.tv_toolbar_title)
    TextView mTvToolbarTitle;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.iv_back)
    ImageView mIvBack;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.nsv_container)
    NestedScrollView mNsvContainer;

    @BindView(R.id.v_toolbar_background)
    View mVToolbarBackground;
    @BindView(R.id.rg_tab)
    RadioGroup mRgTab;
    @BindView(R.id.sdv_background)
    SimpleDraweeView mSdvBackground;
    @BindView(R.id.tv_topic_title)
    TextView mTvTopicTitle;
    @BindView(R.id.tv_follow_count)
    TextView mTvFollowCount;
    @BindView(R.id.tv_btn_follow)
    TextView mTvBtnFollow;

    @BindView(R.id.tv_topic_content)
    TextView mTvTopicContent;

    @BindView(R.id.tv_toggle_topic_content)
    TextView mTvToggleTopicContent;

    private String mTopicId;
    private TopicBean mTopicBean;
    private ICommunityService mCommunityService;

    private DDCommunityDataAdapter mCommunityDataAdapter;
    private List<CommunityDataBean> mHotMaterialList = new ArrayList<>();
    private List<CommunityDataBean> mNewMaterialList = new ArrayList<>();

    private int mHotMaterialPage = 1;
    private int mNewMaterialPage = 1;

    private int mTabType = TYPE_HOT;
    private int mTopicContentLine = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_topic_detail);
        ButterKnife.bind(this);
        initData();
        initView();

    }

    private void initData() {
        mTopicId = getIntent().getStringExtra(Constants.Extras.TOPIC_ID);
        if (TextUtils.isEmpty(mTopicId)) {
            ToastUtil.error("数据出错");
            return;
        }

        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
        getTopicDetail();
        getMaterialList();
    }

    private void updateFollowState(final int action) {
        APIManager.startRequest(mCommunityService.updateTopicFollowState(mTopicId, action),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        ToastUtil.success(action == ICommunityService.TYPE_TOPIC_FOLLOW ?
                                "您已成功关注该话题" : "您已取消关注此话题");
                        updateFollowView(action == ICommunityService.TYPE_TOPIC_FOLLOW);

                        updateFollowCount(action);

                        EventBus.getDefault().post(new TopicFollowChangeEvent(action == ICommunityService.TYPE_TOPIC_FOLLOW,
                                mTopicId));

                    }
                });
    }

    private void updateFollowCount(int action) {
        int followCount;
        try {
            followCount = Integer.valueOf(mTvFollowCount.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        if (action == ICommunityService.TYPE_TOPIC_FOLLOW) {
            followCount++;
        } else {
            followCount--;
        }
        mTvFollowCount.setText(String.valueOf(followCount));
    }

    private void getTopicDetail() {
        ToastUtil.showLoading(this);
        APIManager.startRequest(mCommunityService.getTopicDetail(mTopicId),
                new BaseRequestListener<TopicBean>(this) {
                    @Override
                    public void onSuccess(TopicBean result) {
                        super.onSuccess(result);
                        mTopicBean = result;
                        updateTopicView();
                    }
                });
    }

    private void getMaterialList() {
        int page = mTabType == TYPE_HOT ? mHotMaterialPage : mNewMaterialPage;
        int orderType = mTabType == TYPE_HOT ? ICommunityService.TYPE_MATERIAL_ORDER_HOT
                : ICommunityService.TYPE_MATERIAL_ORDER_NEW;
        APIManager.startRequest(mCommunityService.getMaterialListByTopicId(page, 10,
                mTopicId, orderType), new BaseRequestListener<ListResultBean<CommunityDataBean>>() {
            @Override
            public void onSuccess(ListResultBean<CommunityDataBean> result) {
                super.onSuccess(result);
                mSmartRefreshLayout.finishLoadMore();
                if (result == null || result.getDatas() == null || result.getDatas().isEmpty()) {
                    return;
                }

                List actionList = mTabType == TYPE_HOT ? mHotMaterialList : mNewMaterialList;
                actionList.addAll(result.getDatas());
                updateRecyclerView();
            }
        });
    }

    private void updateTopicView() {

        mTvToolbarTitle.setText(mTopicBean.getTitle());

        mTvTopicTitle.setText(mTopicBean.getTitle());
        mTvFollowCount.setText(String.valueOf(mTopicBean.getHotNum()));
        mSdvBackground.setImageURI(mTopicBean.getBannerImgUrlDetail());
        updateFollowView(mTopicBean.isFollow());

        initTopicContent();

    }


    private void initTopicContent() {
        mTvTopicContent.setText(Html.fromHtml(mTopicBean.getContent()));

        mTvTopicContent.post(new Runnable() {
            @Override
            public void run() {
                DLog.i("lineCount " + mTvTopicContent.getLineCount());
                mTopicContentLine = mTvTopicContent.getLineCount();
                if (mTopicContentLine > 4) {
                    mTvTopicContent.setMaxLines(4);
                    mTvToggleTopicContent.setVisibility(View.VISIBLE);
                }
            }
        });

        mTvToggleTopicContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTvToggleTopicContent.getText().toString();
                if ("展开全部".equals(text)) {
                    mTvTopicContent.setMaxLines(mTopicContentLine);
                    mTvToggleTopicContent.setText("点击收起");
                } else {
                    mTvTopicContent.setMaxLines(4);
                    mTvToggleTopicContent.setText("展开全部");
                }
            }
        });

    }

    private void updateFollowView(boolean isFollow) {
        mTvBtnFollow.setSelected(isFollow);
        mTvBtnFollow.setText(mTvBtnFollow.isSelected() ? "已关注" : "关注");
    }

    private void initView() {

        QMUIStatusBarHelper.translucent(this);
        QMUIStatusBarHelper.setStatusBarLightMode(this);

        initHeaderListener();

        mCommunityDataAdapter = new DDCommunityDataAdapter(this);
        mCommunityDataAdapter.setMode(DDCommunityDataAdapter.Mode.Topic);

        mRgTab.check(R.id.rb_tab1);

        mRgTab.setOnCheckedChangeListener(this);

        mSmartRefreshLayout.setEnableRefresh(false);
        mSmartRefreshLayout.setEnableLoadMore(true);
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setAdapter(mCommunityDataAdapter);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mTabType = mRgTab.getCheckedRadioButtonId() == R.id.rb_tab1 ? TYPE_HOT : TYPE_NEW;
        if (mTabType == TYPE_HOT) {
            mHotMaterialPage++;
        } else {
            mNewMaterialPage++;
        }
        getMaterialList();
    }

    @OnClick(R.id.iv_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.tv_btn_follow)
    void onFollowClicked() {
        final int action = mTvBtnFollow.isSelected() ? ICommunityService.TYPE_TOPIC_UNFOLLOW : ICommunityService.TYPE_TOPIC_FOLLOW;

        if (action == ICommunityService.TYPE_TOPIC_UNFOLLOW) {
            // 取消关注
            new DDMDialog(this).setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showLoading(getParent());
                    updateFollowState(action);
                }
            }).setNegativeButton("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).setContent("确定取消关注此话题？")
                    .show();
        } else {
            // 关注
            ToastUtil.showLoading(this);
            updateFollowState(action);
        }

    }

    private void updateRecyclerView() {
        mCommunityDataAdapter.getItems().clear();
        if (mRgTab.getCheckedRadioButtonId() == R.id.rb_tab1) {
            mCommunityDataAdapter.addItems(mHotMaterialList);
        } else {
            mCommunityDataAdapter.addItems(mNewMaterialList);
        }
    }

    private void initHeaderListener() {
        mNsvContainer.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                DLog.i("onScrollChange: y = " + scrollY + ",  height = " + mSdvBackground.getHeight());
                if (scrollY >= mSdvBackground.getHeight()) {
                    mVToolbarBackground.setAlpha(1);
                    mTvToolbarTitle.setVisibility(View.VISIBLE);
                    mIvBack.setImageResource(R.mipmap.ic_back_black);
                } else {
                    mVToolbarBackground.setAlpha(scrollY * 1.0f / mSdvBackground.getHeight());
                    mTvToolbarTitle.setVisibility(View.GONE);
                    mIvBack.setImageResource(R.mipmap.ic_back_white);
                }
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_tab1) {
            mTabType = TYPE_HOT;
            if (mHotMaterialPage == 1 && mHotMaterialList.isEmpty()) {
                getMaterialList();
                return;
            }
        } else {
            mTabType = TYPE_NEW;
            if (mNewMaterialPage == 1 && mNewMaterialList.isEmpty()) {
                getMaterialList();
                return;
            }
        }
        updateRecyclerView();
    }
}
