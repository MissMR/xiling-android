package com.dodomall.ddmall.module.instant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.instant.adapter.InstantAdapter;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.GetSecondKillProductListModel;
import com.dodomall.ddmall.shared.bean.InstantData;
import com.dodomall.ddmall.shared.component.NoData;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IInstantService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.instant
 * @since 2017-08-01
 */
public class InstantFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.recyclerView)
    RecyclerView mRvList;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout mLayoutRefresh;

    private String mId;
    private InstantData.SecondKill mSecondKill;
    private ArrayList<InstantData.Product> mDatas;
    private InstantAdapter mAdapter;

    public static InstantFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString("id", id);
        InstantFragment fragment = new InstantFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_list_layout, container, false);
        ButterKnife.bind(this, view);
        getIntentData();
        EventBus.getDefault().register(this);
        LogUtils.e("开始注册");
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void getData(boolean isRefresh) {
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        IInstantService service = ServiceManager.getInstance().createService(IInstantService.class);
        APIManager.startRequest(
                service.getSecondKillProductList(
                        mDatas.size() / Constants.PAGE_SIZE + 1,
                        Constants.PAGE_SIZE,
                        mId)
                , new BaseRequestListener<GetSecondKillProductListModel>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(GetSecondKillProductListModel result) {
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
        mAdapter = new InstantAdapter(mDatas);
        InstantBannerView instantBannerView = new InstantBannerView(getContext());
        instantBannerView.setData(mSecondKill);
        mAdapter.addHeaderView(instantBannerView);
        mRvList.setAdapter(mAdapter);
        mLayoutRefresh.setOnRefreshListener(this);
        mAdapter.setEmptyView(new NoData(getContext()).setImgRes(R.mipmap.no_data_order));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        });
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                goProductDetail(mAdapter.getItem(position).skuId, false);
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                goProductDetail(mAdapter.getItem(position).skuId, true);
            }
        });
    }

    private void goProductDetail(String skuId, boolean isBuy) {

//        DDProductDetailActivity.start(getContext(), skuId);

        if (isBuy) {
            EventBus.getDefault().postSticky(new MsgInstant(MsgInstant.ACTION_BUY));
        }
    }

    private void getIntentData() {
        mId = getArguments().getString("id");
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(MsgInstant msgInstant) {
        switch (msgInstant.getAction()) {
            case MsgInstant.ACTION_SEND_SECOND_KILL:
                LogUtils.e(mId + "接收到一个");
                for (InstantData.SecondKill secondKill : msgInstant.getSecondKills()) {
                    if (secondKill.id.equals(mId)) {
                        mSecondKill = secondKill;
                        initView();
                        getData(true);
                        LogUtils.e("匹配成功");
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        getData(true);
    }
}
