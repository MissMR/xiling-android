package com.xiling.ddui.bean;

import com.xiling.shared.util.ConvertUtil;

/**
 * created by Jigsaw at 2019/6/18
 */
public class DDCouponBean {

    // 未使用
    public static final int STATUS_UNUSE = 1;
    // 已使用
    public static final int STATUS_USED = 2;
    // 已过期
    public static final int STATUS_TIME_OUT = 3;

    /**
     * couponId : 4.663070049028936E7
     * createDate : ad aliqua anim
     * startDate : sunt reprehenderit consectetur tempor
     * endDate : eiusmod
     * isUse : -8.567561208401382E7
     * title : elit ipsum nisi sint
     * isOnline : -8880957.898020819
     * start : aliqua in qui
     * end : ut do aliqua elit
     * label : cillum culpa minim magna
     * type : 9.709112643786338E7
     * conditions : esse ex commodo sunt
     * reducedPrice : -9726748.220849395
     * description : nostrud ut dolor velit occaecat
     */
    private String id;
    private String couponId;
    private String createDate;
    private String startDate;
    private String endDate;
    private int isUse;
    private String title;
    private int isOnline;
    private String start;
    private String end;
    private String label;
    private int type;
    private long conditions;
    private long reducedPrice;
    private String description;
    private int status;

    // 是否已经展开 记录adapter item的状态
    private boolean isExpand;
    // 选择优惠券是否选中
    private boolean isSelect;

    public String getConditionText() {
        if (conditions > 0) {
            return String.format("满￥%s使用", ConvertUtil.cent2yuan(getConditions()));
        }
        return "无门槛";
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        this.isExpand = expand;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getIsUse() {
        return isUse;
    }

    public void setIsUse(int isUse) {
        this.isUse = isUse;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getConditions() {
        return conditions;
    }

    public void setConditions(long conditions) {
        this.conditions = conditions;
    }

    public long getReducedPrice() {
        return reducedPrice;
    }

    public void setReducedPrice(long reducedPrice) {
        this.reducedPrice = reducedPrice;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
