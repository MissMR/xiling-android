package com.xiling.shared.bean;

/**
 * created by Jigsaw at 2018/9/15
 */
public class FansBean {

    /**
     * id : 64696
     * headImage : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83er9KtoF2P9UzLIO7oAL4iaoHRv1OdcHJATmN209nKMqd6fh3rnSezgPA6O4zibiaxgjAz0DI40oHicEbA/132
     * nickName : 荆汉青
     * roleName : 店主
     * roleId : 5
     * phone : 187****5967
     * createDate : 2018-09-07
     */

    private String id;
    private String headImage;
    private String nickName;
    private String roleName;
    private int roleId;
    private String phone;
    private String createDate;
    private String superiorNickName;
    private String fansCount;

    public boolean isStoreMaster() {
        return this.roleId >= 15;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperiorNickName() {
        return superiorNickName;
    }

    public void setSuperiorNickName(String superiorNickName) {
        this.superiorNickName = superiorNickName;
    }

    public String getFansCount() {
        return fansCount;
    }

    public void setFansCount(String fansCount) {
        this.fansCount = fansCount;
    }


    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
