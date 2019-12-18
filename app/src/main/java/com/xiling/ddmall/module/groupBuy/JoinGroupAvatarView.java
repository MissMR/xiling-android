package com.xiling.ddmall.module.groupBuy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.util.FrescoUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * When I wrote this, only God and I understood what I was doing
 * Now, God only knows
 * <p>
 * Created by zjm on 2017/11/1.
 */
public class JoinGroupAvatarView extends RelativeLayout {

    @BindView(R.id.ivAvatar)
    SimpleDraweeView mIvAvatar;
    @BindView(R.id.ivTag)
    TextView mIvTag;
    @BindView(R.id.ivAvatarNoData)
    ImageView mIvAvatarNoData;

    @SuppressLint("ResourceAsColor")
    public JoinGroupAvatarView(@NonNull Context context) {
        super(context);
        inflate(context, R.layout.view_join_group_avatar, this);
        ButterKnife.bind(this);
    }

    public void setData(GroupBuyModel.JoinMemberEntity data) {
        if (data == null) {
            mIvTag.setVisibility(GONE);
            mIvAvatar.setVisibility(GONE);
            mIvAvatarNoData.setVisibility(VISIBLE);
        } else {
            FrescoUtil.setImageSmall(mIvAvatar, data.headImage);
            mIvTag.setVisibility(data.role == 1 ? VISIBLE : GONE);
        }
    }

}
