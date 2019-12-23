package com.xiling.ddui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.R;

import com.xiling.ddui.adapter.DDCartAdapter;
import com.xiling.ddui.bean.DDCartRowBean;

import com.xiling.ddui.bean.DDSuggestListBean;
import com.xiling.ddui.custom.DDFooterView;
import com.xiling.ddui.manager.CartAmountManager;
import com.xiling.ddui.tools.DLog;
import com.xiling.module.auth.Config;
import com.xiling.module.pay.PayActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.CartItem;
import com.xiling.shared.bean.CartStore;
import com.xiling.shared.bean.MemberRatio;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.constant.Event;
import com.xiling.shared.contracts.RequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.CartManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;
import com.google.common.base.Joiner;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class DDCartFragment extends BaseFragment implements View.OnClickListener, OnLoadMoreListener, OnRefreshListener {

    public final static String kType = "TYPE";
    public final static int TYPE_FRAGMENT = 0x01;
    public final static int TYPE_ACTIVITY = 0x02;
    int type = TYPE_FRAGMENT;

    boolean mInEditMode = false;

    public final static String TITLE_TEXT = "购物车";
    public final static String TITLE_TEXT_EDIT = "管理";
    public final static String TITLE_TEXT_FINISH = "完成";

    public final static String BOTTOM_TEXT_PAY = "结算";

    //获取购物车数据
    ICartService mCartService;

    //获取会员打折率
    List<MemberRatio> mMemberRatio;
    IUserService mMemberRatioService;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mSmartRefreshLayout;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.headerLayout)
    HeaderLayout mHeaderLayout;

    @BindView(R.id.layoutBottom)
    LinearLayout mLayoutBottom;

    @BindView(R.id.tvRatio)
    TextView mTvRatio;

    @BindView(R.id.totalLayout)
    LinearLayout mTotalLayout;

    @BindView(R.id.totalTv)
    TextView mTotalTv;

    @BindView(R.id.nextBtn)
    TextView mNextBtn;

    @BindView(R.id.deleteBtn)
    TextView mDeleteBtn;

    @BindView(R.id.checkAll)
    TextView mCheckAllBtn;

    //数据源显示适配器
    DDCartAdapter mCartAdapter = null;

    GridLayoutManager gManager = null;

    protected int mPage = 1;
    protected int mSize = 10;

    public static DDCartFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(kType, type);
        DDCartFragment fragment = new DDCartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dd_cart, container, false);
        ButterKnife.bind(this, view);

        getIntentData();

        initService();

        initTitleView();
        initView();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null && isReload) {
            loadNetData();
        }
    }

    public void initService() {
        if (mMemberRatioService == null) {
            mMemberRatioService = ServiceManager.getInstance().createService(IUserService.class);

        }
        if (mCartService == null) {
            mCartService = ServiceManager.getInstance().createService(ICartService.class);
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    boolean isReload = true;

    @Override
    public void onResume() {
        super.onResume();
        DLog.d("DDCart.onResume");
        if (isReload) {
            loadNetData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    private void getIntentData() {
        type = getArguments().getInt(kType);
    }

    /**
     * 初始化标题栏
     */
    void initTitleView() {
        switch (type) {
            case TYPE_FRAGMENT:
                mHeaderLayout.setTitle(TITLE_TEXT);
                mHeaderLayout.setRightText(TITLE_TEXT_EDIT);
                mHeaderLayout.setRightTextColor(R.color.mainColor);
                mHeaderLayout.setOnRightClickListener(this);
                break;
            case TYPE_ACTIVITY:
                mHeaderLayout.setTitle(TITLE_TEXT);
                mHeaderLayout.setRightTextColor(R.color.mainColor);
                mHeaderLayout.setRightText(TITLE_TEXT_EDIT);
                mHeaderLayout.setLeftDrawable(R.mipmap.icon_back_black);
                mHeaderLayout.setOnLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
                mHeaderLayout.setOnRightClickListener(this);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化界面
     */
    void initView() {

        mCartAdapter = new DDCartAdapter(getContext());

        gManager = new GridLayoutManager(getContext(), 2);
        gManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //获取这个位置的View类型
                int type = mCartAdapter.getItemViewType(position);
                if (type == DDCartRowBean.CartType.SuggestData.ordinal()) {
//                    DLog.i("猜你喜欢:2列");
                    //猜你喜欢为2列数据 返回值是1/2的意思

                    //2.0需求变更为1列
                    return 2;
                } else {
                    //其他数据为1列数据,返回值是2/2的意思
//                    DLog.i(type + ":1列");
                    return 2;
                }
            }
        });
        mRecyclerView.setLayoutManager(gManager);

        mSmartRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
        DDFooterView footer = new DDFooterView(getContext());
        mSmartRefreshLayout.setRefreshFooter(footer);

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mSmartRefreshLayout.setEnableRefresh(true);
        mSmartRefreshLayout.setEnableLoadMore(true);

        mCartAdapter.setHasStableIds(false);
        mRecyclerView.setAdapter(mCartAdapter);
        mCartAdapter.notifyDataSetChanged();

        //Activity主动刷新数据
        if (type == TYPE_ACTIVITY) {
            loadNetData();
        }
    }

    /**
     * 初始化价格
     */
    void initPrice() {
        List<CartItem> data = mCartAdapter.getSelectCartItem();
        long totalPrice = CartManager.getSelectTotalMoney(data);
        if (!Config.IS_DISCOUNT) {
            mTvRatio.setText("不含运费");
            mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice));
            return;
        }
//        long orderRadioMoney = CartManager.getOrderRadioMoney(mDatas, mMemberRatio);
//        if (orderRadioMoney <= 0) {
        mTvRatio.setText("暂无折扣");
        mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice));
