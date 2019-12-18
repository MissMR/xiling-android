package com.xiling.ddmall.module.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.module.auth.event.MsgStatus;
import com.xiling.ddmall.module.auth.model.AuthModel;
import com.xiling.ddmall.module.auth.model.CardDetailModel;
import com.xiling.ddmall.module.auth.model.CardItemModel;
import com.xiling.ddmall.module.auth.model.body.AddCardBody;
import com.xiling.ddmall.shared.basic.BaseActivity;
import com.xiling.ddmall.shared.basic.BaseRequestListener;
import com.xiling.ddmall.shared.bean.UploadResponse;
import com.xiling.ddmall.shared.component.dialog.CityPickerDialog;
import com.xiling.ddmall.shared.manager.APIManager;
import com.xiling.ddmall.shared.manager.ServiceManager;
import com.xiling.ddmall.shared.manager.UploadManager;
import com.xiling.ddmall.shared.service.contract.ICaptchaService;
import com.xiling.ddmall.shared.service.contract.IUserService;
import com.xiling.ddmall.shared.util.CountDownRxUtils;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthCardActivity extends BaseActivity {
    @BindView(R.id.etYhZhihang)
    EditText mEtYhZhihang;
    @BindView(R.id.etCardNumber)
    EditText mEtCardNumber;
    @BindView(R.id.IvIdCard1)
    SimpleDraweeView mIvIdCard1;
    @BindView(R.id.ivDeleteCard1)
    ImageView mIvDeleteCard1;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.spSelectYH)
    TextView mSpSelectYH;
    @BindView(R.id.tvYhLocation)
    TextView mTvYhLocation;
    @BindView(R.id.etPhoneNumber)
    EditText mEtPhoneNumber;
    @BindView(R.id.activity_auth_identity)
    ScrollView mActivityAuthIdentity;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.tvNumber)
    TextView mTvNumber;
    @BindView(R.id.etCheckNumber)
    EditText mEtCheckNumber;
    @BindView(R.id.tvGetCheckNumber)
    TextView mTvGetCheckNumber;

    private String mImgUrl = "";
    private CityPickerDialog mCityPickerDialog;
    private String mProvince;
    private String mCity;
    private String mCounty;
    private CardItemModel mCardItemModel;
    private IUserService mService;
    private CardDetailModel mModel;
    private String mUrl = "account/add";
    private boolean mIsEdit;
    private ICaptchaService mCaptchaService;
    private final int TIME_COUNT = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_card);
        ButterKnife.bind(this);
        initData();
        setTitle("绑定银行卡");
        setLeftBlack();
    }


    private void initData() {
        mIsEdit = getIntent().getBooleanExtra("isEdit", false);
        mCityPickerDialog = new CityPickerDialog(this);
        mService = ServiceManager.getInstance().createService(IUserService.class);
        mCaptchaService = ServiceManager.getInstance().createService(ICaptchaService.class);
        if (mIsEdit) {
            mUrl = "account/edit";
            APIManager.startRequest(mService.getCard(), new BaseRequestListener<CardDetailModel>(this) {
                @Override
                public void onSuccess(CardDetailModel model) {
                    mModel = model;
                    mTvName.setText(model.bankUser);
                    mEtCardNumber.setText(model.bankAccount);
                    mImgUrl = model.bankcardFrontImg;
                    mIvIdCard1.setImageURI(model.bankcardFrontImg);
                    mCardItemModel = new CardItemModel(model.bankId, model.bankName);
                    mProvince = model.bankcardProvince;
                    mCity = model.bankcardCity;
                    mCounty = model.bankcardArea;
                    mTvYhLocation.setText(mProvince + mCity + mCounty);
                    mEtYhZhihang.setText(model.bankcardAddress);
                }
            });
        }

        APIManager.startRequest(mService.getBankList(), new BaseRequestListener<List<CardItemModel>>(this) {
            @Override
            public void onSuccess(List<CardItemModel> cardItemModels) {
                initSprinner(cardItemModels);
            }
        });

        APIManager.startRequest(
                mService.getAuth(),
                new BaseRequestListener<AuthModel>(this) {
                    @Override
                    public void onSuccess(AuthModel result) {
                        mTvName.setText(result.getUserName());
                        mTvNumber.setText(result.getIdentityCard());
                    }
                }
        );
    }

    private void initSprinner(final List<CardItemModel> cardItemModels) {
        ArrayList<String> names = new ArrayList<>();
        for (CardItemModel cardItemModel : cardItemModels) {
            names.add(cardItemModel.bankName);
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, names);
        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setAnchorView(mSpSelectYH);
        mSpSelectYH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow.show();
            }
        });
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCardItemModel = cardItemModels.get(position);
                mSpSelectYH.setText(mCardItemModel.bankName);
                listPopupWindow.dismiss();
            }
        });
