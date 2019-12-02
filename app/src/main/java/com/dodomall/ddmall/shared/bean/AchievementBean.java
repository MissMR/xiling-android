package com.dodomall.ddmall.shared.bean;

import android.text.TextUtils;

import com.dodomall.ddmall.ddui.bean.ActivityBannerItemBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * created by Jigsaw at 2018/9/15
 */
public class AchievementBean {

    /**
     * apiMyStatByRoleBean : {"incId":101051,"memberId":"c754f07f3c12480aab9d9bab20020527","headImage":"","nickName":"13昵称8142","inviteCode":20021051,"roleName":"","roleLevel":"","balance":0,"incomeTotal":0,"actualSales":0,"todayTraining":0,"lastWeekTraining":0,"trainingTotal":0,"todayExpectPrize":0,"yesterdayExpectPrize":0,"expectSalePrizeTotal":0,"todayPrize":0,"yesterdayPrize":0,"salePrizeTotal":0,"thisMonthActTeam":0,"thisMonthTeam":0,"prizeTotal":0,"todayBole":0,"thisMonthBole":0,"boleTotal":0,"diffTodayExpectPrize":0,"diffYesterdayExpectPrize":0,"diffExpectPrizeTotal":0,"diffTodayPrize":0,"diffYesterdayPrize":0,"diffPrizeTotal":0,"raiseThisMonthPrize":0,"raisePrizeTotal":0,"payment":0,"paymentIncome":0}
     * fansCount : 0
     */

    private ApiMyStatByRoleBeanBean apiMyStatByRoleBean;
    private long fansCount;

    //   1=未开始0=活动中-1=已结束
    @SerializedName("zeroActivityIsShow")
    private int activityBannerFlag = -2;

    // banner活动图片地址
    @SerializedName("zeroImg")
    private String activityBannerURL;

    // banner活动URL
    @SerializedName("zeroBannerActionUrl")
    private String activityBannerActionURL;

    // 弹框活动URL
    @SerializedName("trainingActionUrl")
    private String activityDialogActionURL;

    // 弹窗活动 图片地址
    @SerializedName("trainingImgUrl")
    private String activityDialogImgURL;

    // 是否显示弹窗活动  1=未开始0=活动中-1=已结束
    @SerializedName("trainingIsShow")
    private int activityDialogFlag = -2;

    private ArrayList<ActivityBannerItemBean> activityList = new ArrayList<>();

    public ArrayList<ActivityBannerItemBean> getActivityList() {
        return activityList;
    }

    public void setActivityList(ArrayList<ActivityBannerItemBean> activityList) {
        this.activityList = activityList;
    }

    public boolean isShowActivityDialog() {
        return this.activityDialogFlag == 0;
    }

    public boolean isShowActivityBanner() {
        return this.activityBannerFlag == 0;
    }

    public int getActivityBannerFlag() {
        return activityBannerFlag;
    }

    public void setActivityBannerFlag(int activityBannerFlag) {
        this.activityBannerFlag = activityBannerFlag;
    }

    public String getActivityBannerActionURL() {
        return activityBannerActionURL;
    }

    public void setActivityBannerActionURL(String activityBannerActionURL) {
        this.activityBannerActionURL = activityBannerActionURL;
    }

    public String getActivityDialogActionURL() {
        return activityDialogActionURL;
    }

    public void setActivityDialogActionURL(String activityDialogActionURL) {
        this.activityDialogActionURL = activityDialogActionURL;
    }

    public String getActivityDialogImgURL() {
        return activityDialogImgURL;
    }

    public void setActivityDialogImgURL(String activityDialogImgURL) {
        this.activityDialogImgURL = activityDialogImgURL;
    }

    public int getActivityDialogFlag() {
        return activityDialogFlag;
    }

    public void setActivityDialogFlag(int activityDialogFlag) {
        this.activityDialogFlag = activityDialogFlag;
    }

    public String getActivityBannerURL() {
        return activityBannerURL;
    }

    public void setActivityBannerURL(String activityBannerURL) {
        this.activityBannerURL = activityBannerURL;
    }

    public ApiMyStatByRoleBeanBean getApiMyStatByRoleBean() {
        return apiMyStatByRoleBean;
    }

