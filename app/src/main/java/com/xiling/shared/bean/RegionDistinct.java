package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;
import com.xiling.shared.contracts.IRegion;

import java.io.Serializable;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-07-06
 */
public class RegionDistinct implements Serializable, IRegion {

    @SerializedName("areaId")
    public String id;
    @SerializedName("area")
    public String name;
    @SerializedName("cityId")
    public String parentId;

    @Override
    public String getType() {
        return "distinct";
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
