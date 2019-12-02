package com.dodomall.ddmall.module.store;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.module.auth.event.MsgStatus;
import com.dodomall.ddmall.shared.basic.BaseActivity;
import com.dodomall.ddmall.shared.basic.BaseRequestListener;
import com.dodomall.ddmall.shared.bean.MemberStore;
import com.dodomall.ddmall.shared.bean.UploadResponse;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.component.HeaderLayout;
import com.dodomall.ddmall.shared.component.dialog.CityPickerDialog;
import com.dodomall.ddmall.shared.component.dialog.RegionSelectDialog;
import com.dodomall.ddmall.shared.component.dialog.RegionSelectDialog2;
import com.dodomall.ddmall.shared.contracts.IRegion;
import com.dodomall.ddmall.shared.contracts.OnSelectRegionLister;
import com.dodomall.ddmall.shared.manager.APIManager;
import com.dodomall.ddmall.shared.manager.OptionsPickerDialogManage;
import com.dodomall.ddmall.shared.manager.ServiceManager;
import com.dodomall.ddmall.shared.manager.UploadManager;
import com.dodomall.ddmall.shared.service.contract.IUserService;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.SessionUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@Deprecated
public class StoreSettingActivity extends BaseActivity {

    private static final int REQUEST_CODE_CHOOSE_PHOTO_AVATAR = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO_QRCODE = 2;

    @BindView(R.id.etName)
    EditText mEtName;
    @BindView(R.id.ivAddAvatar)
    SimpleDraweeView mIvAddAvatar;
    @BindView(R.id.ivDelAvatar)
    ImageView mIvDelAvatar;
    @BindView(R.id.tvCity)
    TextView mTvCity;
    @BindView(R.id.layoutSelectCity)
    FrameLayout mLayoutSelectCity;
    @BindView(R.id.etAddress)
    EditText mEtAddress;
    @BindView(R.id.etContacts)
    EditText mEtContacts;
    @BindView(R.id.etPhone)
    EditText mEtPhone;
    @BindView(R.id.etExpressName)
    EditText mEtExpressName;
    @BindView(R.id.icAddQrCode)
    SimpleDraweeView mIcAddQrCode;
    @BindView(R.id.ivDelQrCode)
    ImageView mIvDelQrCode;
    @BindView(R.id.tvSubmit)
    TextView mTvSubmit;
    @BindView(R.id.tvShipType)
    TextView mTvShipType;
    @BindView(R.id.layoutSelectShipType)
    FrameLayout mLayoutSelectShipType;
    @BindView(R.id.tvShipTypeInfo)
    TextView mTvShipTypeInfo;
    @BindView(R.id.layoutShipTypeTag)
    LinearLayout mLayoutShipTypeTag;
    @BindView(R.id.tvShipCity)
    TextView mTvShipCity;
    @BindView(R.id.layoutSelectShipCity)
    FrameLayout mLayoutSelectShipCity;

