package com.xiling.ddui.custom;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.UserAchievementBean;
import com.xiling.module.user.UserInfoActivity;
import com.xiling.shared.Constants;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IMasterCenterService;
import com.xiling.shared.util.ConvertUtil;
import com.xiling.shared.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Jigsaw
 * @date 2018/12/20
 * 粉丝成就展示
 */
public class DDUserAchievementDialog extends Dialog {


    @BindView(R.id.sdv_avatar)
    SimpleDraweeView sdvAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.iv_phone)
    ImageView ivPhone;
    @BindView(R.id.tv_btn_detail)
    TextView tvBtnDetail;
    @BindView(R.id.tv_expect_income)
    TextView tvExpectIncome;
    @BindView(R.id.tv_total_income)
    TextView tvTotalIncome;
    @BindView(R.id.tv_direct_fans_count)
    TextView tvDirectFansCount;
    @BindView(R.id.tv_team_fans_count)
    TextView tvTeamFansCount;

    private String incId = "";
    private boolean isStoreMaster = false;
    private IMasterCenterService mMasterCenterService;

    private UserAchievementBean mUserAchievementBean;

    public DDUserAchievementDialog(@NonNull Context context) {
        this(context, R.style.DDMDialog);
    }

    public DDUserAchievementDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public DDUserAchievementDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_user_introduce);
        ButterKnife.bind(this);
    }

    public void setIncId(String incId) {
        this.incId = incId;
    }

    private void getUserAchievement(String id) {
        ToastUtil.showLoading(getContext());
        APIManager.startRequest(getMasterCenterService().getUserAchievement(id), new BaseRequestListener<UserAchievementBean>() {
            @Override
            public void onSuccess(UserAchievementBean result) {
                super.onSuccess(result);
                mUserAchievementBean = result;
                ToastUtil.hideLoading();
                show();
                renderUserAchievement(mUserAchievementBean);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                ToastUtil.hideLoading();
                ToastUtil.error(e.getMessage());
            }
        });
    }

    private IMasterCenterService getMasterCenterService() {
        if (mMasterCenterService == null) {
            mMasterCenterService = ServiceManager.getInstance().createService(IMasterCenterService.class);
        }
        return mMasterCenterService;
    }

    private void renderUserAchievement(UserAchievementBean userAchievementBean) {

        sdvAvatar.setImageURI(userAchievementBean.getHeadImage());
        tvNickname.setText(userAchievementBean.getNickName());
        tvPhoneNumber.setText("TEL:" + userAchievementBean.getPhone());

        tvExpectIncome.setText(ConvertUtil.centToCurrency(getContext(), userAchievementBean.getExpectSalePrizeTotal()));
        tvTotalIncome.setText(ConvertUtil.centToCurrency(getContext(), userAchievementBean.getTotalIncome()));

        tvDirectFansCount.setText(userAchievementBean.getDirectFansCount());
        tvTeamFansCount.setText(userAchievementBean.getGroupFansCount());

        showViewByFansType(userAchievementBean.getFansType());

    }

    private void showViewByFansType(int fansType) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
        if (fansType == 1) {
            // 直属粉丝
            layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
            ivPhone.setVisibility(View.VISIBLE);
            tvBtnDetail.setVisibility(View.VISIBLE);
        } else {
            // 团队粉丝
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            ivPhone.setVisibility(View.GONE);
            tvBtnDetail.setVisibility(View.GONE);
        }
        tvPhoneNumber.setLayoutParams(layoutParams);
    }

    public void show(String usersId) {
        getUserAchievement(usersId);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @OnClick(R.id.iv_phone)
    public void onPhoneClicked() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mUserAchievementBean.getPhone()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(intent);
        dismiss();
    }

    @OnClick(R.id.tv_btn_detail)
    public void onBtnDetailClicked() {
        getContext().startActivity(new Intent(getContext(), UserInfoActivity.class)
                .putExtra(Constants.Extras.USER_ID, mUserAchievementBean.getIncId()));
        dismiss();
    }

    @OnClick(R.id.iv_close)
    public void onCloseClicked() {
        dismiss();
    }

}
