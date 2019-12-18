package com.xiling.ddmall.module.community;

import java.io.Serializable;

/**
 * @author Stone
 * @time 2017/12/29  17:25
 * @desc ${TODD}
 */

public class GroupCategoryModel implements Serializable{
    public static final String MATERIAL ="1" ;
    public static final String VIDEO ="2" ;
    /**
     * categoryId : ae73593b579243e6a3581192699f8551
     * name : 转发最多
     * type : 1
     * status : 1
     * sortIndex : 1
     * createDate : 2017-06-17 09:24:26
     * updateDate : 2017-12-29 16:27:08
     * deleteFlag : 0
     */
    private String categoryId;
    private String name;
    private int type;
    private int status;
    private int sortIndex;
    private String createDate;
    private String updateDate;
    private int deleteFlag;
    private String iconUrl;
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getSortIndex() {
        return sortIndex;
    }

    public void setSortIndex(int sortIndex) {
        this.sortIndex = sortIndex;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
