package com.xiling.ddmall.module.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.user.adapter.FamilyOrderAdapter;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.FamilyOrder;
import com.xiling.ddmall.shared.component.NoData;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamlyOrderFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rvList)
    RecyclerView mRvList;
    Unbinder unbinder;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;
    private int mType;
    private FamilyOrderAdapter mAdapter;
    private ArrayList<FamilyOrder.DatasEntity> mDatas;

    public static FamlyOrderFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt("type", type);
        FamlyOrderFragment fragment = new FamlyOrderFragment();
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                service.getFamilyOrderList(mDatas.size() / Constants.PAGE_SIZE + 1, Constants.PAGE_SIZE, mType)
                , new BaseRequestListener<FamilyOrder>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(FamilyOrder result) {
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
        mAdapter = new FamilyOrderAdapter(mDatas);
        mRvList.setAdapter(mAdapter);
        mAdapter.setEmptyView(new NoData(getContext()).setImgRes(R.mipmap.no_data_order));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mLayoutRefresh.setOnRefreshListener(this);
    }

    private void getIntentData() {
        mType = getArguments().getInt("type");
    }


    @Override
    public void onRefresh() {
        getData(true);
    }
}
