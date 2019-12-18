package com.xiling.ddmall.ddui.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xiling.ddmall.R;
import com.xiling.ddmall.shared.bean.FansBean;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * created by Jigsaw at 2018/9/13
 * 粉丝列表适配器
 */
public class FollowerAdapter extends BaseQuickAdapter<FansBean, BaseViewHolder> {
    public FollowerAdapter() {
        super(R.layout.item_follower);
    }

    @Override
    protected void convert(BaseViewHolder helper, FansBean item) {
        helper.setText(R.id.tv_nickname, item.getNickName());
        helper.setText(R.id.tv_follow_count, "全部粉丝：" + item.getFansCount() + "人");
        helper.setText(R.id.tv_time, item.getCreateDate());
        helper.setText(R.id.tv_inviter, "邀请人：" + item.getSuperiorNickName());
        SimpleDraweeView view = helper.getView(R.id.avatar);
        view.setImageURI(item.getHeadImage());
        if (item.isStoreMaster()) {
            helper.setImageResource(R.id.iv_role, R.mipmap.icon_user_role_vip);
        } else {
            helper.setImageResource(R.id.iv_role, R.mipmap.icon_user_role_normal);
        }
    }
}
