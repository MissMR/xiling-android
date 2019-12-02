package com.dodomall.ddmall.ddui.api;

public abstract class ApiEvent {

    /*行标题*/
    public abstract String getTitle();

    /*执行事件*/
    public boolean onEvent() {
        return false;
    }

    /*当前节点ID*/
    private int testId;
    /*当前节点的父节点ID*/
    private int parentId;

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
