package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.dodomall.ddmall.shared.contracts.IRegion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-06
 */
public class RegionProvince implements Serializable, IRegion {

    @SerializedName("provinceId")
    public String id;
    @SerializedName("province")
    public String name;

    private List<RegionCity> items = new ArrayList<>();

    public List<RegionCity> getItems() {
        return items;
    }

    public void setItems(List<RegionCity> items) {
        this.items = items;
    }

    @Override
    public String getType() {
        return "province";
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return getName();
    }
}
