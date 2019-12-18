package com.xiling.ddmall.ddui.bean;

import android.text.TextUtils;

import com.xiling.ddmall.shared.bean.SkuInfo;
import com.xiling.ddmall.shared.util.StringUtil;

import java.io.Serializable;
import java.util.ArrayList;

public class CommunityDataBean implements Serializable {


    private ArrayList<String> postUrls = new ArrayList<>();
    private ArrayList<HProductPostBean> postImages = new ArrayList<>();

    boolean isMergerImage = false;

    /**
     * 合并普通发圈资源和商品资源图片为统一数据源
     */
    public void mergerImage() {
        //防止重复合并浪费资源
        if (isMergerImage) return;
        //清空上一次的数据源
        postImages.clear();
        //合并普通图片
        for (String url : imagesUrlList) {
            HProductPostBean bean = new HProductPostBean();
            bean.setType(HProductPostBean.PostType.Image);
            bean.setUrl(url);
            postImages.add(bean);
            postUrls.add(url);
        }
        //合并商品图片
        for (SkuInfo pt : materialsSkuBeanList) {
            HProductPostBean bean = new HProductPostBean();
            bean.setType(HProductPostBean.PostType.Product);
            bean.setUrl(pt.thumbURL);
            bean.setProduct(pt);
            postImages.add(bean);
            postUrls.add(bean.getUrl());
        }
        //标记合并状态
        isMergerImage = true;
    }

    public String getShareText() {
        StringBuffer parseText = new StringBuffer();
        parseText.append("" + this.getContent());


        if (!TextUtils.isEmpty(this.getSkuId())) {
            parseText.append(" " + this.getProductUrl());
        }

        //更改关联商品判断逻辑为数组
//        ArrayList<SkuInfo> ps = this.getMaterialsSkuBeanList();
//        if (ps != null && !ps.isEmpty()) {
//            SkuInfo skuInfo = ps.get(0);
//            parseText.append(" " + skuInfo.getProductUrl());
//        }

        if (!TextUtils.isEmpty(this.getRelationLink())) {
            parseText.append(" " + this.getRelationLink());
        }

        return StringUtil.html2Text(parseText.toString());
    }

    public String getVideoUrl() {
        String videoUrl = this.getVideoPlayUrl();
        String videoDownloadUrl = this.getVideoDownUrl();
        if (TextUtils.isEmpty(videoDownloadUrl)) {
            videoDownloadUrl = videoUrl;
        }
        return videoDownloadUrl;
    }

    public boolean hasVideo() {
        String videoUrl = getVideoUrl();
        return (!TextUtils.isEmpty(videoUrl) && !TextUtils.isEmpty(videoUrl));
    }

    public boolean hasImage() {
        return getPostUrls().size() > 0;
    }

    private String roundId;//发圈素材Id
    private String topicId;//话题Id
    private String authorId;//作者Id

    private String suggestShareDate = "";//
    private String suggestShareTime = "";//
    private String createTime = "";//创建时间
    private String nickName = "";//作者昵称
    private String headImage = "";//作者头像
    private String content = "";//素材详情内容
    private String title = "";//话题名称

    //发圈图片资源
    private ArrayList<String> imagesUrlList = new ArrayList<>();

    private String videoPlayUrl = "";//素材视频地址
    private String videoDownUrl = "";//素材视频下载地址
    private String videoImageUrl = "";//素材视频的封面图片
    private String canDownVideoFlag = "";//视频是否可下载

    private String skuId;//关联商品SKU ID
    private String productId;//关联商品的SPU ID
    private String skuName = "";//关联商品名称
    private String intro = "";//商品描述
    private String thumbUrlForShopNow = "";//商品缩略图
    private long retailPrice = 0;//关联商品会员价格
    private String rewardPrice = "";//直接返利
    private String productUrl;//商品详情页地址

    private String downNum = "0";//下载次数
    private String likeNum = "0";//喜欢次数
    private String shareNum = "0";//分享次数
    private int isMyLike = 0;//是否已经喜欢该素材(0 不喜欢，1 喜欢)

    private String difftime = "";//友好显示的时间字段

    private String suggestShareDateStr = "";//建议发圈的上下午时间显示文本

    public String getFormatContent() {
        return StringUtil.html2Text(getContent());
    }

    private int qrCodeType = 0;
    private String qrCodeCls = "0";       //字符串类型，0标识不生成二维码，1标识生成
    private String qrCodeUrl = "";          //二维码跳转路径

    //商品素材 没有关联商品的时候，关联链接数据 - H5链接
    private String relationLink = "";
    //商品素材 没有关联商品的时候，关联链接数据 - 标题
    private String relationLinkWord = "";

