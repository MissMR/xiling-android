package com.xiling.module.address;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.bigkoo.pickerview.OptionsPickerView;
import com.blankj.utilcode.utils.StringUtils;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.activity.SelectAddressMapActivity;
import com.xiling.ddui.custom.DDDeleteDialog;
import com.xiling.ddui.manager.AddressMapManager;
import com.xiling.ddui.manager.AddressPicker;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.Address;
import com.xiling.shared.bean.api.RequestResult;
import com.xiling.shared.bean.event.EventMessage;
import com.xiling.shared.constant.Event;
import com.xiling.shared.constant.Key;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IAddressService;
import com.xiling.shared.service.contract.IRegionService;
import com.xiling.shared.util.ToastUtil;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import rx.Observer;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.module.address
 * @since 2017-06-10
 */
public class AddressFormActivity extends BaseActivity {

    private boolean isEditAddress = false;
    private IAddressService mAddressService;
    private Address mAddress = new Address();
    private AMapLocationClient mLocationClient;

    @BindView(R.id.contactsEt)
    protected EditText mContactsEt;
    @BindView(R.id.phoneEt)
    protected EditText mPhoneEt;
    @BindView(R.id.regionTv)
    protected TextView mRegionTv;
    @BindView(R.id.detailEt)
    protected EditText mDetailEt;
    @BindView(R.id.defaultSwitch)
    protected ImageView mDefaultSwitch;
    private IRegionService mRegionService;

    AddressPicker picker = null;

