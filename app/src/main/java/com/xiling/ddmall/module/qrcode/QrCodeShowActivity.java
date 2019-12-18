package com.xiling.ddmall.module.qrcode;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.Config;
import com.xiling.ddmall.module.qrcode.model.GetSubscribe;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseObserver;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.user
 * @since 2017-08-03
 */
public class QrCodeShowActivity extends BaseActivity {

    @BindView(R.id.tvTips)
    TextView mTvTips;
    @BindView(R.id.ivCode)
    SimpleDraweeView mIvCode;
    @BindView(R.id.ivImg)
    ImageView mIvImg;
    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbuscribe_us);
        ButterKnife.bind(this);
        showHeader();
        setLeftBlack();
        getIntentData();
        setData();
    }

    private void setData() {
        switch (mType) {
            case AppTypes.QRCODE.SUBSCRIBE:
                mIvImg.setVisibility(View.GONE);
                mIvCode.setVisibility(View.VISIBLE);
                IUserService service = ServiceManager.getInstance().createService(IUserService.class);
                APIManager.startRequest(service.getSubscribe(), new BaseObserver<GetSubscribe>(QrCodeShowActivity.this) {
                    @Override
                    public void onHandleSuccess(GetSubscribe getSubscribe) {
                        FrescoUtil.setImage(mIvCode, getSubscribe.imgUrl);
                    }
                });
                setTitle("关注我们");
                break;
            case AppTypes.QRCODE.BIND_WECHAT:
                setTitle("绑定微信钱包");
//                mTvTips.setText("登录众享商城微信版，绑定微信钱包即可申请提现");
                break;
            case AppTypes.QRCODE.DEAL:
                setTitle("申请提现");
//                mTvTips.setText("登录众享商城微信版，绑定微信钱包即可申请提现");
                break;
            default:
        }
    }

    private void getIntentData() {
        mType = getIntent().getIntExtra(Config.INTENT_KEY_TYPE_NAME, Config.ERROR_CODE);
    }
}
