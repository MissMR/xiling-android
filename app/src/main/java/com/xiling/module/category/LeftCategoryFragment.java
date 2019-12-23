package com.xiling.module.category;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.LogUtils;
import com.xiling.R;
import com.xiling.shared.bean.event.MsgCategory;
import com.xiling.module.category.adapter.LeftCategoryAdapter;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Category;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.PageManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.bean.api.PaginationEntity;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.service.contract.ICategoryService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.category
 * @since 2017-06-17
 */
public class LeftCategoryFragment extends Fragment implements PageManager.RequestListener {

    private String mCategoryId;
    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private LeftCategoryAdapter mLeftCategoryAdapter;
    private PageManager mPageManager;
    private ICategoryService mCategoryService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.left_category_layout, container, false);
        ButterKnife.bind(this, view);
        initArguments();
        mLeftCategoryAdapter = new LeftCategoryAdapter(getContext());
        mRecyclerView.setAdapter(mLeftCategoryAdapter);
        try {
            mPageManager = PageManager.getInstance()
                    .setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false))
                    .setRecyclerView(mRecyclerView)
                    .setRequestListener(this)
                    .build(getContext());
        } catch (PageManager.PageManagerException e) {
            e.printStackTrace();
        }
        mCategoryService = ServiceManager.getInstance().createService(ICategoryService.class);
        mPageManager.onRefresh();
        return view;
    }

    private void initArguments() {
        mCategoryId = getArguments().getString("categoryId", "");
        if (!mCategoryId.isEmpty()) {
            EventBus.getDefault().post(new EventMessage(Event.changeCategory, mCategoryId));
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

    @Override
    public void nextPage(final int page) {
        APIManager.startRequest(mCategoryService.getTopCategory(page), new BaseRequestListener<PaginationEntity<Category, Object>>() {
            @Override
            public void onSuccess(PaginationEntity<Category, Object> result) {
                EventBus.getDefault().post(new MsgCategory(MsgCategory.ACTION_GONE_NODATA));
                if (page == 1) {
                    mLeftCategoryAdapter.removeAllItems();
                }
                mPageManager.setLoading(false);
                if (result.list == null || result.list.isEmpty()) {
                    return;
                }
                LogUtils.e("拿到数据。。。。。。。");
                if (mCategoryId.isEmpty()) {
                    Category category = result.list.get(0);
                    category.isSelected = true;
                    mCategoryId = category.id;
                    EventBus.getDefault().post(new EventMessage(Event.changeCategory, mCategoryId));
                    LogUtils.e("现在发射第一个 id" + mCategoryId);
                } else {
                    for (Category category : result.list) {
                        category.isSelected = category.id.equalsIgnoreCase(mCategoryId);
                    }
                }
                mPageManager.setTotalPage(result.totalPage);
                mLeftCategoryAdapter.addItems(result.list);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                EventBus.getDefault().post(new MsgCategory(MsgCategory.ACTION_SHOW_NODATA));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectCategory(EventMessage message) {
        if (message.getEvent() == Event.changeCategory) {
            mCategoryId = String.valueOf(message.getData());
            List<Category> items = mLeftCategoryAdapter.getItems();
            for (Category category : items) {
                category.isSelected = category.id.equalsIgnoreCase(mCategoryId);
            }
            mLeftCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessage(MsgCategory msgCategory) {
        switch (msgCategory.getAction()) {
            case MsgCategory.ACTION_RE_QUEST:
                mPageManager.onRefresh();
                break;
        }
    }
}
