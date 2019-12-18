package com.xiling.ddmall.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.utils.SpannableStringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.adapter.MeasurementAdapter;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.bean.ProductEvaluateBean;
import com.xiling.ddmall.ddui.service.IProductMeasurementService;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.util.ToastUtil;

import io.reactivex.Observable;

/**
 * created by Jigsaw at 2019/3/20
 * 品控师列表
 */
public class MeasurementActivity extends DDListActivity<ProductEvaluateBean> {

    private String mEngineerId;
    private String mEngineerName;

    public static void start(Context context, String engineerId, String engineerName) {
        context.startActivity(new Intent(context, MeasurementActivity.class)
                .putExtra(Constants.Extras.ID, engineerId)
                .putExtra(Constants.Extras.NICKNAME, engineerName));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initData();
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        getHeaderLayout().getTitleView().setMaxLines(2);
        getHeaderLayout().getTitleView().setTextSize(16);
        showHeader(mEngineerName);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String spuId = mAdapter.getItem(position).getProductId();
                startActivity(new Intent(MeasurementActivity.this, MeasurementDetailActivity.class)
                        .putExtra(Constants.Extras.SPU_ID, spuId));
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                String spuId = mAdapter.getItem(position).getProductId();
                DDProductDetailActivity.start(MeasurementActivity.this, spuId);
            }
        });
    }

    private void initData() {
        mEngineerId = getIntent().getStringExtra(Constants.Extras.ID);
        mEngineerName = getIntent().getStringExtra(Constants.Extras.NICKNAME);
        if (TextUtils.isEmpty(mEngineerId)) {
            ToastUtil.error("TextUtils.isEmpty(mEngineerId)");
            finish();
        }
    }

    private CharSequence getHeaderTitle(int measurementCount) {
        return SpannableStringUtils.getBuilder(mEngineerName)
                .append("\n")
                .append(measurementCount + "条测评")
                .setForegroundColor(ContextCompat.getColor(this, R.color.ddm_red))
                .setProportion(0.6f)
                .create();
    }

    @Override
    protected void onRequestSuccess(ListResultBean<ProductEvaluateBean> result) {
        super.onRequestSuccess(result);
        if (mPage == 1) {
            int measurementCount = result.getTotalRecord();
            showHeader(getHeaderTitle(measurementCount));
        }
    }

    @Override
    protected Observable<RequestResult<ListResultBean<ProductEvaluateBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IProductMeasurementService.class).getProductMeasurementByEngineerId(mEngineerId, mPage, 10);
    }

    @Override
    protected BaseQuickAdapter<ProductEvaluateBean, ? extends BaseViewHolder> getBaseQuickAdapter() {
        return new MeasurementAdapter();
    }
}
