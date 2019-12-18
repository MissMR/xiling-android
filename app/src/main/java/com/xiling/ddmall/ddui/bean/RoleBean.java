package com.xiling.ddmall.ddui.bean;

/**
 * created by Jigsaw at 2018/9/19
 */
public class RoleBean {

    /**
     * id : 11
     * roleName : 总裁
     * roleLevel : 1
     * needAmount : 50000000
     */

    private int id;
    private String roleName;
    private int roleLevel;
    private int needAmount;

    public boolean isStoreMasterNormal() {
        // 15是普通店主
        return this.id == 15;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public int getNeedAmount() {
        return needAmount;
    }

    public void setNeedAmount(int needAmount) {
        this.needAmount = needAmount;
    }
}