    public void setApiMyStatByRoleBean(ApiMyStatByRoleBeanBean apiMyStatByRoleBean) {
        this.apiMyStatByRoleBean = apiMyStatByRoleBean;
    }

    public long getFansCount() {
        return fansCount;
    }

    public void setFansCount(long fansCount) {
        this.fansCount = fansCount;
    }

    public static class ApiMyStatByRoleBeanBean {
        /**
         * incId : 101051
         * memberId : c754f07f3c12480aab9d9bab20020527
         * headImage :
         * nickName : 13昵称8142
         * inviteCode : 20021051
         * roleName :
         * roleLevel :
         * balance : 0
         * incomeTotal : 0
         * actualSales : 0
         * todayTraining : 0
         * lastWeekTraining : 0
         * trainingTotal : 0
         * todayExpectPrize : 0
         * yesterdayExpectPrize : 0
         * expectSalePrizeTotal : 0
         * todayPrize : 0
         * yesterdayPrize : 0
         * salePrizeTotal : 0
         * thisMonthActTeam : 0
         * thisMonthTeam : 0
         * prizeTotal : 0
         * todayBole : 0
         * thisMonthBole : 0
         * boleTotal : 0
         * diffTodayExpectPrize : 0
         * diffYesterdayExpectPrize : 0
         * diffExpectPrizeTotal : 0
         * diffTodayPrize : 0
         * diffYesterdayPrize : 0
         * diffPrizeTotal : 0
         * raiseThisMonthPrize : 0
         * raisePrizeTotal : 0
         * payment : 0
         * paymentIncome : 0
         */

        private int incId;
        private String memberId;
        private String headImage;
        private String nickName;
        private int inviteCode;
        private String roleName;
        private String roleLevel;
        private long balance;
        private long incomeTotal;
        private long actualSales;
        private long todayTraining;
        private long lastWeekTraining;
        private long trainingTotal;
        private long todayExpectPrize;
        private long yesterdayExpectPrize;
        private long expectSalePrizeTotal;
        private long todayPrize;
        private long yesterdayPrize;
        private long salePrizeTotal;
        private long thisMonthActTeam;
        private long thisMonthTeam;
        private long prizeTotal;
        private long todayBole;
        private long thisMonthBole;
        private long boleTotal;
        private long diffTodayExpectPrize;
        private long diffYesterdayExpectPrize;
        private long diffExpectPrizeTotal;
        private long diffTodayPrize;
        private long diffYesterdayPrize;
        private long diffPrizeTotal;
        private long raiseThisMonthPrize;
        private long raisePrizeTotal;
        private long payment;
        private long paymentIncome;

        public String getNickNameLimit() {
            if (TextUtils.isEmpty(nickName)) {
                return "";
            }
            return nickName.length() > 7 ? nickName.substring(0, 7) + "..." : nickName;
        }

        public int getIncId() {
            return incId;
        }

