package com.xiling.ddmall.module.community;

import android.databinding.ViewDataBinding;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.blankj.utilcode.utils.KeyboardUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.xiling.ddmall.BR;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.PaginationEntity;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICommunityService;

import butterknife.BindView;


/**
 * Created by bigbyto on 16/07/2017.
 */

public abstract class CourseDetailActivity extends BasicActivity {
    @BindView(R.id.titleView)
    TitleView mTitleView;
    @BindView(R.id.tvSend)
    TextView mTvSend;
    @BindView(R.id.etContent)
    EditText mEtContent;
    @BindView(R.id.llComment)
    FrameLayout mLlComment;
    @BindView(R.id.lvList)
    ListView mLvList;
    @BindView(R.id.pullRefresh)
    PtrClassicFrameLayout mPullRefresh;
    private ICommunityService mPageService;


    private class CommentAdapter extends SimpleAdapter<Comment> {
        @Override
        protected int getLayoutId(int position) {
            return R.layout.view_item_comment;
        }

        @Override
        protected void onBindData(ViewDataBinding binding, Comment data, int position) {
            binding.setVariable(BR.item, data);
//            ViewItemCommentBinding b=binding;
//            Glide.with(CourseDetailActivity.this).load(data.headImage).into(b.ivAvatar);
//            LinearLayout llReply = b.llReply;
//            llReply.setVisibility(data.hasReply() ? View.VISIBLE : View.GONE);
        }
    }

    protected abstract void updateData(Course detail);

    protected abstract View createHeaderView();

    private final CommentAdapter adapter = new CommentAdapter();
    private final DataManager<Comment> dataManager = new DataManager<Comment>(adapter) {
        @Override
        protected void onLoadSuccess(PaginationEntity<Comment, Object> result) {
            if (isReload()) {
                mPullRefresh.setLoadMoreEnable(true);
                mPullRefresh.refreshComplete();
            } else {
                mPullRefresh.loadMoreComplete(hasNextPage());
            }
        }

        @Override
        protected void onLoadError(Throwable error) {
            ToastUtils.showShortToast(error.getMessage());
        }

        @Override
        protected void requestData() {
            APIManager.startRequest(mPageService.getCourseCommentList(course.courseId, nextPage()), getCallback());
        }
    };

    private Course course;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        init();
        initView();
    }

    private void init() {
        course = (Course) getIntent().getSerializableExtra(Constants.Extras.COURSE);
        if (course == null) {
            ToastUtils.showShortToast("课程不存在");
            finish();
            return;
        }

        if (course.courseType == Course.TEXT) {
            mTitleView.setVisibility(View.VISIBLE);
        } else {
            mTitleView.setVisibility(View.GONE);
        }
        requestCourseDetail(course.courseId);
    }

    private void initView() {
        mPullRefresh.disableWhenHorizontalMove(true);
        mPullRefresh.setLoadMoreEnable(true);
        mPullRefresh.setPtrHandler(new com.chanven.lib.cptr.PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(com.chanven.lib.cptr.PtrFrameLayout frame) {
                dataManager.reloadData();
            }
        });

        mPullRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                dataManager.loadData();
            }
        });

        mTvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComment();
            }
        });

        mLvList.addHeaderView(createHeaderView(), null, false);
        mLvList.setAdapter(adapter);
        dataManager.reloadData();
    }

    private void addComment() {
        String content = mEtContent.getText().toString();
        if (TextUtils.isEmpty(content)) {
            showToast("请输入评论内容");
            return;
        }

        requestAddComment(content);
    }

    private void requestAddComment(String content) {
        showLoading();
        APIManager.startRequest(mPageService.addCourseComment(course.courseId, content), new RequestListener<Comment>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                showError(e);
            }

            @Override
            public void onSuccess(Comment result) {
                super.onSuccess(result);
                hideLoading();
                course.setCommentCount(course.getOldCount() + 1);
                changeCommentNub(course);
                ToastUtils.showShortToast("发送成功");
                KeyboardUtils.hideSoftInput(CourseDetailActivity.this);
                mEtContent.setText("");
                adapter.getDataList().add(0, result);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void changeCommentNub(Course course) {

    }


    private void requestCourseDetail(String courseId) {

        APIManager.startRequest(mPageService.courseDetail(courseId), new RequestListener<Course>() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(Course result) {
                super.onSuccess(result);
                course = result;
                updateData(result);
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected boolean enableFullScreenToStatusBar() {
        return false;
    }
}
