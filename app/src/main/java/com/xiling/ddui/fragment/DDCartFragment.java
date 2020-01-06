package com.xiling.ddui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.sobot.chat.utils.ScreenUtils;
import com.xiling.R;
import com.xiling.ddui.adapter.CardExpandableAdapter;
import com.xiling.ddui.bean.CardExpandableBean;
import com.xiling.ddui.bean.XLCardListBean;
import com.xiling.dduis.adapter.ShopListAdapter;
import com.xiling.dduis.bean.HomeRecommendDataBean;
import com.xiling.dduis.custom.DDNumberTextView;
import com.xiling.dduis.custom.divider.SpacesItemDecoration;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.DDHomeService;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.xiling.shared.Constants.PAGE_SIZE;
import static com.xiling.shared.constant.Event.cartAmountUpdate;
import static com.xiling.shared.constant.Event.viewHome;

/**
 * @author 逄涛
 * 首页--购物车
 */
public class DDCartFragment extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {

    public final static String TITLE_TEXT = "购物车";
    //获取购物车数据
    ICartService mCartService;
    DDHomeService homeService;
    @BindView(R.id.recycler_card)
    RecyclerView recyclerCard;
    @BindView(R.id.recycler_recommend)
    RecyclerView recyclerRecommend;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.headerLayout)
    HeaderLayout headerLayout;

    Unbinder unbinder;
    @BindView(R.id.checkAll)
    TextView checkAll;
    @BindView(R.id.totalTv)
    DDNumberTextView totalTv;
    @BindView(R.id.nextBtn)
    TextView nextBtn;
    @BindView(R.id.deleteBtn)
    TextView deleteBtn;
    @BindView(R.id.layoutNodata)
    LinearLayout layoutNodata;
    private int pageOffset = 1;
    private int pageSize = PAGE_SIZE;
    private int totalPage = 0;
    List<HomeRecommendDataBean.DatasBean> recommendDataList = new ArrayList<>();
    ShopListAdapter recommendAdapter;
    CardExpandableAdapter cardExpandableAdapter;

    private boolean isEdit = false;


    public static DDCartFragment newInstance() {
        Bundle args = new Bundle();
        DDCartFragment fragment = new DDCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initService();
        View view = inflater.inflate(R.layout.fragment_dd_cart, container, false);
        unbinder = ButterKnife.bind(this, view);
        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        requestData();


        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null) {
            pageOffset = 1;
            requestCardData();
        }
    }

    private void initHeadLayout() {
        headerLayout.setTitle(TITLE_TEXT);
        headerLayout.setRightText("编辑");
        headerLayout.hideRightItem();
        headerLayout.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO 编辑
                if (cardExpandableAdapter != null) {
                    cardExpandableAdapter.setEdit(!isEdit);
                    if (!isEdit) {
                        headerLayout.setRightText("完成");
                        nextBtn.setVisibility(View.GONE);
                        deleteBtn.setVisibility(View.VISIBLE);
                    } else {
                        headerLayout.setRightText("编辑");
                        nextBtn.setVisibility(View.VISIBLE);
                        deleteBtn.setVisibility(View.GONE);
                    }
                    isEdit = !isEdit;
                }
            }
        });
    }

    private void initView() {
        initHeadLayout();
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setOnLoadMoreListener(this);
        refreshLayout.setOnRefreshListener(this);

        GridLayoutManager recommendLayoutManager = new GridLayoutManager(getActivity(), 2) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerRecommend.setLayoutManager(recommendLayoutManager);
        recyclerRecommend.addItemDecoration(new SpacesItemDecoration(ScreenUtils.dip2px(getActivity(), 12), ScreenUtils.dip2px(getActivity(), 12)));
        recommendAdapter = new ShopListAdapter(R.layout.item_home_recommend, recommendDataList);
        recyclerRecommend.setAdapter(recommendAdapter);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerCard.setLayoutManager(linearLayoutManager);
        cardExpandableAdapter = new CardExpandableAdapter(new ArrayList<CardExpandableBean<XLCardListBean.SkuProductListBean>>());
        recyclerCard.setAdapter(cardExpandableAdapter);
        cardExpandableAdapter.setOnSelectChangeListener(new CardExpandableAdapter.OnSelectChangeListener() {
            @Override
            public void onPriceChange(double price) {
                totalTv.setText("¥" + price);
            }

            @Override
            public void onSelectChange(int selectSize) {
                if (selectSize != 0) {
                    nextBtn.setText("结算(" + selectSize + ")");
                } else {
                    nextBtn.setText("结算");
                }

                if (cardExpandableAdapter.isAllSelect()) {
                    checkAll.setSelected(true);
                    checkAll.setText("全不选");
                } else {
                    checkAll.setSelected(false);
                    checkAll.setText("全选");
                }

            }

            @Override
            public void onShopChange(CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean, int quantity) {
                requestAddCart(cardExpandableBean.getBean().getSkuId(), quantity, cardExpandableBean);
            }
        });

    }


    private void initService() {
        if (mCartService == null) {
            mCartService = ServiceManager.getInstance().createService(ICartService.class);
        }

        if (homeService == null) {
            homeService = ServiceManager.getInstance().createService(DDHomeService.class);
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (pageOffset < totalPage) {
            pageOffset++;
            requestRecommend();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageOffset = 1;
        requestData();
    }

    private void requestData() {
        requestUpDataShopCardCount();
        requestCardData();
        requestRecommend();
    }

    private void requestCardData() {
        APIManager.startRequest(mCartService.getAllList(), new BaseRequestListener<List<XLCardListBean>>() {
            @Override
            public void onSuccess(List<XLCardListBean> result) {
                super.onSuccess(result);
                if (result != null && result.size() > 0) {
                    headerLayout.showRightItem();
                } else {
                    headerLayout.hideRightItem();
                }
                List<CardExpandableBean<XLCardListBean.SkuProductListBean>> cardExpandableBeanList = buildAdapterData(result);
                if (cardExpandableBeanList.size() > 0) {
                    layoutNodata.setVisibility(View.GONE);
                } else {
                    layoutNodata.setVisibility(View.VISIBLE);
                }
                cardExpandableAdapter.setNewData(cardExpandableBeanList);

                /**
                 * 每次刷新，重新计算购物车数量与价格
                 */
                int selectSize = cardExpandableAdapter.getSelectList().size();
                if (selectSize != 0) {
                    nextBtn.setText("结算(" + selectSize + ")");
                } else {
                    nextBtn.setText("结算");
                }

                if (cardExpandableAdapter.isAllSelect()) {
                    checkAll.setSelected(true);
                    checkAll.setText("全不选");
                } else {
                    checkAll.setSelected(false);
                    checkAll.setText("全选");
                }
                cardExpandableAdapter.getSelectPrice();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);

                layoutNodata.setVisibility(View.VISIBLE);
                cardExpandableAdapter.setNewData(new ArrayList<CardExpandableBean<XLCardListBean.SkuProductListBean>>());
            }
        });
    }

    /**
     * 根据服务器返回数据，构造购物车UI所需要的数据
     */
    private List<CardExpandableBean<XLCardListBean.SkuProductListBean>> buildAdapterData(List<XLCardListBean> xlCardListBeanList) {
        List<CardExpandableBean<XLCardListBean.SkuProductListBean>> cardExpandableBeanList = new ArrayList<>();
        int mPosition = 0;
        for (int parentPosition = 0; parentPosition < xlCardListBeanList.size(); parentPosition++) {
            XLCardListBean xlCardListBean = xlCardListBeanList.get(parentPosition);
            List<XLCardListBean.SkuProductListBean> skuProductListBeanList = xlCardListBean.getSkuProductList();
            if (skuProductListBeanList != null && skuProductListBeanList.size() > 0) {
                CardExpandableBean<XLCardListBean.SkuProductListBean> parentBean = new CardExpandableBean(true);
                parentBean.setParentId(xlCardListBean.getStoreId());
                parentBean.setParentName(xlCardListBean.getStoreName());
                List<Integer> childPos = new ArrayList<>();
                parentBean.setChildPositions(childPos);
                parentBean.setPosition(mPosition);
                mPosition++;
                for (int childPosition = mPosition; childPosition < skuProductListBeanList.size() + 1; childPosition++) {
                    childPos.add(childPosition);
                }
                cardExpandableBeanList.add(parentBean);
                for (int childPosition = 0; childPosition < skuProductListBeanList.size(); childPosition++) {
                    XLCardListBean.SkuProductListBean skuProductListBean = skuProductListBeanList.get(childPosition);
                    CardExpandableBean<XLCardListBean.SkuProductListBean> childBean = new CardExpandableBean(false);
                    childBean.setBean(skuProductListBean);
                    childBean.setParentId(parentBean.getParentId());
                    childBean.setParentPosition(parentBean.getPosition());
                    childBean.setPosition(mPosition);
                    mPosition++;
                    cardExpandableBeanList.add(childBean);
                }

            }

        }

        // 同步选中状态
        if (cardExpandableAdapter != null && cardExpandableAdapter.getData() != null && cardExpandableAdapter.getData().size() > 0) {
            for (CardExpandableBean<XLCardListBean.SkuProductListBean> newBean : cardExpandableBeanList) {
                for (CardExpandableBean<XLCardListBean.SkuProductListBean> oldBean : cardExpandableAdapter.getData()) {
                    if (newBean.isParent()) {
                        if (oldBean.isParent()) {
                            if (newBean.getParentId().equals(oldBean.getParentId())) {
                                newBean.setSelect(oldBean.isSelect());
                                newBean.setEditSelect(oldBean.isEditSelect());
                            }
                        }

                    } else {
                        if (!oldBean.isParent()) {
                            if (newBean.getBean().getSkuId().equals(oldBean.getBean().getSkuId())) {
                                newBean.setSelect(oldBean.isSelect());
                                newBean.setEditSelect(oldBean.isEditSelect());
                            }
                        }
                    }
                }
            }
        }

        return cardExpandableBeanList;
    }

    /**
     * 推荐数据
     */
    private void requestRecommend() {
        APIManager.startRequest(homeService.getHomeRecommendData(pageOffset, pageSize), new BaseRequestListener<HomeRecommendDataBean>() {
            @Override
            public void onSuccess(HomeRecommendDataBean result) {
                super.onSuccess(result);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (result != null) {
                    if (result.getDatas() != null) {
                        if (pageOffset == 1) {
                            recommendDataList.clear();
                        }
                        totalPage = result.getTotalPage();
                        // 如果已经到最后一页了，关闭上拉加载
                        if (pageOffset >= totalPage) {
                            refreshLayout.setEnableLoadMore(false);
                        } else {
                            refreshLayout.setEnableLoadMore(true);
                        }

                        recommendDataList.addAll(result.getDatas());

                        if (pageOffset == 1) {
                            recommendAdapter.setNewData(recommendDataList);
                        } else {
                            recommendAdapter.setNewData(recommendDataList);
                        }

                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        switch (message.getEvent()) {
            case cartAmountUpdate:
                requestCardData();

                break;
        }
    }


    /**
     * 添加购物车
     */
    private void requestAddCart(String skuId, final int quantity, final CardExpandableBean<XLCardListBean.SkuProductListBean> cardExpandableBean) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("skuId", skuId);
        params.put("quantity", quantity);
        APIManager.startRequest(mCartService.addShopCart(APIManager.buildJsonBody(params)), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                if (result) {
                    requestUpDataShopCardCount();
                }
            }
        });
    }


    /**
     * 删除商品
     */
    private void requestDeleteCart(List<String> skuIds) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("skuIds", skuIds);
        APIManager.startRequest(mCartService.deleteShopCart(APIManager.buildJsonBody(params)), new BaseRequestListener<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                super.onSuccess(result);
                // requestCardData();
                isEdit = false;
                cardExpandableAdapter.setEdit(false);
                nextBtn.setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.GONE);
                headerLayout.setRightText("编辑");

                requestUpDataShopCardCount();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    private void requestUpDataShopCardCount() {
        APIManager.startRequest(mCartService.getCardCount(), new BaseRequestListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                super.onSuccess(result);
                EventBus.getDefault().post(new EventMessage(cartAmountUpdate, result));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @OnClick({R.id.checkAll, R.id.nextBtn, R.id.deleteBtn, R.id.tvGoMain})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.checkAll:
                if (cardExpandableAdapter != null) {
                    if (!cardExpandableAdapter.isAllSelect()) {
                        checkAll.setSelected(true);
                        cardExpandableAdapter.selectAll();
                        checkAll.setText("全不选");
                    } else {
                        checkAll.setSelected(false);
                        cardExpandableAdapter.cancleSelectAll();
                        checkAll.setText("全选");
                    }
                }
                break;
            case R.id.nextBtn:
                //结算
                if (cardExpandableAdapter != null) {
                    Log.d("pangtao", "结算 ：" + cardExpandableAdapter.getSelectList());
                }
                break;
            case R.id.deleteBtn:
                //删除所选
                if (cardExpandableAdapter != null) {
                    List<CardExpandableBean<XLCardListBean.SkuProductListBean>> delectList = cardExpandableAdapter.getSelectList();
                    List<String> skuIdList = new ArrayList<>();
                    for (CardExpandableBean<XLCardListBean.SkuProductListBean> bean : delectList) {
                        skuIdList.add(bean.getBean().getSkuId());
                    }

                    if (skuIdList.size() > 0){
                        requestDeleteCart(skuIdList);
                    }else{
                        ToastUtil.error("您还没有选中商品");
                    }


                }
                break;
            case R.id.tvGoMain:
                EventBus.getDefault().post(new EventMessage(viewHome));
                break;
        }
    }


}
