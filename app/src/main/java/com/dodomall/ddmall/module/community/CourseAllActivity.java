package com.dodomall.ddmall.module.community;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.ToastUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author Stone
 * @time 2018/4/18  19:08
 * @desc ${TODD}
 */

public class CourseAllActivity extends BasicActivity {
    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;
    @BindView(R.id.titleView)
    TitleView mTitleView;
    @BindView(R.id.search_layout)
    LinearLayout searchLayout;
    @BindView(R.id.search_bar)
    OrderSearchBar mSearchBar;
    @BindView(R.id.search_bar_line)
    View searchBarLine;
    private int mType;
    private String categoryId;
    private CourseFragment mFragment;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_course_all;
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        boolean isSearch = getIntent().getBooleanExtra(Constants.KEY_IS_EDIT, false);
        if(isSearch) {
            searchLayout.setVisibility(View.VISIBLE);
            mTitleView.setVisibility(View.GONE);
            mSearchBar.setHintText("搜索标题");
            searchBarLine.setVisibility(View.VISIBLE);
        }
        mType = getIntent().getIntExtra(Constants.KEY_EXTROS, 1);

        categoryId = getIntent().getStringExtra(Constants.KEY_CATEGORY_ID);
        mTitleView.setTitle( getIntent().getStringExtra(Constants.KEY_TITLE));

        mFragment = CourseFragment.newInstance(mType, false,categoryId,isSearch);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
    }

    @OnClick(R.id.search_btn)
    public void onSearchClickEvent(View v){
        requestData();
    }

    private void requestData() {
        String value = mSearchBar.getInputText();
        if(TextUtils.isEmpty(value)) {
            ToastUtils.showShortToast("请输入搜索内容");
            return;
        }
        mFragment.setValueAndRequest(value);
    }

    @OnClick(R.id.back)
    public void onBackClickEvent(View v){
       finish();
    }

    @Override
    protected void initListener() {
        super.initListener();
        mSearchBar.setOnQueryTextListener(new OrderSearchBar.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestData();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
            @Override
            public void onResetQuery() {

            }
            @Override
            public void onClose() {
            }
        });
    }
}
