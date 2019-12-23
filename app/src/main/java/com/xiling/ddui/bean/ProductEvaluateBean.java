package com.xiling.ddui.bean;

import android.text.TextUtils;

import com.xiling.dduis.base.AvatarDemoMaker;
import com.xiling.shared.util.ConvertUtil;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * created by Jigsaw at 2019/4/1
 * 商品测评
 */
public class ProductEvaluateBean {


    /**
     * {
     * "engineerId": 2,
     * "engineerCode": "",
     * "nikeName": "小高",     评测师的昵称
     * "headImage": "",           评测师头像
     * "spuNum": 5,               评测师 评过的商品数量
     * "productId": "0a124e149cb04286aba601a7015ba860",
     * "score": 3,         评测 分数
     * "content": "",     评测的 内容
     * "type": 1,       评测的 类型   1：图文类型     2.  视频类型
     * "imagesUrl": "["[http://oss.dianduoduo.store/materialLibrary/2019-03/201903270144523936T.png](http://oss.dianduoduo.store/materialLibrary/2019-03/201903270144523936T.png)"]",
     * "imageList": [      图文 类型 的 图片的集合  （最多 9个图片）
     * "http://oss.dianduoduo.store/materialLibrary/2019-03/201903270144523936T.png"
     * ],
     * "videoPlayUrl": "",     视频 类型 ，视频的播放地址
     * "videoImageUrl": ""   视频类型  ，视频的封面图片
     * }
     */

    /**
     * 商品详情 返回值
     */
    private String engineerId;
    private String engineerCode;
    private String nikeName;
    private String headImage;
    private int spuNum;
    private String productId;
    private int score;
    private String content;
    // 1图片 2视频
    private int type;
    private String imagesUrl;
    private String videoPlayUrl;
    private String videoImageUrl;
    private List<String> imageList;
    /**
     * engineerId : 8.279996542684641E7
     * spuNum : 4.963589043245089E7
     * score : 5.911368412252912E7
     * type : -3.817912954242924E7
     * productName : incididunt aliquip do
     * intro : labore enim dolore minim
     * minPrice : 7.782758275407854E7
     * maxMarketPrice : -6.645695761068086E7
     * maxRewardPrice : 7976639.739136353
     * thumbUrlForShopNow : occaecat
     * saleCount : 3.834960771390396E7
     * saleIncCount : 2020900.118866071
     */

    /**
     * 测评列表返回值
     */
    private String updateTime;
    private String productName;
    private String intro;
    private long minPrice;
    private long maxMarketPrice;
    private long maxRewardPrice;
    private String thumbUrlForShopNow;
    private int saleCount;
    private int saleIncCount;

    @SerializedName("extendTime")
    private int shareCount;
    private String[] fakeAvatars;


    public String[] getFakeAvatars() {
        return fakeAvatars;
    }

    public void setFakeAvatars(String[] fakeAvatars) {
        this.fakeAvatars = fakeAvatars;
    }


    public int getShareCount() {
        return shareCount;
    }

    public String getShareCountStr() {
        return ConvertUtil.formatWan(this.shareCount);
    }

    public void setShareCount(int shareCount) {
        this.shareCount = shareCount;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public long getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(long minPrice) {
        this.minPrice = minPrice;
    }

    public long getMaxMarketPrice() {
        return maxMarketPrice;
    }

    public void setMaxMarketPrice(long maxMarketPrice) {
        this.maxMarketPrice = maxMarketPrice;
    }

    public long getMaxRewardPrice() {
        return maxRewardPrice;
    }

    public void setMaxRewardPrice(long maxRewardPrice) {
        this.maxRewardPrice = maxRewardPrice;
    }

    public String getSaleCountStr() {
        return ConvertUtil.formatWan(this.saleCount);
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
        this.fakeAvatars = AvatarDemoMaker.randomAvatar(saleCount > 3 ? 3 : saleCount);
    }

    public int getSaleIncCount() {
        return saleIncCount;
    }

    public void setSaleIncCount(int saleIncCount) {
        this.saleIncCount = saleIncCount;
    }

    public boolean hasImages() {
        return type == 1 && this.imageList != null && this.imageList.size() > 0;
    }

    public boolean hasVideo() {
        return type == 2 && !TextUtils.isEmpty(this.videoPlayUrl);
    }

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineerCode() {
        return engineerCode;
    }

    public void setEngineerCode(String engineerCode) {
        this.engineerCode = engineerCode;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public int getSpuNum() {
        return spuNum;
    }

    public void setSpuNum(int spuNum) {
        this.spuNum = spuNum;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImagesUrl() {
        return imagesUrl;
    }

    public void setImagesUrl(String imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getThumbUrlForShopNow() {
        return thumbUrlForShopNow;
    }

    public void setThumbUrlForShopNow(String thumbUrlForShopNow) {
        this.thumbUrlForShopNow = thumbUrlForShopNow;
    }

}
