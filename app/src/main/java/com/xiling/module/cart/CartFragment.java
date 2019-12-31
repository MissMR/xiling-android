package com.xiling.module.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Joiner;
import com.xiling.R;
import com.xiling.module.auth.Config;
import com.xiling.module.cart.adapter.NewCartAdapter;
import com.xiling.module.pay.PayActivity;
import com.xiling.shared.basic.BaseFragment;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.CartItem;
import com.xiling.shared.bean.CartStore;
import com.xiling.shared.bean.Coupon;
import com.xiling.shared.bean.MemberRatio;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.component.CartNoDataView;
import com.xiling.shared.component.HeaderLayout;
import com.xiling.shared.component.dialog.CouponBottomDialog;
import com.xiling.shared.constant.AppTypes;
import com.xiling.shared.constant.Event;
import com.xiling.shared.decoration.SpacesItemDecoration;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.CartManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.ICartService;
import com.xiling.shared.service.contract.ICouponService;
import com.xiling.shared.service.contract.IUserService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.SessionUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.refreshLayout)
    protected SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.headerLayout)
    protected HeaderLayout mHeaderLayout;

    @BindView(R.id.totalLayout)
    protected LinearLayout mTotalLayout;

    @BindView(R.id.totalTv)
    protected TextView mTotalTv;

    @BindView(R.id.nextBtn)
    protected TextView mNextBtn;

    @BindView(R.id.deleteBtn)
    protected TextView mDeleteBtn;

    @BindView(R.id.checkAll)
    protected TextView mCheckAllBtn;

    @BindView(R.id.layoutBottom)
    LinearLayout mLayoutBottom;

    @BindView(R.id.tvRatio)
    TextView mTvRatio;

    private NewCartAdapter mCartAdapter;
    private boolean mInEditMode = false;
    private ICartService mCartService;
    private int mType;
    private ArrayList<CartStore> mDatas = new ArrayList<>();
    private List<MemberRatio> mMemberRatio;
    private IUserService mMemberRatioService;
    private ICouponService mCouponService;

    public static CartFragment newInstance(int type) {
        Bundle args = new Bundle();
        args.putInt(Config.INTENT_KEY_ID, type);
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_layout, container, false);
        ButterKnife.bind(this, view);
        getIntentData();
        initView();
        EventBus.getDefault().register(this);
        initData();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded() && getContext() != null) {
            onRefresh();
        }
    }

    @Override
    protected boolean isNeedLogin() {
        return true;
    }

    private void initData() {
        mMemberRatioService = ServiceManager.getInstance().createService(IUserService.class);
        mCartService = ServiceManager.getInstance().createService(ICartService.class);
        mCouponService = ServiceManager.getInstance().createService(ICouponService.class);
        switch (mType) {
            case AppTypes.CART.FROM_ACTIVITY:
                onRefresh();
                break;
            default:
                break;
        }
    }

    private void initView() {
        mHeaderLayout.show();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(ConvertUtil.dip2px(10), true));
        mCartAdapter = new NewCartAdapter(mDatas);
        mCartAdapter.setHasStableIds(true);
        mRecyclerView.setAdapter(mCartAdapter);
        mRefreshLayout.setOnRefreshListener(this);
        mCartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.itemTitleTv:
                        view.setSelected(!view.isSelected());
                        selectStore(position, view.isSelected());
                        break;
                    case R.id.ivReceviceCoupon:
                        CouponBottomDialog couponBottomDialog = new CouponBottomDialog(getContext());
                        couponBottomDialog.show();
                        couponBottomDialog.setData(mDatas.get(position).mCoupons.get(0));
                        break;
                    default:
                        break;
                }
            }
        });
        mCartAdapter.setEmptyView(new CartNoDataView(getContext()));

        switch (mType) {
            case AppTypes.CART.FROM_HOME:
                mHeaderLayout.setTitle("购物车");
                mHeaderLayout.setRightText("编辑");
                mHeaderLayout.setOnRightClickListener(this);
                break;
            case AppTypes.CART.FROM_ACTIVITY:
                mHeaderLayout.setTitle("购物车");
                mHeaderLayout.setRightText("编辑");
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

    private void selectStore(int position, boolean selected) {
        CartStore cartStore = mDatas.get(position);
        for (CartItem product : cartStore.products) {
            product.isSelected = selected;
        }
        mCartAdapter.notifyDataSetChanged();
        EventBus.getDefault().post(new EventMessage(Event.selectCartItem));
    }

    private void getIntentData() {
        mType = getArguments().getInt(Config.INTENT_KEY_ID);
    }

    @Override
    public void onRefresh() {
        if (!SessionUtil.getInstance().isLogin()) {
            EventBus.getDefault().post(new EventMessage(Event.goToLogin));
            return;
        }
//        APIManager.startRequest(mMemberRatioService.getMemberRatio(), new BaseRequestListener<List<MemberRatio>>(mRefreshLayout) {
//            @Override
//            public void onSuccess(List<MemberRatio> memberRatio) {
//                mMemberRatio = memberRatio;
//                initTotalPrice();
//            }
//        });

    }

    private void setCoupen() {
        for (final CartStore cartStore : mDatas) {
            StringBuilder productIds = new StringBuilder();
            for (CartItem product : cartStore.products) {
                productIds.append(product.productId + ",");
            }
            String ids = productIds.subSequence(0, productIds.length() - 1).toString();
            APIManager.startRequest(
                    mCouponService.getProductCouponByIds(ids),
                    new BaseRequestListener<List<Coupon>>(mRefreshLayout) {
                        @Override
                        public void onSuccess(List<Coupon> result) {
                            cartStore.mCoupons = result;
                            mCartAdapter.notifyDataSetChanged();
                        }
                    }
            );
        }
    }

    @OnClick(R.id.checkAll)
    protected void onCheckAll(View view) {
        view.setSelected(!view.isSelected());
        for (CartStore cartStore : mDatas) {
            if (cartStore.products == null) {
                continue;
            }
            for (CartItem product : cartStore.products) {
                product.isSelected = view.isSelected();
            }
        }
        mCartAdapter.notifyDataSetChanged();
        initTotalPrice();
        initNextBtn();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectCartItem(EventMessage message) {
        if (message.getEvent() != Event.selectCartItem) {
            return;
        }
        LogUtils.e("收到一个  selectCartItem");
        mCartAdapter.notifyDataSetChanged();
        boolean isSelectAll = true;
        for (CartStore cartStore : mDatas) {
            if (!cartStore.isSelected()) {
                isSelectAll = false;
            }
        }
        mCheckAllBtn.setSelected(isSelectAll);

        if (!mInEditMode) {
            initTotalPrice();
            initNextBtn();
        }
    }

    protected void initTotalPrice() {
        long totalPrice = CartManager.getTotalMoney(mDatas);
        if (!Config.IS_DISCOUNT) {
            mTvRatio.setText("不含运费");
            mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice));
            return;
        }
        long orderRadioMoney = CartManager.getOrderRadioMoney(mDatas, mMemberRatio);
        if (orderRadioMoney <= 0) {
            mTvRatio.setText("暂无折扣");
            mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice));
        } else {
            mTvRatio.setText(
                    String.format("会员折扣(-%s)",
                            ConvertUtil.centToCurrency(getContext(), orderRadioMoney))
            );
            mTotalTv.setText(ConvertUtil.centToCurrency(getContext(), totalPrice - orderRadioMoney));
        }
    }

    protected void initNextBtn() {
        int selectedQuantity = CartManager.getSelectedQuantity(mDatas);
        if (selectedQuantity > 0) {
            mNextBtn.setEnabled(true);
            mNextBtn.setText("去结算(" + selectedQuantity + ")");
        } else {
            mNextBtn.setEnabled(false);
            mNextBtn.setText("去结算");
        }
    }

    @Override
    public void onClick(View view) {
        if ("编辑".equals(mHeaderLayout.getRightText())) {
            mHeaderLayout.setRightText("完成");
            mInEditMode = true;
        } else {
            mHeaderLayout.setRightText("编辑");
            mInEditMode = false;
            initTotalPrice();
            initNextBtn();
        }
        mTotalLayout.setVisibility(mInEditMode ? View.GONE : View.VISIBLE);
        mNextBtn.setVisibility(mTotalLayout.getVisibility());
        mDeleteBtn.setVisibility(mInEditMode ? View.VISIBLE : View.GONE);
    }

    @OnClick(R.id.nextBtn)
    public void onNext() {
        if (!mNextBtn.isEnabled()) {
            return;
        }
        ArrayList<String> selectedIds = CartManager.getSelectedIds(mDatas);
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
        ArrayList<String> ids = CartManager.getSelectedIds(mDatas);
        APIManager.startRequest(mCartService.removeItem(Joiner.on(",").join(ids)), new BaseRequestListener<Object>(mRefreshLayout) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("删除成功");
                CartManager.removeSelected(mDatas);
                mCartAdapter.notifyDataSetChanged();
                onRefresh();
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void alipayHandler(EventMessage message) {
        if (message.getEvent().equals(Event.createOrderSuccess)) {
            onRefresh();
        } else if (message.getEvent().equals(Event.logout)) {
            mDatas.clear();
            mCartAdapter.notifyDataSetChanged();
        }
    }

}
