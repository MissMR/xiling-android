package com.xiling.shared.bean;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.shared.util.PhoneNumberUtil;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 用户Bean
 * Created by JayChan on 2016/12/13.
 */
public class User implements Serializable {
    @SerializedName("incId")
    public String incId;
    @SerializedName("memberId")
    public String id = "";
    @SerializedName("inviteCode")
    public String invitationCode = "";
    @SerializedName("headImage")
    public String avatar;
    @SerializedName("phone")
    public String phone = "";

    @SerializedName("nickName")
    public String nickname = "";
    @SerializedName("wechat")
    public String wechat = "";
    @SerializedName("wechatCode")
    public String wechatCode = "";
    @SerializedName("wechatOpenId")
    public String openId = "";
    @SerializedName("wechatUnionId")
    public String unionId = "";
    @SerializedName("userName")
    public String userName = "新用户";
    @SerializedName("identityCard")
    public String identityCard = "";
    /**
     * 订货管家里面的等级 没卵用的
     */
    @SerializedName("memberType")
    public int memberType;
    @SerializedName("memberTypeStr")
    public String memberTypeStr;
    @SerializedName("authStatus")
    public int authStatus;
    @SerializedName("authStatusStr")
    public String authStatusStr;
    @SerializedName("signinStatus")
    public int isSignIn;
    /**
     * 店铺等级
     */
    @SerializedName("storeType")
    public int storeType;

    @SerializedName("storeTypeStr")
    public String storeTypeStr;
    /**
     * 美集品里面的等级
     */
    @SerializedName("vipType")
    public int vipType;
    @SerializedName("vipTypeStr")
    public String vipTypeStr;
    @SerializedName("isStore")
    public int isStore;
    /**
     * inviteMemberType : 0
     */

    @SerializedName("inviteMemberType")
    public int inviteMemberType;

    @SerializedName("roleId")
    public int roleId;

    @SerializedName("superInviteCode")
    public String superInviteCode;

    /**
     * 获取最优邀请码
     * <p>
     * 如果是店主身份，使用自己的邀请码
     * 如果是会员身份，使用上级的邀请码
     * <p>
     * 因逻辑冲突原因，废弃（昨天是会员，分享出去，今天是店主，别人扫昨天的素材会错误绑定）
     */
    @Deprecated
    public String getBestInviteCode() {
        if (isStoreMaster()) {
            return invitationCode;
        } else {
            return superInviteCode;
        }
    }

    public boolean isShowFreeBuy() {
        if (activityFlag == 1) {
            return true;
        } else {
            return false;
        }
    }

    @SerializedName("activityFlag")
    private int activityFlag = 0;

    @SerializedName("activitySkuId")
    public String activitySkuId = "";

    @SerializedName("activitySpuId")
    public String activitySpuId = "";


    public boolean isStoreMasterNormal() {
        //role 15 普通店主
        return isStoreMaster() && roleId == 15;
    }

    /**
     * 是否是店主
     */
    public boolean isStoreUser() {
        return (vipType > 0);
    }

    public String getAuthStatusStr() {
        return UserAuthBean.getAuthStr(authStatus);
    }

    public boolean isStoreMaster() {
        return this.vipType > 0;
    }

    public static User fromJson(@Nullable String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        return new Gson().fromJson(json, User.class);
    }

    public String getNickNameLimit() {
        if (TextUtils.isEmpty(nickname)) {
            return "";
        }
        return nickname.length() > 7 ? nickname.substring(0, 7) + "..." : nickname;
    }

    /**
     * @return 是否显示会员价
     */
    public boolean isShopkeeper() {
        return true;
    }

    /**
     * @return 是否显示店主的一些控件
     */
    public boolean isShowStoreView() {
        return isStore != 0;
    }

    //133****1212
    public String getSecretPhoneNumber() {
        return PhoneNumberUtil.getSecretPhoneNumber(this.phone);
    }

    public int getRadioType() {
        if (memberType == 0) {
            return vipType;
        } else if (memberType == 1) {
            if (vipType > 3) {
                return vipType;
            } else {
                return 3;
            }
        } else if (memberType >= 2) {
            return 4;
        }
        return vipType;
    }
}
