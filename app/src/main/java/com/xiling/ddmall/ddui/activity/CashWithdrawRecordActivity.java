package com.xiling.ddmall.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.ddui.adapter.CashWithdrawAdapter;
import com.xiling.ddmall.ddui.bean.CashWithdrawRecordBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/9/12
 * 提现记录页面
 */
public class CashWithdrawRecordActivity extends DDListActivity<CashWithdrawRecordBean> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();

    }

    private void initView() {
        showHeader("提现记录");
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CashWithdrawRecordBean item = (CashWithdrawRecordBean) adapter.getItem(position);
                startActivity(new Intent(CashWithdrawRecordActivity.this, CashWithdrawDetailActivity.class)
                        .putExtra(Constants.Extras.ID, item.getId()));
            }
        });
    }

    @Override
    protected Observable<RequestResult<ListResultBean<CashWithdrawRecordBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IUserService.class).getWithdraws(mPage, mSize);
    }

    @Override
    protected BaseQuickAdapter<CashWithdrawRecordBean, BaseViewHolder> getBaseQuickAdapter() {
        return new CashWithdrawAdapter();
    }
}