//        } else {
//            mTvRatio.setText(
//                    String.format("会员折扣(-%s)",
//                            ConvertUtil.centToCurrency(getContext(), orderRadioMoney))
//            );
//            mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice - orderRadioMoney));
//        }
    }

    /**
     * 初始化购买按钮
     */
    void initPayButton() {
        List<CartItem> data = mCartAdapter.getSelectCartItem();
        int selectedQuantity = CartManager.getSelectedItemQuantity(data);
        if (selectedQuantity > 0) {
            mNextBtn.setEnabled(true);
            mNextBtn.setText(BOTTOM_TEXT_PAY + "(" + selectedQuantity + ")");
        } else {
            mNextBtn.setEnabled(false);
            mNextBtn.setText(BOTTOM_TEXT_PAY + "");
        }
    }

    void loadButtonStatus() {
        mTotalLayout.setVisibility(mInEditMode ? View.GONE : View.VISIBLE);
        mNextBtn.setVisibility(mTotalLayout.getVisibility());
        mDeleteBtn.setVisibility(mInEditMode ? View.VISIBLE : View.GONE);
        mCheckAllBtn.setSelected(false);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPage = 1;
        loadNetData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPage++;
        getSuggestData();
    }

    protected void finishRefresh() {
        if (mPage == 1) {
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore(0);
        }
    }

    public void loadNetData() {
        //检查用户是否登录
        if (!SessionUtil.getInstance().isLogin()) {
            //如果是Activity模式的时候跳登录
            if (type == TYPE_ACTIVITY) {
                EventBus.getDefault().post(new EventMessage(Event.goToLogin));
            }
            //中断网络请求
            return;
        }

        //去掉获取积分的逻辑 at 2018/10/25 by hanQ
//        APIManager.startRequest(mMemberRatioService.getMemberRatio(), new BaseRequestListener<List<MemberRatio>>() {
//            @Override
//            public void onSuccess(List<MemberRatio> memberRatio) {
//                mMemberRatio = memberRatio;
//                initPrice();
//            }
//        });

        initService();
        try {
            Observable<RequestResult<List<CartStore>>> request = mCartService.getAllList();
            APIManager.startRequest(request, new BaseRequestListener<List<CartStore>>() {
                @Override
                public void onSuccess(List<CartStore> result) {

                    isReload = false;

                    mCheckAllBtn.setSelected(false);

                    int amount = 0;
                    for (CartStore cartStore : result) {
                        for (CartItem product : cartStore.products) {
                            if (product.status != 0 && product.stock > 0 && product.amount <= product.stock) {
                                amount += product.amount;
                            }
                        }
                    }
                    CartAmountManager.share().setAmount(amount);

                    mInEditMode = false;
                    mCartAdapter.setEditMode(mInEditMode);
                    loadButtonStatus();
                    initTitleView();


                    if (result.size() == 0) {
                        DLog.d("隐藏右侧按钮");
                        mHeaderLayout.hideRightItem();
                    } else {
                        DLog.d("显示右侧按钮");
                        mHeaderLayout.showRightItem();
                    }

                    //设置购物车数据
                    //mCartAdapter.setCartData(new ArrayList<CartStore>());//测试空数据代码
                    mCartAdapter.setCartData(result);

                    //显示购物车数据
//                mCartAdapter.showData();

                    initPrice();
                    initPayButton();
                    mLayoutBottom.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    mPage = 1;
                    getSuggestData();
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    finishRefresh();
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    finishRefresh();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取猜你喜欢的数据
     */
    public void getSuggestData() {
        initService();
        APIManager.startRequest(mCartService.getSuggestProduct(mPage, mSize), new RequestListener<DDSuggestListBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DDSuggestListBean result) {
                super.onSuccess(result);
//                mCartAdapter.setSuggestData(new ArrayList<ProductBean>());//测试空数据代码
                if (mPage > 1) {
                    mCartAdapter.appendSuggestData(result.getDatas());
                    mSmartRefreshLayout.finishLoadMore();
                } else {
                    mCartAdapter.setSuggestData(result.getDatas());
                }

                mCartAdapter.showData();

                int total = result.getTotalRecord();
                boolean hasMore = (mCartAdapter.getSuggestCount() < total);
                //设置是否还有更多数据
                mSmartRefreshLayout.setNoMoreData(!hasMore).finishLoadMore();
            }

            @Override
            public void onError(Throwable e) {
                finishRefresh();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    @Override
    public void onClick(View view) {

        if (TITLE_TEXT_EDIT.equals(mHeaderLayout.getRightText())) {
            mHeaderLayout.setRightText(TITLE_TEXT_FINISH);
            mInEditMode = true;
        } else {
            mHeaderLayout.setRightText(TITLE_TEXT_EDIT);
            mInEditMode = false;
        }
        mCartAdapter.setEditMode(mInEditMode);
        mCartAdapter.notifyDataSetChanged();

        initPrice();
        initPayButton();
        loadButtonStatus();
    }

    @OnClick(R.id.checkAll)
    public void onCheckAll(View view) {
        view.setSelected(!view.isSelected());
        mCartAdapter.checkAll(view.isSelected());
        initPrice();
        initPayButton();
    }

    @OnClick(R.id.nextBtn)
    public void onNext() {
        if (!mNextBtn.isEnabled()) {
            return;
        }

        List<CartItem> selects = mCartAdapter.getSelectCartItem();
        DLog.i("购物车选中过来的有效商品数量");

        ArrayList<String> selectedIds = CartManager.getSelectedItemIds(selects);
        if (selectedIds.size() == 0) {
            ToastUtil.error("请选择商品");
            return;
        }

        Intent intent = new Intent(getContext(), PayActivity.class);
        intent.putExtra("from", "cart");
        intent.putStringArrayListExtra("skuIds", selectedIds);
        startActivity(intent);
    }

    @OnClick(R.id.deleteBtn)
    public void deleteSelectedItems() {
        final List<CartItem> data = mCartAdapter.getSelectCartItem();
        ArrayList<String> ids = CartManager.getSelectedItemIds(data);
        if (ids.size() == 0) {
            ToastUtil.error("请选择商品");
            return;
        }
        initService();
        APIManager.startRequest(mCartService.removeItem(Joiner.on(",").join(ids)), new BaseRequestListener<Object>() {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("删除成功");
                CartManager.removeSelectedItem(data);
                mCartAdapter.notifyDataSetChanged();
                loadNetData();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateCartItem(EventMessage message) {
        Event event = message.getEvent();
        if (event == Event.selectCartItem) {
            DLog.d("updateCartItem");
            //设置全选状态
            boolean isSelectAll = mCartAdapter.isSelectAll();
            mCheckAllBtn.setSelected(isSelectAll);
            if (!mInEditMode) {
                initPrice();
                initPayButton();
            }
        } else if (event == Event.AddToCart) {
            DLog.d("isReload = true");
            isReload = true;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(EventMessage message) {
        if (message.getEvent().equals(Event.createOrderSuccess) || message.getEvent().equals(Event.loginSuccess)) {
            //订单支付成功后刷新购物车 //登录成功
            loadNetData();
        } else if (message.getEvent().equals(Event.logout)) {
            //用户退出后清空购物车
            mCartAdapter.clearCart();
        }
    }

}
