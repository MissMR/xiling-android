package com.xiling.ddui.activity;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;

import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;

import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.xiling.MyApplication;
import com.xiling.R;
import com.xiling.ddui.adapter.SelectMapAddressAdapter;
import com.xiling.ddui.manager.AddressMapManager;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseActivity;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectAddressMapActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    /**
     * 业务流程
     * <p>
     * 上层传入 - 省市区+地址数据
     * <p>
     * 地理位置编码找到 经纬度 移动地图中心点到此位置
     * 检测到中心点变化的时候，基于此位置数据搜索附近的POI数据
     * <p>
     * 用户点击POI数据列表直接返回上层 - 经纬度+省市区+详细地址
     * <p>
     * 用户可以通过关键字搜索来显示POI数据，POI城市使用上层传入的城市
     * 显示用户当前定位
     */

    //地图控制器
    AMap aMap;

    //地图选点经纬度数据
    double lat;//纬度
    double lng;//经度

    String province;
    String city;
    String district;
    String address;

    //我的位置
    AMapLocation myLocation = null;

    @BindView(R.id.cityNameTextView)
    TextView cityNameTextView;

    @BindView(R.id.searchEditTextMaskView)
    View searchEditTextMaskView;

    @BindView(R.id.searchEditText)
    EditText searchEditText;//搜索关键字

    @BindView(R.id.mapPanel)
    RelativeLayout mapPanel;

    @BindView(R.id.aMapView)
    MapView mMapView;//地图控件

    @BindView(R.id.addressDataView)
    RecyclerView addressDataView;//列表控件

    @BindView(R.id.myPositionMarkView)
    ImageView myPositionMarkView;//当前位置的锚点图示

    @BindView(R.id.myPositionTextView)
    TextView myPositionTextView;//当前位置的文本描述控件

    SelectMapAddressAdapter addressAdapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //键盘弹出不折叠布局
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setContentView(R.layout.activity_select_address_map);
        ButterKnife.bind(this);

        mMapView.onCreate(savedInstanceState);

        initAMap();
        initView();

        //取出默认的地址
        String defaultAddress = AddressMapManager.share().getDefaultAddress();
        //取出默认的城市
        String defaultCity = AddressMapManager.share().getDefaultCity();
        //发编码找到默认位置锚点
        searchPointInfo(defaultCity, defaultAddress);
    }

    void initView() {
        showHeader("选择地址", true);

        //设置适配器属性
        addressAdapter = new SelectMapAddressAdapter(this);

        //设置列表属性
        addressDataView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        addressDataView.setAdapter(addressAdapter);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchHandler.removeMessages(0);
                searchHandler.sendEmptyMessageDelayed(0, 600);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addressAdapter.setListener(new SelectMapAddressAdapter.EventListener() {
            @Override
            public void onPoiItemClicked(int position, PoiItem item) {
                closeSearchMode();
//                LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
//                //移动坐标到点选的位置
//                moveMapCenterToLatLng(latLng);

                //点击数据中的某一行直接回调到上层界面
                callback(item);
            }
        });

        initShakeAnimation();
    }

    public void callback(PoiItem poiItem) {

        printPoi(poiItem);

        LatLonPoint point = poiItem.getLatLonPoint();
        lat = point.getLatitude();
        lng = point.getLongitude();

        province = poiItem.getProvinceName();
        city = poiItem.getCityName();
        district = poiItem.getAdName();
        address = poiItem.getSnippet();

        callback();
    }

    public void callback() {
        AddressMapManager.share().targetAddressChanged(lat, lng, province, city, district, address);
        finish();
    }

    Handler searchHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String keyword = searchEditText.getText().toString();
                if (searchMode) {
                    searchAddressByKeyword(keyword);
                } else {
                    DLog.d("not search mode.");
                }

            }
        }
    };

    //锚点是否移动 - 控制移动特效
    boolean isPointMove = false;

    //初始化
    public void initAMap() {

        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        //隐藏缩放按钮
        aMap.getUiSettings().setZoomControlsEnabled(false);

        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                DLog.i("onCameraChange:" + cameraPosition);
                if (!isPointMove) {
                    makeCenterPointJumpUp();
                }
                isPointMove = true;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                DLog.i("onCameraChangeFinish:" + cameraPosition);
                makeCenterPointFallDown();
                isPointMove = false;
                onMapCenterChanged(cameraPosition.target);
            }
        });

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(20000);
        int fillColor = Color.parseColor("#1cff4646");
        myLocationStyle.strokeColor(fillColor);
        myLocationStyle.radiusFillColor(fillColor);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.icon_map_current));
        myLocationStyle.strokeWidth(0f);
        // 跟随定位但不改变地图中心点
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);

        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationRotateAngle(180);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                DLog.e("onMyLocationChange:" + location);
