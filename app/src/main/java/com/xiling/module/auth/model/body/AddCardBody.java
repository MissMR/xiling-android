package com.xiling.module.auth.model.body;

import com.google.gson.annotations.SerializedName;
import com.xiling.shared.util.ConvertUtil;

import org.json.JSONException;

import java.util.Map;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/7/5.
 */
public class AddCardBody {

    @SerializedName("bankId")
    public String bankId;
    @SerializedName("bankAccount")
    public String bankAccount;
    @SerializedName("bankUser")
    public String bankUser;
    @SerializedName("bankcardFrontImg")
    public String bankcardFrontImg;
    @SerializedName("bankcardProvince")
    public String bankcardProvince;
    @SerializedName("bankcardCity")
    public String bankcardCity;
    @SerializedName("bankcardArea")
    public String bankcardArea;
    @SerializedName("bankcardAddress")
    public String bankcardAddress;
    @SerializedName("bankReservedPhone")
    public String bankReservedPhone;
    @SerializedName("checkNumber")
    public String checkNumber;
    @SerializedName("userName")
    public String userName;
    @SerializedName("identityCard")
    public String identityCard;

    public AddCardBody(String bankId, String bankAccount, String bankUser, String bankcardFrontImg, String bankcardProvince, String bankcardCity, String bankcardArea, String bankcardAddress, String bankReservedPhone, String checkNumber, String userName, String identityCard) {
        this.bankId = bankId;
        this.bankAccount = bankAccount;
        this.bankUser = bankUser;
        this.bankcardFrontImg = bankcardFrontImg;
        this.bankcardProvince = bankcardProvince;
        this.bankcardCity = bankcardCity;
        this.bankcardArea = bankcardArea;
        this.bankcardAddress = bankcardAddress;
        this.bankReservedPhone = bankReservedPhone;
        this.checkNumber = checkNumber;
        this.userName = userName;
        this.identityCard = identityCard;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = null;
        try {
            map = ConvertUtil.objToMap(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }
}
