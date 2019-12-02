package com.dodomall.ddmall.module.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.Constants;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.bean.api.RequestResult;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.constant.Event;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.service.contract.ICaptchaService;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.CountDownRxUtils;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;


public class AuthPhoneActivity extends BaseActivity {

    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.etCheckNumber)
    EditText mEtCheckNumber;
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @BindView(R.id.tvGetCheckNumber)
    TextView mTvGetCheckNumber;
    private final int TIME_COUNT = 60;
    @BindView(R.id.ivImg)
    ImageView mIvImg;
    @BindView(R.id.tvTag)
    TextView mTvTag;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    private ICaptchaService mCaptchaService;
    private User mUser;
    private IUserService mUserService;
    private int mType;
    private Observable<RequestResult<Object>> mObservable;
    private boolean mIsEdit;
    private String mPhone;
    private long mTransferScore;
    private String mRemark;
    private String mStatusTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_phone);
        ButterKnife.bind(this);
        getIntentData();
        setLeftBlack();
        initView();
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        mType = intent.getIntExtra(Config.INTENT_KEY_TYPE_NAME, Config.ERROR_CODE);
        mIsEdit = intent.getBooleanExtra("isEdit", false);
        mPhone = intent.getStringExtra("phone");
        mTransferScore = intent.getLongExtra("transferScore", 0);
        mRemark = intent.getStringExtra("remark");
        mStatusTips = intent.getStringExtra("statusTips");
    }

    private void initView() {
        switch (mType) {
            case Config.USER.INTENT_KEY_TYPE_AUTH_PHONE:
                setTitle("实名认证");
                mIvImg.setImageResource(R.drawable.bg_auth_phone);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD:
                setTitle("绑定银行卡");
                mIvImg.setImageResource(R.drawable.bg_auth_card);
                break;
            case Config.USER.INTENT_KEY_TYPE_AUTH_DEAL:
                setTitle("申请提现");
                mTvTag.setVisibility(View.GONE);
                mIvImg.setVisibility(View.GONE);
                mTvSubmit.setText("确认");
                break;
            case AppTypes.TRANSFER.SCORE:
                setTitle("转赠");
                mTvTag.setVisibility(View.GONE);
                mIvImg.setVisibility(View.GONE);
                mIvImg.setVisibility(View.GONE);
                mTvSubmit.setText("确认转赠");
                break;
            default:
                break;

        }
    }


    private void initData() {
        if (Config.IS_DEBUG) {
            mEtCheckNumber.setText("20160920");
            mEtPassword.setText("123456");
        }
        mUser = SessionUtil.getInstance().getLoginUser();

        StringBuilder phoneSB = new StringBuilder(mUser.phone);
        String phone = phoneSB.replace(3, 7, "****").toString();
        mTvPhone.setText(phone);

        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    private void getCheckNumber() {
        String token = StringUtil.md5(Constants.API_SALT + mUser.phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForCheck(token, mUser.phone), new BaseRequestListener<Object>(this) {
            @Override
            public void onSuccess(Object result) {
                CountDownRxUtils.textViewCountDown(mTvGetCheckNumber, TIME_COUNT, "获取验证码");
            }
        });
    }

    private void submitAuthPhone() {
        String password = StringUtil.md5(mEtPassword.getText().toString());
        APIManager.startRequest(
                mUserService.checkUserInfo(
                        mUser.phone,
                        mEtCheckNumber.getText().toString(),
                        password
                ),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        switch (mType) {
                            case Config.USER.INTENT_KEY_TYPE_AUTH_PHONE:
                                goAuthIdentityActivity();
                                break;
                            case Config.USER.INTENT_KEY_TYPE_AUTH_CARD:
                                goAuthCardActivity();
                                break;
                            case Config.USER.INTENT_KEY_TYPE_AUTH_DEAL:
                                deal();
                                break;
                            default:
                                break;
                        }
                        finish();
                    }
                }
        );
    }

    /**
     * 积分转赠品
     */
    private void transferScore() {
        String password = StringUtil.md5(mEtPassword.getText().toString());
        HashMap<String, Object> params = new HashMap<>();
        params.put("payeePhone", mPhone);
        params.put("transferScore", mTransferScore);
        params.put("trsMemo", mRemark);
        params.put("password", password);
        params.put("checkNumber", mEtCheckNumber.getText().toString());
        APIManager.startRequest(
                mUserService.transferScore(APIManager.buildJsonBody(params))
                ,
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        finish();

                        Intent intent = new Intent(AuthPhoneActivity.this, SubmitStatusActivity.class);
                        startActivity(intent);
                        MsgStatus msgStatus = new MsgStatus(AppTypes.TRANSFER.TRANSFER_SCORE_SUCESS);
                        msgStatus.setTips(mStatusTips);
                        msgStatus.setMoney(mTransferScore);
                        EventBus.getDefault().postSticky(msgStatus);
                        EventBus.getDefault().post(Event.transferSuccess);
                    }
                }
        );
    }


    /**
     * 提现
     */
    private void deal() {
        if (mObservable != null) {
            APIManager.startRequest(mObservable, new BaseRequestListener<Object>(this) {
                @Override
                public void onSuccess(Object result) {
                    Intent intent = new Intent(AuthPhoneActivity.this, SubmitStatusActivity.class);
                    startActivity(intent);
                    EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_DEAL_SUCESS));
                    EventBus.getDefault().postSticky(new MsgStatus(AppTypes.TRANSFER.DEAL_SUCESS));
                }
            });
        } else {
            ToastUtils.showShortToast("缺少数据");
        }
    }

    private void goAuthIdentityActivity() {
        Intent intent = new Intent(this, AuthIdentityActivity.class);
        intent.putExtra("isEdit", mIsEdit);
        startActivity(intent);
    }

    private void goAuthCardActivity() {
        Intent intent = new Intent(this, AuthCardActivity.class);
        intent.putExtra("isEdit", mIsEdit);
        startActivity(intent);
    }

    @OnClick({R.id.tvGetCheckNumber, R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvGetCheckNumber:
                getCheckNumber();
                break;
            case R.id.tvSubmit:
                if (mType == AppTypes.TRANSFER.SCORE) {
                    transferScore();
                } else {
                    submitAuthPhone();
                }
                break;
            default:
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getMsg(Observable<RequestResult<Object>> observable) {
        EventBus.getDefault().removeStickyEvent(observable);
        mObservable = observable;
    }
}
