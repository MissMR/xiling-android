package com.xiling.module.community;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * @author Stone
 * @time 2018/1/11  12:16
 * @desc ${TODD}
 */

public class CommunityMultiItem implements MultiItemEntity, Serializable {

    public static final int ITEM_TYPE_TEXT = 1;
    public static final int ITEM_TYPE_VIDEO = 2;
    public static final int ITEM_TYPE_LINK = 3;
    public static final int ITEM_TYPE_COMMENT = 4;
    public static final int ITEM_TYPE_HEAD = 5;
    private final GroupFragment.CommunityType communityType;
    private int itemType;
    private MaterialVideoModule content;
    private boolean isLastCommond;

    public boolean isNeedBottomLine() {
        return isNeedBottomLine;
    }

    public void setNeedBottomLine(boolean needBottomLine) {
        isNeedBottomLine = needBottomLine;
    }

    private boolean isNeedBottomLine = true;

    public MaterialVideoModule.CommentModule getCommentModule() {
        return commentModule;
    }

    private MaterialVideoModule.CommentModule commentModule;

    public CommunityMultiItem(int itemTypeText, MaterialVideoModule moment, GroupFragment.CommunityType communityType) {
        this.itemType = itemTypeText;
        this.content = moment;
        this.communityType = communityType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public MaterialVideoModule getContent() {
        return content;
    }

    public GroupFragment.CommunityType getCommunityType() {
        return communityType;
    }

    public void setCommontData(MaterialVideoModule.CommentModule commentModule) {
        this.commentModule = commentModule;
    }

    public void needBottomLine(boolean needBottomLine) {
        this.isNeedBottomLine = needBottomLine;
    }

    public void setLastCommond(boolean isLastCommond) {
        this.isLastCommond=isLastCommond;
    }

    public boolean needCommondLine(){
        return isLastCommond;
    }
}
