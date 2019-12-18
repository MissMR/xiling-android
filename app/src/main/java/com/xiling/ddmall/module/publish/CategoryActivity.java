package com.xiling.ddmall.module.publish;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.community.BasicActivity;
import com.xiling.ddmall.module.community.GroupCategoryModel;
import com.xiling.ddmall.module.community.TitleView;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.contracts.RequestListener;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICommunityService;
import com.xiling.ddmall.shared.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author Stone
 * @time 2018/4/13  11:31
 * @desc ${TODD}
 */

public class CategoryActivity extends BasicActivity {
    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.titleView)
    TitleView titleView;
    private CateGoryAdapter mAdapter;
    private List<GroupCategoryModel> mData;
    private ArrayList<GroupCategoryModel> mCheckedCategory;
    private ICommunityService mPageService;


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_category_publish;
    }

    @Override
    protected void initViewConfig() {
        super.initViewConfig();
        mAdapter = new CateGoryAdapter();
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setAdapter(mAdapter);
    }

    @Override
    protected void initDataNew() {
        super.initDataNew();
        mPageService = ServiceManager.getInstance().createService(ICommunityService.class);
        boolean isVideo = getIntent().getBooleanExtra(Constants.KEY_IS_VIDEO, false);
        mCheckedCategory = (ArrayList<GroupCategoryModel>) getIntent().getSerializableExtra(Constants.KEY_EXTROS);
        int type = isVideo ? 2 : 1;
        APIManager.startRequest(mPageService.getMaterialLibraryCategoryList(String.valueOf(type)), new RequestListener<List<GroupCategoryModel>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }

            @Override
            public void onSuccess(List<GroupCategoryModel> result) {
                super.onSuccess(result);
                dealResult(result);
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GroupCategoryModel categoryModel = mAdapter.getData().get(position);
                categoryModel.setCheck(!categoryModel.isCheck());
                mAdapter.notifyItemChanged(position);
            }
        });
        titleView.setRightCilckListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<GroupCategoryModel> resultData = getResultData();
                if (StringUtils.isNullOrEmpty(resultData)) {
                    ToastUtils.showShortToast("请选择分类");
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra(Constants.KEY_EXTROS, resultData);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private ArrayList<GroupCategoryModel> getResultData() {
        List<GroupCategoryModel> data = mAdapter.getData();
        ArrayList<GroupCategoryModel> resultList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            GroupCategoryModel categoryModel = data.get(i);
            if (categoryModel.isCheck()) {
                resultList.add(categoryModel);
            }
        }
        return resultList;
    }

    private void dealResult(List<GroupCategoryModel> result) {
        mData = result;
        if (mData == null || mData.isEmpty()) {
            return;
        }
        //        mData.get(0).setCheck(true);
        List<GroupCategoryModel> data = mData;
        if (StringUtils.isNullOrEmpty(mCheckedCategory)) {
            data.get(0).setCheck(true);
        } else {
            for (int i = 0; i < data.size(); i++) {
                data.get(i).setCheck(isChecked(data.get(i).getCategoryId()));
            }
        }
        mAdapter.setNewData(mData);
    }

    private boolean isChecked(String categoryId) {
        for (int i = 0; i < mCheckedCategory.size(); i++) {
            if (categoryId.equals(mCheckedCategory.get(i).getCategoryId())) {
                return true;
            }
        }
        return false;
    }
}
