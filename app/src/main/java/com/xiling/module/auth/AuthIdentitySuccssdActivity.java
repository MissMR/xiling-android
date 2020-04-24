package com.xiling.module.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.module.auth.model.AuthModel;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/6/8 下午9:24.
 * <p>
 * 账户认证成功
 */
public class AuthIdentitySuccssdActivity extends BaseActivity {

    @BindView(R.id.ivStatus)
    ImageView mIvStatus;
    @BindView(R.id.tvStatus)
    TextView mTvStatus;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvCardNumber)
    TextView mTvCardNumber;
    @BindView(R.id.tvReAuth)
    TextView mTvReAuth;
    private AuthModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_identity_succssd);
        ButterKnife.bind(this);
        initData();
        setLeftBlack();
        setTitle("账户认证");
    }

    private void initData() {
//        showLoading();
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(service.getAuth(), new BaseRequestListener<AuthModel>(this) {
            @Override
            public void onSuccess(AuthModel model) {
                mModel = model;
                setData(model);
            }
        });

    }

    private void setData(AuthModel model) {
        mTvName.setText(model.getUserName());
        String identityCard = model.getIdentityCard();
        StringBuffer stringBuffer = new StringBuffer(identityCard);
        for (int i = 0; i < stringBuffer.length(); i++) {
            if (i > 1 && i < stringBuffer.length()) {
                stringBuffer.replace(i - 1, i, "*");
            }
        }
        mTvCardNumber.setText(stringBuffer);
    }


    @OnClick(R.id.tvReAuth)
    public void onViewClicked() {
        Intent intent = new Intent(this, AuthPhoneActivity.class);
        intent.putExtra(Config.INTENT_KEY_TYPE_NAME, Config.USER.INTENT_KEY_TYPE_AUTH_PHONE);
        intent.putExtra("isEdit", true);
        startActivity(intent);
    }
}
