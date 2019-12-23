package com.xiling.ddui.bean;

import com.xiling.shared.bean.SkuInfo;

import java.io.Serializable;

public class HProductPostBean implements Serializable {

    public enum PostType {
        /*图片*/
        Image,
        /*产品*/
        Product,
    }


    private boolean isSelect = true;

    private PostType type = PostType.Image;
    private String url = "";
    private SkuInfo product = null;

    public PostType getType() {
        return type;
    }

    public void setType(PostType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SkuInfo getProduct() {
        return product;
    }

    public void setProduct(SkuInfo product) {
        this.product = product;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return url;
    }
}
