package com.dodomall.ddmall.ddui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.activity.DDProductDetailActivity;
import com.dodomall.ddmall.ddui.activity.StoreSettingsActivity;
import com.dodomall.ddmall.ddui.adapter.StoreProductAdapter;
import com.dodomall.ddmall.ddui.bean.ListResultBean;
import com.dodomall.ddmall.ddui.bean.StoreBean;
import com.dodomall.ddmall.ddui.bean.StoreProductBean;
import com.dodomall.ddmall.ddui.custom.DDEmptyView;
import com.dodomall.ddmall.ddui.custom.DDStoreShareDialog;
import com.dodomall.ddmall.ddui.service.ICommunityService;
import com.dodomall.ddmall.ddui.service.IStoreService;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.shared.basic.BaseFragment;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.event.EventMessage;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

/**
 * @author Jigsaw
 * @date 2019/3/21
 * 小店
 */
public class DDStoreFragment extends BaseFragment implements BaseQuickAdapter.OnItemChildClickListener {

    public static final int REQUEST_REFRESH = 0x11;

    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout mSmartRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.fl_toolbar)
    FrameLayout mFlToolbar;
    @BindView(R.id.tv_toolbar_nickname)
    TextView mTvToolbarNickname;
    @BindView(R.id.iv_bottom_share)
    ImageView mIvBottomShare;

    private DDEmptyView mDDEmptyView;

    private IStoreService mStoreService;
    private ICommunityService mCommunityService;

    private Unbinder mUnbinder;

    private StoreProductAdapter mStoreProductAdapter;

    private int mProductPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private StoreBean mStoreBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        initData();
        initView();

        return view;
    }

    private void initData() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mStoreService = ServiceManager.getInstance().createService(IStoreService.class);
        mCommunityService = ServiceManager.getInstance().createService(ICommunityService.class);
    }

    private void initView() {

        //设置拉动事件监听
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mProductPage = 1;
                mSmartRefreshLayout.setNoMoreData(false);
//                mStoreProductAdapter.getItems().clear();
                loadNetData();
            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mProductPage++;
                getProductList();
            }
        });
        mSmartRefreshLayout.setEnableLoadMore(true);

        mRecyclerView.setFocusable(false);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mStoreProductAdapter = new StoreProductAdapter();
        View header = LayoutInflater.from(getContext()).inflate(R.layout.layout_store_header, mRecyclerView, false);
        mStoreProductAdapter.setHeaderView(header);
        mStoreProductAdapter.setHeaderAndEmpty(true);
        mRecyclerView.setAdapter(mStoreProductAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int verticalOffset = mRecyclerView.computeVerticalScrollOffset();
                onScroll(verticalOffset);
//                DLog.i("onScrolled verticalOffset:" + verticalOffset);
            }
        });

        initClickListener();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mStoreBean == null && isVisibleToUser && isAdded() && SessionUtil.getInstance().isLogin()) {
            mIvBottomShare.setVisibility(SessionUtil.getInstance().isMaster() ? View.VISIBLE : View.GONE);
            loadNetData();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REFRESH && resultCode == RESULT_OK) {
            getStoreInfo();
        }
    }

    private void initClickListener() {
        mStoreProductAdapter.getHeaderHolder().tvStoreEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 编辑店铺
                startActivityForResult(new Intent(getContext(), StoreSettingsActivity.class), REQUEST_REFRESH);
            }
        });

        mStoreProductAdapter.setOnItemChildClickListener(this);
        mStoreProductAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (mStoreProductAdapter.getData() == null || mStoreProductAdapter.getData().isEmpty()
                        || position >= adapter.getItemCount() || mStoreProductAdapter.getData().get(position) == null) {
                    DLog.e("item 不能为null");
                    return;
                }
                DDProductDetailActivity.start(getActivity(), mStoreProductAdapter.getItem(position).getProductId());
            }
        });

        mIvBottomShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareStore();
            }
        });

    }

    private void shareStore() {
        // TODO:Jigsaw 2019/4/1 小店 分享相关
        new DDStoreShareDialog(getActivity()).show();
    }

    private void loadNetData() {
        if (!SessionUtil.getInstance().isLogin()) {
            return;
        }
        getStoreInfo();
        getProductList();
    }

    private void getStoreInfo() {
        APIManager.startRequest(mStoreService.getStoreInfo(), new BaseRequestListener<StoreBean>() {
            @Override
            public void onSuccess(StoreBean result) {
                super.onSuccess(result);
                if (result != null) {
                    mStoreBean = result;
                    mStoreProductAdapter.setHeaderData(result);
                    setToolbarNickname(result.getNickName());
                }
            }
        });
    }

    private void getProductList() {
        APIManager.startRequest(mStoreService.getStoreProductList(mProductPage, 10),
                new BaseRequestListener<ListResultBean<StoreProductBean>>(getActivity()) {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(ListResultBean<StoreProductBean> result) {
                        super.onSuccess(result);
                        finishRefresh();
                        if (mProductPage == 1 && mStoreBean != null) {
                            addStoreBrowserRecord();
                        }
                        if (result.getTotalRecord() == 0) {
                            showEmptyView();
                            return;
                        }
                        if (result.getDatas().size() > 0) {
                            if (mProductPage == 1) {
                                mStoreProductAdapter.replaceData(result.getDatas());
                            } else {
                                mStoreProductAdapter.addData(result.getDatas());
                            }
                        } else {
                            if (mProductPage > 1) {
                                mSmartRefreshLayout.setNoMoreData(true);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        finishRefresh();
                        if (mProductPage > 1) {
                            mProductPage--;
                        }
                    }
                });
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        if (mStoreProductAdapter.getData() == null || mStoreProductAdapter.getData().isEmpty() || position >= adapter.getItemCount()
                || mStoreProductAdapter.getData().get(position) == null) {
            DLog.e("item 不能为null");
            return;
        }

        String spuId = mStoreProductAdapter.getItem(position).getProductId();
        switch (view.getId()) {
            case R.id.tv_btn_product_top:
                makeProductItemTop(spuId, position);
                break;
            case R.id.tv_btn_product_remove:
                removeProductItem(spuId, position);
                break;
            case R.id.tv_btn_product_share:
                shareProductItem(spuId);
                break;
            case R.id.tv_btn_product_buy:
                DDProductDetailActivity.start(getActivity(), spuId);
                break;
        }
    }

    private void makeProductItemTop(String spuId, final int position) {

        if (position == 0) {
            return;
        }

        APIManager.startRequest(mStoreService.topProduct(spuId), new BaseRequestListener(getActivity()) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                mStoreProductAdapter.makeItemTop(position);
            }
        });
    }

    private void removeProductItem(String spuId, final int position) {
        APIManager.startRequest(mStoreService.removeProduct(spuId), new BaseRequestListener(getActivity()) {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                mStoreProductAdapter.remove(position);
            }
        });
    }

    private void addStoreBrowserRecord() {
        APIManager.startRequest(mStoreService.addStoreBrowserRecord(mStoreBean.getMemberIncId()), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
            }
        });
    }

    private void shareProductItem(String spuId) {
        new DDStoreShareDialog(getActivity()).show();
    }


    private void showEmptyView() {

        if (mDDEmptyView == null) {
            mDDEmptyView = new DDEmptyView(getContext());
            mStoreProductAdapter.setEmptyView(mDDEmptyView);
        }

        mDDEmptyView.setEmptyImage(R.mipmap.ic_empty_store);
        mDDEmptyView.setEmptyDesc("没有上架任何商品");
        mDDEmptyView.setEmptyBtn(SessionUtil.getInstance().isMaster() ? "去逛逛" : "戳一下邀请人");
        if (!SessionUtil.getInstance().isMaster()) {
            mDDEmptyView.setEmptyBtnEnable(mStoreBean != null ? !mStoreBean.isPokedFlag() : false);
            mDDEmptyView.setEmptyBtn("已通知");
        }
        mDDEmptyView.setOnEmptyBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SessionUtil.getInstance().isMaster()) {
                    // 去逛逛
                    EventBus.getDefault().post(new EventMessage(Event.viewHome));
                } else {
                    notifyStoreMaster();
                }
            }
        });

    }

    private void notifyStoreMaster() {
        APIManager.startRequest(mStoreService.notifyStoreMaster(mStoreBean.getMemberIncId()),
                new BaseRequestListener() {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        mDDEmptyView.setEmptyBtn("已通知");
                        mDDEmptyView.setEmptyBtnEnable(false);
                    }
                });
    }

    private void setToolbarNickname(String nickName) {
        mTvToolbarNickname.setText(nickName);
    }

    private void finishRefresh() {
        if (mProductPage > 1) {
            mSmartRefreshLayout.finishLoadMore();
        } else {
            mSmartRefreshLayout.finishRefresh();
        }
    }

    public void onScroll(int dy) {
        mFlToolbar.setVisibility(dy >= 240 ? View.VISIBLE : View.GONE);
//        DLog.i("mLinearLayoutManager.findFirstVisibleItemPosition()" + mLinearLayoutManager.findLastVisibleItemPosition());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();

    }

    // 成为店主 或 登录成功 刷新小店内容
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(EventMessage eventMessage) {
        switch (eventMessage.getEvent()) {
            case loginSuccess:
            case becomeStoreMaster:
                if (mStoreProductAdapter != null) {
                    mIvBottomShare.setVisibility(SessionUtil.getInstance().isMaster() ? View.VISIBLE : View.GONE);
                    mStoreProductAdapter.updateUserType();
                    mProductPage = 1;
                    loadNetData();
                }
                break;
        }
    }


}
