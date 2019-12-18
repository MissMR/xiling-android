package com.xiling.ddmall.module.page.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.xiling.ddmall.shared.page.bean.Element;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/9/30.
 */
public class CustomMultiElement implements MultiItemEntity {

    public static final int ACTIVITY = 1 << 0;
    public static final int PRODUCT = 1 << 1;
    public static final int SPACER = 1 << 2;
    public static final int BANNER = 1 << 3;
    public static final int SWIPER = 1 << 4;
    public static final int CAROUSEL = 1 << 5;
    public static final int INSTANT_SWIPER = 1 << 6;
    public static final int INSTANT_SPEC = 1 << 7;
    public static final int LINKS = 1 << 8;
    public static final int NOTICE = 1 << 9;
    public static final int PRODUCT_LARGE = 1 << 10;

    private int mItemType;

    private Element mElement;

    public CustomMultiElement(int itemType) {
        mItemType = itemType;
    }

    @Override
    public int getItemType() {
        return mItemType;
    }

    public Element getElement() {
        return mElement;
    }

    public void setElement(Element element) {
        mElement = element;
    }
}
