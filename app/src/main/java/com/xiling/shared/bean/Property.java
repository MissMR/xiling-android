package com.xiling.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Property {
    @SerializedName("propertyId")
    public String id;
    @SerializedName("propertyName")
    public String name;
    @SerializedName("propertyValues")
    public List<PropertyValue> values;

    public void reset() {
        for (PropertyValue v : values) {
            v.reset();
        }
    }

}