    @OnClick(R.id.selectNowAddress)
    void onSelectNowAddress() {
        //判断是否有读取用户信息权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onCompleted() {
                        DLog.i("LOCATION.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.e("LOCATION.onError:" + e.getMessage());
                        ToastUtil.error("申请位置权限失败，请前往APP应用设置中打开此权限");
                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            jumpMapActivity();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtil.error("缺少位置权限，请同意位置权限的申请");
                        } else {
                            ToastUtil.error("缺少位置权限，请前往APP应用设置中打开此权限");
                        }
                    }
                });
    }

    public void jumpMapActivity() {
        AddressMapManager.share().setListener(new AddressMapManager.AMApAddressDataListener() {
            @Override
            public void onAddressChanged(double lat, double lng, String province, String city, String district, String address) {
                DLog.i("========================================");
                DLog.d("onAddressChanged:"
                        + "\nlat:" + lat
                        + "\nlng:" + lng
                        + "\nprovince:" + province
                        + "\ncity:" + city
                        + "\ndistrict:" + district
                        + "\naddress:" + address
                );
                DLog.i("========================================");

                //转换选择的数据为App规则数据
                convertMapData(province, city, district, address);
            }
        });

        //设置默认城市
        AddressMapManager.share().setDefaultCity(mAddress.cityName);
        //设置默认地址
        AddressMapManager.share().setDefaultAddress(mAddress.detail);

        Intent mapIntent = new Intent(context, SelectAddressMapActivity.class);
        startActivity(mapIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_form_layout);
        ButterKnife.bind(this);
        mAddressService = ServiceManager.getInstance().createService(IAddressService.class);
        mRegionService = ServiceManager.getInstance().createService(IRegionService.class);
        mDefaultSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDefaultSwitch.setSelected(!mDefaultSwitch.isSelected());
            }
        });

        Intent intent = getIntent();
        String addressId = "";
        if (intent == null) {
            isEditAddress = false;
        } else if (intent.getExtras() == null) {
            isEditAddress = false;
        } else {
            String action = intent.getExtras().getString("action");
            isEditAddress = action != null && action.equals(Key.EDIT_ADDRESS);
            addressId = intent.getExtras().getString("addressId");
        }
        setTitle(isEditAddress ? "编辑收货地址" : "添加收货地址");
        initHeaderButtons();
        if (isEditAddress && addressId != null && !addressId.isEmpty()) {
            getAddressInfo(addressId);
        } else {
            autoLocationCity();
        }

        picker = new AddressPicker(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 自动定位城市
     */
    private void autoLocationCity() {
        AMapLocation mAMapLocation = MyApplication.mAMapLocation;
        if (mAMapLocation == null) {
            return;
        }
        if (mAMapLocation.getErrorCode() != 0) {
            return;
        }
        final String provinceName = mAMapLocation.getProvince();
        final String cityName = mAMapLocation.getCity();
        final String distinctName = mAMapLocation.getDistrict();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("" + mAMapLocation.getStreet());
        stringBuffer.append("" + mAMapLocation.getStreetNum());
        stringBuffer.append("" + mAMapLocation.getPoiName());
        final String addressValue = stringBuffer.toString();

        convertMapData(provinceName, cityName, distinctName, addressValue);
    }

    /**
     * 转化地图数据为系统内部编码可识别的数据
     */
    public void convertMapData(final String provinceName, final String cityName, final String distinctName, final String addressValue) {
        final AddressPicker picker = new AddressPicker(context);
        picker.setDataLoadListener(new AddressPicker.AddressDataLoadListener() {
            @Override
            public void onLoadFinish() {
                AddressPicker.Province province = picker.getProvinceByName(provinceName);
                if (province != null) {

                    mAddress.provinceId = province.getCode();
                    mAddress.provinceName = province.getName();

                    int pIndex = picker.getProvinceIndex(province);
                    if (pIndex > -1) {
                        AddressPicker.City city = picker.getCityByName(pIndex, cityName);
                        if (city != null) {

                            mAddress.cityId = city.getCode();
                            mAddress.cityName = city.getName();

                            int cIndex = picker.getCityIndex(pIndex, city);
                            if (cIndex > -1) {
                                AddressPicker.Area area = picker.getAreaByName(pIndex, cIndex, distinctName);
                                if (area != null) {

                                    mAddress.districtId = area.getCode();
                                    mAddress.districtName = area.getName();

                                    DLog.i("=================================");
                                    DLog.d("命中！");
                                    DLog.d("" + province);
                                    DLog.d("" + city);
                                    DLog.d("" + area);
                                    DLog.i("=================================");

                                    setPCAText(mAddress.getFullRegion());
                                    mDetailEt.setText("" + addressValue);
                                }
                            }
                        }
                    }
                }
            }
        });
        picker.readData();
    }

    public void setPCAText(String text) {
        mRegionTv.setText("" + text);
        mRegionTv.setTextColor(Color.BLACK);
    }

    private void initHeaderButtons() {
        getHeaderLayout().setLeftDrawable(R.mipmap.icon_back_black);
        getHeaderLayout().setOnLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (isEditAddress) {
            getHeaderLayout().setRightDrawable(R.mipmap.icon_address_trash);
            getHeaderLayout().setOnRightClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DDDeleteDialog dialog = new DDDeleteDialog(context);
                    dialog.setTitle("确定要狠心删除此地址吗");
                    dialog.setListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            deleteAddress();
                        }
                    });
                    dialog.show();
                }
            });
        }
    }

    @OnClick(R.id.contactPanel)
    void selectFromContact() {
        //判断是否有读取用户信息权限
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_CONTACTS)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onCompleted() {
                        DLog.i("READ_CONTACTS.onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DLog.e("READ_CONTACTS.onError:" + e.getMessage());
                        ToastUtil.error("申请读取联系人数据权限失败，请前往APP应用设置中打开此权限");
                    }

                    @Override
                    public void onNext(Permission permission) {
                        if (permission.granted) {
                            jumpToContact();
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            ToastUtil.error("缺少去读联系人数据权限，请同意读取联系人信息");
                        } else {
                            ToastUtil.error("缺少读取联系人数据权限，请前往APP应用设置中打开此权限");
                        }
                    }
                });
    }

    static int RequestCode = 0x0;

    void jumpToContact() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        this.startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == RequestCode) {

//            data.getScheme();//content://
//            data.getDataString()//

            try {
                ContentResolver resolver = getContentResolver();
                Uri contactData = data.getData();
                Cursor cursor = managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
                String username = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                Cursor cursorPhone = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null,
                        null);

                ArrayList<String> phones = new ArrayList<>();
                DLog.i("=========" + username + "===========");
                while (cursorPhone.moveToNext()) {
                    String phoneNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    DLog.i("phoneNumber:" + phoneNumber);
                    phones.add(phoneNumber);
                }
                DLog.i("====================");

                setContactData(username, phones);

            } catch (Exception e) {
                ToastUtil.error("无法读取联系人数据");
                DLog.e("" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void setContactData(String username, final ArrayList<String> phones) {

        if (!TextUtils.isEmpty(username)) {
            mAddress.contacts = username;
            mContactsEt.setText("" + username);
        }

        if (phones != null && phones.size() > 0) {
            if (phones.size() == 1) {
                String phoneNumber = phones.get(0);
                mAddress.phone = phoneNumber;
                mPhoneEt.setText(phoneNumber);
            } else {
                ToastUtil.success("选择的联系人有多个手机号，请选择其中任意一个");

                OptionsPickerView.Builder options = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        DLog.i("select phone:" + options1);
                        if (options1 < phones.size()) {
                            String phoneNumber = phones.get(options1);
                            mAddress.phone = phoneNumber;
                            mPhoneEt.setText(phoneNumber);
                        } else {
                            ToastUtil.error("数据异常");
                        }
                    }
                });
                options.setSubmitColor(Color.parseColor("#FF4647"));//确定按钮文字颜色
                options.setCancelColor(Color.parseColor("#A9A9A9"));//取消按钮文字颜色
                options.setTitleText("选择手机号");
                options.setSubmitText("选择");
                options.setCancelText("取消");

                OptionsPickerView pickerView = options.build();
                pickerView.setPicker(phones);
                pickerView.show();
            }
        } else {
            ToastUtil.error("无法识别联系人数据");
        }
    }

    private void deleteAddress() {
        APIManager.startRequest(mAddressService.deleteAddress(mAddress.addressId), new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("删除成功");
                EventBus.getDefault().post(new EventMessage(Event.deleteAddress, mAddress));
                finish();
            }
        });
    }

    private void getAddressInfo(final String addressId) {
        APIManager.startRequest(mAddressService.getAddressDetail(addressId), new BaseRequestListener<Address>(this) {
            @Override
            public void onSuccess(Address address) {
                mAddress = address;
                mContactsEt.setText(mAddress.contacts);
                mContactsEt.setSelection(mContactsEt.getText().length());

                mPhoneEt.setText(mAddress.phone);

                setPCAText(mAddress.getFullRegion());

                mDetailEt.setText(mAddress.detail);
                mDefaultSwitch.setSelected(mAddress.isDefault);
            }
        });
    }

    @OnClick(R.id.regionLayout)
    protected void showRegionSelector() {

        hideKeyboard();

        picker.setDefaultData(mAddress.provinceId, mAddress.cityId, mAddress.districtId);
        picker.setListener(new AddressPicker.AddressPickerListener() {
            @Override
            public void onAddressSelected(AddressPicker.Province province, AddressPicker.City city, AddressPicker.Area area) {

                DLog.d("fullAddress:" + province + " " + city + " " + area);
                DLog.d("ids:" + province.getCode() + "," + city.getCode() + "," + area.getCode());

                if (province != null) {
                    mAddress.provinceId = province.getCode();
                    mAddress.provinceName = province.getName();
                }

                if (city != null) {
                    mAddress.cityId = city.getCode();
                    mAddress.cityName = city.getName();
                }

                if (area != null) {
                    mAddress.districtId = area.getCode();
                    mAddress.districtName = area.getName();
                }

                setPCAText(mAddress.getFullRegion());
            }
        });
        picker.showPickerView();
    }

    @OnClick(R.id.saveBtn)
    protected void onSave() {
        String contacts = mContactsEt.getText().toString();
        if (contacts.isEmpty()) {
            ToastUtil.error("收件人不能为空");
            return;
        }
        String phone = mPhoneEt.getText().toString();
        if (phone.isEmpty()) {
            ToastUtil.error("联系电话不能为空");
            return;
        }
//        if (!RegexUtils.isMobileSimple(phone)) {
//            ToastUtil.error("请检查联系电话格式");
//            return;
//        }
        if (StringUtils.isEmpty(mAddress.provinceId) || StringUtils.isEmpty(mAddress.cityId) || StringUtils.isEmpty(mAddress.districtId)) {
            ToastUtil.error("请选择地区");
            return;
        }
        String detail = mDetailEt.getText().toString();
        if (detail.isEmpty()) {
            ToastUtil.error("请输入收货地址");
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("isDefault", mDefaultSwitch.isSelected() ? 1 : 0);
        params.put("contact", contacts);
        params.put("phone", phone);
        params.put("provinceName", mAddress.provinceName);
        params.put("cityName", mAddress.cityName);
        params.put("districtName", mAddress.districtName);
        params.put("provinceId", mAddress.provinceId);
        params.put("cityId", mAddress.cityId);
        params.put("districtId", mAddress.districtId);
        params.put("detail", detail);
        Observable<RequestResult<Object>> resultObservable;
        if (isEditAddress) {
            resultObservable = mAddressService.editAddress(mAddress.addressId, params);
        } else {
            resultObservable = mAddressService.createAddress(params);
        }
        APIManager.startRequest(resultObservable, new BaseRequestListener<Object>(this) {

            @Override
            public void onSuccess(Object result) {
                ToastUtil.success("保存成功");
                EventBus.getDefault().post(new EventMessage(Event.saveAddress));
                finish();
            }
        });
    }
}
