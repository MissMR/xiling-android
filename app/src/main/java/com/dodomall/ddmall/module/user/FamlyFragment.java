package com.dodomall.ddmall.module.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.user.adapter.FamilyAdapter;
import com.dodomall.ddmall.module.user.event.MsgFamily;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.Family;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.ConvertUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamlyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rvList)
    RecyclerView mRvList;
    Unbinder unbinder;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;

    private int mType;
    private int mMemberType = 99;
    private FamilyAdapter mAdapter;
    private ArrayList<Family.DatasEntity> mDatas;
    private TextView mCountView;

    public static FamlyFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        FamlyFragment fragment = new FamlyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_famly_order, container, false);
        unbinder = ButterKnife.bind(this, view);

        getIntentData();
        initView();
        getData(true);
        EventBus.getDefault().register(this);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
        }
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                service.getFamilyList(mDatas.size() / Constants.PAGE_SIZE + 1, Constants.PAGE_SIZE, mType, mMemberType)
                , new BaseRequestListener<Family>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(Family result) {
                        mCountView.setText("总人数：" + result.totalRecord);
                        mDatas.addAll(result.datas);
                        mAdapter.notifyDataSetChanged();
                        if (result.datas.size() < Constants.PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();
                    }
                });
    }

    private void initView() {
        mRvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mDatas = new ArrayList<>();
        mAdapter = new FamilyAdapter(mDatas);
        mRvList.setAdapter(mAdapter);
        mAdapter.setEmptyView(new NoData(getContext()).setImgRes(R.mipmap.no_data_order));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mLayoutRefresh.setOnRefreshListener(this);
        mAdapter.addHeaderView(createCountView());
    }

    private TextView createCountView() {
        if (mCountView == null) {
            mCountView = new TextView(getContext());
            mCountView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtil.dip2px(30)));
            mCountView.setBackgroundColor(getResources().getColor(R.color.bg_gray));
            mCountView.setTextColor(getResources().getColor(R.color.text_gray));
            mCountView.setTextSize(14);
            mCountView.setPadding(ConvertUtil.dip2px(15), 0, 0, 0);
            mCountView.setGravity(Gravity.CENTER_VERTICAL);
        }
        return mCountView;
    }

    private void getIntentData() {
        mType = getArguments().getInt("type");
    }


    @Override
    public void onRefresh() {
        getData(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(MsgFamily msgFamily) {
        if (msgFamily.getAction() == MsgFamily.ACTION_FAMILY_MENBER_TYPE_CLICK && mType == msgFamily.getFmailyType()) {
            EventBus.getDefault().removeStickyEvent(msgFamily);
            if (mMemberType != msgFamily.getMemberType()) {
                mMemberType = msgFamily.getMemberType();
                getData(true);
            }
        }
    }
}
