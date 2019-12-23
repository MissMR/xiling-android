package com.xiling.ddui.tools;

import android.content.Context;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.bean.RegionCity;
import com.xiling.shared.bean.RegionDistinct;
import com.xiling.shared.bean.RegionProvince;
import com.xiling.shared.contracts.IRegion;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IRegionService;
import com.xiling.shared.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class AddressTools {

    Context context = null;

    public AddressTools(Context context) {
        this.context = context;
        mRegionService = ServiceManager.getInstance().createService(IRegionService.class);
    }

    private IRegionService mRegionService;

    private interface DDMAddressLoadListener {
        void onDataLoad(ArrayList<IRegion> data);
    }

    public void show() {
        loadProvinceList(new DDMAddressLoadListener() {
            @Override
            public void onDataLoad(final ArrayList<IRegion> provinces) {
                IRegion province = provinces.get(0);
                loadCityListById(province.getId(), new DDMAddressLoadListener() {
                    @Override
                    public void onDataLoad(final ArrayList<IRegion> citys) {
                        IRegion city = citys.get(0);
                        loadDistinctListById(city.getId(), new DDMAddressLoadListener() {
                            @Override
                            public void onDataLoad(final ArrayList<IRegion> areas) {
                                firstShowData(provinces, citys, areas);
                            }
                        });
                    }
                });
            }
        });
    }

    void firstShowData(ArrayList<IRegion> provinces, ArrayList<IRegion> citys, ArrayList<IRegion> areas) {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                DLog.i("onOptionsSelect:" + options1 + "," + options2 + "," + options3);
            }
        });
        OptionsPickerView<IRegion> pickerView = builder.build();
        pickerView.setNPicker(provinces, citys, areas);
        pickerView.show();
    }

    private void loadProvinceList(final DDMAddressLoadListener listener) {
        APIManager.startRequest(mRegionService.getProvinceList(), new BaseRequestListener<List<RegionProvince>>() {
            @Override
            public void onSuccess(List<RegionProvince> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    return;
                }

                ArrayList<IRegion> items = new ArrayList<>();
                for (RegionProvince province : result) {
                    items.add(province);
                }

                if (listener != null) {
                    listener.onDataLoad(items);
                }
            }
        });
    }

    private void loadCityListById(final String pId, final DDMAddressLoadListener listener) {
        APIManager.startRequest(mRegionService.getCityList(pId), new BaseRequestListener<List<RegionCity>>() {
            @Override
            public void onSuccess(List<RegionCity> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    return;
                }

                ArrayList<IRegion> items = new ArrayList<>();
                for (RegionCity city : result) {
                    items.add(city);
                }

                if (listener != null) {
                    listener.onDataLoad(items);
                }
            }
        });
    }

    private void loadDistinctListById(final String cId, final DDMAddressLoadListener listener) {
        APIManager.startRequest(mRegionService.getDistinctList(cId), new BaseRequestListener<List<RegionDistinct>>() {
            @Override
            public void onSuccess(List<RegionDistinct> result) {
                if (result.isEmpty()) {
                    ToastUtil.error("地址库错误，请联系管理员");
                    return;
                }

                ArrayList<IRegion> items = new ArrayList<>();
                for (RegionDistinct distinct : result) {
                    items.add(distinct);
                }

                if (listener != null) {
                    listener.onDataLoad(items);
                }
            }
        });
    }
}
