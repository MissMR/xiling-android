package com.xiling.ddmall.shared.bean.body;

import com.google.gson.annotations.SerializedName;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/26.
 */
public class SaveIdentityCardBody {

    /**
     * addressId : 29f7e9a362f84e6a9b01a9d46d65c202
     * identityCard : 440182199003050680
     */

    @SerializedName("addressId")
    public String addressId;
    @SerializedName("identityCard")
    public String identityCard;

    public SaveIdentityCardBody(String addressId, String identityCard) {
        this.addressId = addressId;
        this.identityCard = identityCard;
    }
}
