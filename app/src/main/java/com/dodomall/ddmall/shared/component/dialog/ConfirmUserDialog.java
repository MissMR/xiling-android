package com.dodomall.ddmall.shared.component.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.dodomall.ddmall.R;
import com.dodomall.ddmall.shared.bean.User;
import com.dodomall.ddmall.shared.constant.AppTypes;
import com.dodomall.ddmall.shared.util.FrescoUtil;
import com.dodomall.ddmall.shared.util.StringUtil;
import com.dodomall.ddmall.shared.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author JayChan <voidea@foxmail.com>
 * @version 1.0
 * @package com.tengchi.zxyjsc.shared.component
 * @since 2017-08-03
 */
public class ConfirmUserDialog extends Dialog {

    @BindView(R.id.tvTips)
    TextView mTvTips;
    private User mUser;
    private int mType;

    @BindView(R.id.avatarIv)
    protected SimpleDraweeView mAvatarIv;
    @BindView(R.id.nameTv)
    protected TextView mNameTv;
    @BindView(R.id.phoneTv)
    protected TextView mPhoneTv;
    @BindView(R.id.nameEt)
    protected EditText mNameEt;

    protected View.OnClickListener mConfirmListener;

    public ConfirmUserDialog(@NonNull Context context, User user) {
        this(context, user, AppTypes.TRANSFER.MONEY);
    }

    public ConfirmUserDialog(@NonNull Context context, User user, int type) {
        super(context);
        mUser = user;
        mType = type;
    }

    private ConfirmUserDialog(Context context, int themeResId) {
        super(context, R.style.Theme_Light_Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_user);
        ButterKnife.bind(this);
        FrescoUtil.setImage(mAvatarIv, mUser.avatar);
        mNameTv.setText(String.format("%s（%s）", mUser.nickname, StringUtil.maskName(mUser.userName)));
        mPhoneTv.setText(mUser.phone);

        if (mType == AppTypes.TRANSFER.SCORE) {
            mNameEt.setVisibility(View.GONE);
            mTvTips.setText("请确认对方信息");
        }
    }

    public void setOnConfirmListener(@NonNull View.OnClickListener listener) {
        mConfirmListener = listener;
    }

    @OnClick(R.id.cancelBtn)
    protected void onClose(View view) {
        dismiss();
    }

    @OnClick(R.id.confirmBtn)
    protected void onConfirm(View view) {
        if (mType == AppTypes.TRANSFER.MONEY) {
            String name = mNameEt.getText().toString();
            if (name.isEmpty()) {
                ToastUtil.error("请输入对方姓名");
                return;
            }
            if (!name.equals(mUser.userName)) {
                ToastUtil.error("姓名不一致");
                return;
            }
        }
        if (mConfirmListener != null) {
            mConfirmListener.onClick(view);
        }
        dismiss();
    }
}
