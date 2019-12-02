package com.dodomall.ddmall.ddui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.TeamOrderDetailActivity;
import com.dodomall.ddmall.ddui.adapter.TeamOrderAdapter;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.TeamOrderBean;
import com.dodomall.ddmall.module.MainActivity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMasterCenterService;
import com.dodomall.ddmall.shared.util.EmptyViewUtils;

import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/21
 * 团队订单
 */
public class TeamOrderListFragment extends DDListFragment<TeamOrderBean> {

    public static final String PARAM_SETTLEMENT_STATUS = "key1";
    private int mSettlementStatus = IMasterCenterService.SETTLEMENT_ALL;
    private OnOrderCountReceiver mOnOrderCountReceiver;
    private int mOrderCount = 0;

    public TeamOrderListFragment() {
        // Required empty public constructor
    }

    public static TeamOrderListFragment newInstance(int settlementStatus) {
        TeamOrderListFragment fragment = new TeamOrderListFragment();
        Bundle args = new Bundle();
        args.putInt(PARAM_SETTLEMENT_STATUS, settlementStatus);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSettlementStatus = getArguments().getInt(PARAM_SETTLEMENT_STATUS);
        }
    }

    @Override
    protected void init() {
        setEnableLazyLoad(true);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TeamOrderBean teamOrderBean = mAdapter.getItem(position);
                TeamOrderDetailActivity.navInFromTeamOrder(getActivity(), teamOrderBean.getOrderCode(), teamOrderBean.getSettlementStatus());
            }

        });
        initEmptyView();
    }

    private void initEmptyView() {
        View emptyView = EmptyViewUtils.getAdapterEmptyView(getContext());
        ((ImageView) emptyView.findViewById(R.id.iv_empty)).setImageResource(R.mipmap.ic_team_order_empty);
        ImageView ivBtn = ((ImageView) emptyView.findViewById(R.id.iv_btn));
        ivBtn.setImageResource(R.mipmap.btn_make_money);
        ivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.goBack(getContext(), 0);
            }
        });
        mAdapter.setEmptyView(emptyView);
        mAdapter.getEmptyView().setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnOrderCountReceiver) {
            this.mOnOrderCountReceiver = (OnOrderCountReceiver) context;
        }
    }

    @Override
    protected void onRequestSuccess(ListResultBean<TeamOrderBean> resultBean) {
        super.onRequestSuccess(resultBean);
        mOrderCount = resultBean.getTotalRecord();
        if (mOnOrderCountReceiver != null) {
            mOnOrderCountReceiver.onOrderCountReceive(mOrderCount);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mOnOrderCountReceiver != null) {
                mOnOrderCountReceiver.onOrderCountReceive(mOrderCount);
            }
        }
    }

    @Override
    protected Observable<RequestResult<ListResultBean<TeamOrderBean>>> getApiObservable() {
        return ServiceManager.getInstance().createService(IMasterCenterService.class).getTeamOrderList(mPage, mSize, mSettlementStatus);
    }

    @Override
    protected BaseQuickAdapter<TeamOrderBean, BaseViewHolder> getBaseQuickAdapter() {
        return new TeamOrderAdapter();
    }


    public interface OnOrderCountReceiver {
        void onOrderCountReceive(int count);
    }

}
