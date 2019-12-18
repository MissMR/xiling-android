package com.xiling.ddmall.shared.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/8/3.
 */
public class ViewHistory {

    /**
     * datas : [{"skuId":"0254a58af35a48ecb1caa955178d7039","productId":"6d0db50238624e15b6ba7583084be7aa","skuName":"澳洲潮牌ROXY 舒适时尚休闲蓝色女性滑雪服","intro":"","stock":999,"saleCount":0,"retailPrice":0,"marketPrice":0,"thumbUrl":"http://112.74.134.143/G1/M00/00/06/cEqGj1iwHkeABLUKAAHwXi_yKUc145.jpg"},{"skuId":"0f294b54e92e48b8a952f6e7b9cf0f5f","productId":"21a40b84d9304346b594dda0ce85b5ac","skuName":"尤妮佳纸尿裤L54(4包装)，适用于9-14kg宝宝","intro":"","stock":999,"saleCount":2,"retailPrice":3000,"marketPrice":4000,"thumbUrl":"http://112.74.134.143/G1/M00/00/07/eEwZu1iwKMaASLhvAAHmgI0kWdY809.jpg"}]
     * pageOffset : 1
     * pageSize : 15
     * totalRecord : 2
     * totalPage : 1
     */

    @SerializedName("pageOffset")
    public int pageOffset;
    @SerializedName("pageSize")
    public int pageSize;
    @SerializedName("totalRecord")
    public int totalRecord;
    @SerializedName("totalPage")
    public int totalPage;
    @SerializedName("datas")
    public List<SkuInfo> datas;

}
