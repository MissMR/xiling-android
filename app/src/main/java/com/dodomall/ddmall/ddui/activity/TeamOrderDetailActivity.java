package com.dodomall.ddmall.ddui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dodomall.ddmall.R;
import com.dodomall.ddmall.ddui.adapter.TeamOrderProductAdapter;
import com.dodomall.ddmall.ddui.bean.TeamOrderDetailBean;
import com.dodomall.ddmall.ddui.bean.TeamOrderSkuBean;
import com.dodomall.ddmall.ddui.custom.DDUserAchievementDialog;
import com.dodomall.ddmall.ddui.tools.DLog;
import com.dodomall.ddmall.ddui.tools.TextTools;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.contracts.RequestListener;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.IMasterCenterService;
import com.dodomall.ddmall.shared.util.ConvertUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * @author Jigsaw
 * @date 2018/12/21
 * 团队订单详情
 * <p>
 * 测试账号:
 * 孙腾飞 - 13280888572
 * 赵志强 - 13356390371
 */
public class TeamOrderDetailActivity extends BaseActivity {

    private static String kOrderNo = "TeamOrderDetailActivity.kOrderNo";

    private static String kMode = "TeamOrderDetailActivity.kMode";
    private static String kStatus = "TeamOrderDetailActivity.kStatus";
    private static String kPrizeType = "TeamOrderDetailActivity.kPrizeType";

    private static String kMoney = "TeamOrderDetailActivity.kMoney";

    //从团队订单列表进入
    public static final int vModeTeamOrderList = 0x1;
    //从收益记录进入
    public static final int vModeIncomeList = 0x2;

    /**
     * 显示收益记录订单详情
     *
     * @param context   上下文对象
     * @param orderNo   订单编号
     * @param prizeType 奖项类型
     */
    public static void navInFromIncome(Context context, String orderNo, int prizeType) {
        navInFromIncome(context, orderNo, prizeType, -1);
    }

    /**
     * 显示收益记录订单详情
     *
     * @param context   上下文对象
     * @param orderNo   订单编号
     * @param prizeType 奖项类型
     * @param money     金额
     */
    public static void navInFromIncome(Context context, String orderNo, int prizeType, long money) {
        if (context == null) {
            DLog.i("上下文为空");
            ToastUtil.error("团队订单数据异常,请刷新后重试(-1|1001)");
            return;
        }
        Intent intent = new Intent(context, TeamOrderDetailActivity.class);
        intent.putExtra(kMode, vModeIncomeList);
        intent.putExtra(kOrderNo, orderNo);
        intent.putExtra(kPrizeType, prizeType);
        intent.putExtra(kMoney, money);
        context.startActivity(intent);
    }

    /**
     * 显示团队订单详情
     *
     * @param context          上下文对象
     * @param orderNo          订单编号
     * @param settlementStatus 状态
     */
    public static void navInFromTeamOrder(Context context, String orderNo, int settlementStatus) {
        if (context == null) {
            DLog.i("上下文为空");
            ToastUtil.error("团队订单数据异常,请刷新后重试(-1|1001)");
            return;
        }
        Intent intent = new Intent(context, TeamOrderDetailActivity.class);
        intent.putExtra(kMode, vModeTeamOrderList);
        intent.putExtra(kOrderNo, orderNo);
        intent.putExtra(kStatus, settlementStatus);
        context.startActivity(intent);
    }

