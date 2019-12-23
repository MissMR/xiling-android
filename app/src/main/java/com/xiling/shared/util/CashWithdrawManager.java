package com.xiling.shared.util;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.xiling.ddui.activity.AuthActivity;
import com.xiling.ddui.activity.BankCardActivity;
import com.xiling.ddui.activity.SecurityQuestionActivity;
import com.xiling.ddui.bean.UserAuthBean;
import com.xiling.ddui.bean.WithdrawBean;
import com.xiling.ddui.custom.DDResultDialog;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.basic.BaseRequestListener;
import com.xiling.shared.component.dialog.DDMDialog;
import com.xiling.shared.manager.APIManager;
import com.xiling.shared.manager.ServiceManager;
import com.xiling.shared.service.contract.IUserService;

/**
 * created by Jigsaw at 2018/12/25
 * 提现管理类
 */
public class CashWithdrawManager {
    private Activity mContext;
    private IUserService mUserService;
    private UserAuthBean mUserAuthBean;
    private OnCheckListener mOnCheckListener;

    public CashWithdrawManager(Activity context) {
        mContext = context;
        mUserService = ServiceManager.getInstance().createService(IUserService.class);
    }

    public void checkCanWithdraw(OnCheckListener onCheckListener) {
        this.mOnCheckListener = onCheckListener;
        checkCanWithdraw();
    }

    private void checkCanWithdraw() {
        APIManager.startRequest(mUserService.getWithdrawInfo(), new BaseRequestListener<WithdrawBean>(mContext) {
            @Override
            public void onSuccess(WithdrawBean result, String msg) {
                super.onSuccess(result, msg);
                if (result.getToMiniProgram() == 1) {
                    // 后台控制 不可提现
                    DDResultDialog dialog = new DDResultDialog(mContext);
                    dialog.removeTopImage();
                    dialog.setContent(msg);
                    dialog.setConfirmText("我知道了");
                    dialog.show();
                    if (mOnCheckListener != null) {
                        mOnCheckListener.onCheckFailed(null);
                    }
                } else {
                    getUserAuth();
                }
            }

        });
    }

    private void getUserAuth() {
        APIManager.startRequest(mUserService.getUserAuth(), new BaseRequestListener<UserAuthBean>(mContext) {
            @Override
            public void onSuccess(UserAuthBean result) {
                super.onSuccess(result);
                mUserAuthBean = result;
                if (mOnCheckListener != null) {
                    if (mUserAuthBean.canCashWithdraw()) {
                        mOnCheckListener.onCheckSuccess(mUserAuthBean);
                    } else {
                        showNotAllowedWithdrawDialog();
                        mOnCheckListener.onCheckFailed(mUserAuthBean);
                    }
                }
            }
        });
    }

    private void showNotAllowedWithdrawDialog() {
        String msg = "";
        String positiveButtonText = "";
        final Intent intent = new Intent();
        if (!mUserAuthBean.isPassedAuth()) {
            msg = "请先进行实名认证~";
            positiveButtonText = "去认证";
            intent.setClass(mContext, AuthActivity.class);
        } else if (!mUserAuthBean.isBindedBankCard()) {
            msg = "请先绑定银行卡~";
            positiveButtonText = "去绑定";
            intent.setClass(mContext, BankCardActivity.class);
        } else if (!mUserAuthBean.isSetTradePassword()) {
            msg = "请先设置交易密码";
            positiveButtonText = "去设置";
            intent.setClass(mContext, SecurityQuestionActivity.class)
                    .putExtra(SecurityQuestionActivity.kMode, SecurityQuestionActivity.vModePassword);
        } else if (!mUserAuthBean.isSetSecurityQuestion()) {
            msg = "请先设置安全问题";
            positiveButtonText = "去设置";
            intent.setClass(mContext, SecurityQuestionActivity.class)
                    .putExtra(SecurityQuestionActivity.kMode, SecurityQuestionActivity.vModeQuestion);
        } else {
            DLog.e("所有提现条件都达成了？？？");
            return;
        }
        if (!TextUtils.isEmpty(msg)) {
            new DDMDialog(mContext)
                    .setTitle("提示")
                    .setContent(msg)
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }).setPositiveButton(positiveButtonText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(intent);
                }
            }).show();
        }
    }

    public interface OnCheckListener {
        void onCheckSuccess(UserAuthBean userAuthBean);

        void onCheckFailed(UserAuthBean userAuthBean);
    }
}
