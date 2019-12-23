package com.xiling.module.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.R;
import com.xiling.shared.Constants;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICommunityService;
import com.xiling.shared.util.EmptyViewUtils;
import com.xiling.shared.util.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/4/17  17:44
 * @desc ${TODD}
 */

public class CourseFragment extends BaseFragment {

    private int mType;
    private int pageOffset = 1;
    private int pageSize = 15;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.titleView)
    TitleView titleView;
    private BaseQuickAdapter mAdapter;
    private boolean mIsNeedHeader;
    private String mCategoryId;
    private String url = "course/getCourseListByCId";
    private boolean isSearch;
    private String key = "";
    private ICommunityService mPageService;

    @Override
    protected int getFragmentResId() {
        return R.layout.activity_base_list;
    }

    public static CourseFragment newInstance(int type, boolean isNeedHeader, String categoryId, boolean isSearch) {
        CourseFragment courseFragment = new CourseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putBoolean("isNeedHeader", isNeedHeader);
        bundle.putString("categoryId", categoryId);
        bundle.putBoolean("isSearch", isSearch);
        courseFragment.setArguments(bundle);
        return courseFragment;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        titleView.setVisibility(View.GONE);
        mType = getArguments().getInt("type", 0);
        mIsNeedHeader = getArguments().getBoolean("isNeedHeader", true);
        mCategoryId = getArguments().getString("categoryId");
        isSearch = getArguments().getBoolean("isSearch", false);
        if (isSearch) {
            url = "course/searchCourseList";
        }
        mAdapter = createAdapter();
        mAdapter.setEmptyView(EmptyViewUtils.getEmptyView(mActivity));
        rvList.setLayoutManager(new LinearLayoutManager(mActivity));
        rvList.setAdapter(mAdapter);
    }

    private BaseQuickAdapter createAdapter() {
        if (mType == 1) {
            mAdapter = new CourseSectionAdapter(mType, R.layout.view_item_course);
        } else if (mType == 2) {
            mAdapter = new CourseSectionAdapter(mType, R.layout.item_course_layout, mIsNeedHeader);
        } else if (mType == 3) {
            mAdapter = new CourseSectionAdapter(mType, R.layout.item_course_video_layout);
        }
        return mAdapter;
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                pageOffset++;
                requestData();
            }
        }, rvList);
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        addHeadView();
        if (!isSearch) {
            requestData();
        }
    }

    private void addHeadView() {
        if (pageOffset == 1 && mIsNeedHeader && !isSearch) {
            LinearLayout headerLayout = mAdapter.getHeaderLayout();
            if (headerLayout != null) {
                headerLayout.removeAllViews();
            }
            View headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_head_search_course, null);
            headView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSearchClick();
                }
            });
            mAdapter.addHeaderView(headView);
        }
    }

    private void onSearchClick() {
        Intent intent = new Intent(mActivity, CourseAllActivity.class);
        intent.putExtra(Constants.KEY_IS_EDIT, true);
        intent.putExtra(Constants.KEY_EXTROS, mType);
        startActivity(intent);
    }

    private void requestData() {
        if (mIsNeedHeader) {
            APIManager.startRequest(mPageService.getCourseList(mType, pageOffset, pageSize), new RequestListener<PaginationEntity<CourseModule, Object>>() {
                @Override
                public void onSuccess(PaginationEntity<CourseModule, Object> result) {
                    super.onSuccess(result);
                    dealListData(result);
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
        } else {
            APIManager.startRequest(mPageService.getCourseListByCategoryId(url, key, mCategoryId, mType, pageOffset, pageSize), new RequestListener<PaginationEntity<Course, Object>>() {
                @Override
                public void onSuccess(PaginationEntity<Course, Object> result) {
                    super.onSuccess(result);
                    dealAllSortList(result);
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

    private void dealAllSortList(PaginationEntity<Course, Object> result) {
        ArrayList<CourseSection> sections = new ArrayList<>();
        if (mType == 3) {
            sections.add(new CourseSection(result.list));
        } else {
            for (Course course : result.list) {
                sections.add(new CourseSection(course));
            }
        }
        adapterSetData(result.totalPage, sections);

    }

    private void dealListData(PaginationEntity<CourseModule, Object> result) {
        ArrayList<CourseSection> sections = new ArrayList<>();
        if (StringUtils.isNullOrEmpty(result.list)) {
            return;
        }
        for (CourseModule courseModule : result.list) {
            if (mIsNeedHeader && !StringUtils.isNullOrEmpty(courseModule.getCourseList())) {
                boolean isShowMore = courseModule.getCourseCount() > 4;
                CourseSection courseSection = new CourseSection(true, courseModule.getCategory().getTitle(), courseModule.getCategory(), isShowMore);
                sections.add(courseSection);
            }
            if (mType != 3) {
                for (Course courseListBean : courseModule.getCourseList()) {
                    CourseSection courseSection1 = new CourseSection(courseListBean);
                    sections.add(courseSection1);
                }
            } else if (!StringUtils.isNullOrEmpty(courseModule.getCourseList())) {
                CourseSection courseSection1 = new CourseSection(courseModule.getCourseList());
                sections.add(courseSection1);
            }
        }
        adapterSetData(result.totalPage, sections);

    }

    private void adapterSetData(int totalPage, ArrayList<CourseSection> sections) {
        if (pageOffset == 1) {
            mAdapter.setNewData(sections);
        } else {
            mAdapter.addData(sections);
        }
        if (pageOffset < totalPage) {
            mAdapter.loadMoreComplete();
        } else {
            mAdapter.loadMoreEnd();
        }
    }

    public void setValueAndRequest(String value) {
        key = value;
        requestData();
    }
}

