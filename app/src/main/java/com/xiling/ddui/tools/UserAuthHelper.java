package com.xiling.ddui.tools;

import android.widget.TextView;

import com.xiling.R;
import com.xiling.ddui.bean.UserAuthBean;

/**
 * created by Jigsaw at 2018/9/21
 */
public class UserAuthHelper {
    private UserAuthBean mUserAuthBean;

    public UserAuthHelper(UserAuthBean userAuthBean) {
        mUserAuthBean = userAuthBean;
    }

    /**
     * 1，账户认证：若未认证，显示：未认证（字体红色显示）
     * 若已认证，显示规则：只显示名字最后一个字，如**璐；
     * 若审核失败，显示：审核失败（字体红色显示）
     */
    public void setAuthState(TextView textView) {
        if (mUserAuthBean.isPassedAuth()) {
            // 已认证
            textView.setText(mUserAuthBean.getSecretUserName());
            setDefaultTextColor(textView);
        } else if (mUserAuthBean.getAuthStatus() == 3 || mUserAuthBean.getAuthStatus() == 4) {
            // 审核失败
            textView.setText("审核失败");
            setRedText(textView);
        } else if (mUserAuthBean.getAuthStatus() == 1) {
            textView.setText("待审核");
            setRedText(textView);
        } else {
            // 显示 未认证
            textView.setText("未认证");
            setRedText(textView);
        }
    }

    /**
     * 2，银行卡信息：若未绑卡，显示：未绑卡（字体红色显示）
     * 若已绑卡，显示规则：中信银行（尾号3866）
     */
    public void setBankState(TextView textView) {
        if (mUserAuthBean.isBindedBankCard()) {
            String text = mUserAuthBean.getBankName() +
                    "(" + mUserAuthBean.getBankAccount().substring(mUserAuthBean.getBankAccount().length() - 4) + ")";
            textView.setText(text);
            setDefaultTextColor(textView);
        } else {
            textView.setText("未绑卡");
            setRedText(textView);
        }
    }

    /**
     * 3，交易密码：若从未设置过交易密码，状态显示为：未设置（字体红色显示）
     * 若交易密码已设置，状态显示为：去重置
     *
     * @param textView
     */
    public void setTradePasswordState(TextView textView) {
        if (mUserAuthBean.isSetTradePassword()) {
            textView.setText("去重置");
            setDefaultTextColor(textView);
        } else {
            textView.setText("未设置");
            setRedText(textView);
        }
    }

    /**
     * 4，安全问题：若未设置，显示：未设置（字体红色显示）
     * 若已设置，显示：去重置
     *
     * @param textView
     */
    public void setQuestionState(TextView textView) {
        if (mUserAuthBean.isSetSecurityQuestion()) {
            textView.setText("去重置");
            setDefaultTextColor(textView);
        } else {
            textView.setText("未设置");
            setRedText(textView);
        }
    }

    private void setRedText(TextView textView) {
        textView.setTextColor(textView.getContext().getResources().getColor(R.color.ddm_red));
    }

    private void setDefaultTextColor(TextView textView) {
        textView.setTextColor(textView.getContext().getResources().getColor(R.color.ddm_gray_dark));
    }
}
