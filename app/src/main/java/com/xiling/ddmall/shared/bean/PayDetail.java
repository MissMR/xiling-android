package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Chan on 2017/6/22.
 *
 * @author Chan
 * @package com.tengchi.zxyjsc.shared.bean
 * @since 2017/6/22 上午9:40
 */

public class PayDetail {

    @SerializedName("payMoney")
    public long payMoney;
    @SerializedName("createDate")
    public String createDate;
    @SerializedName("order")
    public Order order;

}
