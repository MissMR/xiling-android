package com.xiling.ddmall.shared.component.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.blankj.utilcode.utils.ToastUtils;
import com.google.gson.Gson;
import com.xiling.ddmall.shared.util.GetJsonDataUtil;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/5/3.
 */
public class CityPickerDialog {
    private Context mContext;
    private Thread thread;
    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private CitySelectListener mCitySelectListener;
    private boolean isLoaded = false;

    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    if (listener != null) {
                        listener.onLoadAddressDataEnd();
                    }
                    break;

                case MSG_LOAD_FAILED:
                    Toast.makeText(mContext, "解析地址数据失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    public CityPickerDialog(Context context) {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mContext = context;
        initJsonData();
    }

    public interface AddressDataListener {
        void onLoadAddressDataEnd();
    }

    AddressDataListener listener = null;

    public void setListener(AddressDataListener listener) {
        this.listener = listener;
    }

    String dProvince = "";
    String dCity = "";
    String dArea = "";

    public void setDefaultAddress(String province, String city, String area) {
        this.dProvince = province;
        this.dCity = city;
        this.dArea = area;
    }

    /**
     * 弹出地址选择器
     */
    public void showPickerView() {
        if (!isLoaded) {
            ToastUtils.showShortToast("请稍后，正在获取地址信息");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (mCitySelectListener != null) {
                    mCitySelectListener.select(options1Items.get(options1).getPickerViewText(),
                            options2Items.get(options1).get(options2),
                            options3Items.get(options1).get(options2).get(options3));
                }
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(options1Items, options2Items, options3Items);

        if (!TextUtils.isEmpty(dProvince)) {
            if (!TextUtils.isEmpty(dCity)) {
                if (!TextUtils.isEmpty(dArea)) {
                    int provinceIndex = -1;
                    for (int i = 0; i < options1Items.size(); i++) {
                        String item = options1Items.get(i).toString();
                        if (dProvince.equals(item)) {
                            provinceIndex = i;
                            break;
                        }
                    }

                    if (provinceIndex > -1) {
                        int cityIndex = -1;
                        ArrayList<String> citys = options2Items.get(provinceIndex);
                        for (int j = 0; j < citys.size(); j++) {
                            String item = citys.get(j).toString();
                            if (dCity.equals(item)) {
                                cityIndex = j;
                                break;
                            }
                        }
                        if (cityIndex > -1) {
                            int areaIndex = -1;
                            ArrayList<String> areas = options3Items.get(provinceIndex).get(cityIndex);
                            for (int k = 0; k < areas.size(); k++) {
                                String item = citys.get(k).toString();
                                if (dCity.equals(item)) {
                                    areaIndex = k;
                                    break;
                                }
                            }
                            if (areaIndex > -1) {
                                pvOptions.setSelectOptions(provinceIndex, cityIndex, areaIndex);
                            }
                        }

                    }

                }
            }
        }


        pvOptions.show();
    }


    public void show2ItemPickerView() {
        if (!isLoaded) {
            ToastUtils.showShortToast("请稍后，获取地址信息");
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (mCitySelectListener != null) {
                    mCitySelectListener.select(options1Items.get(options1).getPickerViewText(),
                            options2Items.get(options1).get(options2),
                            options3Items.get(options1).get(options2).get(options3));
                }
            }
        })
                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        pvOptions.setPicker(options1Items, options2Items);
        pvOptions.show();
    }

    public void show2ItemPickerView(CitySelectListener listener) {
        setCitySelectListener(listener);
        show2ItemPickerView();
    }

    public void showPickerView(CitySelectListener listener) {
        setCitySelectListener(listener);
        showPickerView();
    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(mContext, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    public void setCitySelectListener(CitySelectListener citySelectListener) {
        mCitySelectListener = citySelectListener;
    }

    public class JsonBean implements IPickerViewData {


        /**
         * name : 省份
         * city : [{"name":"北京市","area":["东城区","西城区","崇文区","宣武区","朝阳区"]}]
         */

        private String name;
        private List<CityBean> city;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<CityBean> getCityList() {
            return city;
        }

        public void setCityList(List<CityBean> city) {
            this.city = city;
        }

        // 实现 IPickerViewData 接口，
        // 这个用来显示在PickerView上面的字符串，
        // PickerView会通过IPickerViewData获取getPickerViewText方法显示出来。
        @Override
        public String getPickerViewText() {
            return this.name;
        }


        public class CityBean {
            /**
             * name : 城市
             * area : ["东城区","西城区","崇文区","昌平区"]
             */

            private String name;
            private List<String> area;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<String> getArea() {
                return area;
            }

            public void setArea(List<String> area) {
                this.area = area;
            }
        }

        public class AreaBean {

        }
    }

    public interface CitySelectListener {
        /**
         * 地址选择
         *
         * @param province 广东省
         * @param city     广州市
         * @param county   天河区
         */
        void select(String province, String city, String county);
    }
}
