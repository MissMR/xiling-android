package com.xiling.ddui.bean;

import com.xiling.dduis.bean.DDProductBean;
import com.xiling.shared.bean.CartItem;

import java.io.Serializable;

public class DDCartRowBean implements Serializable {

    public enum CartType {
        Empty,/*空数据区域*/
        Product,/*产品*/
        SuggestHeader,/*猜你喜欢头部*/
        SuggestData/*猜你喜欢数据*/
    }

    private CartType type;
    CartItem product;
    DDProductBean suggest;

    public CartType getType() {
        return type;
    }

    public void setType(CartType type) {
        this.type = type;
    }

    public CartItem getProduct() {
        return product;
    }

    public void setProduct(CartItem product) {
        this.product = product;
    }

    public DDProductBean getSuggest() {
        return suggest;
    }

    public void setSuggest(DDProductBean suggest) {
        this.suggest = suggest;
    }
}
