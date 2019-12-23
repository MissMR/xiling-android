package com.xiling.ddui.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.xiling.MyApplication;
import com.xiling.ddui.bean.DDHomeBanner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class BannerManager {

    private static BannerManager self = null;

    public static BannerManager share() {
        if (self == null) {
            self = new BannerManager();
            self.loadData4Disk();
        }
        return self;
    }

    private ArrayList<DDHomeBanner> data = new ArrayList<>();
    private SharedPreferences sp = null;

    private BannerManager() {
        //获取Activity的Preferences对象
        sp = getContext().getSharedPreferences("BANNER", MODE_PRIVATE);
    }

    public Context getContext() {
        return MyApplication.getInstance();
    }

    /**
     * 获取首页Banner图片数据源
     */
    public ArrayList<DDHomeBanner> getData() {
        return data;
    }

    /**
     * 设置首页数据源
     *
     * @param data
     */
    public void setData(ArrayList<DDHomeBanner> data) {
        if (data != null) {
            this.data = data;
            saveData2Disk();
        }
    }

    /**
     * 将数据源保存到本地
     */
    private void saveData2Disk() {
        SharedPreferences.Editor edit = sp.edit();
        String json = new Gson().toJson(data);
        edit.putString("data", json);
        edit.commit();
    }

    /**
     * 从本地取出数据
     */
    private void loadData4Disk() {
        String json = sp.getString("data", "");
        if (!TextUtils.isEmpty(json)) {
            try {
                Type type = new TypeToken<ArrayList<DDHomeBanner>>() {
                }.getType();
                data = new Gson().fromJson(json, type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