//                aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
            }
        });
        // 设置定位资源
        aMap.setLocationSource(this);
        // 开启我的位置定位
        aMap.setMyLocationEnabled(true);

        // 初始化搜索对象
        initGeoSearch();

        startMyLocation();
    }

    AMapLocationClient mlocationClient = null;
    OnLocationChangedListener mLocationChangedListener = null;
    AMapLocationClientOption mLocationOption = null;

    void startMyLocation() {
        //初始化定位
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位回调监听
        mlocationClient.setLocationListener(this);
        //设置为高精度定位模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        mlocationClient.startLocation();//启动定位
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    boolean searchMode = false;

    @OnClick(R.id.searchEditTextMaskView)
    void gotoSearchMode() {
        searchMode = true;
        searchEditTextMaskView.setVisibility(View.GONE);
        mapPanel.setVisibility(View.GONE);
        showSoftInputFromWindow(searchEditText);
    }

    void closeSearchMode() {
        searchMode = false;
        searchEditText.setText("");
        searchEditTextMaskView.setVisibility(View.VISIBLE);
        mapPanel.setVisibility(View.VISIBLE);
        hideKeyboard();
    }

    @OnClick(R.id.findMyLocationButton)
    void onFindMyLocationPressed() {
        //将地图中心点移动到当前用户的位置
        if (myLocation == null) {
            //如果进入地图没有定位成功则使用App级别的定位权限
           // myLocation = MyApplication.mAMapLocation;
        }
        double myLat = myLocation.getLatitude();
        double myLng = myLocation.getLongitude();
        LatLng latLng = new LatLng(myLat, myLng);
        //移动中心点
        moveMapCenterToLatLng(latLng);
    }

    /**
     * 让中心点弹起
     */
    void makeCenterPointJumpUp() {
        makeCenterPointMove(false);
    }

    /**
     * 让中心点落下
     */
    void makeCenterPointFallDown() {
        makeCenterPointMove(true);
    }

    //锚点动画集
    AnimationSet shakeAnimation = null;

    //初始化动画集
    void initShakeAnimation() {

        float scaleSmall = 1.0f;
        float scaleLarge = 1.1f;
        float shakeDegrees = 8f;
        long duration = 1500;

        //由小变大
        Animation scaleAnim = new ScaleAnimation(scaleSmall, scaleLarge, scaleSmall, scaleLarge);
        //从左向右
        Animation rotateAnim = new RotateAnimation(-shakeDegrees, shakeDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnim.setDuration(duration);
        rotateAnim.setDuration(duration / 10);
        rotateAnim.setRepeatMode(Animation.RESTART);
        rotateAnim.setRepeatCount(Animation.INFINITE);

        shakeAnimation = new AnimationSet(false);
        shakeAnimation.addAnimation(scaleAnim);
        shakeAnimation.addAnimation(rotateAnim);
        shakeAnimation.setRepeatMode(Animation.RESTART);
        shakeAnimation.setRepeatCount(Animation.INFINITE);
    }

    /**
     * 锚点动画
     */
    void makeCenterPointMove(boolean isDown) {
        if (isDown) {
            myPositionMarkView.clearAnimation();
        } else {
            myPositionMarkView.startAnimation(shakeAnimation);
        }
    }

    void changeCallbackData(PoiItem poi) {
        LatLonPoint point = poi.getLatLonPoint();
        double lat = point.getLatitude();
        double lng = point.getLongitude();
        String province = poi.getProvinceName();
        String city = poi.getCityName();
        String district = poi.getAdName();
        String address = poi.getSnippet();
        changeCallbackData(lat, lng, province, city, district, address);
    }

    void changeCallbackData(GeocodeAddress geoAddress) {
        String province = geoAddress.getProvince();
        String city = geoAddress.getCity();
        String district = geoAddress.getDistrict();

        String address = geoAddress.getFormatAddress();
        //替换省市区数据
        address = address.replace(province + city + district, "");

        LatLonPoint point = geoAddress.getLatLonPoint();
        double lat = point.getLatitude();
        double lng = point.getLongitude();

        changeCallbackData(lat, lng, province, city, district, address);
    }

    void changeCallbackData(AMapLocation location) {
        String province = location.getProvince();
        String city = location.getCity();
        String district = location.getDistrict();

        String address = location.getAddress();

        double lat = location.getLatitude();
        double lng = location.getLongitude();

        changeCallbackData(lat, lng, province, city, district, address);
    }

    void changeCallbackData(double lat, double lng, String province, String city, String district, String address) {
        this.lat = lat;
        this.lng = lng;
        this.province = province;
        this.city = city;
        this.district = district;
        this.address = address;

        cityNameTextView.setText("" + city);
    }

    void setCurrentPoi(PoiItem poi) {
        if (poi != null) {
            changeCallbackData(poi);
            myPositionTextView.setText(poi.getTitle());
            myPositionTextView.setVisibility(View.VISIBLE);
        } else {
            myPositionTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        DLog.d("location activate.");
        mLocationChangedListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        DLog.d("location deactivate.");
        mLocationChangedListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null && location.getErrorCode() == 0) {
            myLocation = location;
            if (mLocationChangedListener != null) {
                //调用系统显示我的位置数据
                mLocationChangedListener.onLocationChanged(location);
            }
        }
    }

    boolean isAnimation = false;

    /**
     * 移动地图中心点到经纬度
     */
    void moveMapCenterToLatLng(LatLng latLng) {
        aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    void moveMapCenterToLatLng(LatLonPoint point) {
        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
        moveMapCenterToLatLng(latLng);
    }

    void moveMapCenterToLatLng(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);
        moveMapCenterToLatLng(latLng);
    }

    /**
     * 地图中心点改变
     */
    void onMapCenterChanged(LatLng latLng) {
        DLog.d("onMapCenterChanged:" + latLng);
        //获取当前地图中心点附近的建议位置
        searchAddressByLatLng(latLng);
    }

    GeocodeSearch geocoderSearch = null;

    void initGeoSearch() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

//                province = regeocodeResult.getRegeocodeAddress().getProvince();
//                city = regeocodeResult.getRegeocodeAddress().getCity();
//                district = regeocodeResult.getRegeocodeAddress().getDistrict();
//
//                DLog.i("=======================================");
//                DLog.d("\nprovince:" + province + "\ncity:" + city + "\ndistrict:" + district);
//                DLog.i("=======================================");

            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
                List<GeocodeAddress> result = geocodeResult.getGeocodeAddressList();
                DLog.i("onGeocodeSearched data size : " + result.size());
                if (result.size() > 0) {
                    GeocodeAddress geoAddress = result.get(0);
                    changeCallbackData(geoAddress);
                    moveMapCenterToLatLng(geoAddress.getLatLonPoint());
                } else {
                    ToastUtil.error("无法找到地址，自动使用当前定位");
                  //  AMapLocation mAMapLocation = MyApplication.mAMapLocation;
                   // changeCallbackData(mAMapLocation);
                    //moveMapCenterToLatLng(mAMapLocation.getLatitude(), mAMapLocation.getLongitude());
                }
            }
        });
    }

    /**
     * 地理位置反编码
     */
    void searchPointInfo(String city, String address) {
        DLog.d("反编码地址:" + address + " 城市:" + city);
        GeocodeQuery query = new GeocodeQuery(address, city);
        geocoderSearch.getFromLocationNameAsyn(query);
    }

    /**
     * 根据关键字搜索地址信息
     */
    void searchAddressByKeyword(String keyword) {
        DLog.d("searchAddressByKeyword:" + keyword);

        if (TextUtils.isEmpty(city)) {
            city = "";
        }
        PoiSearch.Query query = new PoiSearch.Query(keyword, "120000|190106|190107|190600", city);
        //设置每页最多返回多少条
        query.setPageSize(100);
        //设置查询页码
        query.setPageNum(0);
        //
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                ArrayList<PoiItem> data = poiResult.getPois();
                DLog.d("onPoiSearched data size:" + data.size());
                addressAdapter.setItems(data);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                DLog.i("onPoiItemSearched:" + poiItem + "," + i);
            }
        });
        poiSearch.searchPOIAsyn();
    }

    /**
     * 根据经纬度返回地址信息
     */
    void searchAddressByLatLng(LatLng latLng) {
        DLog.d("searchAddressByLatLng:" + latLng);
        PoiSearch.Query query = new PoiSearch.Query("", "120000|190106|190107|190600", "");
        //设置每页最多返回多少条
        query.setPageSize(100);
        //设置查询页码
        query.setPageNum(0);
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 5000));
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                ArrayList<PoiItem> data = poiResult.getPois();
                DLog.d("onPoiSearched data size:" + data.size());
                if (data.size() > 0) {
                    PoiItem item = data.get(0);
                    setCurrentPoi(item);
                } else {
                    setCurrentPoi(null);
                }
                addressAdapter.setItems(data);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                DLog.i("onPoiItemSearched:" + poiItem + "," + i);
            }
        });
        poiSearch.searchPOIAsyn();
    }

    void printPoi(PoiItem data) {

        StringBuffer buffer = new StringBuffer();
        buffer.append("\ngetTitle:" + data.getTitle());

        buffer.append("\ngetAdCode:" + data.getAdCode());
        buffer.append("\ngetAdName:" + data.getAdName());

        buffer.append("\ngetTypeCode:" + data.getTypeCode());
        buffer.append("\ngetTypeDes:" + data.getTypeDes());

        buffer.append("\ngetProvinceCode:" + data.getProvinceCode());
        buffer.append("\ngetProvinceName:" + data.getProvinceName());

        buffer.append("\ngetCityCode:" + data.getCityCode());
        buffer.append("\ngetCityName:" + data.getCityName());

        buffer.append("\ngetBusinessArea:" + data.getBusinessArea());

        buffer.append("\ngetDirection:" + data.getDirection());
        buffer.append("\ngetEmail:" + data.getEmail());
        buffer.append("\ngetParkingType:" + data.getParkingType());
        buffer.append("\ngetPoiId:" + data.getPoiId());
        buffer.append("\ngetPostcode:" + data.getPostcode());

        buffer.append("\ngetShopID:" + data.getShopID());
        buffer.append("\ngetSnippet:" + data.getSnippet());
        buffer.append("\ngetTel:" + data.getTel());
        buffer.append("\ngetWebsite:" + data.getWebsite());
        buffer.append("\ngetDistance:" + data.getDistance());

        buffer.append("\ngetEnter:" + data.getEnter());
        buffer.append("\ngetExit:" + data.getExit());

        buffer.append("\ngetIndoorData:" + data.getIndoorData());
        buffer.append("\ngetLatLonPoint:" + data.getLatLonPoint());
        buffer.append("\ngetPhotos:" + data.getPhotos());
        buffer.append("\ngetPoiExtension:" + data.getPoiExtension());
        buffer.append("\ngetSubPois:" + data.getSubPois());

        DLog.i("" + buffer);
    }

}
