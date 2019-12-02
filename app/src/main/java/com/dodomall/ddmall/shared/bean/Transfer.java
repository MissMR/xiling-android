package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan on 2017/6/20.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017/6/20 下午5:48
 */

public class Transfer {

    @SerializedName("transferId")
    public String transferId;
    @SerializedName("outPhone")
    public String outPhone;
    @SerializedName("phone")
    public String phone;
    @SerializedName("outNickName")
    public String outNickName;
    @SerializedName("outUserName")
    public String outUserName;
    @SerializedName("inPhone")
    public String inPhone;
    @SerializedName("inNickName")
    public String inNickName;
    @SerializedName("inUserName")
    public String inUserName;
    @SerializedName("transferTypeStr")
    public String transferTypeStr;
    @SerializedName("transferMoney")
    public long transferMoney;
    @SerializedName("statusStr")
    public String statusStr;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("transferMemo")
    public String transferMemo;

}
