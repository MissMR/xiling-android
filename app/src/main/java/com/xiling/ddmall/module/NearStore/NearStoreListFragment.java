package com.xiling.ddmall.module.NearStore;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.blankj.utilcode.utils.LocationUtils;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.NearStore.adapter.NearStoreAdapter;
import com.xiling.ddmall.module.NearStore.model.NearStoreModel;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.user.NewRegisterActivity;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseFragment;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.component.HeaderLayout;
import com.xiling.ddmall.shared.component.NoData;
import com.xiling.ddmall.shared.decoration.ListDividerDecoration;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.RvUtils;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;


/**
 * A simple {@link Fragment} subclass.
 *
 * @author zjm
 */
public class NearStoreListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.rvList)
    RecyclerView mRvList;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;
    Unbinder unbinder;
    @BindView(R.id.headerLayout)
    HeaderLayout mHeaderLayout;

    private ArrayList<NearStoreModel.DatasEntity> mDatas = new ArrayList<>();
    private NearStoreAdapter mAdapter;
    private IUserService mService;
    private AMapLocationClient mLocationClient;
    private AMapLocation mLocation;
    private boolean mIsSelect;


    public static NearStoreListFragment newInstance(boolean isSlect) {

        Bundle args = new Bundle();
        args.putBoolean("isSelect", isSlect);
        NearStoreListFragment fragment = new NearStoreListFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_near_store, container, false);
        getIntentData();
        unbinder = ButterKnife.bind(this, view);
        mService = ServiceManager.getInstance().createService(IUserService.class);
        initView();
        return view;
    }

    private void getIntentData() {
        mIsSelect = getArguments().getBoolean("isSelect");
    }


    @Override
    protected boolean isNeedLogin() {
        return false;
    }

    private void initData() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.request(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    initLocation();
                    mLocationClient.startLocation();
                    mAdapter.setEmptyView(new NoData(getContext()));
                } else {
                    mLayoutRefresh.setRefreshing(false);
                    mAdapter.setEmptyView(getNoPermissonView());
                    ToastUtil.error("没有权限");
                }
            }
        });

    }

    private NoData getNoPermissonView() {
        NoData noData = new NoData(getContext());
        noData.setImgRes(R.drawable.ic_no_data_location);
        noData.setTextView("无法获取您当前的位置，请开放软件定位权限");
        TextView tvReRefresh = (TextView) noData.findViewById(R.id.tvReRefresh);
        tvReRefresh.setVisibility(View.VISIBLE);
        tvReRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });
        return noData;
    }

    private NoData getLocationErrorView() {
        NoData noData = new NoData(getContext());
        noData.setImgRes(R.drawable.ic_no_data_location);
        noData.setTextView("获取位置信息失败，请重试");
        TextView tvReRefresh = (TextView) noData.findViewById(R.id.tvReRefresh);
        tvReRefresh.setVisibility(View.VISIBLE);
        tvReRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutRefresh.setRefreshing(true);
                mLocationClient.startLocation();
                mAdapter.setEmptyView(new NoData(getContext()));
            }
        });
        return noData;
    }

    private void initLocation() {
        mLocationClient = new AMapLocationClient(getActivity().getApplicationContext());
        mLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mLayoutRefresh.setRefreshing(false);
                mLocation = aMapLocation;
                getListData(true);
            }
        });
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        option.setOnceLocation(false);
        option.setNeedAddress(true);
        option.setMockEnable(true);
        option.setLocationCacheEnable(true);
        option.setInterval(1000 * 60 * 10);
        mLocationClient.setLocationOption(option);
    }

    private void initView() {
        mHeaderLayout.setTitle("附近店主");
        if (mIsSelect) {
            mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
            mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        mLayoutRefresh.setOnRefreshListener(this);
        mAdapter = new NearStoreAdapter(mDatas);
        RvUtils.configRecycleView(getActivity(), mRvList, mAdapter);
        mRvList.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                itemClick(mDatas.get(position), view);
            }
        });
        mRvList.addItemDecoration(new ListDividerDecoration(getContext()));
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getListData(false);
            }
        });

        mLayoutRefresh.post(new Runnable() {
            @Override
            public void run() {
                mLayoutRefresh.setRefreshing(true);
                initData();
            }
        });
    }

    private void itemClick(NearStoreModel.DatasEntity datasEntity, View view) {
//        Intent intent = new Intent(getContext(), NearStoreDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("data", datasEntity);
//        intent.putExtras(bundle);
//        intent.putExtra("isSelect", mIsSelect);
//        startActivity(intent);
        Intent intent = new Intent(getContext(), NewRegisterActivity.class);
        User user = new User();
        user.phone = datasEntity.phone;
        user.invitationCode = datasEntity.inviteCode;
        user.nickname = datasEntity.nickName;
        user.avatar = datasEntity.headImage;
        intent.putExtra("user", user);
        startActivity(intent);
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_SELECT_STORE));
    }

    private void getListData(boolean isRefresh) {
        if (mLocation == null) {
            ToastUtils.showShortToast("等待定位数据");
            mLayoutRefresh.setRefreshing(false);
            return;
        }
        if (mLocation.getErrorCode() != 0) {
            ToastUtil.error("定位失败 " + mLocation.getErrorInfo());
            mLayoutRefresh.setRefreshing(false);
            mAdapter.setEmptyView(getLocationErrorView());
            return;
        }
        if (isRefresh) {
            mDatas.clear();
            mAdapter.notifyDataSetChanged();
        }
        LogUtils.e(
                "getLongitude " + mLocation.getLongitude() +
                        "\ngetLatitude " + mLocation.getLatitude() +
                        "\ngetProvince " + mLocation.getProvince() +
                        "\ngetCity " + mLocation.getCity()
        );
        APIManager.startRequest(
                mService.getNearStoreModelList(
                        mLocation.getLongitude(),
                        mLocation.getLatitude(),
                        mLocation.getProvince(),
                        mLocation.getCity(),
                        mDatas.size() / Constants.PAGE_SIZE + 1,
                        Constants.PAGE_SIZE
                ),
                new BaseRequestListener<NearStoreModel>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(NearStoreModel model) {
                        mDatas.addAll(model.datas);
                        if (model.datas.size() < Constants.PAGE_SIZE) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.loadMoreComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mAdapter.loadMoreFail();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        LocationUtils.unregister();
    }

    @Override
    public void onRefresh() {
        getListData(true);
    }
}
