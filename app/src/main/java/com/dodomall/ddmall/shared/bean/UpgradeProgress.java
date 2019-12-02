package com.dodomall.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017-08-03
 */
public class UpgradeProgress {

    @SerializedName("meRetailMoney")
    public long meRetailMoney;
    @SerializedName("totalRetailMoney")
    public long totalRetailMoney;
    @SerializedName("groupRetailMoney")
    public long groupRetailMoney;
    @SerializedName("totalSumMoeny")
    public long totalSumMoeny;
    @SerializedName("level")
    public int level;
    @SerializedName("firLevelStr")
    public String firLevelStr;
    @SerializedName("secLevelStr")
    public String secLevelStr;
    @SerializedName("thrLevelStr")
    public String thrLevelStr;
    @SerializedName("progress")
    public int progress;
}
