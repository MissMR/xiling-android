package com.xiling.ddmall.module.deal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.AuthPhoneActivity;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.auth.model.CardDetailModel;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.MyStatus;
import com.xiling.ddmall.shared.bean.ProfitData;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.api.RequestResult;
import com.xiling.ddmall.shared.component.dialog.WJDialog;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IBalanceService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.ConvertUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;

public class DealFirstActivity extends BaseActivity {

    @BindView(R.id.etMoney)
    EditText mEtMoney;
    @BindView(R.id.tvAll)
    TextView mTvAll;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;

    private IBalanceService mBalanceService;
    private ProfitData mProfitData;
    private MyStatus mMyStatus;
    private IUserService mUserService;
    private CardDetailModel mCardDetailModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_first);
        ButterKnife.bind(this);
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        mBalanceService = ServiceManager.getInstance().createService(IBalanceService.class);
        APIManager.startRequest(mBalanceService.get(), new BaseRequestListener<ProfitData>(this) {

            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ProfitData profitData) {
                mProfitData = profitData;
                mEtMoney.setHint("可提现零钱 " + ConvertUtil.cent2yuan(profitData.availableMoney));
            }
        });

        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(mUserService.getMyStatus(), new BaseRequestListener<MyStatus>(this) {

            @Override
            public void onSuccess(MyStatus myStatus) {
                mMyStatus = myStatus;
            }
        });
        APIManager.startRequest(mUserService.getCard(), new BaseRequestListener<CardDetailModel>(this) {

            @Override
            public void onSuccess(CardDetailModel model) {
                mCardDetailModel = model;
            }
        });
    }

    private void initView() {
        setLeftBlack();
        setTitle("申请提现");
        mTvAll.setVisibility(View.GONE);
    }

    @OnClick(R.id.tvAll)
    public void onMTvAllClicked() {
        mEtMoney.setText(mProfitData.availableMoney * 1.0f / 100 + "");
    }

    @OnClick(R.id.tvSubmit)
    public void onMTvSubmitClicked() {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        if (loginUser == null || mMyStatus == null) {
            ToastUtils.showShortToast("等待数据");
            return;
        }
        if (loginUser.authStatus != AppTypes.AUTH_STATUS.SUCESS) {
            ToastUtils.showShortToast("请先实名认证");
        } else if (mMyStatus.bindBankStatus != AppTypes.CARD_STATUS.SUCESS) {
            switch (mMyStatus.bindBankStatus) {
                case AppTypes.CARD_STATUS.NO_SUBMIT:
                    showErrorDialog();
                    break;
                case AppTypes.CARD_STATUS.WAIT:
                    ToastUtils.showShortToast("我们正在审核您的银行卡，请稍后再试");
                    break;
                case AppTypes.CARD_STATUS.FAIL:
                    showErrorDialog();
                    break;
                default:
                    break;
            }
        } else {
            addDeal();
        }
    }

    private void showErrorDialog() {
        WJDialog wjDialog = new WJDialog(DealFirstActivity.this);
        wjDialog.show();
        wjDialog.setOnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DealFirstActivity.this, AuthPhoneActivity.class);
                intent.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_CARD);
                startActivity(intent);
            }
        });
        switch (mMyStatus.bindBankStatus) {
            case AppTypes.CARD_STATUS.FAIL:
                wjDialog.setContentText("您的银行卡信息未通过审核，请重新提交");
                wjDialog.setConfirmText("去查看");
                break;
            case AppTypes.CARD_STATUS.NO_SUBMIT:
                wjDialog.setConfirmText("去绑定");
                wjDialog.setContentText("请先绑定银行卡");
                break;
            default:
                break;
        }
    }

    private void addDeal() {
        if (StringUtils.isEmpty(mEtMoney.getText().toString())) {
            ToastUtil.error("请填写提现金额");
            return;
        }

        Intent intent = new Intent(this, AuthPhoneActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_DEAL);
        startActivity(intent);

        long money = ConvertUtil.stringMoney2Long(mEtMoney.getText().toString());
        Observable<RequestResult<Object>> observable = mBalanceService.addDeal(money, mCardDetailModel.accountId);
        EventBus.getDefault().postSticky(observable);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MsgStatus status) {
        switch (status.getAction()) {
            case MsgStatus.ACTION_DEAL_SUCESS:
                finish();
                break;
            default:
                break;
        }
    }
}
