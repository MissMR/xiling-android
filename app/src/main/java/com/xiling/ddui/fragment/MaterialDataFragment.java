package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiling.R;
import com.xiling.ddui.adapter.DDCommunityDataAdapter;
import com.xiling.ddui.bean.CommunityDataBean;
import com.xiling.ddui.bean.ListResultBean;
import com.xiling.ddui.service.ICommunityService;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.util.RvUtils;
import com.xiling.shared.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * created by Jigsaw at 2018/10/11
 * 商品素材/营销素材
 */
public class MaterialDataFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    // 商品素材
    public static final int MATERIAL_PRODUCT = 0;
    // 营销素材
    public static final int MATERIAL_MARKETING = 1;


    @BindView(R.id.rv_material)
    RecyclerView mRvMaterial;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.iv_to_top)
    ImageView mIvToTop;

    private Unbinder unbinder;
    private DDCommunityDataAdapter mMaterialAdapter;
    private ICommunityService mCommunityService;
    private int mPage = 1;
    private int mType = MATERIAL_PRODUCT;

    public MaterialDataFragment() {
    }

    public static MaterialDataFragment newInstance(int type) {
        MaterialDataFragment materialDataFragment = new MaterialDataFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Extras.TYPE, type);
        materialDataFragment.setArguments(bundle);
        return materialDataFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mType = bundle.getInt(Constants.Extras.TYPE, MATERIAL_PRODUCT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community_circle, null);
        unbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        initView();
        initData();
        return view;
    }

    private void initData() {
        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
        ToastUtil.showLoading(getActivity());
        getMaterialList();
    }

    private void initView() {

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mSmartRefreshLayout.setRefreshFooter(new ClassicsFooter(getActivity()));
        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);
        mSmartRefreshLayout.setFocusable(false);

        mMaterialAdapter = new DDCommunityDataAdapter(getActivity(), getAdapterModeByType(mType));
        mRvMaterial.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvMaterial.setNestedScrollingEnabled(false);
        mRvMaterial.setAdapter(mMaterialAdapter);
        mRvMaterial.setFocusable(false);

        RvUtils.clearItemAnimation(mRvMaterial);

    }


    private void getMaterialList() {
        APIManager.startRequest(mCommunityService.getMaterialList(mPage, 10,
                mType == MATERIAL_MARKETING ? ICommunityService.TYPE_MATERIAL_MARKETING : ICommunityService.TYPE_MATERIAL_PRODUCT),
                new BaseRequestListener<ListResultBean<CommunityDataBean>>(getActivity()) {
                    @Override
                    public void onSuccess(ListResultBean<CommunityDataBean> result) {
                        super.onSuccess(result);
                        if (isDetached()) {
                            return;
                        }
                        finishRefresh();
                        if (result == null || result.getDatas() == null || result.getDatas().isEmpty()) {
                            if (mPage > 1) {
                                mPage--;
                            }
                            mSmartRefreshLayout.setNoMoreData(true);
                            return;
                        }
                        mSmartRefreshLayout.setNoMoreData(false);
                        if (mPage == 1) {
                            mMaterialAdapter.getItems().clear();
                        }
                        mMaterialAdapter.addItems(result.getDatas());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (isDetached()) {
                            return;
                        }
                        finishRefresh();
                    }
                });
    }

    private void initFloatButton() {
        mIvToTop.setVisibility(View.GONE);
        mRvMaterial.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                mIvToTop.setVisibility(dy > 0 ? View.VISIBLE : View.GONE);
            }
        });
        mIvToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRvMaterial.scrollTo(0, 0);
            }
        });
    }


    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 1;
        getMaterialList();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        getMaterialList();
    }

    private void finishRefresh() {
        if (mPage == 1) {
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventMessage message) {
        if (message.getEvent().equals(Event.materialUnLike)) {
            String id = (String) message.getData();
            mMaterialAdapter.setItemUnLike(id);
        }
    }

    private DDCommunityDataAdapter.Mode getAdapterModeByType(int type) {
        return type == MATERIAL_PRODUCT ? DDCommunityDataAdapter.Mode.MaterialProduct : DDCommunityDataAdapter.Mode.MaterialMarketing;
    }
}
