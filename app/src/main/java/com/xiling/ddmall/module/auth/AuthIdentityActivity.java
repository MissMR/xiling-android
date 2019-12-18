package com.xiling.ddmall.module.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.auth.model.AuthModel;
import com.xiling.ddmall.module.auth.model.body.SubmitAuthBody;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.UploadResponse;
import com.xiling.ddmall.shared.bean.User;
import com.xiling.ddmall.shared.constant.AppTypes;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.manager.UploadManager;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.FrescoUtil;
import com.xiling.ddmall.shared.util.SessionUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthIdentityActivity extends BaseActivity {

    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.etIdNumber)
    EditText mEtIdNumber;
    @BindView(R.id.IvIdCard1)
    SimpleDraweeView mIvIdCard1;
    @BindView(R.id.IvIdCard2)
    SimpleDraweeView mIvIdCard2;
    @BindView(R.id.IvIdCard3)
    SimpleDraweeView mIvIdCard3;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;

    private static final int REQUEST_CODE_CHOOSE_PHOTO_CARD1 = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO_CARD2 = 2;
    private static final int REQUEST_CODE_CHOOSE_PHOTO_CARD3 = 3;
    @BindView(R.id.ivDeleteCard1)
    ImageView mIvDeleteCard1;
    @BindView(R.id.ivDeleteCard2)
    ImageView mIvDeleteCard2;
    @BindView(R.id.ivDeleteCard3)
    ImageView mIvDeleteCard3;

    private AuthModel mModel;
    private String mImgUrl1;
    private String mImgUrl2;
    private String mImgUrl3;
    private String mUrl = "auth/add";
    private boolean mIsEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_identity);

        ButterKnife.bind(this);
        setTitle("实名认证");
        setLeftBlack();
        initData();
    }

    private void initData() {
        mIsEdit = getIntent().getBooleanExtra("isEdit", false);
        if (mIsEdit) {
            mUrl = "auth/edit";
            IUserService service = ServiceManager.getInstance().createService(IUserService.class);
            APIManager.startRequest(service.getAuth(), new BaseRequestListener<AuthModel>(this) {
                @Override
                public void onSuccess(AuthModel model) {
                    mModel = model;
                    mEtName.setText(model.userName);
                    mEtIdNumber.setText(model.getIdentityCard());
                    mImgUrl1 = model.getIdcardFrontImg();
                    mImgUrl2 = model.getIdcardBackImg();
                    mImgUrl3 = model.getIdcardHeadImg();
                    FrescoUtil.setImageSmall(mIvIdCard1, mImgUrl1);
                    FrescoUtil.setImageSmall(mIvIdCard2, mImgUrl2);
                    FrescoUtil.setImageSmall(mIvIdCard3, mImgUrl3);
                    mIvDeleteCard1.setVisibility(View.VISIBLE);
                    mIvDeleteCard2.setVisibility(View.VISIBLE);
                    mIvDeleteCard3.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    @OnClick({R.id.ivDeleteCard1, R.id.ivDeleteCard2, R.id.ivDeleteCard3, R.id.tvSubmit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDeleteCard1:
                mIvIdCard1.setImageURI("");
                mImgUrl1 = "";
                mIvDeleteCard1.setVisibility(View.GONE);
                break;
            case R.id.ivDeleteCard2:
                mIvIdCard2.setImageURI("");
                mImgUrl2 = "";
                mIvDeleteCard2.setVisibility(View.GONE);
                break;
            case R.id.ivDeleteCard3:
                mIvIdCard3.setImageURI("");
                mImgUrl3 = "";
                mIvDeleteCard3.setVisibility(View.GONE);
                break;
            case R.id.tvSubmit:
                if (checkData()) {
                    ToastUtils.showShortToast("请输入完整信息");
                    return;
                }
                submitAuth();
                break;
            default:
        }
    }

    /**
     * 提交实名认证资料
     */
    private void submitAuth() {
        IUserService service = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(
                service.submitAuth(mUrl, createBody().toMap()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        super.onSuccess(result);
                        goSubmitSucceedActivity();
                    }
                }
        );
    }

    private SubmitAuthBody createBody() {
        return new SubmitAuthBody(
                mImgUrl1,
                mImgUrl2,
                mImgUrl3,
                mEtName.getText().toString(),
                mEtIdNumber.getText().toString(),
                ""
        );
    }

    private void goSubmitSucceedActivity() {
        User loginUser = SessionUtil.getInstance().getLoginUser();
        loginUser.authStatus = AppTypes.AUTH_STATUS.WAIT;
        loginUser.authStatusStr = "审核中";
        SessionUtil.getInstance().setLoginUser(loginUser);

        startActivity(new Intent(this, SubmitStatusActivity.class));
        EventBus.getDefault().postSticky(new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_IDENTITY_SUBMIT_SUCCESS));
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_USER_CHANGE));
        finish();
    }

    /**
     * @return 是否输入了所有信息
     */
    private boolean checkData() {
//        boolean imgNull = StringUtils.isEmpty(mImgUrl1) || StringUtils.isEmpty(mImgUrl2) || StringUtils.isEmpty(mImgUrl3);
//        boolean etNull = mEtIdNumber.getText().length() < 1 || mEtName.getText().length() < 1;
//        return (etNull || imgNull);
        return mEtName.getText().length() < 1;
    }


    private void selectImage(final int request) {
        UploadManager.selectImage(this, request, 1);
    }

    private void updateImage(final int request, final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case REQUEST_CODE_CHOOSE_PHOTO_CARD1:
                        mIvIdCard1.setImageURI(uri.toString());
                        mImgUrl1 = result.url;
                        mIvDeleteCard1.setVisibility(View.VISIBLE);
                        break;
                    case REQUEST_CODE_CHOOSE_PHOTO_CARD2:
                        mIvIdCard2.setImageURI(uri.toString());
                        mImgUrl2 = result.url;
                        mIvDeleteCard2.setVisibility(View.VISIBLE);
                        break;
                    case REQUEST_CODE_CHOOSE_PHOTO_CARD3:
                        mIvIdCard3.setImageURI(uri.toString());
                        mImgUrl3 = result.url;
                        mIvDeleteCard3.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.IvIdCard1, R.id.IvIdCard2, R.id.IvIdCard3})
    public void onClick2(View view) {
        switch (view.getId()) {
            case R.id.IvIdCard1:
                selectImage(REQUEST_CODE_CHOOSE_PHOTO_CARD1);
                break;
            case R.id.IvIdCard2:
                selectImage(REQUEST_CODE_CHOOSE_PHOTO_CARD2);
                break;
            case R.id.IvIdCard3:
                selectImage(REQUEST_CODE_CHOOSE_PHOTO_CARD3);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PHOTO_CARD1:
                case REQUEST_CODE_CHOOSE_PHOTO_CARD2:
                case REQUEST_CODE_CHOOSE_PHOTO_CARD3:
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    updateImage(requestCode, uris.get(0));
                    break;
            }
        }
    }

}
