package com.xiling.ddui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.xiling.R;
import com.xiling.ddui.bean.PlatformBean;
import com.xiling.ddui.custom.popupwindow.PlatformSelectPopWindow;
import com.xiling.ddui.tools.GlideEngine;
import com.xiling.image.GlideUtils;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.UploadResponse;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.manager.UploadManager;
import com.xiling.shared.service.INewUserService;
import com.xiling.shared.util.PhoneNumberUtil;
import com.xiling.shared.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @auth 宋秉经
 * 实名认证-信息录入
 */
public class IdentificationInputActivity extends BaseActivity {
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_identity)
    EditText etIdentity;
    @BindView(R.id.et_shop_name)
    EditText etShopName;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    PlatformSelectPopWindow platformSelectPopWindow;
    @BindView(R.id.iv_business)
    ImageView ivBusiness;
    @BindView(R.id.tv_platform)
    TextView tvPlatform;

    //姓名
    private String userName = "";
    //身份证号
    private String identityCard = "";
    //店铺平台类型（1电商 2代购 3微商）- 目前直接写死1
    private String storeType = "1";
    //店铺名称
    private String storeName = "";
    //公司名称
    private String companyName = "";
    //营业执照
    private String businessLicense = "";
    //店铺ID（多个以，分隔）
    private List<PlatformBean> storeIdList = new ArrayList<>();

    INewUserService mUserService;


    private String checkInfo() {
        userName = etName.getText().toString();
        identityCard = etIdentity.getText().toString();
        companyName = etCompanyName.getText().toString();
        storeName = etShopName.getText().toString();


        if (TextUtils.isEmpty(userName)) {
            return "请输入姓名";
        }

        String checkID = PhoneNumberUtil.checkIDNumber(identityCard);
        if (!TextUtils.isEmpty(checkID)) {
            return checkID;
        }


        if (storeIdList.size() == 0) {
            return "请选择店铺平台/身份";
        }

        if (TextUtils.isEmpty(storeName)) {
            return "请输入店铺名称";
        }

        if (TextUtils.isEmpty(companyName)) {
            return "请输入公司名称";
        }

        if (TextUtils.isEmpty(businessLicense)) {
            return "请上传营业执照";
        }


        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_identification_input);
        ButterKnife.bind(this);
        setTitle("实名认证");
        mUserService = ServiceManager.getInstance().createService(INewUserService.class);
        setLeftBlack();
    }

    private void requestSubmit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("identityCard", identityCard);
        params.put("storeType", storeType);
        params.put("storeName", storeName);
        params.put("companyName", companyName);
        params.put("businessLicense", businessLicense);

        String storeIds = "";
        for (int i = 0; i < storeIdList.size(); i++) {
            String id = storeIdList.get(i).getStoreId();
            if (i == storeIdList.size() - 1) {
                storeIds += id;
            } else {
                storeIds += id + ",";
            }

        }
        params.put("storeIdList", storeIds);

        APIManager.startRequest(mUserService.editAuth(params), new BaseRequestListener<Object>() {
            @Override
            public void onSuccess(Object result, String message) {
                super.onSuccess(result);
                ToastUtil.error(message);
                EventBus.getDefault().post(new EventMessage(Event.REAL_AUTH_SUCCESS));
                finish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());

            }
        });
    }


    @OnClick({R.id.btn_submit, R.id.btn_choice_identity, R.id.btn_upload})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                //提交
                String check = checkInfo();
                if (!TextUtils.isEmpty(check)) {
                    ToastUtil.error(check);
                } else {
                    requestSubmit();
                }
                break;
            case R.id.btn_choice_identity:
                //选择平台
                platformSelectPopWindow = new PlatformSelectPopWindow(context, storeIdList);
                platformSelectPopWindow.setPlatformSelectListener(new PlatformSelectPopWindow.PlatformSelectListener() {
                    @Override
                    public void onPlatformSelect(List<PlatformBean> platformBeanList) {
                        storeIdList = platformBeanList;
                        String platName = "";
                        for (int i = 0; i < platformBeanList.size(); i++) {
                            if (i == platformBeanList.size() - 1) {
                                platName += platformBeanList.get(i).getStoreName();
                            } else {
                                platName += platformBeanList.get(i).getStoreName() + ",";
                            }
                        }
                        tvPlatform.setText(platName);
                    }
                });

                platformSelectPopWindow.show();
                break;
            case R.id.btn_upload:
                //上传营业执照
                openAlbum();
                break;
        }
    }


    public void openAlbum() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("com.xiling.fileProvider")
                .start(10101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10101) {
            if (data != null) {
                ArrayList<Photo> resultPhotos = data.getParcelableArrayListExtra(EasyPhotos.RESULT_PHOTOS);
                if (resultPhotos.size() > 0) {
                    uploadImage(resultPhotos.get(0).path);
                }
            }
        }
    }

    /* new SelectCallback() {
        @Override
        public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {

        }
    }*/


    private void uploadImage(final String path) {
        UploadManager.uploadImage(this, path, new BaseRequestListener<UploadResponse>(this) {
            @Override
            public void onSuccess(UploadResponse result) {
                businessLicense = result.url;
                GlideUtils.loadImage(context, ivBusiness, businessLicense);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.error(e.getMessage());
                businessLicense = "";
                GlideUtils.loadImage(context, ivBusiness, businessLicense);
            }


        });
    }

}