//        mSpSelectYH.setAdapter(adapter);
//        mSpSelectYH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                mCardItemModel = cardItemModels.get(position);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    @OnClick({R.id.ivDeleteCard1, R.id.tvSubmit, R.id.tvYhLocation, R.id.IvIdCard1})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivDeleteCard1:
                mIvIdCard1.setImageURI("");
                mImgUrl = "";
                mIvDeleteCard1.setVisibility(View.GONE);
                break;
            case R.id.tvSubmit:
                if (checkData()) {
                    addCard();
                }
                break;
            case R.id.tvYhLocation:
                showLocationWindow();
                break;
            case R.id.IvIdCard1:
                UploadManager.selectImage(this);
                break;
        }
    }

    private void uploadImage(final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                mImgUrl = result.url;
                mIvIdCard1.setImageURI(result.url);
                mIvDeleteCard1.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLocationWindow() {
        mCityPickerDialog.showPickerView(new CityPickerDialog.CitySelectListener() {
            @Override
            public void select(String province, String city, String county) {
                mProvince = province;
                mCity = city;
                mCounty = county;

                mTvYhLocation.setText(mProvince + mCity + mCounty);
            }
        });
    }

    private void addCard() {
        AddCardBody addCardBody = new AddCardBody(
                mCardItemModel.bankId,
                mEtCardNumber.getText().toString(),
                mTvName.getText().toString(),
                mImgUrl,
                mProvince,
                mCity,
                mCounty,
                mEtYhZhihang.getText().toString(),
                mEtPhoneNumber.getText().toString(),
                mEtCheckNumber.getText().toString(),
                mTvName.getText().toString(),
                mTvNumber.getText().toString()
        );
        APIManager.startRequest(mService.addCard(mUrl, addCardBody.toMap()), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                super.onSuccess(result);
                goSubmitSucceedActivity();
                EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_CARD_CHANGE));
            }
        });
    }

    private void goSubmitSucceedActivity() {
        ToastUtils.showShortToast("提交银行卡信息成功");
        startActivity(new Intent(this, SubmitStatusActivity.class));
        EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_CARD_CHANGE));
        EventBus.getDefault().postSticky(new MsgStatus(Config.USER.INTENT_KEY_TYPE_AUTH_CARD_SUBMIT_SUCCESS));
        finish();
    }

    /**
     * @return 是否输入了所有信息
     */
    private boolean checkData() {
        if (mEtCardNumber.getText().length() < 1) {
            ToastUtils.showShortToast("请输入银行卡号");
            return false;
        } else if (StringUtils.isEmpty(mImgUrl)) {
            ToastUtils.showShortToast("请上传银行卡照片");
            return false;
        } else if (mCardItemModel == null) {
            ToastUtils.showShortToast("请选择银行");
            return false;
        } else if (StringUtils.isEmpty(mCity)) {
            ToastUtils.showShortToast("请选择开户地址");
            return false;
        } else if (StringUtils.isEmpty(mEtYhZhihang.getText().toString())) {
            ToastUtils.showShortToast("请输入支行地址");
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Config.REQUEST_CODE_CHOOSE_IMAGE_SELECT && resultCode == RESULT_OK) {
            List<Uri> uris = Matisse.obtainResult(data);
            LogUtils.e("拿到图片" + uris.get(0).getPath());
            uploadImage(uris.get(0));
        }
    }

    @OnClick(R.id.tvGetCheckNumber)
    public void onViewClicked() {
        APIManager.startRequest(
                mCaptchaService.getMemberAuthMsgByReservedPhone(mEtPhoneNumber.getText().toString()),
                new BaseRequestListener<Object>(this) {
                    @Override
                    public void onSuccess(Object result) {
                        CountDownRxUtils.textViewCountDown(mTvGetCheckNumber, TIME_COUNT, "获取验证码");
                    }
                }
        );
    }
}
