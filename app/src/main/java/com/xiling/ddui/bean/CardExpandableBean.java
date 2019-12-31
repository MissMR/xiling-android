package com.xiling.ddui.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 购物车列表专用bean
 * 从接口请求到的数据，都要转成它
 */
public class CardExpandableBean<T> implements MultiItemEntity {
    public static final int PARENT = 0;
    public static final int CHILD = 1;
    private T bean;
    private boolean isParent;
    private String parentName;
    private String parentId;
    private int size = 1;
    private int parentPosition;
    List<Integer> childPositions = new ArrayList<>();
    private int position;
    private boolean isSelect = false;
    private boolean editSelect = false;

    public boolean isEditSelect() {
        return editSelect;
    }

    public void setEditSelect(boolean editSelect) {
        this.editSelect = editSelect;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getParentPosition() {
        return parentPosition;
    }

    public void setParentPosition(int parentPosition) {
        this.parentPosition = parentPosition;
    }

    public List<Integer> getChildPositions() {
        return childPositions;
    }

    public void setChildPositions(List<Integer> childPositions) {
        this.childPositions = childPositions;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public CardExpandableBean(boolean isParent) {
        this.isParent = isParent;
    }

    @Override
    public int getItemType() {
        int type = PARENT;
        if (isParent) {
            type = PARENT;
        } else {
            type = CHILD;
        }
        return type;
    }

    public T getBean() {
        return bean;
    }

    public void setBean(T bean) {
        this.bean = bean;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}
