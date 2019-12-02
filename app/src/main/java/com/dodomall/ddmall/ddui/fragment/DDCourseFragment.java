package com.dodomall.ddmall.ddui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.InfoDetailActivity;
import com.dodomall.ddmall.ddui.adapter.EconomicCourseAdapter;
import com.dodomall.ddmall.ddui.bean.EconomicCourseBean;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.service.IEconomicClubService;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DDCourseFragment extends Fragment {
    private static final String ARG_TYPE = "type";
    public static final String ARG_ID = "id";

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    Unbinder unbinder;

    private int mType;
    private int mPage = 1;
    private int mSize = 10;
    private IEconomicClubService mEconomicClubService;
    private List<EconomicCourseBean> mCourseList = new ArrayList<>();
    private EconomicCourseAdapter mCourseAdapter;
    private String mCategoryId;

    private int mCurrentClickedIndex = -1;

    public DDCourseFragment() {
    }

    public static DDCourseFragment newInstance(String categoryId, int type) {
        DDCourseFragment fragment = new DDCourseFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        args.putString(ARG_ID, categoryId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
            mCategoryId = getArguments().getString(ARG_ID);
        }
        mEconomicClubService = ServiceManager.getInstance().createService(IEconomicClubService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_followers, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCurrentClickedIndex > -1) {
            // 实时改变已读人数
            EconomicCourseBean courseBean = mCourseAdapter.getData().get(mCurrentClickedIndex);
            courseBean.setReadCount(courseBean.getReadCount() + 1);
            mCourseAdapter.notifyItemChanged(mCurrentClickedIndex);
            mCurrentClickedIndex = -1;
        }
    }

    private void initView() {
        int itemLayout;
        // 1 是小图
        if (mType > 1) {
            itemLayout = R.layout.item_economic_course_newer_large;
        } else {
            itemLayout = R.layout.item_economic_course_store_master;
        }
        mCourseAdapter = new EconomicCourseAdapter(itemLayout, mCourseList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mCourseAdapter);

        mCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mCurrentClickedIndex = position;
                EconomicCourseBean courseBean = mCourseAdapter.getItem(position);
                InfoDetailActivity.jumpDetail(view.getContext(), courseBean.getTitle(), courseBean.getCourseId(),
                        InfoDetailActivity.InfoType.Learn, courseBean);
            }
        });

        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mRefreshLayout.autoRefresh();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPage = 1;
                mCourseList.clear();
                getNetData();
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPage++;
                getNetData();
            }
        });


    }

    private void getNetData() {
        APIManager.startRequest(mEconomicClubService.getCourseList(mCategoryId, mPage, mSize), new BaseRequestListener<ListResultBean<EconomicCourseBean>>() {
            @Override
            public void onSuccess(ListResultBean<EconomicCourseBean> result) {
                super.onSuccess(result);
                if (mRefreshLayout == null) {
                    return;
                }
                finishRefresh();
                List<EconomicCourseBean> list = result.getDatas();
                if (list != null && list.size() > 0) {
                    mCourseList.addAll(list);
                    mCourseAdapter.notifyDataSetChanged();
                } else {
                    if (mPage > 1) {
                        mRefreshLayout.setNoMoreData(true);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                finishRefresh();
            }
        });
    }

    private void finishRefresh() {
        if (mRefreshLayout == null) {
            return;
        }
        if (mPage == 1) {
            mRefreshLayout.finishRefresh();
        } else {
            mRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}