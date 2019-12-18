package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Chan on 2017/6/17.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017/6/17 下午3:03
 */

public class Balance implements Serializable {
    @SerializedName("did")
    public String did;
    @SerializedName("code")
    public String code;
    @SerializedName("typeId")
    public long typeId;
    @SerializedName("money")
    public long money;
    @SerializedName("typeName")
    public String typeName;
    @SerializedName("statusStr")
    public String statusStr;
    @SerializedName("createDate")
    public String createDate;
}
