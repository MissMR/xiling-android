package com.dodomall.ddmall.ddui.manager;

import com.dodomall.ddmall.ddui.tools.DLog;

public class AddressMapManager {

    public interface AMApAddressDataListener {
        /**
         * @param lat      纬度
         * @param lng      经度
         * @param province 省名字
         * @param city     市名字
         * @param district 区名字
         * @param address  详细地址名字
         */
        public void onAddressChanged(double lat, double lng, String province, String city, String district, String address);
    }

    private static AddressMapManager self = null;

    public static AddressMapManager share() {
        if (self == null) {
            self = new AddressMapManager();
        }
        return self;
    }

    //默认地址
    private String defaultAddress = "市南区海航万邦";
    private String defaultCity = "青岛市";

    public String getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(String defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getDefaultCity() {
        return defaultCity;
    }

    public void setDefaultCity(String defaultCity) {
        this.defaultCity = defaultCity;
    }

    private AMApAddressDataListener listener = null;

    public AMApAddressDataListener getListener() {
        return listener;
    }

    public void setListener(AMApAddressDataListener listener) {
        this.listener = listener;
    }

    public void targetAddressChanged(double lat, double lng, String province, String city, String district, String address) {
        if (listener != null) {
            listener.onAddressChanged(lat, lng, province, city, district, address);
        } else {
            DLog.i("========================================");
            DLog.w("no listener target for :"
                    + "\nlat:" + lat
                    + "\nlng:" + lng
                    + "\nprovince:" + province
                    + "\ncity:" + city
                    + "\ndistrict:" + district
                    + "\naddress:" + address
            );
            DLog.i("========================================");
        }
    }
}
