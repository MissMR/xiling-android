package com.xiling.module.push;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.shared.bean.Category;
import com.xiling.shared.manager.PopupWindowManage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
public class ProductScreenView extends LinearLayout {

    @BindView(R.id.tvCategory)
    TextView mTvCategory;
    @BindView(R.id.tvSort)
    TextView mTvSort;
    private Context mContext;

    private String[] mSorrName = {"差价由高到低", "差价由低到高"};
    private int mIndex;
    private SortChangeLisnener mSortLisnener;
    private PopupWindowManage mWindowManage;
    private String mCategoryId;
    private PopupWindowsCategoryView mCategoryView;
    private CategorySelectListener mCategoryClickListener;

    /**
     * 分类 排序
     */
    public final static int SHOW_ALL = 1;
    /**
     * 排序
     */
    public final static int SHOW_SORT = 2;
    private View mYinYing;

    public ProductScreenView(Context context) {
        this(context, null);
    }

    public ProductScreenView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        inflate(mContext, R.layout.view_product_screen, this);
        ButterKnife.bind(this);
    }

    public void setCategoryId(String categoryId) {
        mCategoryId = categoryId;
    }

    public void setShowModel(int showModel) {
        switch (showModel) {
            case SHOW_ALL:
                break;
            case SHOW_SORT:
                mTvCategory.setVisibility(GONE);
                break;
        }
    }

    @OnClick(R.id.tvCategory)
    public void onMTvCategoryClicked() {
        if (mWindowManage == null) {
            mWindowManage = PopupWindowManage.getInstance(mContext);
            mWindowManage.setYinYing(mYinYing);
        }
        if (mWindowManage.isShowing()) {
            mWindowManage.dismiss();
            return;
        }
        if (mCategoryView == null) {
            mCategoryView = new PopupWindowsCategoryView(mContext);
            mCategoryView.setCategoryId(mCategoryId);
            mCategoryView.setCategoryClickListener(new PopupWindowsCategoryView.OnCategoryClickListener() {
                @Override
                public void onClick(Category category) {
                    if (mCategoryClickListener != null) {
                        mCategoryClickListener.onSelect(category);
                    }
                    mWindowManage.dismiss();
                    mTvCategory.setText(category.name);
                }
            });
        }
        mWindowManage.showWindow(this, mCategoryView);
    }

    @OnClick(R.id.tvSort)
    public void onMTvSortClicked() {
        mIndex++;
        mTvSort.setText(mSorrName[mIndex % 2]);
        if (mSortLisnener != null) {
            mSortLisnener.onChange(mIndex % 2 + 1);
        }
    }

    public void setSortLisnener(SortChangeLisnener sortLisnener) {
        mSortLisnener = sortLisnener;
    }

    public void setYinYing(View yinYing) {
        mYinYing = yinYing;
    }

    public interface SortChangeLisnener {
        void onChange(int sort);
    }

    public void setCategorySelectListener(CategorySelectListener categoryClickListener) {
        mCategoryClickListener = categoryClickListener;
    }

    public interface CategorySelectListener {
        void onSelect(Category category);
    }
}