    private String mAvatarUrl;
    private String mQrcodeUrl;
    private CityPickerDialog mCityPickerDialog;
    private String mProvince;
    private String mCity;
    private String mCounty;
    private IUserService mService;
    private OptionsPickerView mSelectShipTypeDialog;
    private final String SHIP_TYPE_INFO_1 = "选择自己发货时，顾客购买店多多自营商品所支付的钱将全部打到您账上，需要您发货给顾客，发生退货等售后问题时亦需要您去处理。";
    private final String SHIP_TYPE_INFO_2 = "选择上级代发时，顾客购买店多多自营商品所支付的钱只有差价部分会打到您账上，你不需要进行发货，上级将替您发货并处理售后问题。";
    private String[] mInfos = {SHIP_TYPE_INFO_1, SHIP_TYPE_INFO_2};
    private int[] mTypes = {1, 2};
    private int mCurrentType = 1;
    private String mShipProvince;
    private String mShipCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_setting);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        mService = ServiceManager.getInstance().createService(IUserService.class);
        APIManager.startRequest(mService.getEditMemberStore(), new BaseRequestListener<MemberStore>(this) {
            @Override
            public void onSuccess(MemberStore memberStore) {
                if (memberStore.status != 4) {
                    setData(memberStore);
                }
            }
        });
    }

    private void setData(MemberStore memberStore) {
        mEtName.setText(memberStore.storeName);
        if (!StringUtils.isEmpty(memberStore.thumbUrl)) {
            mIvDelAvatar.setVisibility(View.VISIBLE);
            FrescoUtil.setImageSmall(mIvAddAvatar, memberStore.thumbUrl);
            mAvatarUrl = memberStore.thumbUrl;
        }
        mProvince = memberStore.province;
        mCity = memberStore.city;
        mCounty = memberStore.district;
        mTvCity.setText(memberStore.province + memberStore.city + memberStore.district);
        mTvCity.setTextColor(getResources().getColor(R.color.text_black));
        mEtAddress.setText(memberStore.address);
        mEtContacts.setText(memberStore.contact);
        mEtPhone.setText(memberStore.phone);
        mEtExpressName.setText(memberStore.expressName);
        mTvShipCity.setText(memberStore.shipAddress);
        if (!StringUtils.isEmpty(memberStore.wxQrCode)) {
            mIvDelQrCode.setVisibility(View.VISIBLE);
            FrescoUtil.setImageSmall(mIcAddQrCode, memberStore.thumbUrl);
            mQrcodeUrl = memberStore.wxQrCode;
        }

        User loginUser = SessionUtil.getInstance().getLoginUser();
        if (loginUser.storeType == 3 || loginUser.storeType == 4) {
            mCurrentType = memberStore.shipType;
            String typeStr;
            switch (mCurrentType) {
                case 1:
                    typeStr = "自己发货";
                    mTvShipTypeInfo.setText(mInfos[0]);
                    mTvShipTypeInfo.setVisibility(View.VISIBLE);
                    mTvShipType.setTextColor(getResources().getColor(R.color.text_black));
                    break;
                case 2:
                    mTvShipTypeInfo.setText(mInfos[1]);
                    mTvShipTypeInfo.setVisibility(View.VISIBLE);
                    typeStr = "上级代发";
                    mTvShipType.setTextColor(getResources().getColor(R.color.text_black));
                    break;
                default:
                    typeStr = "请选择发货方式";
                    mTvShipTypeInfo.setVisibility(View.GONE);
                    break;
            }
            mTvShipType.setText(typeStr);
            mLayoutSelectShipType.setVisibility(View.VISIBLE);
            mLayoutShipTypeTag.setVisibility(View.VISIBLE);
        } else {
            mLayoutSelectShipType.setVisibility(View.GONE);
            mLayoutShipTypeTag.setVisibility(View.GONE);
        }

    }

    private void initView() {
        setTitle("修改店铺配置");
        setLeftBlack();
        HeaderLayout headerLayout = getHeaderLayout();
        headerLayout.setRightText("保存");
        headerLayout.setRightTextColor(R.color.red);
        headerLayout.setOnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTvSubmitClicked();
            }
        });
        mCityPickerDialog = new CityPickerDialog(this);

        final ArrayList<String> items = new ArrayList<>();
        items.add("自己发货");
        items.add("上级代发");
        mSelectShipTypeDialog = OptionsPickerDialogManage.getOptionsDialog(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mTvShipTypeInfo.setText(mInfos[options1]);
                mTvShipType.setText(items.get(options1));
                mTvShipType.setTextColor(getResources().getColor(R.color.text_black));
                mCurrentType = mTypes[options1];
                mTvShipTypeInfo.setVisibility(View.VISIBLE);
            }
        });
        mSelectShipTypeDialog.setPicker(items);
    }

    private void selectImage(final int request) {
        UploadManager.selectImage(this, request, 1);
    }

    private void updateImage(final int request, final Uri uri) {
        UploadManager.uploadImage(this, uri, new BaseRequestListener<UploadResponse>(this) {

            @Override
            public void onSuccess(UploadResponse result) {
                switch (request) {
                    case REQUEST_CODE_CHOOSE_PHOTO_AVATAR:
                        FrescoUtil.setImageSmall(mIvAddAvatar, uri.toString());
                        mAvatarUrl = result.url;
                        mIvDelAvatar.setVisibility(View.VISIBLE);
                        break;
                    case REQUEST_CODE_CHOOSE_PHOTO_QRCODE:
                        FrescoUtil.setImageSmall(mIcAddQrCode, uri.toString());
                        mQrcodeUrl = result.url;
                        mIvDelQrCode.setVisibility(View.VISIBLE);
                        break;
                    default:
                }
            }
        });
    }

    private boolean checkData() {
        if (StringUtils.isEmpty(mEtName.getText().toString())) {
            ToastUtil.error("请填写店铺名字");
            return false;
        }
//        if (StringUtils.isEmpty(mEtContacts.getText().toString())) {
//            ToastUtil.error("请填写联系人姓名");
//            return false;
//        }
//        if (StringUtils.isEmpty(mEtPhone.getText().toString())) {
//            ToastUtil.error("请填写联系电话");
//            return false;
//        }
//        if (StringUtils.isEmpty(mAvatarUrl)) {
//            ToastUtil.error("请选择店铺头像");
//            return false;
//        }
//        if (StringUtils.isEmpty(mQrcodeUrl)) {
//            ToastUtil.error("请选择二维码");
//            return false;
//        }
//        if (StringUtils.isEmpty(mEtExpressName.getText().toString())) {
//            ToastUtil.error("请填写默认快递");
//            return false;
//        }
//        if (mCurrentType == 0) {
//            ToastUtil.error("请选择发货方式");
//            return false;
//        }
        if (StringUtils.isEmpty(mCounty)) {
            ToastUtil.error("请选择店铺地址");
            return false;
        } else if (StringUtils.isEmpty(mEtAddress.getText().toString())) {
            ToastUtil.error("请输入店铺详细地址");
            return false;
        }
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CHOOSE_PHOTO_AVATAR:
                case REQUEST_CODE_CHOOSE_PHOTO_QRCODE:
                    List<Uri> uris = Matisse.obtainResult(data);
                    LogUtils.e("拿到图片" + uris.get(0).getPath());
                    updateImage(requestCode, uris.get(0));
                    break;
                default:
            }
        }
    }

    @OnClick(R.id.tvSubmit)
    public void onTvSubmitClicked() {
        if (checkData()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("storeName", mEtName.getText().toString());
            params.put("contact", mEtContacts.getText().toString());
            params.put("phone", mEtPhone.getText().toString());
            params.put("province", mProvince);
            params.put("city", mCity);
            params.put("district", mCounty);
            params.put("address", mEtAddress.getText().toString());
            params.put("expressName", mEtExpressName.getText().toString());
            params.put("shipAddress", mShipProvince + mShipCity);
            params.put("thumbUrl", mAvatarUrl);
            params.put("wxQrCode", mQrcodeUrl);
            params.put("shipType", mCurrentType);
            APIManager.startRequest(
                    mService.addOrUpdateMemberStore(APIManager.buildJsonBody(params)),
                    new BaseRequestListener<Object>(this) {
                        @Override
                        public void onSuccess(Object result) {
                            finish();
                            EventBus.getDefault().post(new MsgStatus(MsgStatus.ACTION_STORE_CHANGE));
//                            startActivity(new Intent(StoreSettingActivity.this, StoreExpressSettingActivity.class));
                        }
                    }
            );
        }
    }


    @OnClick({R.id.ivAddAvatar, R.id.ivDelAvatar, R.id.icAddQrCode, R.id.ivDelQrCode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivAddAvatar:
                selectImage(REQUEST_CODE_CHOOSE_PHOTO_AVATAR);
                break;
            case R.id.ivDelAvatar:
                mIvAddAvatar.setImageURI("");
                mAvatarUrl = "";
                mIvDelAvatar.setVisibility(View.GONE);
                break;
            case R.id.icAddQrCode:
                selectImage(REQUEST_CODE_CHOOSE_PHOTO_QRCODE);
                break;
            case R.id.ivDelQrCode:
                mIcAddQrCode.setImageURI("");
                mQrcodeUrl = "";
                mIvDelQrCode.setVisibility(View.GONE);
                break;
            default:
        }
    }

    @OnClick(R.id.layoutSelectCity)
    public void onViewClicked() {
        new RegionSelectDialog(this, new OnSelectRegionLister() {
            @Override
            public void selected(HashMap<String, IRegion> regions) {
                IRegion province = regions.get("province");
                mProvince = "";
                mCity = "";
                mCounty = "";
                if (province != null) {
                    mProvince = province.getName();
                }
                IRegion city = regions.get("city");
                if (city != null) {
                    mCity = city.getName();
                }
                IRegion distinct = regions.get("distinct");
                if (distinct != null) {
                    mCounty = distinct.getName();
                }
                mTvCity.setText(mProvince + mCity + mCounty);
            }
        }).show();
    }

    @OnClick(R.id.layoutSelectShipCity)
    public void onSelectShipCity() {
        new RegionSelectDialog2(this, new OnSelectRegionLister() {
            @Override
            public void selected(HashMap<String, IRegion> regions) {
                IRegion province = regions.get("province");
                mShipProvince = "";
                mShipCity = "";
                if (province != null) {
                    mShipProvince = province.getName();
                }
                IRegion city = regions.get("city");
                if (city != null) {
                    mShipCity = city.getName();
                }
                mTvShipCity.setText(mShipProvince + mShipCity);
            }
        }).show();
    }

    @OnClick(R.id.layoutSelectShipType)
    public void onSelectShipType() {
        mSelectShipTypeDialog.show();
    }
}
