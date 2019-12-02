package com.dodomall.ddmall.module.push;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.Config;
import com.dodomall.ddmall.module.category.adapter.CategoryAdapter;
import com.dodomall.ddmall.module.search.SearchActivity;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Category;
import com.dodomall.ddmall.shared.bean.api.PaginationEntity;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICategoryService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/6.
 */
public class ProductPushActivity extends BaseActivity {
    @BindView(R.id.rvPush)
    RecyclerView mRvPush;
    private CategoryAdapter mAdapter;
    private ICategoryService mCategoryService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_push);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
        setTitle("美集推手");
        setLeftBlack();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setAutoMeasureEnabled(true);
        mRvPush.setLayoutManager(gridLayoutManager);
        mAdapter = new CategoryAdapter(this, CategoryAdapter.TYPE_PUSH);
        mRvPush.setAdapter(mAdapter);
    }

    private void initData() {
        mCategoryService = ServiceManager.getInstance().createService(ICategoryService.class);
        APIManager.startRequest(mCategoryService.getPushTopCategory(1, 1000), new BaseRequestListener<PaginationEntity<Category, Object>>(this) {
            @Override
            public void onSuccess(PaginationEntity<Category, Object> result) {
                if (result.list == null || result.list.isEmpty()) {
                    return;
                }
                mAdapter.addItems(result.list);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    @OnClick({R.id.tvPushRexiao, R.id.tvPushZhutui, R.id.tvPushZhuande})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, PushHotProductListActivityActivity.class);
        switch (view.getId()) {
            case R.id.tvPushRexiao:
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME, PushHotProductListActivityActivity.TYPE_REXIAO);
                break;
            case R.id.tvPushZhutui:
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME, PushHotProductListActivityActivity.TYPE_ZHUTUI);
                break;
            case R.id.tvPushZhuande:
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME, PushHotProductListActivityActivity.TYPE_ZHUANDE);
                break;
            default:
        }
        startActivity(intent);
    }

    @OnClick(R.id.searchLayout)
    public void onSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, AppTypes.PUSH.PRODUCT_TYPE);
        startActivity(intent);
    }
}
