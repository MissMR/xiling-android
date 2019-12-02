package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan on 2017/6/22.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017/6/22 上午10:19
 */

public class DealDetail {

    @SerializedName("dealId")
    public String dealId;
    @SerializedName("dealCode")
    public String dealCode;
    @SerializedName("dealStatus")
    public long dealStatus;
    @SerializedName("dealStatusStr")
    public String dealStatusStr;
    @SerializedName("applyMemberId")
    public String applyMemberId;
    @SerializedName("applyAccountId")
    public String applyAccountId;
    @SerializedName("applyMoney")
    public long applyMoney;
    @SerializedName("applyDate")
    public String applyDate;
    @SerializedName("checkDate")
    public String checkDate;
    @SerializedName("checkStatus")
    public long checkStatus;
    @SerializedName("checkResult")
    public String checkResult;
    @SerializedName("giveDate")
    public String giveDate;
    @SerializedName("giveResult")
    public String giveResult;
    @SerializedName("giveInvoice")
    public String giveInvoice;
    @SerializedName("account")
    public Account account;
    @SerializedName("applyMonth")
    public String applyMonth;
    @SerializedName("serviceChargeMoney")
    public long serviceChargeMoney;
    @SerializedName("proxyTaxMoney")
    public long proxyTaxMoney;
    @SerializedName("backMoney")
    public long backMoney;
    @SerializedName("giveMoney")
    public long giveMoney;
    @SerializedName("remainderMoney")
    public long remainderMoney;


}
