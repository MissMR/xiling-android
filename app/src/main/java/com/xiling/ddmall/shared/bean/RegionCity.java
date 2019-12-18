package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.xiling.ddmall.shared.contracts.IRegion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-06
 */
public class RegionCity implements Serializable, IRegion {

    @SerializedName("cityId")
    public String id;
    @SerializedName("city")
    public String name;
    @SerializedName("provinceId")
    public String parentId;

    private List<RegionDistinct> items = new ArrayList<>();

    public List<RegionDistinct> getItems() {
        return items;
    }

    public void setItems(List<RegionDistinct> items) {
        this.items = items;
    }

    @Override
    public String getType() {
        return "city";
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
        return name;
    }
}