    // 是否已经显示过 全文 解决闪动问题  bug1767
    private boolean showedFullButton = false;

    public boolean isShowedFullButton() {
        return showedFullButton;
    }

    public void setShowedFullButton(boolean showedFullButton) {
        this.showedFullButton = showedFullButton;
    }

    private ArrayList<SkuInfo> materialsSkuBeanList = new ArrayList<>();

    /**
     * 是否创建合成二维码
     */
    public boolean isCreateQR() {
        return (this.qrCodeType > 0);
    }

    /**
     * 获取建议发圈时间
     */
    public String getSuggestShareDateTime() {
        String hintDay = suggestShareDateStr;
        if (TextUtils.isEmpty(suggestShareDateStr)) {
            if ("1".equals(suggestShareDate)) {
                hintDay = "上午";
            } else if ("2".equals(suggestShareDate)) {
                hintDay = "下午";
            } else if ("3".equals(suggestShareDate)) {
                hintDay = "晚上";
            }
        }
        return "" + hintDay + "" + suggestShareTime;
    }


    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getSuggestShareDate() {
        return suggestShareDate;
    }

    public void setSuggestShareDate(String suggestShareDate) {
        this.suggestShareDate = suggestShareDate;
    }

    public String getSuggestShareTime() {
        return suggestShareTime;
    }

    public void setSuggestShareTime(String suggestShareTime) {
        this.suggestShareTime = suggestShareTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getImagesUrlList() {
        return imagesUrlList;
    }

    public void setImagesUrlList(ArrayList<String> imagesUrlList) {
        this.imagesUrlList = imagesUrlList;
    }

    public String getVideoPlayUrl() {
        return videoPlayUrl;
    }

    public void setVideoPlayUrl(String videoPlayUrl) {
        this.videoPlayUrl = videoPlayUrl;
    }

    public String getVideoDownUrl() {
        return videoDownUrl;
    }

    public void setVideoDownUrl(String videoDownUrl) {
        this.videoDownUrl = videoDownUrl;
    }

    public String getVideoImageUrl() {
        return videoImageUrl;
    }

    public void setVideoImageUrl(String videoImageUrl) {
        this.videoImageUrl = videoImageUrl;
    }

    public String getCanDownVideoFlag() {
        return canDownVideoFlag;
    }

    public void setCanDownVideoFlag(String canDownVideoFlag) {
        this.canDownVideoFlag = canDownVideoFlag;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
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

    public long getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(long retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getRewardPrice() {
        return rewardPrice;
    }

    public void setRewardPrice(String rewardPrice) {
        this.rewardPrice = rewardPrice;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getDownNum() {
        return downNum;
    }

    public void setDownNum(String downNum) {
        this.downNum = downNum;
    }

    public String getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(String likeNum) {
        this.likeNum = likeNum;
    }

    public String getShareNum() {
        return shareNum;
    }

    public void setShareNum(String shareNum) {
        this.shareNum = shareNum;
    }

    public int getIsMyLike() {
        return isMyLike;
    }

    public void setIsMyLike(int isMyLike) {
        this.isMyLike = isMyLike;
    }

    public String getDifftime() {
        return difftime;
    }

    public void setDifftime(String difftime) {
        this.difftime = difftime;
    }

    public String getSuggestShareDateStr() {
        return suggestShareDateStr;
    }

    public void setSuggestShareDateStr(String suggestShareDateStr) {
        this.suggestShareDateStr = suggestShareDateStr;
    }

    public int getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(int qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getQrCodeCls() {
        return qrCodeCls;
    }

    public void setQrCodeCls(String qrCodeCls) {
        this.qrCodeCls = qrCodeCls;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getRelationLink() {
        return relationLink;
    }

    public void setRelationLink(String relationLink) {
        this.relationLink = relationLink;
    }

    public String getRelationLinkWord() {
        return relationLinkWord;
    }

    public void setRelationLinkWord(String relationLinkWord) {
        this.relationLinkWord = relationLinkWord;
    }

    public ArrayList<SkuInfo> getMaterialsSkuBeanList() {
        return materialsSkuBeanList;
    }

    public void setMaterialsSkuBeanList(ArrayList<SkuInfo> materialsSkuBeanList) {
        this.materialsSkuBeanList = materialsSkuBeanList;
    }

    public ArrayList<String> getPostUrls() {
        return postUrls;
    }

    public void setPostUrls(ArrayList<String> postUrls) {
        this.postUrls = postUrls;
    }

    public ArrayList<HProductPostBean> getPostImages() {
        return postImages;
    }

    public void setPostImages(ArrayList<HProductPostBean> postImages) {
        this.postImages = postImages;
    }
}
