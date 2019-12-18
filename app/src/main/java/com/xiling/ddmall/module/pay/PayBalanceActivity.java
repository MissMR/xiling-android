package com.xiling.ddmall.module.pay;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.Constants;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.Order;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.bean.event.EventMessage;
import com.xiling.ddmall.shared.component.CaptchaBtn;
import com.xiling.ddmall.shared.constant.Event;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IOrderService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.EventUtil;
import com.xiling.ddmall.shared.util.StringUtil;
import com.xiling.ddmall.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.pay
 * @since 2017-07-12
 */
public class PayBalanceActivity extends BaseActivity {

    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.captchaEt)
    protected EditText mCaptchaEt;
    @BindView(R.id.captchaBtn)
    protected CaptchaBtn mCaptchaBtn;
    @BindView(R.id.passwordEt)
    protected EditText mPasswordEt;
    private ICaptchaService mCaptchaService;
    private IUserService mUserService;
    private User mUser;
    private IOrderService mOrderService;
    private Order mOrder;
    private boolean mIsOrderPay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_balance);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mIsOrderPay = intent.getBooleanExtra("isOrderPay", false);
        if (!(intent == null || intent.getExtras() == null)) {
            mOrder = (Order) intent.getExtras().get("order");
        }
        if (mOrder == null) {
            ToastUtil.error("参数错误");
            finish();
            return;
        }
        showHeader();
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsOrderPay) {
                    EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BALANCE_SUCCEED));
                    EventUtil.viewOrderDetail(PayBalanceActivity.this, mOrder.orderMain.orderCode);
                }
                finish();
            }
        });
        setTitle("零钱支付");
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
        mOrderService = ServiceManager.getInstance().createService(IOrderService.class);
        getUserInfo();
    }

    private void getUserInfo() {
        APIManager.startRequest(mUserService.getUserInfo(), new BaseRequestListener<User>(this) {
            @Override
            public void onSuccess(User result) {
                mUser = result;
                mPhoneTv.setText(StringUtil.maskPhone(mUser.phone));
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error("获取用户信息失败");
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (!mIsOrderPay) {
                EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BALANCE_SUCCEED));
                EventUtil.viewOrderDetail(PayBalanceActivity.this, mOrder.orderMain.orderCode);
            }
            finish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick(R.id.captchaBtn)
    protected void getCaptcha() {
        String token = StringUtil.md5(Constants.API_SALT + mUser.phone);
        APIManager.startRequest(mCaptchaService.getCaptchaForCheck(token, mUser.phone), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                mCaptchaBtn.start();
            }
        });
    }

    @OnClick(R.id.confirmBtn)
    protected void confirmToPay() {
        String captcha = mCaptchaEt.getText().toString();
        if (captcha.isEmpty()) {
            ToastUtil.error("验证码不能为空");
            return;
        }
        String password = mPasswordEt.getText().toString();
        if (password.isEmpty()) {
            ToastUtil.error("密码不能为空");
            return;
        }
        APIManager.startRequest(mOrderService.payBalance(mOrder.orderMain.orderCode, StringUtil.md5(password), captcha), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("支付成功");
                EventBus.getDefault().post(new EventMessage(Event.paySuccess, mOrder));
                EventUtil.viewOrderDetail(PayBalanceActivity.this, mOrder.orderMain.orderCode);
                EventBus.getDefault().post(new PayMsg(PayMsg.ACTION_BALANCE_SUCCEED));
                finish();
            }
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                mCaptchaBtn.stop();
//            }
        });
    }
}
