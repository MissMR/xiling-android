package com.dodomall.ddmall.ddui.manager;

import android.text.TextUtils;

import com.blankj.utilcode.utils.SPUtils;
import com.dodomall.ddmall.shared.util.SharedPreferenceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DDSearchWordManager {

    /**
     * 搜索结果的条数限制
     */
    private static int size_limit = 10;

    private static DDSearchWordManager self = null;

    public static DDSearchWordManager share() {
        if (self == null) {
            self = new DDSearchWordManager();
        }
        return self;
    }

    private String kValue = "kValue";

    SPUtils spUtils = null;
    ArrayList<String> keywords = new ArrayList<>();
    Gson gson = null;

    private DDSearchWordManager() {
        init();
    }

    public void init() {
        gson = new Gson();
        spUtils = SharedPreferenceUtil.getInstance();
        keywords = getData();
    }

    private void clearOldKeyword(String keyword) {
        String target = "";
        for (String item : keywords) {
            if (item.equals(keyword)) {
                target = item;
                break;
            }
        }
        keywords.remove(target);
    }

    public void addKeyword(String keyword) {
        clearOldKeyword(keyword);
        keywords.add(0, keyword);
        if (keywords.size() > size_limit) {
            try {
                keywords = new ArrayList<>(keywords.subList(0, size_limit));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        setData();
    }

    public void clear() {
        keywords.clear();
        setData();
    }

    public ArrayList<String> getAll() {
        getData();
        return keywords;
    }

    private ArrayList<String> getData() {
        ArrayList<String> value = new ArrayList<>();
        String data = spUtils.getString(kValue, "");
        if (TextUtils.isEmpty(data)) {
            value = new ArrayList<>();
        } else {
            try {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                value = gson.fromJson(data, type);
            } catch (Exception e) {
                e.printStackTrace();
                value = new ArrayList<>();
            }
        }
        return value;
    }

    private void setData() {
        String data = gson.toJson(keywords);
        spUtils.putString(kValue, data);
    }
}