        public void setIncId(int incId) {
            this.incId = incId;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
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

        public int getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(int inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getRoleLevel() {
            return roleLevel;
        }

        public void setRoleLevel(String roleLevel) {
            this.roleLevel = roleLevel;
        }

        public long getBalance() {
            return balance;
        }

        public void setBalance(long balance) {
            this.balance = balance;
        }

        public long getIncomeTotal() {
            return incomeTotal;
        }

        public void setIncomeTotal(long incomeTotal) {
            this.incomeTotal = incomeTotal;
        }

        public long getActualSales() {
            return actualSales;
        }

        public void setActualSales(long actualSales) {
            this.actualSales = actualSales;
        }

        public long getTodayTraining() {
            return todayTraining;
        }

        public void setTodayTraining(long todayTraining) {
            this.todayTraining = todayTraining;
        }

        public long getLastWeekTraining() {
            return lastWeekTraining;
        }

        public void setLastWeekTraining(long lastWeekTraining) {
            this.lastWeekTraining = lastWeekTraining;
        }

        public long getTrainingTotal() {
            return trainingTotal;
        }

        public void setTrainingTotal(long trainingTotal) {
            this.trainingTotal = trainingTotal;
        }

        public long getTodayExpectPrize() {
            return todayExpectPrize;
        }

        public void setTodayExpectPrize(long todayExpectPrize) {
            this.todayExpectPrize = todayExpectPrize;
        }

        public long getYesterdayExpectPrize() {
            return yesterdayExpectPrize;
        }

        public void setYesterdayExpectPrize(long yesterdayExpectPrize) {
            this.yesterdayExpectPrize = yesterdayExpectPrize;
        }

        public long getExpectSalePrizeTotal() {
            return expectSalePrizeTotal;
        }

        public void setExpectSalePrizeTotal(long expectSalePrizeTotal) {
            this.expectSalePrizeTotal = expectSalePrizeTotal;
        }

        public long getTodayPrize() {
            return todayPrize;
        }

        public void setTodayPrize(long todayPrize) {
            this.todayPrize = todayPrize;
        }

        public long getYesterdayPrize() {
            return yesterdayPrize;
        }

        public void setYesterdayPrize(long yesterdayPrize) {
            this.yesterdayPrize = yesterdayPrize;
        }

        public long getSalePrizeTotal() {
            return salePrizeTotal;
        }

        public void setSalePrizeTotal(long salePrizeTotal) {
            this.salePrizeTotal = salePrizeTotal;
        }

        public long getThisMonthActTeam() {
            return thisMonthActTeam;
        }

        public void setThisMonthActTeam(long thisMonthActTeam) {
            this.thisMonthActTeam = thisMonthActTeam;
        }

        public long getThisMonthTeam() {
            return thisMonthTeam;
        }

        public void setThisMonthTeam(long thisMonthTeam) {
            this.thisMonthTeam = thisMonthTeam;
        }

        public long getPrizeTotal() {
            return prizeTotal;
        }

        public void setPrizeTotal(long prizeTotal) {
            this.prizeTotal = prizeTotal;
        }

        public long getTodayBole() {
            return todayBole;
        }

        public void setTodayBole(long todayBole) {
            this.todayBole = todayBole;
        }

        public long getThisMonthBole() {
            return thisMonthBole;
        }

        public void setThisMonthBole(long thisMonthBole) {
            this.thisMonthBole = thisMonthBole;
        }

        public long getBoleTotal() {
            return boleTotal;
        }

        public void setBoleTotal(long boleTotal) {
            this.boleTotal = boleTotal;
        }

        public long getDiffTodayExpectPrize() {
            return diffTodayExpectPrize;
        }

        public void setDiffTodayExpectPrize(long diffTodayExpectPrize) {
            this.diffTodayExpectPrize = diffTodayExpectPrize;
        }

        public long getDiffYesterdayExpectPrize() {
            return diffYesterdayExpectPrize;
        }

        public void setDiffYesterdayExpectPrize(long diffYesterdayExpectPrize) {
            this.diffYesterdayExpectPrize = diffYesterdayExpectPrize;
        }

        public long getDiffExpectPrizeTotal() {
            return diffExpectPrizeTotal;
        }

        public void setDiffExpectPrizeTotal(long diffExpectPrizeTotal) {
            this.diffExpectPrizeTotal = diffExpectPrizeTotal;
        }

        public long getDiffTodayPrize() {
            return diffTodayPrize;
        }

        public void setDiffTodayPrize(long diffTodayPrize) {
            this.diffTodayPrize = diffTodayPrize;
        }

        public long getDiffYesterdayPrize() {
            return diffYesterdayPrize;
        }

        public void setDiffYesterdayPrize(long diffYesterdayPrize) {
            this.diffYesterdayPrize = diffYesterdayPrize;
        }

        public long getDiffPrizeTotal() {
            return diffPrizeTotal;
        }

        public void setDiffPrizeTotal(long diffPrizeTotal) {
            this.diffPrizeTotal = diffPrizeTotal;
        }

        public long getRaiseThisMonthPrize() {
            return raiseThisMonthPrize;
        }

        public void setRaiseThisMonthPrize(long raiseThisMonthPrize) {
            this.raiseThisMonthPrize = raiseThisMonthPrize;
        }

        public long getRaisePrizeTotal() {
            return raisePrizeTotal;
        }

        public void setRaisePrizeTotal(long raisePrizeTotal) {
            this.raisePrizeTotal = raisePrizeTotal;
        }

        public long getPayment() {
            return payment;
        }

        public void setPayment(long payment) {
            this.payment = payment;
        }

        public long getPaymentIncome() {
            return paymentIncome;
        }

        public void setPaymentIncome(long paymentIncome) {
            this.paymentIncome = paymentIncome;
        }
    }
}
