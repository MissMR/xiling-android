package com.dodomall.ddmall.module.push;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Category;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICategoryService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
public class PopupWindowsCategoryView extends FrameLayout {

    @BindView(R.id.rvCategory)
    RecyclerView mRvCategory;

    private Context mContext;
    private ArrayList<Category> mDatas;
    private CategoryGridAdapter mAdapter;
    private String mCategoryId;
    private ICategoryService mService;
    private OnCategoryClickListener mCategoryClickListener;

    public PopupWindowsCategoryView(@NonNull Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
        initData();
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(ICategoryService.class);
        APIManager.startRequest(
                mService.getPushChildCategory(mCategoryId, 1, 1000),
                new BaseRequestListener<PaginationEntity<Category, Object>>() {
                    @Override
                    public void onSuccess(PaginationEntity<Category, Object> result) {
                        Category category = new Category();
                        category.id = mCategoryId;
                        category.name = "全部";
                        mDatas.add(category);
                        mDatas.addAll(result.list);
                        mAdapter.notifyDataSetChanged();
                    }

                });
    }

    private void initView() {
        inflate(mContext, R.layout.view_category, this);
        ButterKnife.bind(this);

        mRvCategory.setLayoutManager(new GridLayoutManager(mContext, 3));
        mDatas = new ArrayList<>();
        mAdapter = new CategoryGridAdapter(mDatas);
        mRvCategory.setAdapter(mAdapter);
        mRvCategory.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mCategoryClickListener != null) {
                    mCategoryClickListener.onClick(mDatas.get(position));
                }
            }
        });
    }

    public void setCategoryClickListener(OnCategoryClickListener categoryClickListener) {
        mCategoryClickListener = categoryClickListener;
    }

    public interface OnCategoryClickListener{
        void onClick(Category category);
    }
}
