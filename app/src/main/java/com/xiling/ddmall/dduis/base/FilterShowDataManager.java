package com.xiling.ddmall.dduis.base;

import com.xiling.ddmall.dduis.bean.FilterShowData;

import java.util.HashMap;

public class FilterShowDataManager {

    static FilterShowDataManager self = null;

    public static FilterShowDataManager getInstance() {
        if (self == null) {
            synchronized (self) {
                self = new FilterShowDataManager();
            }
        }
        return self;
    }

    //记录数据
    HashMap<String, FilterShowData> mem = new HashMap<>();

    public void add(String categoryId, FilterShowData data) {
        if (data != null) {
            mem.put(categoryId, data);
        } else {
            mem.remove(categoryId);
        }
    }

    public FilterShowData get(String categoryId) {
        if (mem.containsKey(categoryId)) {
            return mem.get(categoryId);
        } else {
            return null;
        }
    }

}
