package com.xiling.ddmall.module.store;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.module.order.OrderListActivity;
import com.xiling.ddmall.module.order.SellerRefundListActivity;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.GetOrderStatusCount;
import com.xiling.ddmall.shared.bean.MemberAchievement;
import com.xiling.ddmall.shared.component.ItemWithIcon;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ConvertUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class StoreManageActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tvMonthMoney)
    TextView mTvMonthMoney;
    @BindView(R.id.tvTotlaMoney)
    TextView mTvTotlaMoney;
    @BindView(R.id.itemDaifahuo)
    ItemWithIcon mItemDaifahuo;
    @BindView(R.id.itemYifahuo)
    ItemWithIcon mItemYifahuo;
    @BindView(R.id.itemYishouhuo)
    ItemWithIcon mItemYishouhuo;
    @BindView(R.id.itemYiguanbi)
    ItemWithIcon mItemYiguanbi;
    @BindView(R.id.tvRefundQuantity)
    TextView mTvRefundQuantity;
    @BindView(R.id.layoutRefund)
    LinearLayout mLayoutRefund;
    @BindView(R.id.tvCommentQuantity)
    TextView mTvCommentQuantity;
    @BindView(R.id.layoutComent)
    LinearLayout mLayoutComent;
    @BindView(R.id.layoutRefresh)
    SwipeRefreshLayout mLayoutRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_manage);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        getOrderData();
        getAchievemenData();
    }

    private void getAchievemenData() {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                service.getMemberAchievement(),
                new BaseRequestListener<MemberAchievement>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(MemberAchievement result) {
                        setAchievmenData(result);
                    }
                }
        );
    }

    private void setAchievmenData(MemberAchievement result) {
        mTvTotlaMoney.setText(String.format("总销售额(元)：% .2f", ConvertUtil.cent2yuan(result.totalRetailMoney)));
        mTvMonthMoney.setText(String.format("%.2f", ConvertUtil.cent2yuan(result.monthTotalRetailMoney)));
    }

    private void getOrderData() {
        IOrderService service = ServiceManager.getInstance().createService(IOrderService.class);
        APIManager.startRequest(
                service.getOrderStatusCount(),
                new BaseRequestListener<GetOrderStatusCount>(mLayoutRefresh) {
                    @Override
                    public void onSuccess(GetOrderStatusCount result) {
                        setOrderData(result);
                    }
                }
        );
    }

    private void setOrderData(GetOrderStatusCount result) {
        mItemDaifahuo.setBadge(result.waitShip);
        mItemYifahuo.setBadge(result.hasShip);
//        mItemYishouhuo.setBadge(result.hasComplete);
//        mItemYiguanbi.setBadge(result.hasClose);
        if (result.afterSales > 0) {
            mTvRefundQuantity.setVisibility(View.VISIBLE);
            result.afterSales = result.afterSales > 99 ? 99 : result.afterSales;
            mTvRefundQuantity.setText(result.afterSales + "");
        } else {
            mTvRefundQuantity.setVisibility(View.GONE);
        }
        if (result.comment > 0) {
            mTvCommentQuantity.setVisibility(View.VISIBLE);
            mTvCommentQuantity.setText(result.comment + "");
        } else {
            mTvCommentQuantity.setVisibility(View.GONE);
        }
    }

    private void initView() {
        setTitle("发货");
        setLeftBlack();
        mLayoutRefresh.setOnRefreshListener(this);
    }

    @OnClick({R.id.itemDaifahuo, R.id.itemYifahuo, R.id.itemYishouhuo, R.id.itemYiguanbi})
    public void onViewClicked(View view) {
        Intent intent = new Intent(this, OrderListActivity.class);
        switch (view.getId()) {
            case R.id.itemDaifahuo:
                intent.putExtra("type", AppTypes.ORDER.SELLER_WAIT_SHIP);
                break;
            case R.id.itemYifahuo:
                intent.putExtra("type", AppTypes.ORDER.SELLER_HAS_SHIP);
                break;
            case R.id.itemYishouhuo:
                intent.putExtra("type", AppTypes.ORDER.SELLER_HAS_COMPLETE);
                break;
            case R.id.itemYiguanbi:
                intent.putExtra("type", AppTypes.ORDER.SELLER_HAS_CLOSE);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @OnClick(R.id.layoutRefund)
    public void onMLayoutRefundClicked() {
//        startActivity(new Intent(this,RefundOrderListActivity.class));
        startActivity(new Intent(this,SellerRefundListActivity.class));
    }

    @OnClick(R.id.layoutComent)
    public void onMLayoutComentClicked() {
        Intent intent = new Intent(this, StoreCommentActivity.class);
        startActivity(intent);
    }


}
