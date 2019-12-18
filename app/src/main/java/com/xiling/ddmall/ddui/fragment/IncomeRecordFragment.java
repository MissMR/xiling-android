package com.xiling.ddmall.ddui.fragment;

import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.ddui.activity.TeamOrderDetailActivity;
import com.xiling.ddmall.ddui.adapter.IncomeRecordAdapter;
import com.xiling.ddmall.ddui.bean.AchievementRecordBean;
import com.xiling.ddmall.ddui.bean.ListResultBean;
import com.xiling.ddmall.ddui.service.HtmlService;
import com.xiling.ddmall.module.page.WebViewActivity;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IMasterCenterService;
import com.xiling.ddmall.shared.util.EmptyViewUtils;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/30
 * 收益记录
 */
public class IncomeRecordFragment extends DDListFragment<AchievementRecordBean> {

    private static final String ARG_TYPE = "type";

    private int mType = 1;

    public IncomeRecordFragment() {
    }

    public static IncomeRecordFragment newInstance(int type) {
        IncomeRecordFragment fragment = new IncomeRecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void init() {
        if (getArguments() != null) {
            mType = getArguments().getInt(ARG_TYPE);
        }
        setEnableLazyLoad(true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AchievementRecordBean achievementBean = mAdapter.getItem(position);
                TeamOrderDetailActivity.navInFromIncome(getActivity(),
                        achievementBean.getOrderCode(), achievementBean.getPrizeType(), achievementBean.getPrize());
            }
        });

        View emptyView = EmptyViewUtils.getAdapterEmptyView(getContext());
        emptyView.findViewById(R.id.tv_desc).setVisibility(View.GONE);
        emptyView.findViewById(R.id.iv_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebViewActivity.jumpUrl(getContext(), HtmlService.MASTER_SHARE_PACKAGE);
            }
        });

        mAdapter.setEmptyView(emptyView);
        mAdapter.getEmptyView().setVisibility(View.GONE);
    }

    @Override
    protected void onRequestSuccess(ListResultBean<AchievementRecordBean> resultBean) {
        super.onRequestSuccess(resultBean);
    }


    @Override
    protected Observable<RequestResult<ListResultBean<AchievementRecordBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IMasterCenterService.class).getIncomeList(mPage, mSize, mType);
    }

    @Override
    protected BaseQuickAdapter<AchievementRecordBean, BaseViewHolder> getBaseQuickAdapter() {
        return new IncomeRecordAdapter();
    }

}