    IMasterCenterService iMasterCenterService = null;

    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;

    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    @BindView(R.id.tv_income)
    TextView tvIncome;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.tv_order_no)
    TextView tvOrderNo;

    @BindView(R.id.tv_create_datetime)
    TextView tvCreateDatetime;

    @BindView(R.id.tv_pay_datetime)
    TextView tvPayDatetime;

    @BindView(R.id.tv_pay_status)
    TextView tvPayStatus;

    @BindView(R.id.remarkLayout)
    LinearLayout remarkLayout;

    @BindView(R.id.tv_remark)
    TextView tvRemark;

    int mode = 0;
    String orderNo = "";
    int status = 0;
    int type = 0;
    long money = -1;

    private TeamOrderProductAdapter mTeamOrderProductAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化网络服务
        iMasterCenterService = ServiceManager.getInstance().createService(IMasterCenterService.class);

        //接收上层传递的参数
        mode = getIntent().getIntExtra(kMode, 0);
        orderNo = getIntent().getStringExtra(kOrderNo);
        status = getIntent().getIntExtra(kStatus, IMasterCenterService.SettlementStatus.DEFAULT);
        type = getIntent().getIntExtra(kPrizeType, 0);
        money = getIntent().getLongExtra(kMoney, -1);

        //处理订单编号异常的情况
        if (mode < 1) {
            DLog.e("模式不正确");
            ToastUtil.error("团队订单数据异常,请刷新后重试(-1|1002)");
            finish();
            return;
        } else if (TextUtils.isEmpty(orderNo)) {
            DLog.e("订单号为空");
            ToastUtil.error("团队订单数据异常,请刷新后重试(-1|1002)");
            finish();
            return;
        }

        setContentView(R.layout.activity_team_order_detail);
        ButterKnife.bind(this);
        initView();

        //加载网络数据
        loadNetData();
    }

    private void initView() {
        showHeader("订单结算详情");
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mTeamOrderProductAdapter = new TeamOrderProductAdapter();
        mRecyclerView.setAdapter(mTeamOrderProductAdapter);
    }

    //加载网络数据
    public void loadNetData() {
        Observable<RequestResult<TeamOrderDetailBean>> requestResultObservable = null;
        if (mode == vModeTeamOrderList) {
            requestResultObservable = iMasterCenterService.getTeamOrderDetail(orderNo, status);
        } else {
            requestResultObservable = iMasterCenterService.getIncomeOrderDetail(orderNo, type);
        }
        APIManager.startRequest(requestResultObservable, new RequestListener<TeamOrderDetailBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(TeamOrderDetailBean result) {
                super.onSuccess(result);
                setViewData(result);
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.error("" + e.getMessage());
                finish();
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private String orderIncId = "";

    //设置UI数据
    public void setViewData(TeamOrderDetailBean result) {
        List<TeamOrderSkuBean> products = result.getSkuDetail();
        mTeamOrderProductAdapter.getData().clear();
        mTeamOrderProductAdapter.addData(products);

        orderIncId = result.getIncId();

        //用户头像
        String avatarUrl = result.getHeadImage();
        //用户昵称
        String nickname = result.getNickName();

        //预期收益
        long income = result.getEstimateDirectRebate();
        String orderNo = result.getOrderCode();
        String orderCreateDateTime = result.getOrderDate();
        String orderPayDateTime = result.getPayDate();
        String orderPayStatus = result.getSettlementStatusStr();
        String orderRemark = result.getText();

        sdvAvatar.setImageURI("" + avatarUrl);
        tvNickname.setText("" + nickname);

        if (money > -1) {
            tvIncome.setText("预估收益:" + ConvertUtil.cent2yuanNoZero(money) + "元");
        } else {
            tvIncome.setText("预估收益:" + ConvertUtil.cent2yuanNoZero(income) + "元");
        }

        tvOrderNo.setText("订单号:" + orderNo);
        tvCreateDatetime.setText("下单时间:" + orderCreateDateTime);
        tvPayDatetime.setText("付款时间:" + orderPayDateTime);
        tvPayStatus.setText("结算状态:" + orderPayStatus);
        if (TextUtils.isEmpty(orderRemark)) {
            tvRemark.setText("");
            remarkLayout.setVisibility(View.GONE);
        } else {
            Spanned remarkText = TextTools.fromHtml("" + orderRemark + "");
            tvRemark.setText(remarkText);
            remarkLayout.setVisibility(View.VISIBLE);
        }

    }

    @OnClick(R.id.rl_user)
    void onUserClicked() {
        DDUserAchievementDialog dialog = new DDUserAchievementDialog(this);
        dialog.show(orderIncId);
    }
}
